package com.edu.pdf.domain.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable

@Keep
@Immutable
data class PdfFile(
    val id: String,
    val name: String,
    val path: String,
    val sizeInBytes: Long,
    val lastModified: Long,
    val isFavorite: Boolean = false,
    val lastOpenedTime: Long = 0L,
    val virtualParentId: String? = null, // 🌟 NAYA: Ye file kis folder me hai?
    val isVault: Boolean = false         // 🌟 NAYA: Kya ye file locked hai?
)