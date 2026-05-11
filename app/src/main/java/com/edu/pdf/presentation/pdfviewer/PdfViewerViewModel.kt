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
// Future me Gemini AI aur Scanner ki saari states yahin aayengi!
data class PdfViewerUiState(
    val isTopBarVisible: Boolean = true,
    val isNightMode: Boolean = false,
    val isSearchActive: Boolean = false // 🌟 NAYA: Find in page ke liye
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

    val pdfUri: Uri? = savedStateHandle.get<String>("pdfPath")?.let(fun(path: String): Uri? {
        return if (path.startsWith("content://") || path.startsWith("file://")) path.toUri() else Uri.fromFile(File(path))
    })

    // 🌟 3. STRICT MVI: State Flow
    private val _uiState = MutableStateFlow(PdfViewerUiState())
    val uiState = _uiState.asStateFlow()

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