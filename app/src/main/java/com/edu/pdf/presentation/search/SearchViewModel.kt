package com.edu.pdf.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PdfRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<PdfFile>> = _searchQuery
        .debounce(300L)
        .map { it.trim() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                repository.searchPdfs(query)
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val searchHistory: StateFlow<List<String>> = repository.getRecentSearchQueries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun saveSearchQuery(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch { repository.saveSearchQuery(query.trim()) }
        }
    }

    fun removeSearchQuery(query: String) {
        viewModelScope.launch { repository.deleteSearchQuery(query) }
    }

    fun clearAllHistory() {
        viewModelScope.launch { repository.clearAllSearchHistory() }
    }

    fun markPdfAsOpened(pdfId: String) {
        viewModelScope.launch { repository.updateLastOpenedTime(pdfId, System.currentTimeMillis()) }
    }

    fun toggleFavorite(pdf: PdfFile) {
        viewModelScope.launch { repository.toggleFavorite(pdf.id, !pdf.isFavorite) }
    }

    fun renamePdf(pdf: PdfFile, newName: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch { onResult(repository.renamePdf(pdf, newName)) }
    }

    fun deletePdf(pdf: PdfFile, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deletePdfs(listOf(pdf))
            onComplete()
        }
    }
}