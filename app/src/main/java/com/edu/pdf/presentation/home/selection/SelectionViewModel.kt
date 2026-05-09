package com.edu.pdf.presentation.home.selection

import androidx.lifecycle.ViewModel
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectionViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode = _isSelectionMode.asStateFlow()

    // 🌟 2026 FIX: Ab ye PersistentSet hai. Compose ko pata chalega ki ye stable hai.
    private val _selectedPdfs = MutableStateFlow<PersistentSet<String>>(persistentSetOf())
    val selectedPdfs = _selectedPdfs.asStateFlow()

    fun setSelectionMode(enabled: Boolean) {
        _isSelectionMode.value = enabled
        if (!enabled) _selectedPdfs.value = persistentSetOf() // Reset to empty immutable set
    }

    fun toggleSelection(pdfId: String) {
        val current = _selectedPdfs.value
        // 🌟 MAGIC: .add() aur .remove() automatically ek NAYA PersistentSet return karte hain fast speed me
        _selectedPdfs.value = if (current.contains(pdfId)) current.remove(pdfId) else current.add(pdfId)
    }

    fun selectAll(pdfIds: List<String>) {
        _selectedPdfs.value = pdfIds.toPersistentSet()
    }

    fun clearSelection() {
        _selectedPdfs.value = persistentSetOf()
    }
}