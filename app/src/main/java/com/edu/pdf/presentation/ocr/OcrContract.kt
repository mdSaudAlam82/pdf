package com.edu.pdf.presentation.ocr

import android.graphics.Bitmap

/**
 * 🌟 MVI State: OCR UI कैसा दिखेगा
 */
data class OcrUiState(
    val isSheetOpen: Boolean = false,
    val isLoading: Boolean = false,
    val extractedText: String = "",
    val errorMessage: String? = null
)

/**
 * 🌟 MVI Actions: यूज़र क्या-क्या कर सकता है
 * Note: हम Kotlin 2.x का 'data object' यूज़ कर रहे हैं जो मेमोरी के लिए बेस्ट है।
 */
sealed interface OcrAction {
    data class ExtractText(val bitmap: Bitmap) : OcrAction
    data object CloseSheet : OcrAction
    data object ClearError : OcrAction
}