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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

// 🌟 1. STRICT MVI STATE
data class PdfPickerState(
    val currentFolderId: String? = null,
    val breadcrumbs: List<Folder> = emptyList(),
    val items: List<HomeItem> = emptyList(),
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

// 🌟 2. STRICT MVI ACTIONS
sealed interface PdfPickerAction {
    data class NavigateToFolder(val folder: Folder?) : PdfPickerAction
    data class OnSearchQueryChange(val query: String) : PdfPickerAction
    data class ToggleSelection(val pdfId: String) : PdfPickerAction
    data object ClearSelection : PdfPickerAction
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PdfPickerViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    private val _currentFolderId = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedIds = MutableStateFlow<PersistentSet<String>>(persistentSetOf())

    // 🌟 ZERO BLOAT: Breadcrumbs generator
    private val breadcrumbsFlow = _currentFolderId.flatMapLatest { folderId ->
        repository.getAllManagedFolders(isVault = false).map { allFolders ->
            val list = mutableListOf<Folder>()
            var curr = allFolders.find { it.folderId == folderId }
            while (curr != null) {
                list.add(0, curr)
                curr = allFolders.find { it.folderId == curr.parentFolderId }
            }
            list
        }
    }

    // 🌟 SMART ENGINE: Search OR Folder Navigation
    private val itemsFlow = combine(_currentFolderId, _searchQuery) { folderId, query -> Pair(folderId, query) }
        .flatMapLatest { (folderId, query) ->
            if (query.isNotBlank()) {
                // Search Mode: Sirf PDFs dikhao
                repository.searchPdfs(query).map { pdfs -> pdfs.map { HomeItem.PdfItem(it) } }
            } else {
                // Navigation Mode: Folders + PDFs dono dikhao
                combine(
                    repository.getManagedFolders(folderId, isVault = false),
                    repository.getManagedPdfs(folderId, isVault = false)
                ) { folders, pdfs ->
                    val fItems = folders.map { HomeItem.FolderItem(it) }.sortedBy { it.folder.name.lowercase() }
                    val pItems = pdfs.map { HomeItem.PdfItem(it) }.sortedByDescending { it.pdf.lastModified }
                    fItems + pItems
                }
            }
        }.flowOn(Dispatchers.IO)

    // 🌟 COMBINED UI STATE
    val state: StateFlow<PdfPickerState> = combine(
        _currentFolderId, breadcrumbsFlow, itemsFlow, _selectedIds, _searchQuery
    ) { folderId, breadcrumbs, items, selectedIds, query ->
        PdfPickerState(
            currentFolderId = folderId,
            breadcrumbs = breadcrumbs,
            items = items,
            selectedIds = selectedIds,
            searchQuery = query,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PdfPickerState())

    fun onAction(action: PdfPickerAction) {
        when (action) {
            is PdfPickerAction.NavigateToFolder -> {
                _currentFolderId.value = action.folder?.folderId
                _searchQuery.value = "" // Folder me jane par search clear kar do
            }
            is PdfPickerAction.OnSearchQueryChange -> _searchQuery.value = action.query
            is PdfPickerAction.ToggleSelection -> {
                val current = _selectedIds.value
                _selectedIds.value = if (current.contains(action.pdfId)) current.remove(action.pdfId) else current.add(action.pdfId)
            }
            is PdfPickerAction.ClearSelection -> _selectedIds.value = persistentSetOf()
        }
    }
}