package com.edu.pdf.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderEntity)

    // 🌟 ELITE FIX: Grouping by ID (New Primary Key) taaki count accurate rahe
    @Query("""
        SELECT f.*, COUNT(p.id) as pdfCount 
        FROM managed_folders f 
        LEFT JOIN pdf_table p ON f.absolutePath = p.parentPath 
        WHERE f.parentPath IS :parentPath AND f.isVault = :isVault 
        GROUP BY f.id 
        ORDER BY f.name ASC
    """)
    fun getFoldersByParentWithCount(parentPath: String?, isVault: Boolean): Flow<List<FolderWithCount>>

    @Query("""
        SELECT f.*, COUNT(p.id) as pdfCount 
        FROM managed_folders f 
        LEFT JOIN pdf_table p ON f.absolutePath = p.parentPath 
        WHERE f.isVault = :isVault 
        GROUP BY f.id 
        ORDER BY f.name ASC
    """)
    fun getAllFoldersWithCount(isVault: Boolean): Flow<List<FolderWithCount>>

    @Query("UPDATE managed_folders SET name = :newName, absolutePath = :newPath, parentPath = :newParentPath WHERE absolutePath = :oldPath")
    suspend fun renameFolder(oldPath: String, newName: String, newPath: String, newParentPath: String?)

    @Query("DELETE FROM managed_folders WHERE absolutePath = :path")
    suspend fun deleteFolder(path: String)

    @Query("UPDATE managed_folders SET parentPath = :newParentPath, absolutePath = :newAbsolutePath, isVault = :isVault WHERE absolutePath = :oldAbsolutePath")
    suspend fun moveFolder(oldAbsolutePath: String, newAbsolutePath: String, newParentPath: String?, isVault: Boolean)

    @Query("UPDATE managed_folders SET lastOpenedTime = :time WHERE absolutePath = :path")
    suspend fun updateFolderLastOpenedTime(path: String, time: Long)

    @Query("""
        SELECT f.*, COUNT(p.id) as pdfCount 
        FROM managed_folders f 
        LEFT JOIN pdf_table p ON f.absolutePath = p.parentPath 
        WHERE f.lastOpenedTime > 0 AND f.isVault = 0 
        GROUP BY f.id 
        ORDER BY f.lastOpenedTime DESC LIMIT 50
    """)
    fun getRecentFoldersWithCount(): Flow<List<FolderWithCount>>


    // ==========================================
    // 🌪️ ELITE CASCADE RENAME ENGINE (Tumhara Original Smart Logic!)
    // ==========================================
    @Query("""
        UPDATE managed_folders 
        SET 
            absolutePath = REPLACE(absolutePath, :oldPath, :newPath), 
            parentPath = REPLACE(parentPath, :oldPath, :newPath), 
            name = CASE WHEN absolutePath = :oldPath THEN :newName ELSE name END 
        WHERE absolutePath = :oldPath OR absolutePath LIKE :oldPath || '/%'
    """)
    suspend fun cascadeRenameFolders(oldPath: String, newPath: String, newName: String)

    @Query("""
        UPDATE pdf_table 
        SET 
            path = REPLACE(path, :oldPath, :newPath), 
            parentPath = REPLACE(parentPath, :oldPath, :newPath) 
        WHERE parentPath = :oldPath OR path LIKE :oldPath || '/%'
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

    @Query("UPDATE pdf_table SET id = :newId, path = :newPath WHERE id = :oldId")
    suspend fun updatePdfIdAndPath(oldId: String, newId: String, newPath: String)

    @Query("SELECT id FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfIds(): List<String>

    @Query("SELECT path FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfPaths(): List<String>

    @Query("SELECT * FROM pdf_table WHERE id IN (:ids)")
    suspend fun getPdfsByIds(ids: List<String>): List<PdfEntity>


    // ==========================================
    // 🔍 FTS SEARCH & PAGING ENGINE
    // ==========================================
    @Query("""
        SELECT pdf_table.* FROM pdf_table 
        JOIN pdf_fts_table ON pdf_table.roomId = pdf_fts_table.rowid 
        WHERE pdf_fts_table MATCH :query || '*' AND pdf_table.isVault = 0
        ORDER BY pdf_table.lastModified DESC
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
    @Query("DELETE FROM managed_folders WHERE absolutePath LIKE :path || '%'")
    suspend fun cascadeDeleteFolders(path: String)

    @Query("DELETE FROM pdf_table WHERE path LIKE :path || '%' OR parentPath LIKE :path || '%'")
    suspend fun cascadeDeletePdfs(path: String)

}