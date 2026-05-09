package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Keep
@Entity(
    tableName = "managed_folders",
    indices = [
        Index(value = ["parentFolderId"]),
        Index(value = ["isVault"])
    ]
)
data class FolderEntity(
    @PrimaryKey val folderId: String,
    val name: String,
    val parentFolderId: String? = null,
    val isVault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastOpenedTime: Long = 0L // 🌟 NAYA: Track recent activity
)