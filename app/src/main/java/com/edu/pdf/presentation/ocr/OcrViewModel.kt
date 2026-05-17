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

    private val _state = MutableStateFlow(OcrUiState())
    val state: StateFlow<OcrUiState> = _state.asStateFlow()

    fun onAction(action: OcrAction) {
        when (action) {
            // Naya action: Jab user button dabaye
            is OcrAction.StartLiveText -> processLiveText(action.bitmap)

            // Naya action: Jab user cancel kare
            OcrAction.StopLiveText -> _state.update {
                it.copy(
                    isLiveTextActive = false,
                    capturedBitmap = null, // Photo memory se hata do
                    extractedBlocks = emptyList(), // Text memory se hata do
                    errorMessage = null
                )
            }

            OcrAction.ClearError -> _state.update {
                it.copy(errorMessage = null)
            }
        }
    }

    private fun processLiveText(bitmap: Bitmap) {
        viewModelScope.launch {
            // 1. Loading shuru karo, Live mode ON karo, aur screenshot (bitmap) memory me save karo
            _state.update {
                it.copy(
                    isLoading = true,
                    isLiveTextActive = true,
                    capturedBitmap = bitmap,
                    errorMessage = null
                )
            }

            // 2. Engine se blocks (Text + Coordinates) nikaalo
            val result = textRecognitionEngine.extractTextFromBitmap(bitmap)

            // 3. Result aane par UI ko batao
            result.fold(
                onSuccess = { blocks ->
                    _state.update {
                        it.copy(isLoading = false, extractedBlocks = blocks)
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