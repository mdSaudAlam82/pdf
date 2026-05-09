package com.edu.pdf.presentation.navigation

import kotlinx.serialization.Serializable
import com.edu.pdf.domain.model.FolderType

/**
 * 🌟 PURE MVVM & CLEAN NAVIGATION
 * Ise hum 'Type-Safe Navigation' kehte hain.
 * Har ek object ya class ek unique screen ko represent karti hai.
 */
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

    /**
     * 🌟 GOD MODE NAVIGATION UPGRADE:
     * Purane 'FolderDetail' aur 'NestedFolder' ko hata kar humne
     * ek 'UnifiedFolder' bana diya hai. Ab physical, virtual aur vault sab yahi khulenge.
     */
    @Serializable
    data class UnifiedFolder(
        val folderId: String,      // Physical Path ya Virtual UUID yahan aayega
        val folderName: String,
        val folderType: FolderType // Ye batayega ki folder kis type ka hai
    ) : Screen

    /**
     * PdfViewer: PDF open karne ke liye sirf uska file path chahiye.
     */
    @Serializable
    data class PdfViewer(val pdfPath: String) : Screen

    // 🌟 GOD MODE: Dedicated Vault Route
    @Serializable
    data object Vault : Screen
}