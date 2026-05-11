package com.edu.pdf.presentation.common.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// 🌟 1. STRICT MVI STATE (No separate variables)
data class PdfPickerState(
    val currentFolderId: String? = null,
    val breadcrumbs: List<Folder> = emptyList(),
    val items: List<HomeItem> = emptyList(),
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

sealed interface PdfPickerAction {
    data class NavigateToFolder(val folder: Folder?) : PdfPickerAction
    data class OnSearchQueryChange(val query: String) : PdfPickerAction
    data class ToggleSelection(val pdfId: String) : PdfPickerAction
    data object ClearSelection : PdfPickerAction
}

@HiltViewModel
class PdfPickerViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    // 🌟 THE ELITE FIX: Single Source of Truth
    private val _state = MutableStateFlow(PdfPickerState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null
    private var dataLoadJob: Job? = null

    init {
        loadFolderData(null) // Load root folder initially
    }

    // 🌟 THE REDUCER: UI sirf Intents bhejta hai, aur ye function unhe handle karta hai
    fun onAction(action: PdfPickerAction) {
        when (action) {
            is PdfPickerAction.NavigateToFolder -> {
                val folderId = action.folder?.folderId
                _state.update { it.copy(currentFolderId = folderId, searchQuery = "", isLoading = true) }
                loadFolderData(folderId)
            }
            is PdfPickerAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query, isLoading = true) }
                executeSearch(action.query)
            }
            is PdfPickerAction.ToggleSelection -> {
                _state.update { currentState ->
                    val currentSelection = currentState.selectedIds
                    val newSelection = if (currentSelection.contains(action.pdfId)) {
                        currentSelection.remove(action.pdfId)
                    } else {
                        currentSelection.add(action.pdfId)
                    }
                    currentState.copy(selectedIds = newSelection)
                }
            }
            is PdfPickerAction.ClearSelection -> {
                _state.update { it.copy(selectedIds = persistentSetOf()) }
            }
        }
    }

    private fun loadFolderData(folderId: String?) {
        dataLoadJob?.cancel()
        dataLoadJob = viewModelScope.launch(Dispatchers.IO) {

            val foldersFlow = repository.getManagedFolders(folderId, isVault = false)

            // 🌟 ELITE FIX: Agar Picker Root par hai, toh pure phone ki PDF lao!
            val pdfsFlow = if (folderId == null) {
                repository.getAllPdfs(com.edu.pdf.domain.model.SortType.DATE_DESC)
            } else {
                repository.getManagedPdfs(folderId, isVault = false)
            }

            combine(
                foldersFlow,
                pdfsFlow,
                repository.getAllManagedFolders(isVault = false)
            ) { folders, pdfs, allFolders ->
                // Generate Breadcrumbs
                val breadcrumbList = mutableListOf<Folder>()
                var curr = allFolders.find { it.folderId == folderId }
                while (curr != null) {
                    breadcrumbList.add(0, curr)
                    curr = allFolders.find { it.folderId == curr.parentFolderId }
                }

                val items = folders.map { HomeItem.FolderItem(it) }.sortedBy { it.folder.name.lowercase() } +
                        pdfs.map { HomeItem.PdfItem(it) }.sortedByDescending { it.pdf.lastModified }

                Pair(breadcrumbList, items)
            }.collect { (breadcrumbs, items) ->
                _state.update { it.copy(breadcrumbs = breadcrumbs, items = items, isLoading = false) }
            }
        }
    }

    private fun executeSearch(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            loadFolderData(_state.value.currentFolderId)
            return
        }

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300) // 🌟 PRO FIX: Debounce! User ke type karte hi DB crash hone se bachayega
            repository.searchPdfsFast(query, isVault = false).let { pdfs ->
                val items = pdfs.map { HomeItem.PdfItem(it) }
                _state.update { it.copy(items = items, isLoading = false) }
            }
        }
    }
}