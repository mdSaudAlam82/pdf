package com.edu.pdf.domain.repository

import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface PdfRepository {
    fun getManagedFolders(parentPath: String?, isVault: Boolean = false): Flow<List<Folder>>
    fun getAllManagedFolders(isVault: Boolean = false): Flow<List<Folder>>
    fun getManagedPdfs(parentPath: String?, isVault: Boolean = false): Flow<List<PdfFile>>

    suspend fun createManagedFolder(name: String, parentPath: String?, isVault: Boolean = false): Result<String>
    suspend fun renameManagedFolder(oldPath: String, newName: String): Result<Unit>
    suspend fun deleteManagedFolder(path: String): Result<Unit>

    suspend fun importPdfFromUri(uriString: String, targetPath: String?, isVault: Boolean = false, isPhysicalFolder: Boolean = false): Result<Unit>
    suspend fun movePdfsToVirtualFolder(pdfIds: List<String>, targetPath: String?, isVault: Boolean): Int
    suspend fun moveFolderToVirtualFolder(folderPath: String, targetPath: String?, isVault: Boolean): Result<Unit>

    fun getRecentPdfs(): Flow<List<PdfFile>>
    
    fun getUncategorizedPdfs(sortType: SortType): Flow<List<PdfFile>>
    fun getAllPdfs(sortType: SortType): Flow<List<PdfFile>>

    fun getAllPdfsPaged(sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>>
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
    suspend fun updateFolderLastOpenedTime(path: String, time: Long)
    suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfFile>
    fun getSecureVaultStreamUri(encryptedPath: String): String

    fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfFile>>
    fun getPaginatedPdfsInPhysicalFolder(folderPath: String, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>>

    fun getPaginatedManagedPdfs(parentPath: String?, isVault: Boolean = false, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>>

    suspend fun getUncategorizedPdfIdsFast(): List<String>
    suspend fun getFavoritePdfIdsFast(): List<String>
    suspend fun getManagedPdfIdsFast(parentPath: String?, isVault: Boolean): List<String>
    suspend fun getPhysicalFolderPdfIdsFast(folderPath: String): List<String>

    suspend fun markPdfsForWorker(pdfIds: List<String>, batchId: Long)
    suspend fun getPdfsForWorkerBatch(batchId: Long): List<PdfFile>
}
