package com.edu.pdf.data.local.dao

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

// 🌟 THE ELITE FIX: Let the Database do the math!
data class FolderWithCount(
    @Embedded val folder: FolderEntity,
    val pdfCount: Int
)

@Dao
interface PdfDao {

    // ==========================================
    // 📁 VIRTUAL FOLDERS (MANAGED HUB & VAULT)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderEntity)

    // 🌟 UPGRADED: Now joins the pdf_table to accurately count files
    @Query("""
        SELECT f.*, COUNT(p.id) as pdfCount 
        FROM managed_folders f 
        LEFT JOIN pdf_table p ON f.folderId = p.virtualParentId 
        WHERE f.parentFolderId IS :parentId AND f.isVault = :isVault 
        GROUP BY f.folderId 
        ORDER BY f.name ASC
    """)
    fun getFoldersByParentWithCount(parentId: String?, isVault: Boolean): Flow<List<FolderWithCount>>

    // 🌟 NAYA: Fetches ALL folders with their counts for the Move Picker Tree
    @Query("""
        SELECT f.*, COUNT(p.id) as pdfCount 
        FROM managed_folders f 
        LEFT JOIN pdf_table p ON f.folderId = p.virtualParentId 
        WHERE f.isVault = :isVault 
        GROUP BY f.folderId 
        ORDER BY f.name ASC
    """)
    fun getAllFoldersWithCount(isVault: Boolean): Flow<List<FolderWithCount>>

    @Query("UPDATE managed_folders SET name = :newName WHERE folderId = :folderId")
    suspend fun renameFolder(folderId: String, newName: String)

    @Query("DELETE FROM managed_folders WHERE folderId = :folderId")
    suspend fun deleteFolder(folderId: String)

    @Query("UPDATE managed_folders SET parentFolderId = :newParentId, isVault = :isVault WHERE folderId = :folderId")
    suspend fun moveFolder(folderId: String, newParentId: String?, isVault: Boolean)

    // ==========================================
    // 📄 PDF FILES (CORE)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPdf(pdf: PdfEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPdfs(pdfs: List<PdfEntity>)

    @Query("SELECT * FROM pdf_table WHERE virtualParentId IS :parentId AND isVault = :isVault ORDER BY lastModified DESC")
    fun getPdfsByParent(parentId: String?, isVault: Boolean): Flow<List<PdfEntity>>

    @Query("SELECT * FROM pdf_table WHERE lastOpenedTime > 0 AND isVault = 0 ORDER BY lastOpenedTime DESC LIMIT 50")
    fun getRecentPdfs(): Flow<List<PdfEntity>>

    @RawQuery(observedEntities = [PdfEntity::class])
    fun getSortedPdfs(query: SupportSQLiteQuery): Flow<List<PdfEntity>>

    @Query("UPDATE pdf_table SET lastOpenedTime = :time WHERE id = :pdfId")
    suspend fun updateLastOpenedTime(pdfId: String, time: Long)

    @Query("UPDATE pdf_table SET isFavorite = :isFav WHERE id = :pdfId")
    suspend fun updateFavoriteStatus(pdfId: String, isFav: Boolean)

    @Query("SELECT id FROM pdf_table")
    suspend fun getAllPdfIds(): List<String>

    @Query("DELETE FROM pdf_table WHERE id IN (:ids)")
    suspend fun deletePdfsByIds(ids: List<String>)

    @Query("UPDATE pdf_table SET name = :newName, path = :newPath WHERE id = :pdfId")
    suspend fun updatePdfNameAndPath(pdfId: String, newName: String, newPath: String)

    @Query("UPDATE pdf_table SET virtualParentId = :newParentId, isVault = :isVault WHERE id = :pdfId")
    suspend fun movePdfToVirtualFolder(pdfId: String, newParentId: String?, isVault: Boolean)

    // ==========================================
    // 📁 FOLDER RECENT QUERIES
    // ==========================================
    // 🌟 UPGRADED: Recent folders now also show their exact PDF counts
    @Query("""
        SELECT f.*, COUNT(p.id) as pdfCount 
        FROM managed_folders f 
        LEFT JOIN pdf_table p ON f.folderId = p.virtualParentId 
        WHERE f.lastOpenedTime > 0 AND f.isVault = 0 
        GROUP BY f.folderId 
        ORDER BY f.lastOpenedTime DESC LIMIT 50
    """)
    fun getRecentFoldersWithCount(): Flow<List<FolderWithCount>>

    @Query("UPDATE managed_folders SET lastOpenedTime = :time WHERE folderId = :folderId")
    suspend fun updateFolderLastOpenedTime(folderId: String, time: Long)

    // ==========================================
    // 🔍 FTS SEARCH ENGINE
    // ==========================================
    @Query("""
        SELECT pdf_table.* FROM pdf_table 
        JOIN pdf_fts_table ON pdf_table.roomId = pdf_fts_table.rowid 
        WHERE pdf_fts_table MATCH :query || '*' AND pdf_table.isVault = 0
        ORDER BY pdf_table.lastModified DESC
        LIMIT 50 -- 🌟 THE ELITE FIX: UI freeze hone se bachayega
    """)
    fun searchPdfsInDatabase(query: String): Flow<List<PdfEntity>>

    @Query("INSERT INTO pdf_fts_table(pdf_fts_table) VALUES('optimize')")
    suspend fun optimizeSearchIndex()
    // 🌟 2026 FIX: OOM Crash bachane ke liye (Sirf wahi files laao jo chahiye)
    @Query("SELECT * FROM pdf_table WHERE id IN (:ids)")
    suspend fun getPdfsByIds(ids: List<String>): List<PdfEntity>

    // 🌟 2026 FIX: Vault Sync Bug bachane ke liye (Sirf public files ke IDs laao)
    @Query("SELECT id FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfIds(): List<String>
    // 🌟 GOD MODE FIX: Path ke through compare karenge taaki MediaStore ke lag ki wajah se file delete na ho!
    @Query("SELECT path FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfPaths(): List<String>

    @Query("DELETE FROM pdf_table WHERE path IN (:paths)")
    suspend fun deletePdfsByPaths(paths: List<String>)

    @Query("""
        SELECT * FROM pdf_table 
        WHERE name LIKE '%' || :query || '%' AND isVault = :isVault 
        ORDER BY lastModified DESC 
        LIMIT 50 -- 🌟 THE ELITE FIX: RAM bachaane ke liye
    """)
    suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfEntity>

    // 🌟 THE VAULT FIX: Naya ID aur Path update karne ke liye
    @Query("UPDATE pdf_table SET id = :newId, path = :newPath WHERE id = :oldId")
    suspend fun updatePdfIdAndPath(oldId: String, newId: String, newPath: String)
    // ==========================================
    // 🌟 2026 ARCHITECT FIX: PHYSICAL FOLDER QUERY
    // ==========================================
    // Ye query directly database se wahi files layegi jo specific folder mein hain. No OOM Crash!
    @Query("""
        SELECT * FROM pdf_table 
        WHERE path LIKE :folderPath || '/%' 
        AND path NOT LIKE :folderPath || '/%/%' 
        AND isVault = 0 
        ORDER BY lastModified DESC
    """)
    fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfEntity>>

    // 🌟 2026 ARCHITECT FIX: Paging 3 Engine for Smooth 120fps Scrolling
    @Query("""
        SELECT * FROM pdf_table 
        WHERE path LIKE :folderPath || '/%' 
        AND path NOT LIKE :folderPath || '/%/%' 
        AND isVault = 0 
        ORDER BY lastModified DESC
    """)
    fun getPaginatedPdfsInPhysicalFolder(folderPath: String): androidx.paging.PagingSource<Int, PdfEntity>
}