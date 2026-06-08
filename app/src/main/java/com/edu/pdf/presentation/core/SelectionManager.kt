package com.edu.pdf.presentation.core

import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectionManager @Inject constructor() {
    private val _selectionState = MutableStateFlow(SelectionState())
    val selectionState = _selectionState.asStateFlow()

    data class SelectionState(
        val isSelectionMode: Boolean = false,
        val selectedIds: PersistentSet<String> = persistentSetOf()
    )

    fun toggleSelection(id: String) {
        _selectionState.update { state ->
            val newSelection = if (state.selectedIds.contains(id)) {
                state.selectedIds.remove(id)
            } else {
                state.selectedIds.add(id)
            }
            state.copy(
                selectedIds = newSelection,
                isSelectionMode = newSelection.isNotEmpty()
            )
        }
    }

    fun selectAll(ids: List<String>) {
        _selectionState.update { it.copy(
            selectedIds = persistentSetOf<String>().addAll(ids),
            isSelectionMode = ids.isNotEmpty()
        ) }
    }

    fun setSelectionMode(enabled: Boolean) {
        _selectionState.update { it.copy(
            isSelectionMode = enabled,
            selectedIds = if (!enabled) persistentSetOf() else it.selectedIds
        ) }
    }

    fun clearSelection() {
        _selectionState.update { it.copy(isSelectionMode = false, selectedIds = persistentSetOf()) }
    }
}
