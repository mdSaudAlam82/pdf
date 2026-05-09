package com.edu.pdf.data.repository

import androidx.core.net.toUri
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import com.edu.pdf.data.local.dao.PdfDao
import com.edu.pdf.data.local.dao.SearchHistoryDao
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.SearchHistoryEntity
import com.edu.pdf.data.mapper.toDomainModel
import com.edu.pdf.data.mapper.toEntity
import com.edu.pdf.data.source.DeviceStorageDataSource
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.util.UUID
import javax.inject.Inject

class PdfRepositoryImpl @Inject constructor(
    private val pdfDao: PdfDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val deviceStorage: DeviceStorageDataSource,
    private val vaultStorage: com.edu.pdf.data.source.VaultDataSource, // 🌟 NAYA PERFECT ENGINE
    private val userPreferences: com.edu.pdf.data.preferences.UserPreferences
) : PdfRepository {

    override fun getManagedFolders(parentId: String?, isVault: Boolean): Flow<List<Folder>> {
        return pdfDao.getFoldersByParentWithCount(parentId, isVault).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getAllManagedFolders(isVault: Boolean): Flow<List<Folder>> {
        return pdfDao.getAllFoldersWithCount(isVault).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getManagedPdfs(parentId: String?, isVault: Boolean): Flow<List<PdfFile>> {
        return pdfDao.getPdfsByParent(parentId, isVault).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun createManagedFolder(name: String, parentId: String?, isVault: Boolean): Result<String> {
        return try {
            val folderId = UUID.randomUUID().toString()
            pdfDao.insertFolder(FolderEntity(folderId, name, parentId, isVault))
            Result.success(folderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun renameManagedFolder(folderId: String, newName: String): Result<Unit> {
        pdfDao.renameFolder(folderId, newName)
        return Result.success(Unit)
    }

    override suspend fun deleteManagedFolder(folderId: String): Result<Unit> {
        pdfDao.deleteFolder(folderId)
        return Result.success(Unit)
    }

    override fun getSecureVaultStreamUri(encryptedPath: String): String {
        return vaultStorage.getSecureVaultStreamUri(encryptedPath) // 🌟 VAULT KO BHEJA
    }

    override suspend fun movePdfsToVirtualFolder(pdfIds: List<String>, targetFolderId: String?, isVault: Boolean): Int {
        var movedCount = 0
        pdfIds.chunked(500).forEach { chunkedIds ->
            val pdfsToMove = pdfDao.getPdfsByIds(chunkedIds)
            pdfsToMove.forEach { pdfToMove ->
                val pdfId = pdfToMove.id
                if (isVault && !pdfToMove.isVault) {
                    // 🌟 FIX: deviceStorage nahi, vaultStorage use kiya! Aur sync bhi karwaya.
                    val securePath = vaultStorage.moveToInternalVault(pdfToMove.toDomainModel()) { oldPath ->
                        deviceStorage.syncWithMediaStore(oldPath, null)
                    }
                    if (securePath != null) {
                        pdfDao.updatePdfNameAndPath(pdfId, pdfToMove.name, securePath)
                        pdfDao.movePdfToVirtualFolder(pdfId, targetFolderId, isVault = true)
                        movedCount++
                    }
                } else if (!isVault && pdfToMove.isVault) {
                    // 🌟 FIX: Vault Storage se restore
                    val restoredData = vaultStorage.restoreFromInternalVault(pdfToMove.path, pdfToMove.name)
                    if (restoredData != null) {
                        val (newId, newPath) = restoredData
                        pdfDao.updatePdfIdAndPath(oldId = pdfId, newId = newId, newPath = newPath)
                        pdfDao.movePdfToVirtualFolder(newId, targetFolderId, isVault = false)
                        movedCount++
                    }
                } else {
                    pdfDao.movePdfToVirtualFolder(pdfId, targetFolderId, isVault)
                    movedCount++
                }
            }
            yield()
        }
        return movedCount
    }

    override suspend fun moveFolderToVirtualFolder(folderId: String, targetFolderId: String?, isVault: Boolean): Result<Unit> {
        pdfDao.moveFolder(folderId, targetFolderId, isVault)
        return Result.success(Unit)
    }

    override fun getRecentPdfs(): Flow<List<PdfFile>> =
        pdfDao.getRecentPdfs().map { list -> list.map { it.toDomainModel() } }

    private fun getSortQuery(baseQuery: String, sortType: SortType): String {
        val orderBy = when (sortType) {
            SortType.NAME_ASC -> "name ASC"
            SortType.NAME_DESC -> "name DESC"
            SortType.DATE_DESC -> "lastModified DESC"
            SortType.DATE_ASC -> "lastModified ASC"
            SortType.SIZE_DESC -> "sizeInBytes DESC"
            SortType.SIZE_ASC -> "sizeInBytes ASC"
        }
        return "$baseQuery ORDER BY $orderBy"
    }

    override fun getAllPdfs(sortType: SortType): Flow<List<PdfFile>> {
        val query = SimpleSQLiteQuery(getSortQuery("SELECT * FROM pdf_table WHERE isVault = 0", sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }

    override fun getFavoritePdfs(sortType: SortType): Flow<List<PdfFile>> {
        val query = SimpleSQLiteQuery(getSortQuery("SELECT * FROM pdf_table WHERE isFavorite = 1 AND isVault = 0", sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }

    override fun searchPdfs(query: String): Flow<List<PdfFile>> {
        return pdfDao.searchPdfsInDatabase(query).map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun scanAndSavePdfs(): Unit = withContext(Dispatchers.IO) {
        val isInitialCompleted = userPreferences.isInitialScanCompleted()
        val lastSyncTime = if (!isInitialCompleted) 0L else userPreferences.getLastSyncTime()
        val currentTime = System.currentTimeMillis()

        deviceStorage.processDevicePdfUpdates(
            lastSyncTime = lastSyncTime,
            onNewPdfsBatch = { chunk ->
                pdfDao.insertAllPdfs(chunk.map { it.toEntity().copy(virtualParentId = null, isVault = false) })
                yield()
            }
        )

        if (!isInitialCompleted) {
            userPreferences.setInitialScanCompleted(true)
        }
        userPreferences.updateLastSyncTime(currentTime)
        Unit
    }

    override suspend fun checkFileExists(fileId: String): Boolean = deviceStorage.doesFileExist(fileId)
    override suspend fun updateLastOpenedTime(pdfId: String, time: Long) = pdfDao.updateLastOpenedTime(pdfId, time)
    override suspend fun toggleFavorite(pdfId: String, isFavorite: Boolean) = pdfDao.updateFavoriteStatus(pdfId, isFavorite)

    override suspend fun renamePdf(pdf: PdfFile, newName: String): Boolean {
        val newFile = deviceStorage.movePhysicalFile(pdf.path, File(pdf.path).parentFile?.absolutePath ?: return false)
        return newFile != null
    }

    override suspend fun deletePdfs(pdfs: List<PdfFile>): Boolean {
        val trashedIds = deviceStorage.moveToTrash(pdfs)
        if (trashedIds.isNotEmpty()) {
            trashedIds.chunked(100).forEach { chunk -> pdfDao.deletePdfsByIds(chunk); yield() }
        }
        return true
    }

    override fun getRecentFolders(): Flow<List<Folder>> {
        return pdfDao.getRecentFoldersWithCount().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun updateFolderLastOpenedTime(folderId: String, time: Long) {
        pdfDao.updateFolderLastOpenedTime(folderId, time)
    }

    override fun getRecentSearchQueries(): Flow<List<String>> = searchHistoryDao.getRecentSearches().map { list -> list.map { it.query } }
    override suspend fun saveSearchQuery(query: String) { if (query.isNotBlank()) searchHistoryDao.insertSearchQuery(SearchHistoryEntity(query.trim(), System.currentTimeMillis())) }
    override suspend fun deleteSearchQuery(query: String) = searchHistoryDao.deleteSearchQuery(query)
    override suspend fun clearAllSearchHistory() = searchHistoryDao.clearAllHistory()

    override suspend fun importPdfFromUri(uriString: String, targetFolderId: String?, isVault: Boolean, isPhysicalFolder: Boolean): Result<Unit> {
        return try {
            val uri = uriString.toUri()
            val targetPhysicalPath = if (isPhysicalFolder) targetFolderId else null

            val importedFile = deviceStorage.importFileFromUri(uri, isVault, targetPhysicalPath)

            if (importedFile != null) {
                val newEntity = com.edu.pdf.data.local.entity.PdfEntity(
                    id = importedFile.id,
                    name = importedFile.name,
                    path = importedFile.path,
                    sizeInBytes = importedFile.sizeInBytes,
                    lastModified = importedFile.lastModified,
                    virtualParentId = if (isPhysicalFolder) null else targetFolderId,
                    isVault = isVault,
                    isFavorite = false
                )

                withContext(NonCancellable) {
                    pdfDao.insertPdf(newEntity)
                }

                Result.success(Unit)
            } else {
                Result.failure(Exception("File copying failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfFile> {
        return pdfDao.searchPdfsFast(query, isVault).map { it.toDomainModel() }
    }
    override fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfFile>> {
        return pdfDao.getPdfsInPhysicalFolder(folderPath).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getPaginatedPdfsInPhysicalFolder(folderPath: String): Flow<androidx.paging.PagingData<PdfFile>> {
        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(
                pageSize = 20, // 🌟 Ek baar mein sirf 20 PDF memory mein load honge
                prefetchDistance = 5, // 🌟 User ke end tak pahunchne se 5 item pehle hi agla page load ho jayega (Zero Lag!)
                enablePlaceholders = false
            ),
            pagingSourceFactory = { pdfDao.getPaginatedPdfsInPhysicalFolder(folderPath) }
        ).flow.map { pagingData ->
            // Entity ko tumhare clean Domain Model mein convert kar rahe hain
            pagingData.map { it.toDomainModel() }
        }
    }
}