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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import javax.inject.Inject

class PdfRepositoryImpl @Inject constructor(
    private val pdfDao: PdfDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val deviceStorage: DeviceStorageDataSource,
    private val vaultStorage: com.edu.pdf.data.source.VaultDataSource,
    private val userPreferences: com.edu.pdf.data.preferences.UserPreferences
) : PdfRepository {

    override fun getManagedFolders(parentPath: String?, isVault: Boolean): Flow<List<Folder>> {
        return pdfDao.getFoldersByParentWithCount(parentPath, isVault).map { list -> list.map { it.toDomainModel() } }
    }

    override fun getAllManagedFolders(isVault: Boolean): Flow<List<Folder>> {
        return pdfDao.getAllFoldersWithCount(isVault).map { list -> list.map { it.toDomainModel() } }
    }

    override fun getManagedPdfs(parentPath: String?, isVault: Boolean): Flow<List<PdfFile>> {
        return pdfDao.getPdfsByParent(parentPath, isVault).map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun createManagedFolder(name: String, parentPath: String?, isVault: Boolean): Result<String> {
        return try {
            val root = parentPath ?: deviceStorage.getPdfProRootFolder()
            val targetFile = File(root, name)
            if (targetFile.exists()) return Result.failure(Exception("A folder named '$name' already exists here!"))
            val newPhysicalPath = deviceStorage.createPhysicalFolder(name, parentPath)
            if (newPhysicalPath != null) {
                pdfDao.insertFolder(FolderEntity(absolutePath = newPhysicalPath, name = name, parentPath = parentPath, isVault = isVault))
                Result.success(newPhysicalPath)
            } else Result.failure(Exception("Failed to create physical folder. Check permissions."))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun renameManagedFolder(oldPath: String, newName: String): Result<Unit> {
        val oldFile = File(oldPath)
        val parentDir = oldFile.parentFile ?: return Result.failure(Exception("Invalid folder path"))
        val targetFile = File(parentDir, newName)
        if (targetFile.exists() && !oldFile.name.equals(newName, ignoreCase = true)) return Result.failure(Exception("A folder named '$newName' already exists here!"))
        val newPhysicalPath = deviceStorage.renamePhysicalFile(oldPath, newName)
        return if (newPhysicalPath != null) {
            pdfDao.cascadeRenameFolders(oldPath, newPhysicalPath, newName)
            pdfDao.cascadeRenamePdfs(oldPath, newPhysicalPath)
            Result.success(Unit)
        } else Result.failure(Exception("Rename physically failed"))
    }

    override suspend fun deleteManagedFolder(path: String): Result<Unit> = withContext(Dispatchers.IO) {
        val success = deviceStorage.deletePhysicalPath(path)
        if (success) {
            pdfDao.cascadeDeleteFolders(path)
            pdfDao.cascadeDeletePdfs(path)
            Result.success(Unit)
        } else Result.failure(Exception("Could not delete folder physically"))
    }

    override suspend fun moveFolderToVirtualFolder(folderPath: String, targetPath: String?, isVault: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        val target = targetPath ?: deviceStorage.getPdfProRootFolder()
        val normalizedSource = File(folderPath).absolutePath
        val normalizedTarget = File(target).absolutePath
        if (normalizedTarget == normalizedSource || normalizedTarget.startsWith("$normalizedSource/")) return@withContext Result.failure(Exception("Cannot move a folder into itself or its own sub-folder!"))
        val newPhysicalPath = deviceStorage.movePhysicalFile(folderPath, target)
        if (newPhysicalPath != null) {
            val newName = File(newPhysicalPath).name
            pdfDao.cascadeRenameFolders(folderPath, newPhysicalPath, newName)
            pdfDao.cascadeRenamePdfs(folderPath, newPhysicalPath)
            val newParentForMovedFolder = if (target == deviceStorage.getPdfProRootFolder()) null else target
            pdfDao.moveFolder(oldAbsolutePath = newPhysicalPath, newAbsolutePath = newPhysicalPath, newParentPath = newParentForMovedFolder, isVault = isVault)
            pdfDao.updateFolderLastOpenedTime(newPhysicalPath, 0L)
            deviceStorage.syncWithMediaStore(folderPath, newPhysicalPath)
            Result.success(Unit)
        } else Result.failure(Exception("Move physically failed. Check storage permissions or collision."))
    }

    /**
     * 🌟 THE INDUSTRY-STANDARD BULK MOVE ENGINE
     * Strictly Sequential & Transactional for maximum stability.
     */
    override suspend fun movePdfsToVirtualFolder(pdfIds: List<String>, targetPath: String?, isVault: Boolean): Int = withContext(Dispatchers.IO) {
        val targetPhysicalDir = targetPath ?: deviceStorage.getPdfProRootFolder()
        val allPhysicalPathsToSync = mutableListOf<String>()
        val dbUpdates = mutableListOf<com.edu.pdf.data.local.entity.PdfEntity>()
        var totalMoved = 0

        // 🛡️ PROTOCOL: Silence scanners during operation
        userPreferences.setSyncLocked(true)

        try {
            pdfIds.chunked(100).forEach { chunk ->
                val pdfsInChunk = pdfDao.getPdfsByIds(chunk)
                pdfsInChunk.forEach { pdfEntity ->
                    val newPath = deviceStorage.movePhysicalFile(pdfEntity.path, targetPhysicalDir)
                    if (newPath != null) {
                        allPhysicalPathsToSync.add(pdfEntity.path)
                        allPhysicalPathsToSync.add(newPath)
                        dbUpdates.add(pdfEntity.copy(path = newPath, parentPath = targetPath, isVault = isVault))
                        totalMoved++
                    }
                }
                yield()
            }
            
            // 📦 ATOMIC DISPATCH: All DB rows updated in one single transaction
            if (dbUpdates.isNotEmpty()) pdfDao.movePdfsBulk(dbUpdates)
            
            // OS Sync using stable path-array approach
            if (allPhysicalPathsToSync.isNotEmpty()) deviceStorage.syncWithMediaStoreBulk(allPhysicalPathsToSync)
        } finally {
            userPreferences.setSyncLocked(false)
            // 🧹 FINAL POLISH: One final rescan to ensure MediaStore is 100% accurate
            scanAndSavePdfs()
        }
        totalMoved
    }

    override suspend fun importPdfFromUri(uriString: String, targetPath: String?, isVault: Boolean, isPhysicalFolder: Boolean): Result<Unit> {
        return try {
            val importedFile = deviceStorage.importFileFromUri(uriString.toUri(), isVault, targetPath)
            if (importedFile != null) {
                pdfDao.insertPdf(com.edu.pdf.data.local.entity.PdfEntity(id = importedFile.id, name = importedFile.name, path = importedFile.path, sizeInBytes = importedFile.sizeInBytes, lastModified = importedFile.lastModified, parentPath = targetPath, isVault = isVault, isFavorite = false, lastOpenedTime = 0))
                Result.success(Unit)
            } else Result.failure(Exception("File copying failed"))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun scanAndSavePdfs() {
        withContext(Dispatchers.IO) {
            val stalePaths = pdfDao.getAllPublicPdfPaths().filter { !File(it).exists() }
            if (stalePaths.isNotEmpty()) stalePaths.chunked(100).forEach { pdfDao.deletePdfsByPaths(it); yield() }
            val staleFolderPaths = pdfDao.getAllStoredFolderPaths().filter { !File(it).exists() }
            if (staleFolderPaths.isNotEmpty()) staleFolderPaths.forEach { pdfDao.deleteFolder(it) }
            syncPhysicalFoldersWithDb()
            deviceStorage.processDevicePdfUpdates { chunk -> pdfDao.insertAllPdfs(chunk.map { it.toEntity().copy(isVault = false) }); yield() }
            userPreferences.setInitialScanCompleted(true)
            userPreferences.updateLastSyncTime(System.currentTimeMillis())
        }
    }

    override suspend fun renamePdf(pdf: PdfFile, newName: String): Boolean {
        val finalName = if (newName.endsWith(".pdf", ignoreCase = true)) newName else "$newName.pdf"
        val newPhysicalPath = deviceStorage.renamePhysicalFile(pdf.path, finalName)
        if (newPhysicalPath != null) { pdfDao.updatePdfNameAndPath(pdf.id, finalName, newPhysicalPath); return true }
        return false
    }

    override suspend fun deletePdfs(pdfs: List<PdfFile>): Boolean {
        val trashedIds = deviceStorage.moveToTrash(pdfs)
        if (trashedIds.isNotEmpty()) trashedIds.chunked(100).forEach { pdfDao.deletePdfsByIds(it); yield() }
        return true
    }

    override fun getSecureVaultStreamUri(encryptedPath: String): String = vaultStorage.getSecureVaultStreamUri(encryptedPath)
    override fun getRecentPdfs(): Flow<List<PdfFile>> = pdfDao.getRecentPdfs().map { list -> list.map { it.toDomainModel() } }

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
    override fun getUncategorizedPdfs(sortType: SortType): Flow<List<PdfFile>> {
        val baseQuery = "SELECT * FROM pdf_table WHERE isVault = 0 AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))"
        val query = SimpleSQLiteQuery(getSortQuery(baseQuery, sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }
    override fun getAllPdfsPaged(sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val baseQuery = "SELECT * FROM pdf_table WHERE isVault = 0 AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))"
        val query = SimpleSQLiteQuery(getSortQuery(baseQuery, sortType))
        return androidx.paging.Pager(config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false), pagingSourceFactory = { pdfDao.getUncategorizedPdfsPaged(query) }).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }
    override fun getFavoritePdfs(sortType: SortType): Flow<List<PdfFile>> {
        val query = SimpleSQLiteQuery(getSortQuery("SELECT * FROM pdf_table WHERE isFavorite = 1 AND isVault = 0", sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }
    override fun searchPdfs(query: String): Flow<List<PdfFile>> = pdfDao.searchPdfsInDatabase(query).map { list -> list.map { it.toDomainModel() } }
    override suspend fun checkFileExists(fileId: String): Boolean = deviceStorage.doesFileExist(fileId)
    override suspend fun updateLastOpenedTime(pdfId: String, time: Long) = pdfDao.updateLastOpenedTime(pdfId, time)
    override suspend fun toggleFavorite(pdfId: String, isFavorite: Boolean) = pdfDao.updateFavoriteStatus(pdfId, isFavorite)
    override fun getRecentFolders(): Flow<List<Folder>> = pdfDao.getRecentFoldersWithCount().map { list -> list.map { it.toDomainModel() } }
    override suspend fun updateFolderLastOpenedTime(path: String, time: Long) = pdfDao.updateFolderLastOpenedTime(path, time)
    override fun getRecentSearchQueries(): Flow<List<String>> = searchHistoryDao.getRecentSearches().map { list -> list.map { it.query } }
    override suspend fun saveSearchQuery(query: String) { if (query.isNotBlank()) searchHistoryDao.insertSearchQuery(SearchHistoryEntity(query.trim(), System.currentTimeMillis())) }
    override suspend fun deleteSearchQuery(query: String) = searchHistoryDao.deleteSearchQuery(query)
    override suspend fun clearAllSearchHistory() = searchHistoryDao.clearAllHistory()
    override suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfFile> = pdfDao.searchPdfsFast(query, isVault).map { it.toDomainModel() }
    override fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfFile>> = pdfDao.getPdfsInPhysicalFolder(folderPath).map { list -> list.map { it.toDomainModel() } }
    override fun getPaginatedPdfsInPhysicalFolder(folderPath: String, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val baseQuery = "SELECT * FROM pdf_table WHERE path LIKE ? || '/%' AND path NOT LIKE ? || '/%/%' AND isVault = 0"
        val query = SimpleSQLiteQuery(getSortQuery(baseQuery, sortType), arrayOf(folderPath, folderPath))
        return androidx.paging.Pager(config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false), pagingSourceFactory = { pdfDao.getPaginatedPdfsInPhysicalFolderRaw(query) }).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }
    private suspend fun syncPhysicalFoldersWithDb() {
        val rootPath = deviceStorage.getPdfProRootFolder()
        val rootDir = File(rootPath)
        if (rootDir.exists() && rootDir.isDirectory) {
            rootDir.walkTopDown().filter { it.isDirectory && it.absolutePath != rootPath }.forEach { folder ->
                val parentFile = folder.parentFile?.absolutePath
                val normalizedParent = if (parentFile == rootPath) null else parentFile
                pdfDao.insertFolder(FolderEntity(absolutePath = folder.absolutePath, name = folder.name, parentPath = normalizedParent, isVault = false))
            }
        }
    }
    override fun getPaginatedManagedPdfs(parentPath: String?, isVault: Boolean, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val isVaultInt = if (isVault) 1 else 0
        val parentCondition = if (parentPath == null) "parentPath IS NULL" else "parentPath = ?"
        val query = if (parentPath == null) SimpleSQLiteQuery(getSortQuery("SELECT * FROM pdf_table WHERE $parentCondition AND isVault = $isVaultInt", sortType)) else SimpleSQLiteQuery(getSortQuery("SELECT * FROM pdf_table WHERE $parentCondition AND isVault = $isVaultInt", sortType), arrayOf(parentPath))
        return androidx.paging.Pager(config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false), pagingSourceFactory = { pdfDao.getPaginatedPdfsByParentRaw(query) }).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }
    override suspend fun getUncategorizedPdfIdsFast() = pdfDao.getUncategorizedPdfIdsFast()
    override suspend fun getFavoritePdfIdsFast() = pdfDao.getFavoritePdfIdsFast()
    override suspend fun getManagedPdfIdsFast(parentPath: String?, isVault: Boolean) = pdfDao.getManagedPdfIdsFast(parentPath, isVault)
    override suspend fun getPhysicalFolderPdfIdsFast(folderPath: String) = pdfDao.getPhysicalFolderPdfIdsFast(folderPath)

    override suspend fun markPdfsForWorker(pdfIds: List<String>, batchId: Long) = pdfDao.markPdfsForWorker(pdfIds, batchId)
    override suspend fun getPdfsForWorkerBatch(batchId: Long): List<PdfFile> = pdfDao.getPdfsByWorkerBatchId(batchId).map { it.toDomainModel() }
}
