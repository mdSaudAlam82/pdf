package com.edu.pdf.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface SearchSheetState {
    data object None : SearchSheetState
    data class PdfMenu(val pdf: PdfFile) : SearchSheetState
    data class RenameDialog(val pdf: PdfFile, val currentName: String) : SearchSheetState
    data class MovePicker(val pdf: PdfFile) : SearchSheetState
    data class DeleteConfirmation(val pdf: PdfFile) : SearchSheetState
}

// 🌟 STRICT MVI: State Definition
data class SearchUiState(
    val query: String = "",
    val results: ImmutableList<PdfFile> = persistentListOf(),
    val history: ImmutableList<String> = persistentListOf(),
    val isGridView: Boolean = false,
    val isLoading: Boolean = false,
    val activeSheetState: SearchSheetState = SearchSheetState.None,
    val renameInput: String = ""
)

// 🌟 STRICT MVI: Actions (Intents from UI)
sealed interface SearchAction {
    data class OnQueryChange(val query: String) : SearchAction
    data object ClearSearch : SearchAction
    data class SaveSearchQuery(val query: String) : SearchAction
    data class RemoveHistoryItem(val query: String) : SearchAction
    data object ClearAllHistory : SearchAction
    data class MarkPdfAsOpened(val pdfId: String) : SearchAction
    data class ToggleFavorite(val pdf: PdfFile) : SearchAction
    data class UpdateRenameInput(val input: String) : SearchAction
    data object ConfirmRename : SearchAction
    data class OpenSheet(val state: SearchSheetState) : SearchAction
    data object CloseSheet : SearchAction
    data object ConfirmDeletePdf : SearchAction
    data class MoveToFolder(val pdf: PdfFile, val targetFolderId: String?) : SearchAction
    data class ToggleVaultStatus(val pdf: PdfFile) : SearchAction
    data object ToggleViewMode : SearchAction
}

// 🌟 MVI EVENT: ViewModel से UI को मैसेज (Toast) भेजने के लिए
sealed interface SearchEvent {
    data class ShowSnackbar(val message: String) : SearchEvent
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val markPdfAsOpenedUseCase: MarkPdfAsOpenedUseCase,
    private val moveItemsUseCase: MoveItemsUseCase,
    private val toggleVaultUseCase: ToggleVaultUseCase,
    private val updateUserPreferencesUseCase: UpdateUserPreferencesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _internalState = MutableStateFlow(SearchUiState())
    private val _events = kotlinx.coroutines.channels.Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    // 🌟 INSTANT SEARCH
    private val searchResultsFlow = _searchQuery
        .map { it.trim() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(persistentListOf())
            } else {
                repository.searchPdfs(query).map { it.toImmutableList() }
            }
        }

    private val searchHistoryFlow = repository.getRecentSearchQueries()
        .map { it.toImmutableList() }

    // 🌟 SINGLE SOURCE OF TRUTH FOR UI (Universal Sync)
    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        searchResultsFlow,
        searchHistoryFlow,
        updateUserPreferencesUseCase.userPreferences.isGridViewFlow, // 🌟 SHARED SYNC
        _internalState
    ) { query, results, history, isGrid, internal ->
        internal.copy(
            query = query,
            results = results,
            history = history,
            isGridView = isGrid,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SearchUiState(isLoading = false)
    )

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnQueryChange -> _searchQuery.value = action.query
            is SearchAction.ClearSearch -> _searchQuery.value = ""
            is SearchAction.SaveSearchQuery -> if (action.query.isNotBlank()) {
                viewModelScope.launch { repository.saveSearchQuery(action.query.trim()) }
            }
            is SearchAction.RemoveHistoryItem -> viewModelScope.launch { repository.deleteSearchQuery(action.query) }
            is SearchAction.ClearAllHistory -> viewModelScope.launch { repository.clearAllSearchHistory() }
            is SearchAction.MarkPdfAsOpened -> viewModelScope.launch { markPdfAsOpenedUseCase(action.pdfId) }
            is SearchAction.ToggleFavorite -> viewModelScope.launch { 
                toggleFavoriteUseCase(action.pdf.id, !action.pdf.isFavorite) 
                _internalState.update { it.copy(activeSheetState = SearchSheetState.None) }
            }
            is SearchAction.UpdateRenameInput -> _internalState.update { it.copy(renameInput = action.input) }
            is SearchAction.ConfirmRename -> {
                val sheetState = _internalState.value.activeSheetState
                if (sheetState is SearchSheetState.RenameDialog) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val success = renamePdfUseCase(sheetState.pdf, _internalState.value.renameInput)
                        if (success) {
                            _events.send(SearchEvent.ShowSnackbar("Renamed successfully"))
                            _internalState.update { it.copy(activeSheetState = SearchSheetState.None) }
                        }
                    }
                }
            }
            is SearchAction.OpenSheet -> _internalState.update { 
                it.copy(
                    activeSheetState = action.state,
                    renameInput = if (action.state is SearchSheetState.RenameDialog) action.state.currentName else it.renameInput
                ) 
            }
            is SearchAction.CloseSheet -> _internalState.update { it.copy(activeSheetState = SearchSheetState.None) }
            is SearchAction.ConfirmDeletePdf -> {
                val sheetState = _internalState.value.activeSheetState
                if (sheetState is SearchSheetState.DeleteConfirmation) {
                    viewModelScope.launch(Dispatchers.IO) {
                        deletePdfsUseCase(listOf(sheetState.pdf))
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(activeSheetState = SearchSheetState.None) }
                            _events.send(SearchEvent.ShowSnackbar("File deleted permanently"))
                        }
                    }
                }
            }
            is SearchAction.MoveToFolder -> viewModelScope.launch {
                _internalState.update { it.copy(activeSheetState = SearchSheetState.None) }
                val result = moveItemsUseCase(
                    selectedIds = setOf(action.pdf.id),
                    folderIds = emptyList(),
                    targetFolderId = action.targetFolderId,
                    sourcePath = null, // Search results can stay in search
                    isVault = false
                )
                if (result.isSuccess) _events.send(SearchEvent.ShowSnackbar("Moved successfully"))
            }
            is SearchAction.ToggleVaultStatus -> viewModelScope.launch {
                val result = toggleVaultUseCase(action.pdf)
                if (result.isSuccess) {
                    val msg = if (action.pdf.isVault) "Removed from Vault" else "Secured in Vault"
                    _events.send(SearchEvent.ShowSnackbar(msg))
                }
            }
            is SearchAction.ToggleViewMode -> viewModelScope.launch {
                updateUserPreferencesUseCase.toggleGridView()
            }
        }
    }
}
