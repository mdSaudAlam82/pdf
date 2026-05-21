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
import javax.inject.Inject

class PdfRepositoryImpl @Inject constructor(
    private val pdfDao: PdfDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val deviceStorage: DeviceStorageDataSource,
    private val vaultStorage: com.edu.pdf.data.source.VaultDataSource,
    private val userPreferences: com.edu.pdf.data.preferences.UserPreferences
) : PdfRepository {

    override fun getManagedFolders(parentPath: String?, isVault: Boolean): Flow<List<Folder>> {
        // 🌟 Simple clean query: Null will match Null
        return pdfDao.getFoldersByParentWithCount(parentPath, isVault).map { list -> list.map { it.toDomainModel() } }
    }

    override fun getAllManagedFolders(isVault: Boolean): Flow<List<Folder>> {
        return pdfDao.getAllFoldersWithCount(isVault).map { list -> list.map { it.toDomainModel() } }
    }

    override fun getManagedPdfs(parentPath: String?, isVault: Boolean): Flow<List<PdfFile>> {
        return pdfDao.getPdfsByParent(parentPath, isVault).map { list -> list.map { it.toDomainModel() } }
    }

    // 🌟 THE ELITE FIX: Physical Folder Creation Sync
    override suspend fun createManagedFolder(name: String, parentPath: String?, isVault: Boolean): Result<String> {
        return try {
            val root = parentPath ?: deviceStorage.getPdfProRootFolder()
            val targetFile = File(root, name)

            // 🛑 YAHAN ROK LIYA: Agar us exact jagah par same naam hai
            if (targetFile.exists()) {
                return Result.failure(Exception("A folder named '$name' already exists here!"))
            }

            // 1. Pehle Physical OS me folder banao
            val newPhysicalPath = deviceStorage.createPhysicalFolder(name, parentPath)
            if (newPhysicalPath != null) {
                // 2. Agar physical ban gaya, toh Database me Index karo
                pdfDao.insertFolder(FolderEntity(absolutePath = newPhysicalPath, name = name, parentPath = parentPath, isVault = isVault))
                Result.success(newPhysicalPath)
            } else {
                Result.failure(Exception("Failed to create physical folder. Check permissions."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun renameManagedFolder(oldPath: String, newName: String): Result<Unit> {
        val oldFile = File(oldPath)
        val parentDir = oldFile.parentFile ?: return Result.failure(Exception("Invalid folder path"))
        val targetFile = File(parentDir, newName)

        // 🛑 YAHAN ROK LIYA: (Maths aur maths ko alag rakhne ke liye ignoreCase = true lagaya hai)
        if (targetFile.exists() && !oldFile.name.equals(newName, ignoreCase = true)) {
            return Result.failure(Exception("A folder named '$newName' already exists here!"))
        }

        val newPhysicalPath = deviceStorage.renamePhysicalFile(oldPath, newName)

        return if (newPhysicalPath != null) {
            pdfDao.cascadeRenameFolders(oldPath, newPhysicalPath, newName)
            pdfDao.cascadeRenamePdfs(oldPath, newPhysicalPath)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Rename physically failed"))
        }
    }

    // 🌟 THE ELITE FIX: Deep Recursive Delete
    override suspend fun deleteManagedFolder(path: String): Result<Unit> = withContext(Dispatchers.IO) {
        // 1. Delete from OS permanently
        val success = deviceStorage.deletePhysicalPath(path)
        if (success) {
            // 🌟 ELITE FIX 1: Ek sath folder aur uske andar ka sab kuch DB se clear karo
            pdfDao.cascadeDeleteFolders(path)
            pdfDao.cascadeDeletePdfs(path)
            return@withContext Result.success(Unit)
        }
        return@withContext Result.failure(Exception("Could not delete folder physically"))
    }

    override suspend fun moveFolderToVirtualFolder(folderPath: String, targetPath: String?, isVault: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        val target = targetPath ?: deviceStorage.getPdfProRootFolder()

        // 🌟 ELITE FIX 2: INCEPTION LOOP GUARD (BULLETPROOF)
        // Check if we are moving folder into itself or its children
        val normalizedSource = File(folderPath).absolutePath
        val normalizedTarget = File(target).absolutePath
        
        if (normalizedTarget == normalizedSource || normalizedTarget.startsWith("$normalizedSource/")) {
            return@withContext Result.failure(Exception("Cannot move a folder into itself or its own sub-folder!"))
        }

        val newPhysicalPath = deviceStorage.movePhysicalFile(folderPath, target)

        return@withContext if (newPhysicalPath != null) {
            // 1. अंदर के सारे बच्चे (Sub-folders & PDFs) का पाथ अपडेट करो
            val newName = File(newPhysicalPath).name
            pdfDao.cascadeRenameFolders(folderPath, newPhysicalPath, newName)
            pdfDao.cascadeRenamePdfs(folderPath, newPhysicalPath)

            // 🌟 2. ELITE FIX: जिस फोल्डर को मूव किया है, उसका अपना 'parentPath' भी अपडेट करो!
            val newParentForMovedFolder = if (target == deviceStorage.getPdfProRootFolder()) null else target

            // 🌟 ELITE FIX: cascadeRenameFolders ने पाथ बदल दिया है,
            // इसलिए अब हम फोल्डर को उसके 'नये' पाथ से ही ढूंढेंगे!
            pdfDao.moveFolder(
                oldAbsolutePath = newPhysicalPath, // ✅ बस folderPath को हटाकर newPhysicalPath कर दो!
                newAbsolutePath = newPhysicalPath,
                newParentPath = newParentForMovedFolder,
                isVault = isVault
            )

            // 3. Move होने के बाद उसे Recent से हटाओ
            pdfDao.updateFolderLastOpenedTime(newPhysicalPath, 0L)

            // 🌟 SMART REFRESH: MediaStore ko turant batao taaki UI update ho jaye
            deviceStorage.syncWithMediaStore(folderPath, newPhysicalPath)

            Result.success(Unit)
        } else {
            Result.failure(Exception("Move physically failed. Check storage permissions or collision."))
        }
    }

    override suspend fun movePdfsToVirtualFolder(pdfIds: List<String>, targetPath: String?, isVault: Boolean): Int {
        var movedCount = 0
        val targetPhysicalDir = targetPath ?: deviceStorage.getPdfProRootFolder()

        pdfIds.chunked(200).forEach { chunkedIds ->
            val pdfsToMove = pdfDao.getPdfsByIds(chunkedIds)
            pdfsToMove.forEach { pdfToMove ->
                val pdfId = pdfToMove.id
                if (isVault && !pdfToMove.isVault) {
                    val securePath = vaultStorage.moveToInternalVault(pdfToMove.toDomainModel()) { oldPath ->
                        deviceStorage.syncWithMediaStore(oldPath, null)
                    }
                    if (securePath != null) {
                        pdfDao.updatePdfNameAndPath(pdfId, pdfToMove.name, securePath)
                        pdfDao.movePdfToFolder(pdfId, null, isVault = true)
                        movedCount++
                    }
                } else if (!isVault && pdfToMove.isVault) {
                    val restoredData = vaultStorage.restoreFromInternalVault(pdfToMove.path, pdfToMove.name)
                    if (restoredData != null) {
                        val (newId, newPath) = restoredData
                        pdfDao.updatePdfIdAndPath(oldId = pdfId, newId = newId, newPath = newPath)
                        pdfDao.movePdfToFolder(newId, targetPhysicalDir, isVault = false)
                        movedCount++
                    }
                } else {
                    // 🌟 NORMAL MOVE: Move in OS first, then update DB
                    val newPhysicalPath = deviceStorage.movePhysicalFile(pdfToMove.path, targetPhysicalDir)
                    if (newPhysicalPath != null) {
                        pdfDao.updatePdfNameAndPath(pdfId, pdfToMove.name, newPhysicalPath)
                        pdfDao.movePdfToFolder(pdfId, targetPath, isVault)
                        
                        // 🌟 SMART REFRESH: MediaStore sync for each file
                        deviceStorage.syncWithMediaStore(pdfToMove.path, newPhysicalPath)

                        movedCount++
                    }
                }
            }
            yield()
        }
        return movedCount
    }

    override suspend fun importPdfFromUri(uriString: String, targetPath: String?, isVault: Boolean, isPhysicalFolder: Boolean): Result<Unit> {
        return try {
            val uri = uriString.toUri()
            val importedFile = deviceStorage.importFileFromUri(uri, isVault, targetPath)

            if (importedFile != null) {
                val newEntity = com.edu.pdf.data.local.entity.PdfEntity(
                    id = importedFile.id,
                    name = importedFile.name,
                    path = importedFile.path,
                    sizeInBytes = importedFile.sizeInBytes,
                    lastModified = importedFile.lastModified,
                    parentPath = targetPath, // 🌟 Accurate Path Mapping
                    isVault = isVault,
                    isFavorite = false
                )
                withContext(NonCancellable) { pdfDao.insertPdf(newEntity) }
                Result.success(Unit)
            } else {
                Result.failure(Exception("File copying failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🌟 THE ELITE FIX: Flawless Scanning Engine
    // 🌟 THE ELITE FIX: Flawless Scanning Engine (PDFs + Folders Both)
    override suspend fun scanAndSavePdfs() {
        withContext(Dispatchers.IO) {

            // 🧹 1. GHOST CLEANUP FOR PDFS
            // Jo PDFs bahar se delete/rename hui hain, unhe hatao
            val allPublicPaths = pdfDao.getAllPublicPdfPaths()
            val stalePaths = allPublicPaths.filter { path -> !File(path).exists() }

            if (stalePaths.isNotEmpty()) {
                stalePaths.chunked(100).forEach { chunk ->
                    pdfDao.deletePdfsByPaths(chunk)
                    yield()
                }
            }

            // 🧹 2. 🌟 NAYA: GHOST CLEANUP FOR FOLDERS
            // Jo Folders bahar se delete/rename hue hain, unka purana naam DB se hatao
            val allFolderPaths = pdfDao.getAllStoredFolderPaths()
            val staleFolderPaths = allFolderPaths.filter { path -> !File(path).exists() }

            if (staleFolderPaths.isNotEmpty()) {
                staleFolderPaths.forEach { path ->
                    pdfDao.deleteFolder(path)
                }
            }

            // 📁 3. Physical Folders (Naye folders) ko DB me dalo
            syncPhysicalFoldersWithDb()

            // 📄 4. PURE DEVICE SCAN (Nayi PDFs ko DB me dalo)
            deviceStorage.processDevicePdfUpdates { chunk ->
                pdfDao.insertAllPdfs(chunk.map { it.toEntity().copy(isVault = false) })
                yield()
            }

            userPreferences.setInitialScanCompleted(true)
            userPreferences.updateLastSyncTime(System.currentTimeMillis())
        }
    }

    override suspend fun renamePdf(pdf: PdfFile, newName: String): Boolean {
        // Automatically .pdf extension laga dega agar user bhool gaya
        val finalName = if (newName.endsWith(".pdf", ignoreCase = true)) newName else "$newName.pdf"
        val newPhysicalPath = deviceStorage.renamePhysicalFile(pdf.path, finalName)

        if (newPhysicalPath != null) {
            pdfDao.updatePdfNameAndPath(pdf.id, finalName, newPhysicalPath)
            return true
        }
        return false
    }

    override suspend fun deletePdfs(pdfs: List<PdfFile>): Boolean {
        val trashedIds = deviceStorage.moveToTrash(pdfs)
        if (trashedIds.isNotEmpty()) {
            trashedIds.chunked(100).forEach { chunk -> pdfDao.deletePdfsByIds(chunk); yield() }
        }
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
        val baseQuery = """
            SELECT * FROM pdf_table 
            WHERE isVault = 0 
            AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))
        """.trimIndent()

        val query = SimpleSQLiteQuery(getSortQuery(baseQuery, sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }
    // 🌟 THE ELITE FIX: Database se Paging 3 ke page mangwane ka logic
    // 🌟 THE ELITE FIX: Database se Paging 3 ke page mangwane ka logic
    override fun getAllPdfsPaged(sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val baseQuery = """
            SELECT * FROM pdf_table 
            WHERE isVault = 0 
            AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))
        """.trimIndent()

        val query = SimpleSQLiteQuery(getSortQuery(baseQuery, sortType))

        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false),
            pagingSourceFactory = { pdfDao.getUncategorizedPdfsPaged(query) }
        ).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
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
        val fullQueryString = getSortQuery(baseQuery, sortType)
        val query = SimpleSQLiteQuery(fullQueryString, arrayOf(folderPath, folderPath))

        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false),
            pagingSourceFactory = { pdfDao.getPaginatedPdfsInPhysicalFolderRaw(query) }
        ).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }

    // 🌟 ELITE FIX: Ye function physical storage scan karke pehle se bane folders ko Database me wapas layega
    // 🌟 ELITE FIX: Ye function physical storage scan karke pehle se bane folders ko Database me wapas layega
    private suspend fun syncPhysicalFoldersWithDb() {
        val rootPath = deviceStorage.getPdfProRootFolder()
        val rootDir = File(rootPath)

        if (rootDir.exists() && rootDir.isDirectory) {
            rootDir.walkTopDown()
                .filter { it.isDirectory && it.absolutePath != rootPath }
                .forEach { folder ->
                    // 🌟 ELITE FIX: Agar iska parent 'PdfPro' root hai, toh parentPath NULL rakho
                    val parentFile = folder.parentFile?.absolutePath
                    val normalizedParent = if (parentFile == rootPath) null else parentFile

                    // बस इतना ही रखना है! कोई -1 वाला लॉजिक नहीं। (PdfDao में IGNORE अपना काम खुद कर लेगा)
                    pdfDao.insertFolder(
                        FolderEntity(
                            absolutePath = folder.absolutePath,
                            name = folder.name,
                            parentPath = normalizedParent,
                            isVault = false
                        )
                    )
                }
        }
    }
    override fun getPaginatedManagedPdfs(parentPath: String?, isVault: Boolean, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val isVaultInt = if (isVault) 1 else 0
        val parentCondition = if (parentPath == null) "parentPath IS NULL" else "parentPath = ?"
        val baseQuery = "SELECT * FROM pdf_table WHERE $parentCondition AND isVault = $isVaultInt"

        val fullQueryString = getSortQuery(baseQuery, sortType)

        val query = if (parentPath == null) {
            SimpleSQLiteQuery(fullQueryString)
        } else {
            SimpleSQLiteQuery(fullQueryString, arrayOf(parentPath))
        }

        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false),
            pagingSourceFactory = { pdfDao.getPaginatedPdfsByParentRaw(query) }
        ).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }

    override suspend fun getUncategorizedPdfIdsFast() = pdfDao.getUncategorizedPdfIdsFast()
    override suspend fun getFavoritePdfIdsFast() = pdfDao.getFavoritePdfIdsFast()
    override suspend fun getManagedPdfIdsFast(parentPath: String?, isVault: Boolean) = pdfDao.getManagedPdfIdsFast(parentPath, isVault)
    override suspend fun getPhysicalFolderPdfIdsFast(folderPath: String) = pdfDao.getPhysicalFolderPdfIdsFast(folderPath)
}