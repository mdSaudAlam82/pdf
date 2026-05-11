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
import javax.inject.Inject

// 🌟 STRICT MVI: State Definition
data class SearchUiState(
    val query: String = "",
    val results: ImmutableList<PdfFile> = persistentListOf(),
    val history: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = false
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
    data class RenamePdf(val pdf: PdfFile, val newName: String, val onResult: (Boolean) -> Unit) : SearchAction
    data class DeletePdf(val pdf: PdfFile, val onComplete: () -> Unit) : SearchAction
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PdfRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    // 🌟 RE-ENGINEERED FLOWS FOR STRICT MVI STATE
    private val searchResultsFlow = _searchQuery
        .debounce(300L) // Prevent DB crash while fast typing
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
        searchHistoryFlow
    ) { query, results, history ->
        SearchUiState(
            query = query,
            results = results,
            history = history,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState(isLoading = true)
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
                viewModelScope.launch { action.onResult(repository.renamePdf(action.pdf, action.newName)) }
            }
            is SearchAction.DeletePdf -> {
                viewModelScope.launch {
                    repository.deletePdfs(listOf(action.pdf))
                    action.onComplete()
                }
            }
        }
    }
}