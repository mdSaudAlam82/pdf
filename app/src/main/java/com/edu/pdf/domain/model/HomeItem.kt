package com.edu.pdf.domain.model

import androidx.compose.runtime.Immutable

/**
 * 🌟 GOD MODE WRAPPER:
 * Ye interface Compose ko batayega ki list me Folder aa raha hai ya PDF.
 * Dono ka common parameter 'id' aur 'lastModified' hai taaki sorting flawless ho.
 */
@Immutable
sealed interface HomeItem {
    val id: String
    val lastModified: Long

    data class FolderItem(val folder: Folder) : HomeItem {
        override val id: String = folder.folderId
        override val lastModified: Long = folder.createdAt
    }

    data class PdfItem(val pdf: PdfFile) : HomeItem {
        override val id: String = pdf.id
        override val lastModified: Long = pdf.lastModified
    }
}