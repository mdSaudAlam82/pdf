package com.edu.pdf.presentation.common.picker

import com.edu.pdf.domain.model.Folder

// Pure Immutable State
data class MovePickerState(
    val allFolders: List<Folder> = emptyList(), // Holds the entire folder tree
    val currentParentId: String? = null,
    val breadcrumbs: List<Folder> = emptyList(),
    val subFolders: List<Folder> = emptyList(),
    val isCreatingFolder: Boolean = false,
    val newFolderName: String = "",
    val isLoading: Boolean = false
)

// Actions triggered from UI
sealed interface MovePickerAction {
    data class UpdateFolders(val folders: List<Folder>) : MovePickerAction // Receives folders from Home
    data class NavigateTo(val folderId: String?) : MovePickerAction

    data object NavigateBack : MovePickerAction
    data class UpdateFolderName(val name: String) : MovePickerAction
    data class ToggleCreateFolderDialog(val show: Boolean) : MovePickerAction
    data object CreateAndEnterFolder : MovePickerAction
    data object ConfirmMoveHere : MovePickerAction
}

// One-time Events to UI
sealed interface MovePickerEvent {
    data class MoveToTarget(val targetFolderId: String?) : MovePickerEvent
    data class ShowSnackbar(val message: String) : MovePickerEvent
}