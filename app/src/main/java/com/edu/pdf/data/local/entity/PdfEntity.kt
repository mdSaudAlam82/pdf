package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Keep
@Entity(
    tableName = "pdf_table",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["path"], unique = true), // 🌟 Path badlega, par unique hona chahiye
        Index(value = ["parentPath"]), // 🌟 Sync engine ke liye super-fast lookup
        Index(value = ["isVault"]),
        Index(value = ["lastOpenedTime"])
    ]
)
data class PdfEntity(
    @PrimaryKey(autoGenerate = true) val roomId: Long = 0,
    val id: String, // MediaStore ID ya UUID
    val name: String,
    val path: String,
    val sizeInBytes: Long,
    val lastModified: Long,
    val isFavorite: Boolean = false,
    val lastOpenedTime: Long = 0L,
    val parentPath: String? = null,
    val isVault: Boolean = false
)