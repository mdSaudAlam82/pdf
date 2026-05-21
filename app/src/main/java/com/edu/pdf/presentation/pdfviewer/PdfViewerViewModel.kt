package com.edu.pdf.presentation.pdfviewer

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.MarkPdfAsOpenedUseCase
import com.edu.pdf.domain.usecase.PrintPdfUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import com.edu.pdf.domain.usecase.ToggleFavoriteUseCase
import com.edu.pdf.domain.usecase.ToggleVaultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class PdfViewerUiState(
    val pdfUri: Uri? = null,
    val isTopBarVisible: Boolean = true,
    val isNightMode: Boolean = false,
    val currentPageNumber: Int = 1,
    val pdfFileName: String = "",
    val pdfFile: PdfFile? = null
)

sealed interface PdfViewerEvent {
    data object NavigateBack : PdfViewerEvent
    data class ShowToast(val message: String) : PdfViewerEvent
}

sealed interface PdfViewerAction {
    data class SetTopBarVisible(val visible: Boolean) : PdfViewerAction
    data object ToggleTopBar : PdfViewerAction
    data object ToggleNightMode : PdfViewerAction
    data class RenameFile(val newName: String) : PdfViewerAction
    data object DeleteFile : PdfViewerAction
    data object ToggleVaultStatus : PdfViewerAction
    data class ToggleFavorite(val isFav: Boolean) : PdfViewerAction
    data class PrintFile(val context: Context) : PdfViewerAction
    data class MoveToFolder(val targetFolderId: String?) : PdfViewerAction // 🌟 NAYA: Move button ke liye
}

@HiltViewModel
class PdfViewerViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val toggleVaultUseCase: ToggleVaultUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val markPdfAsOpenedUseCase: MarkPdfAsOpenedUseCase,
    private val printPdfUseCase: PrintPdfUseCase,
    private val moveItemsUseCase: com.edu.pdf.domain.usecase.MoveItemsUseCase, // 🌟 NAYA: Move logic ke liye
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfViewerUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<PdfViewerEvent>()
    val events = _events.receiveAsFlow()

    init {
        val path = savedStateHandle.get<String>("pdfPath")
        val uri = path?.let {
            if (it.startsWith("content://") || it.startsWith("file://")) it.toUri()
            else Uri.fromFile(File(it))
        }
        val fileName = path?.let { File(it).nameWithoutExtension } ?: ""
        
        _uiState.update { it.copy(pdfUri = uri, pdfFileName = fileName) }

        path?.let { loadAndMarkPdf(it) }
    }

    private fun loadAndMarkPdf(path: String) {
        viewModelScope.launch {
            // 🌟 Use Case: Mark as opened
            markPdfAsOpenedUseCase(path)

            // 🌟 ELITE FIX: Database se actual PdfFile object nikalo
            repository.getAllPdfs(SortType.DATE_DESC).first().find { it.path == path }?.let { file ->
                _uiState.update { it.copy(pdfFile = file) }
            } ?: run {
                // Agar DB me nahi hai (e.g. bahar se open kiya), toh path se construct karo
                val file = PdfFile(
                    id = path,
                    name = File(path).name,
                    path = path,
                    sizeInBytes = File(path).length(),
                    lastModified = File(path).lastModified()
                )
                _uiState.update { it.copy(pdfFile = file) }
            }
        }
    }

    fun onAction(action: PdfViewerAction) {
        when (action) {
            is PdfViewerAction.SetTopBarVisible -> _uiState.update { it.copy(isTopBarVisible = action.visible) }
            is PdfViewerAction.ToggleTopBar -> _uiState.update { it.copy(isTopBarVisible = !it.isTopBarVisible) }
            is PdfViewerAction.ToggleNightMode -> _uiState.update { it.copy(isNightMode = !it.isNightMode) }
            
            is PdfViewerAction.RenameFile -> {
                viewModelScope.launch {
                    val currentPdf = _uiState.value.pdfFile ?: return@launch
                    val success = renamePdfUseCase(currentPdf, action.newName)
                    if (success) {
                        _uiState.update { it.copy(pdfFileName = action.newName) }
                        _events.send(PdfViewerEvent.ShowToast("Renamed successfully"))
                    }
                }
            }

            is PdfViewerAction.DeleteFile -> {
                viewModelScope.launch {
                    val currentPdf = _uiState.value.pdfFile ?: return@launch
                    val success = deletePdfsUseCase(listOf(currentPdf))
                    if (success) {
                        _events.send(PdfViewerEvent.ShowToast("Deleted successfully"))
                        _events.send(PdfViewerEvent.NavigateBack)
                    }
                }
            }

            is PdfViewerAction.ToggleVaultStatus -> {
                viewModelScope.launch {
                    val currentPdf = _uiState.value.pdfFile ?: return@launch
                    val result = toggleVaultUseCase(currentPdf)
                    if (result.isSuccess) {
                        val msg = if (currentPdf.isVault) "Removed from Vault" else "Moved to Vault"
                        _events.send(PdfViewerEvent.ShowToast(msg))
                        _events.send(PdfViewerEvent.NavigateBack)
                    }
                }
            }

            is PdfViewerAction.ToggleFavorite -> {
                viewModelScope.launch {
                    val currentPdf = _uiState.value.pdfFile ?: return@launch
                    toggleFavoriteUseCase(currentPdf.id, action.isFav)
                    _uiState.update { it.copy(pdfFile = it.pdfFile?.copy(isFavorite = action.isFav)) }
                }
            }

            is PdfViewerAction.PrintFile -> {
                val currentPdf = _uiState.value.pdfFile ?: return
                printPdfUseCase.invoke(action.context, currentPdf)
            }

            is PdfViewerAction.MoveToFolder -> {
                viewModelScope.launch {
                    val currentPdf = _uiState.value.pdfFile ?: return@launch
                    val result = moveItemsUseCase(
                        items = listOf(com.edu.pdf.domain.model.HomeItem.PdfItem(currentPdf)),
                        targetFolderId = action.targetFolderId,
                        isVault = false
                    )
                    if (result.isSuccess) {
                        _events.send(PdfViewerEvent.ShowToast("Moved successfully"))
                        _events.send(PdfViewerEvent.NavigateBack)
                    }
                }
            }
        }
    }
}
