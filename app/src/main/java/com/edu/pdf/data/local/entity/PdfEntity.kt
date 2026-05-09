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
        Index(value = ["lastOpenedTime"]),
        Index(value = ["isFavorite"]),
        Index(value = ["name"]),
        Index(value = ["lastModified"]),
        Index(value = ["virtualParentId"]), // 🌟 NAYA: Folder mapping ke liye
        Index(value = ["isVault"])          // 🌟 NAYA: Private Vault security
    ]
)
data class PdfEntity(
    @PrimaryKey(autoGenerate = true) val roomId: Long = 0,
    val id: String,
    val name: String,
    val path: String,
    val sizeInBytes: Long,
    val lastModified: Long,
    val isFavorite: Boolean,
    val lastOpenedTime: Long = 0L,

    // 🌟 GOD MODE UPGRADES:
    val virtualParentId: String? = null, // FolderEntity ke 'folderId' se link hoga
    val isVault: Boolean = false         // True matlab ye file Vault me lock hai
)