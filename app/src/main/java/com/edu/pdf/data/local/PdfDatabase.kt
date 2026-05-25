package com.edu.pdf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edu.pdf.data.local.dao.PdfDao
import com.edu.pdf.data.local.dao.SearchHistoryDao
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.PdfEntity
import com.edu.pdf.data.local.entity.PdfFtsEntity
import com.edu.pdf.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        PdfEntity::class,
        SearchHistoryEntity::class,
        PdfFtsEntity::class,
        FolderEntity::class
    ],
    version = 11, // 🌟 NAYA: Bumped from 10 to 11
    exportSchema = true
)
abstract class PdfDatabase : RoomDatabase() {
    abstract val pdfDao: PdfDao
    abstract val searchHistoryDao: SearchHistoryDao
}