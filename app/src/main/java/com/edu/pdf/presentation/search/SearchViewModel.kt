package com.edu.pdf.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
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

// 🌟 STRICT MVI: State Definition
data class SearchUiState(
    val query: String = "",
    val results: ImmutableList<PdfFile> = persistentListOf(),
    val history: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = false,
    val pdfToDelete: PdfFile? = null
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
    // 🌟 MVI FIX: Callbacks हटा दिए गए हैं
    data class RenamePdf(val pdf: PdfFile, val newName: String) : SearchAction
    data class ShowDeleteConfirmation(val pdf: PdfFile) : SearchAction
    data object DismissDeleteConfirmation : SearchAction
    data object ConfirmDeletePdf : SearchAction
}

// 🌟 MVI EVENT: ViewModel से UI को मैसेज (Toast) भेजने के लिए
sealed interface SearchEvent {
    data class ShowSnackbar(val message: String) : SearchEvent
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PdfRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _pdfToDelete = MutableStateFlow<PdfFile?>(null)
    private val _events = kotlinx.coroutines.channels.Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    // 🌟 INSTANT SEARCH: Debounce puri tarah hata diya
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

    // 🌟 SINGLE SOURCE OF TRUTH FOR UI
    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        searchResultsFlow,
        searchHistoryFlow,
        _pdfToDelete
    ) { query, results, history, pdfToDelete ->
        SearchUiState(
            query = query,
            results = results,
            history = history,
            isLoading = false,
            pdfToDelete = pdfToDelete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly, // 🌟 Changed to Eagerly for better test reliability
        initialValue = SearchUiState(isLoading = false) // 🌟 Set initial loading to false
    )

    // 🌟 THE REDUCER: UI bas Actions bhejega yahan
    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnQueryChange -> {
                _searchQuery.value = action.query
            }
            is SearchAction.ClearSearch -> {
                _searchQuery.value = ""
            }
            is SearchAction.SaveSearchQuery -> {
                if (action.query.isNotBlank()) {
                    viewModelScope.launch { repository.saveSearchQuery(action.query.trim()) }
                }
            }
            is SearchAction.RemoveHistoryItem -> {
                viewModelScope.launch { repository.deleteSearchQuery(action.query) }
            }
            is SearchAction.ClearAllHistory -> {
                viewModelScope.launch { repository.clearAllSearchHistory() }
            }
            is SearchAction.MarkPdfAsOpened -> {
                viewModelScope.launch { repository.updateLastOpenedTime(action.pdfId, System.currentTimeMillis()) }
            }
            is SearchAction.ToggleFavorite -> {
                viewModelScope.launch { repository.toggleFavorite(action.pdf.id, !action.pdf.isFavorite) }
            }
            is SearchAction.RenamePdf -> {
                // 🌟 MVI FIX: Action लेकर Event वापस भेजना
                viewModelScope.launch(Dispatchers.IO) {
                    val success = repository.renamePdf(action.pdf, action.newName)
                    withContext(Dispatchers.Main) {
                        val msg = if (success) "Renamed successfully" else "Rename failed"
                        _events.send(SearchEvent.ShowSnackbar(msg))
                    }
                }
            }
            is SearchAction.ShowDeleteConfirmation -> {
                _pdfToDelete.value = action.pdf
            }
            is SearchAction.DismissDeleteConfirmation -> {
                _pdfToDelete.value = null
            }
            is SearchAction.ConfirmDeletePdf -> {
                val targetPdf = _pdfToDelete.value
                if (targetPdf != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.deletePdfs(listOf(targetPdf))
                        withContext(Dispatchers.Main) {
                            _pdfToDelete.value = null // पॉप-अप बंद करो
                            _events.send(SearchEvent.ShowSnackbar("File deleted permanently"))
                        }
                    }
                }
            }
        }
    }
}