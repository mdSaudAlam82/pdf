package com.edu.pdf.presentation.ocr

import android.graphics.Bitmap
import com.edu.pdf.domain.ocr.OcrTextBlock

/**
 * 🌟 MVI State: Live Text Overlay kaisa dikhega aur kya yaad rakhega
 */
data class OcrUiState(
    val isLiveTextActive: Boolean = false, // Sheet nahi, ab screen ke upar ka transparent overlay active hoga
    val isLoading: Boolean = false,
    val capturedBitmap: Bitmap? = null, // Screen ka freeze kiya hua snapshot
    val extractedBlocks: List<OcrTextBlock> = emptyList(), // ML Kit se nikle hue words aur unke X,Y coordinates
    val errorMessage: String? = null
)

/**
 * 🌟 MVI Actions: User screen par kya actions le sakta hai
 */
sealed interface OcrAction {
    // Jab user Live Text button dabaye aur screen freeze karni ho
    data class StartLiveText(val bitmap: Bitmap) : OcrAction

    // Jab user 'X' (Close) dabaye aur wapas normal PDF dekhna ho
    data object StopLiveText : OcrAction

    data object ClearError : OcrAction
}