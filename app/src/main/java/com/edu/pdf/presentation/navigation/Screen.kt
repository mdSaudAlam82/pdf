package com.edu.pdf.presentation.navigation

import kotlinx.serialization.Serializable
import com.edu.pdf.domain.model.FolderType

sealed interface Screen {

    @Serializable
    data object Permission : Screen
    @Serializable
    data object Home : Screen

    @Serializable
    data object Folders : Screen

    @Serializable
    data object Tools : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Search : Screen

    // 🌟 THE ELITE FIX: Default values hata diye taaki crash na ho aur strict matching ho
    @Serializable
    data class UnifiedFolder(
        val folderId: String,
        val folderName: String,
        val folderType: FolderType
    ) : Screen

    @Serializable
    data class PdfViewer(val pdfPath: String) : Screen

    // 🌟 GOD MODE: Dedicated Vault Route
    @Serializable
    data object Vault : Screen
}