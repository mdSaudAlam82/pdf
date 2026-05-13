package com.edu.pdf.presentation.pdfviewer

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri

// 🌟 1. STRICT MVI: State (Single Source of Truth)
// Yahan humne pdfUri ko add kar diya hai
data class PdfViewerUiState(
    val pdfUri: Uri? = null,
    val isTopBarVisible: Boolean = true,
    val isNightMode: Boolean = false,
    val isSearchActive: Boolean = false
)

// 🌟 2. STRICT MVI: Actions (Intents from UI)
sealed interface PdfViewerAction {
    data class SetTopBarVisible(val visible: Boolean) : PdfViewerAction
    data object ToggleTopBar : PdfViewerAction
    data object ToggleNightMode : PdfViewerAction
    data object ToggleSearch : PdfViewerAction
}

@HiltViewModel
class PdfViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 🌟 3. STRICT MVI: State Flow
    private val _uiState = MutableStateFlow(PdfViewerUiState())
    val uiState = _uiState.asStateFlow()

    // 🌟 NAYA: App start hote hi PDF ka path nikal kar hum state me daal rahe hain
    init {
        val path = savedStateHandle.get<String>("pdfPath")
        val uri = path?.let {
            if (it.startsWith("content://") || it.startsWith("file://")) it.toUri() else Uri.fromFile(File(it))
        }
        _uiState.update { it.copy(pdfUri = uri) }
    }

    // 🌟 4. STRICT MVI: Reducer (Brain of the ViewModel)
    fun onAction(action: PdfViewerAction) {
        when (action) {
            is PdfViewerAction.SetTopBarVisible -> {
                _uiState.update { it.copy(isTopBarVisible = action.visible) }
            }
            is PdfViewerAction.ToggleTopBar -> {
                _uiState.update { it.copy(isTopBarVisible = !it.isTopBarVisible) }
            }
            is PdfViewerAction.ToggleNightMode -> {
                _uiState.update { it.copy(isNightMode = !it.isNightMode) }
            }
            is PdfViewerAction.ToggleSearch -> {
                _uiState.update { it.copy(isSearchActive = !it.isSearchActive) }
            }
        }
    }
}