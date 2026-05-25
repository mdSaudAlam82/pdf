package com.edu.pdf.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Folder(
    val folderId: String,
    val name: String,
    val parentFolderId: String? = null,
    val pdfCount: Int = 0,
    val isVault: Boolean = false,
    val createdAt: Long = 0L,
    val lastOpenedTime: Long = 0L
)
