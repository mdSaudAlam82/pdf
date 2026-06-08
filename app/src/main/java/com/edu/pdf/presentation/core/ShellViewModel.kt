package com.edu.pdf.presentation.core

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ShellUiState(
    val isSelectionMode: Boolean = false,
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val isBottomBarVisible: Boolean = true
)

sealed interface ShellAction {
    data class SetSelectionMode(val enabled: Boolean) : ShellAction
    data class ToggleSelection(val id: String) : ShellAction
    data class SelectAll(val ids: List<String>) : ShellAction
    data object ClearSelection : ShellAction
    data class SetBottomBarVisible(val visible: Boolean) : ShellAction
}

@HiltViewModel
class ShellViewModel @Inject constructor(
    private val selectionManager: SelectionManager
) : ViewModel() {

    private val _isBottomBarVisible = MutableStateFlow(true)

    val uiState: StateFlow<ShellUiState> = combine(
        selectionManager.selectionState,
        _isBottomBarVisible
    ) { selection, bottomBarVisible ->
        ShellUiState(
            isSelectionMode = selection.isSelectionMode,
            selectedIds = selection.selectedIds,
            isBottomBarVisible = bottomBarVisible
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShellUiState())

    fun onAction(action: ShellAction) {
        when (action) {
            is ShellAction.SetSelectionMode -> selectionManager.setSelectionMode(action.enabled)
            is ShellAction.ToggleSelection -> selectionManager.toggleSelection(action.id)
            is ShellAction.SelectAll -> selectionManager.selectAll(action.ids)
            is ShellAction.ClearSelection -> selectionManager.clearSelection()
            is ShellAction.SetBottomBarVisible -> _isBottomBarVisible.value = action.visible
        }
    }
}
