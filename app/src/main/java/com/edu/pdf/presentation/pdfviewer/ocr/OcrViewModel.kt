package com.edu.pdf.presentation.pdfviewer.ocr

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.ocr.TextRecognitionEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val textRecognitionEngine: TextRecognitionEngine
) : ViewModel() {

    private val _state = MutableStateFlow(OcrState())
    val state = _state.asStateFlow()

    // 🌟 ELITE FIX: UI को Toast मैसेज भेजने के लिए Channel का इस्तेमाल
    private val _events = Channel<OcrEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: OcrAction) {
        when (action) {
            is OcrAction.StartLiveText -> {
                // UI ओपन करो और लोडिंग दिखाओ
                _state.update {
                    it.copy(
                        isLiveTextActive = true,
                        isProcessing = true,
                        error = null,
                        selectedText = "" // पुराना सिलेक्शन हटा दो
                    )
                }
                extractText(action.bitmap)
            }

            is OcrAction.TextSelected -> {
                // जब यूजर कोई टेक्स्ट कॉपी/सेलेक्ट करे
                _state.update { it.copy(selectedText = action.text) }
            }

            is OcrAction.StopLiveText -> {
                // ओवरले बंद कर दो और सब रीसेट कर दो
                _state.update { OcrState() }
            }
        }
    }

    private fun extractText(bitmap: Bitmap) {
        viewModelScope.launch {
            val result = textRecognitionEngine.extractTextFromBitmap(bitmap)

            result.onSuccess { blocks ->
                _state.update { it.copy(extractedTextBlocks = blocks, isProcessing = false) }
                // 🌟 ELITE MEMORY FIX: काम खत्म होने के बाद Bitmap को मेमोरी से उड़ा दो!
                bitmap.recycle()
            }.onFailure { exception ->
                _state.update { it.copy(error = exception.message, isProcessing = false) }
                // UI को एरर का टोस्ट (Toast) दिखाओ
                _events.send(OcrEvent.ShowToast(exception.message ?: "Scanner Failed"))
                bitmap.recycle()
            }
        }
    }
}