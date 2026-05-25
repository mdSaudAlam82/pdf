package com.edu.pdf.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.PdfEntity
import kotlinx.coroutines.flow.Flow

data class FolderWithCount(
    @Embedded val folder: FolderEntity,
    val pdfCount: Int
)

@Dao
interface PdfDao {

    // ==========================================
    // 🧹 PHASE 3: GHOST CLEANUP WEAPONS (New Safety Net)
    // ==========================================
    @Query("DELETE FROM managed_folders WHERE absolutePath NOT IN (:existingPaths) AND isVault = 0")
    suspend fun deleteStaleFolders(existingPaths: List<String>)

    @Query("DELETE FROM pdf_table WHERE path NOT IN (:existingPaths) AND isVault = 0")
    suspend fun deleteStalePdfs(existingPaths: List<String>)

    @Query("SELECT absolutePath FROM managed_folders WHERE isVault = 0")
    suspend fun getAllStoredFolderPaths(): List<String>


    // ==========================================
    // 📁 PHYSICAL FOLDERS (MANAGED HUB & VAULT)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolder(folder: FolderEntity)

    // 🌟 ELITE FIX: Grouping by ID (New Primary Key) taaki count accurate rahe
    // 🌟 ELITE FIX: Ab ye PDF + Sub-folders dono ko ginega (34 files + 1 folder = 35 items)
    @Query("""
        SELECT f.*, 
        ( (SELECT COUNT(id) FROM pdf_table WHERE parentPath = f.absolutePath) + 
          (SELECT COUNT(id) FROM managed_folders WHERE parentPath = f.absolutePath) ) as pdfCount
        FROM managed_folders f 
        WHERE f.parentPath IS :parentPath AND f.isVault = :isVault 
        ORDER BY f.name ASC
    """)
    fun getFoldersByParentWithCount(parentPath: String?, isVault: Boolean): Flow<List<FolderWithCount>>

    @Query("""
        SELECT f.*, 
        ( (SELECT COUNT(id) FROM pdf_table WHERE parentPath = f.absolutePath) + 
          (SELECT COUNT(id) FROM managed_folders WHERE parentPath = f.absolutePath) ) as pdfCount
        FROM managed_folders f 
        WHERE f.isVault = :isVault 
        ORDER BY f.name ASC
    """)
    fun getAllFoldersWithCount(isVault: Boolean): Flow<List<FolderWithCount>>

    @Query("""
        SELECT f.*, 
        ( (SELECT COUNT(id) FROM pdf_table WHERE parentPath = f.absolutePath) + 
          (SELECT COUNT(id) FROM managed_folders WHERE parentPath = f.absolutePath) ) as pdfCount
        FROM managed_folders f 
        WHERE f.lastOpenedTime > 0 AND f.isVault = 0 
        ORDER BY f.lastOpenedTime DESC LIMIT 50
    """)
    fun getRecentFoldersWithCount(): Flow<List<FolderWithCount>>

    @Query("UPDATE managed_folders SET name = :newName, absolutePath = :newPath, parentPath = :newParentPath WHERE absolutePath = :oldPath")
    suspend fun renameFolder(oldPath: String, newName: String, newPath: String, newParentPath: String?)

    @Query("DELETE FROM managed_folders WHERE absolutePath = :path")
    suspend fun deleteFolder(path: String)

    @Query("UPDATE managed_folders SET parentPath = :newParentPath, absolutePath = :newAbsolutePath, isVault = :isVault WHERE absolutePath = :oldAbsolutePath")
    suspend fun moveFolder(oldAbsolutePath: String, newAbsolutePath: String, newParentPath: String?, isVault: Boolean)

    @Query("UPDATE managed_folders SET lastOpenedTime = :time WHERE absolutePath = :path")
    suspend fun updateFolderLastOpenedTime(path: String, time: Long)

    // ==========================================
    // 🌪️ ELITE CASCADE RENAME ENGINE (Tumhara Original Smart Logic!)
    // ==========================================
    // फोल्डर और उसके सब-फोल्डर्स का नाम बदलने का नया कोड
    @Query("""
        UPDATE managed_folders 
        SET 
            absolutePath = :newPath || SUBSTR(absolutePath, LENGTH(:oldPath) + 1),
            parentPath = CASE 
                WHEN parentPath = :oldPath THEN :newPath
                WHEN parentPath LIKE :oldPath || '/%' THEN :newPath || SUBSTR(parentPath, LENGTH(:oldPath) + 1)
                ELSE parentPath 
            END,
            name = CASE WHEN absolutePath = :oldPath THEN :newName ELSE name END
        WHERE absolutePath = :oldPath OR absolutePath LIKE :oldPath || '/%'
    """)
    suspend fun cascadeRenameFolders(oldPath: String, newPath: String, newName: String)

    @Query("""
        UPDATE pdf_table 
        SET 
            path = :newPath || SUBSTR(path, LENGTH(:oldPath) + 1),
            parentPath = CASE 
                WHEN parentPath = :oldPath THEN :newPath
                WHEN parentPath LIKE :oldPath || '/%' THEN :newPath || SUBSTR(parentPath, LENGTH(:oldPath) + 1)
                ELSE parentPath
            END
        WHERE parentPath = :oldPath OR parentPath LIKE :oldPath || '/%' OR path LIKE :oldPath || '/%'
    """)
    suspend fun cascadeRenamePdfs(oldPath: String, newPath: String)


    // ==========================================
    // 📄 PDF FILES (CORE)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPdf(pdf: PdfEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPdfs(pdfs: List<PdfEntity>)

    @Query("SELECT * FROM pdf_table WHERE parentPath IS :parentPath AND isVault = :isVault ORDER BY lastModified DESC")
    fun getPdfsByParent(parentPath: String?, isVault: Boolean): Flow<List<PdfEntity>>

    @Query("SELECT * FROM pdf_table WHERE parentPath IS :parentPath AND isVault = :isVault ORDER BY lastModified DESC")
    fun getPaginatedPdfsByParent(parentPath: String?, isVault: Boolean): PagingSource<Int, PdfEntity>
    @Query("SELECT * FROM pdf_table WHERE lastOpenedTime > 0 AND isVault = 0 ORDER BY lastOpenedTime DESC LIMIT 50")
    fun getRecentPdfs(): Flow<List<PdfEntity>>

    @RawQuery(observedEntities = [PdfEntity::class])
    fun getSortedPdfs(query: SupportSQLiteQuery): Flow<List<PdfEntity>>

    @RawQuery(observedEntities = [PdfEntity::class])
    fun getSortedPdfsPaged(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    @Query("UPDATE pdf_table SET lastOpenedTime = :time WHERE id = :pdfId")
    suspend fun updateLastOpenedTime(pdfId: String, time: Long)

    @Query("UPDATE pdf_table SET isFavorite = :isFav WHERE id = :pdfId")
    suspend fun updateFavoriteStatus(pdfId: String, isFav: Boolean)

    @Query("DELETE FROM pdf_table WHERE id IN (:ids)")
    suspend fun deletePdfsByIds(ids: List<String>)

    @Query("DELETE FROM pdf_table WHERE path IN (:paths)")
    suspend fun deletePdfsByPaths(paths: List<String>)

    @Query("UPDATE pdf_table SET name = :newName, path = :newPath WHERE id = :pdfId")
    suspend fun updatePdfNameAndPath(pdfId: String, newName: String, newPath: String)

    @Query("UPDATE pdf_table SET parentPath = :newParentPath, isVault = :isVault WHERE id = :pdfId")
    suspend fun movePdfToFolder(pdfId: String, newParentPath: String?, isVault: Boolean)

    // 🌟 THE 2026 PERSISTENCE ENGINE: Mark IDs for Worker (Bypass 10KB limit)
    @Query("UPDATE pdf_table SET lastOpenedTime = :workerBatchId WHERE id IN (:ids)")
    suspend fun markPdfsForWorker(ids: List<String>, workerBatchId: Long)

    @Query("SELECT * FROM pdf_table WHERE lastOpenedTime = :workerBatchId")
    suspend fun getPdfsByWorkerBatchId(workerBatchId: Long): List<PdfEntity>

    @Query("UPDATE pdf_table SET id = :newId, path = :newPath WHERE id = :oldId")
    suspend fun updatePdfIdAndPath(oldId: String, newId: String, newPath: String)

    @Transaction
    @Update
    suspend fun movePdfsBulk(updates: List<PdfEntity>)

    @Query("SELECT id FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfIds(): List<String>

    @Query("SELECT path FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfPaths(): List<String>

    @Query("SELECT * FROM pdf_table WHERE id IN (:ids)")
    suspend fun getPdfsByIds(ids: List<String>): List<PdfEntity>


    // ==========================================
    // 🔍 FTS SEARCH & PAGING ENGINE
    // ==========================================
    // ==========================================
    // 🔍 SMART SEARCH & PAGING ENGINE
    // ==========================================
    @Query("""
        SELECT * FROM pdf_table 
        WHERE name LIKE '%' || :query || '%' AND isVault = 0
        ORDER BY lastModified DESC
        LIMIT 50
    """)
    fun searchPdfsInDatabase(query: String): Flow<List<PdfEntity>>

    @Query("INSERT INTO pdf_fts_table(pdf_fts_table) VALUES('optimize')")
    suspend fun optimizeSearchIndex()

    @Query("""
        SELECT * FROM pdf_table 
        WHERE name LIKE '%' || :query || '%' AND isVault = :isVault 
        ORDER BY lastModified DESC 
        LIMIT 50
    """)
    suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfEntity>

    @Query("""
        SELECT * FROM pdf_table 
        WHERE path LIKE :folderPath || '/%' 
        AND path NOT LIKE :folderPath || '/%/%' 
        AND isVault = 0 
        ORDER BY lastModified DESC
    """)
    fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfEntity>>

    @Query("""
        SELECT * FROM pdf_table 
        WHERE path LIKE :folderPath || '/%' 
        AND path NOT LIKE :folderPath || '/%/%' 
        AND isVault = 0 
        ORDER BY lastModified DESC
    """)
    fun getPaginatedPdfsInPhysicalFolder(folderPath: String): PagingSource<Int, PdfEntity>
    // ==========================================
    // 🌪️ MISSING CASCADE DELETE ENGINE
    // ==========================================
    // फोल्डर्स को डिलीट करने के लिए नया सुरक्षित कोड
    @Query("""
        DELETE FROM managed_folders 
        WHERE absolutePath = :path OR absolutePath LIKE :path || '/%'
    """)
    suspend fun cascadeDeleteFolders(path: String)

    @Query("""
        DELETE FROM pdf_table 
        WHERE parentPath = :path OR parentPath LIKE :path || '/%' OR path LIKE :path || '/%'
    """)
    suspend fun cascadeDeletePdfs(path: String)

    // FILE: com/edu/pdf/data/local/dao/PdfDao.kt

    // 🌟 ELITE FIX: Home Screen के लिए Paging 3 Query
    @RawQuery(observedEntities = [PdfEntity::class])
    fun getUncategorizedPdfsPaged(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    // 🌟 ELITE MVI FIX: Dynamic Paging 3 Sorting
    @RawQuery(observedEntities = [PdfEntity::class])
    fun getPaginatedPdfsByParentRaw(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    @RawQuery(observedEntities = [PdfEntity::class])
    fun getPaginatedPdfsInPhysicalFolderRaw(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    // 🌟 FAST SELECT ALL ENGINE (Sirf ID nikalega, Zero Memory Full)
    @Query("SELECT id FROM pdf_table WHERE isVault = 0 AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))")
    suspend fun getUncategorizedPdfIdsFast(): List<String>

    @Query("SELECT id FROM pdf_table WHERE isFavorite = 1 AND isVault = 0")
    suspend fun getFavoritePdfIdsFast(): List<String>

    @Query("SELECT id FROM pdf_table WHERE parentPath IS :parentPath AND isVault = :isVault")
    suspend fun getManagedPdfIdsFast(parentPath: String?, isVault: Boolean): List<String>

    @Query("SELECT id FROM pdf_table WHERE path LIKE :folderPath || '/%' AND path NOT LIKE :folderPath || '/%/%' AND isVault = 0")
    suspend fun getPhysicalFolderPdfIdsFast(folderPath: String): List<String>


}