package com.edu.pdf.presentation.common.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovePickerViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovePickerState())
    val state = _state.asStateFlow()

    private val _events = Channel<MovePickerEvent>()
    val events = _events.receiveAsFlow()

    init {
        // 🌟 AUTOMATIC FOLDER LOAD: Kisi bhi screen se khule, ye khud folders load karega
        viewModelScope.launch {
            repository.getAllManagedFolders(isVault = false)
                .onEach { folders ->
                    onAction(MovePickerAction.UpdateFolders(folders))
                }
                .launchIn(this)
        }
    }

    fun onAction(action: MovePickerAction) {
        when (action) {
            is MovePickerAction.UpdateFolders -> {
                _state.update { currentState ->
                    val subFolders = action.folders.filter { it.parentFolderId == currentState.currentParentId && !it.isVault }.sortedBy { it.name.lowercase() }
                    val breadcrumbs = calculateBreadcrumbs(action.folders, currentState.currentParentId)
                    currentState.copy(allFolders = action.folders, subFolders = subFolders, breadcrumbs = breadcrumbs, isLoading = false)
                }
            }
            is MovePickerAction.NavigateTo -> {
                _state.update { currentState ->
                    val subFolders = currentState.allFolders.filter { it.parentFolderId == action.folderId && !it.isVault }.sortedBy { it.name.lowercase() }
                    val breadcrumbs = calculateBreadcrumbs(currentState.allFolders, action.folderId)
                    currentState.copy(currentParentId = action.folderId, subFolders = subFolders, breadcrumbs = breadcrumbs)
                }
            }
            // 🌟 LATEST 2026 MVI LOGIC: Ek step piche jane ka calculation
            is MovePickerAction.NavigateBack -> {
                _state.update { currentState ->
                    // Breadcrumbs se pichle folder ki ID nikaalo (dropLast(1) karke)
                    val parentId = currentState.breadcrumbs.dropLast(1).lastOrNull()?.folderId

                    val subFolders = currentState.allFolders.filter { it.parentFolderId == parentId && !it.isVault }.sortedBy { it.name.lowercase() }
                    val breadcrumbs = calculateBreadcrumbs(currentState.allFolders, parentId)
                    currentState.copy(currentParentId = parentId, subFolders = subFolders, breadcrumbs = breadcrumbs)
                }
            }
            is MovePickerAction.UpdateFolderName -> {
                _state.update { it.copy(newFolderName = action.name) }
            }
            is MovePickerAction.ToggleCreateFolderDialog -> {
                _state.update { it.copy(isCreatingFolder = action.show, newFolderName = "") }
            }
            is MovePickerAction.CreateAndEnterFolder -> createAndEnterFolder()
            is MovePickerAction.ConfirmMoveHere -> {
                viewModelScope.launch {
                    _events.send(MovePickerEvent.MoveToTarget(_state.value.currentParentId))
                }
            }
        }
    }

    private fun calculateBreadcrumbs(allFolders: List<Folder>, currentId: String?): List<Folder> {
        val breadcrumbs = mutableListOf<Folder>()
        var curr = allFolders.find { it.folderId == currentId }
        while (curr != null) {
            breadcrumbs.add(0, curr)
            curr = allFolders.find { it.folderId == curr.parentFolderId }
        }
        return breadcrumbs
    }

    private fun createAndEnterFolder() {
        val name = _state.value.newFolderName.trim()
        if (name.isBlank()) return

        val currentParent = _state.value.currentParentId

        viewModelScope.launch(Dispatchers.IO) {
            // 🌟 FIX: डायलॉग को खुला रहने दो, सिर्फ लोडिंग ऑन करो
            _state.update { it.copy(isLoading = true) }
            val result = repository.createManagedFolder(name, currentParent, isVault = false)

            result.onSuccess { newFolderId ->
                onAction(MovePickerAction.NavigateTo(newFolderId))
                // 🌟 FIX: जब सच में फोल्डर बन जाए, तभी डायलॉग को बंद करो (isCreatingFolder = false)
                _state.update { it.copy(isCreatingFolder = false, newFolderName = "", isLoading = false) }
            }.onFailure { e ->
                // 🌟 FIX: अगर एरर आया, तो बस लोडिंग बंद होगी और टोस्ट आएगा, डायलॉग/कीबोर्ड खुला रहेगा!
                _state.update { it.copy(isLoading = false) }
                _events.send(MovePickerEvent.ShowSnackbar(e.message ?: "Error creating folder"))
            }
        }
    }
}