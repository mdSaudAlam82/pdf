package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Fts4 // 🌟 Aapka sahi pakda hua FTS4
@Keep
@Fts4(contentEntity = PdfEntity::class)
@Entity(tableName = "pdf_fts_table")
data class PdfFtsEntity(
    val name: String
)