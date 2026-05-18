package com.edu.pdf.presentation.pdfviewer.ocr

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.edu.pdf.domain.ocr.OcrTextBlock

// 1. STATE: Jo UI (LiveTextOverlay) को दिखेगा
@Immutable
data class OcrState(
    val isLiveTextActive: Boolean = false,
    val isProcessing: Boolean = false,
    // 🌟 ELITE FIX: Bitmap को यहाँ से हटा दिया गया है ताकि Memory Leak ना हो!
    val extractedTextBlocks: List<OcrTextBlock> = emptyList(),
    val selectedText: String = "", // जो टेक्स्ट यूजर सेलेक्ट करेगा वो यहाँ सेव होगा
    val error: String? = null
)

// 2. ACTION: जो यूजर UI से भेजेगा
sealed interface OcrAction {
    data class StartLiveText(val bitmap: Bitmap) : OcrAction
    data class TextSelected(val text: String) : OcrAction // 🌟 NAYA: Gemini के लिए टेक्स्ट सेलेक्ट करने का एक्शन
    data object StopLiveText : OcrAction
}

// 3. EVENT: ViewModel से UI को वन-टाइम मैसेज (Toast) भेजने के लिए
sealed interface OcrEvent {
    data class ShowToast(val message: String) : OcrEvent
}