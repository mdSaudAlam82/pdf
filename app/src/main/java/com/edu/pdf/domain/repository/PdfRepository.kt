package com.edu.pdf.domain.repository

import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface PdfRepository {
    fun getManagedFolders(parentId: String?, isVault: Boolean = false): Flow<List<Folder>>
    fun getAllManagedFolders(isVault: Boolean = false): Flow<List<Folder>> // 🌟 NAYA: To fetch complete tree
    fun getManagedPdfs(parentId: String?, isVault: Boolean = false): Flow<List<PdfFile>>
    suspend fun createManagedFolder(name: String, parentId: String?, isVault: Boolean = false): Result<String>
    suspend fun renameManagedFolder(folderId: String, newName: String): Result<Unit>
    suspend fun deleteManagedFolder(folderId: String): Result<Unit>
    suspend fun importPdfFromUri(uriString: String, targetFolderId: String?, isVault: Boolean, isPhysicalFolder: Boolean = false): Result<Unit>
    suspend fun movePdfsToVirtualFolder(pdfIds: List<String>, targetFolderId: String?, isVault: Boolean): Int
    suspend fun moveFolderToVirtualFolder(folderId: String, targetFolderId: String?, isVault: Boolean): Result<Unit>
    fun getRecentPdfs(): Flow<List<PdfFile>>
    fun getAllPdfs(sortType: SortType): Flow<List<PdfFile>>
    fun getFavoritePdfs(sortType: SortType): Flow<List<PdfFile>>
    fun searchPdfs(query: String): Flow<List<PdfFile>>
    suspend fun scanAndSavePdfs()
    suspend fun checkFileExists(fileId: String): Boolean
    suspend fun updateLastOpenedTime(pdfId: String, time: Long)
    suspend fun toggleFavorite(pdfId: String, isFavorite: Boolean)
    suspend fun renamePdf(pdf: PdfFile, newName: String): Boolean
    suspend fun deletePdfs(pdfs: List<PdfFile>): Boolean
    fun getRecentSearchQueries(): Flow<List<String>>
    suspend fun saveSearchQuery(query: String)
    suspend fun deleteSearchQuery(query: String)
    suspend fun clearAllSearchHistory()
    fun getRecentFolders(): Flow<List<Folder>>
    suspend fun updateFolderLastOpenedTime(folderId: String, time: Long)
    suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfFile>
    fun getSecureVaultStreamUri(encryptedPath: String): String

    fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfFile>>

    fun getPaginatedPdfsInPhysicalFolder(folderPath: String): Flow<androidx.paging.PagingData<PdfFile>>
}