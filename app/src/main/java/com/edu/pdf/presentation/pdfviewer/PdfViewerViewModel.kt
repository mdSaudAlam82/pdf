package com.edu.pdf.presentation.pdfviewer

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class PdfViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val pdfUri: Uri? = savedStateHandle.get<String>("pdfPath")?.let(fun(path: String): Uri? {
        return if (path.startsWith("content://") || path.startsWith("file://")) path.toUri() else Uri.fromFile(
            File(path)
        )
    })
    private val _isTopBarVisible = MutableStateFlow(true)
    val isTopBarVisible = _isTopBarVisible.asStateFlow()
    private val _isNightMode = MutableStateFlow(false)
    val isNightMode = _isNightMode.asStateFlow()
    fun setTopBarVisible(visible: Boolean) {
        if (_isTopBarVisible.value != visible) {
            _isTopBarVisible.value = visible
        }
    }
    fun toggleTopBar() { _isTopBarVisible.value = !_isTopBarVisible.value }
    fun toggleNightMode() { _isNightMode.value = !_isNightMode.value }
}