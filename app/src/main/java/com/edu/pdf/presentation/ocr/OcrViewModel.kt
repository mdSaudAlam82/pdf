package com.edu.pdf.presentation.ocr

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.ocr.TextRecognitionEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val textRecognitionEngine: TextRecognitionEngine
) : ViewModel() {

    // 🌟 UDF Rule: _state प्राइवेट रहेगा (ताकि UI इसे बदल ना सके)
    private val _state = MutableStateFlow(OcrUiState())
    // state पब्लिक रहेगा (UI सिर्फ इसे Observe करेगा)
    val state: StateFlow<OcrUiState> = _state.asStateFlow()

    /**
     * UI सिर्फ इसी फंक्शन को कॉल करेगा और Action पास करेगा
     */
    fun onAction(action: OcrAction) {
        when (action) {
            is OcrAction.ExtractText -> processOcr(action.bitmap)

            OcrAction.CloseSheet -> _state.update {
                it.copy(isSheetOpen = false, extractedText = "")
            }

            OcrAction.ClearError -> _state.update {
                it.copy(errorMessage = null)
            }
        }
    }

    private fun processOcr(bitmap: Bitmap) {
        viewModelScope.launch {
            // 1. Loading शुरू करो और Sheet ओपन करो
            _state.update {
                it.copy(isLoading = true, isSheetOpen = true, errorMessage = null)
            }

            // 2. Engine से टेक्स्ट निकालो (Background thread)
            val result = textRecognitionEngine.extractTextFromBitmap(bitmap)

            // 3. Kotlin Result API (Success/Failure handle करना)
            result.fold(
                onSuccess = { text ->
                    _state.update {
                        it.copy(isLoading = false, extractedText = text)
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Scanning failed")
                    }
                }
            )
        }
    }
}