package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Keep
@Entity(
    tableName = "managed_folders",
    indices = [
        Index(value = ["absolutePath"], unique = true),
        Index(value = ["parentPath"]),
        Index(value = ["isVault"])
    ]
)
data class FolderEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val absolutePath: String,
    val name: String,
    val parentPath: String? = null,
    val isVault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastOpenedTime: Long = 0L
)
