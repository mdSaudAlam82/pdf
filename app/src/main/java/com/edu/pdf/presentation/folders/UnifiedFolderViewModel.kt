package com.edu.pdf.presentation.folders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import androidx.paging.map
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeleteFolderUseCase
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import com.edu.pdf.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface UnifiedFolderSheetState {
    data object None : UnifiedFolderSheetState
    data object SortPicker : UnifiedFolderSheetState
    data class CreateFolderDialog(val parentId: String?) : UnifiedFolderSheetState
    data class ItemMenu(val item: HomeItem) : UnifiedFolderSheetState
    data class RenameDialog(val item: HomeItem, val currentName: String) : UnifiedFolderSheetState
    data class DetailsDialog(val item: HomeItem) : UnifiedFolderSheetState
    data class DeleteConfirm(val items: List<HomeItem>) : UnifiedFolderSheetState
    data class MovePicker(val items: List<HomeItem>) : UnifiedFolderSheetState
    data class AppPdfPicker(val allPdfs: List<com.edu.pdf.domain.model.PdfFile>) : UnifiedFolderSheetState
}

sealed interface UnifiedFolderEvent {
    data class ShowSnackbar(val message: String) : UnifiedFolderEvent
    data object ClearMultiSelection : UnifiedFolderEvent
}

sealed interface UnifiedFolderAction {
    data class OpenSheet(val state: UnifiedFolderSheetState) : UnifiedFolderAction
    data object CloseSheet : UnifiedFolderAction
    data class OnTextInputChange(val text: String) : UnifiedFolderAction
    data class UpdateSortType(val type: SortType) : UnifiedFolderAction
    data object ToggleViewMode : UnifiedFolderAction
    data object ConfirmCreateFolder : UnifiedFolderAction
    data object ConfirmRename : UnifiedFolderAction
    data object ConfirmDelete : UnifiedFolderAction
    data class ConfirmMove(val targetFolderId: String?) : UnifiedFolderAction
    data class ToggleFavorite(val pdfId: String, val isFav: Boolean) : UnifiedFolderAction
    data class ImportFile(val uriString: String) : UnifiedFolderAction
    data object OpenAppPdfPicker : UnifiedFolderAction
    data class ToggleVaultStatus(val pdf: com.edu.pdf.domain.model.PdfFile) : UnifiedFolderAction
    data class MovePdfsToCurrentFolder(val pdfIds: List<String>) : UnifiedFolderAction
}

data class UnifiedFolderUiState(
    val isLoading: Boolean = true,
    val isProcessing: Boolean = false,
    val folderType: FolderType = FolderType.PHYSICAL_DEVICE,
    val folderId: String = "",
    val folderName: String = "",
    val items: ImmutableList<HomeItem> = persistentListOf(),
    val breadcrumbs: ImmutableList<Folder> = persistentListOf(),
    val isGridView: Boolean = false,
    val sortType: SortType = SortType.DATE_DESC,
    val activeSheetState: UnifiedFolderSheetState = UnifiedFolderSheetState.None,
    val textInput: String = "",
    val canCreateSubFolders: Boolean = false,
    val canImport: Boolean = false,
    val canRenameOrDelete: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UnifiedFolderViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val userPreferences: UserPreferences,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = savedStateHandle.toRoute<Screen.UnifiedFolder>()
    private val currentFolderId = args.folderId
    private val currentFolderType = args.folderType

    private val actualFolderId = when {
        currentFolderId.isBlank() || currentFolderId == "root" || currentFolderId == "vault_root" -> null
        else -> currentFolderId
    }

    private val _events = Channel<UnifiedFolderEvent>()

    private val _sortType = MutableStateFlow(SortType.DATE_DESC)
    private val _internalState = MutableStateFlow(UnifiedFolderUiState(folderId = currentFolderId, folderName = args.folderName, folderType = currentFolderType))

    val pagedPhysicalItems = repository.getPaginatedPdfsInPhysicalFolder(actualFolderId ?: "")
        .map { pagingData ->
            pagingData.map { pdfFile ->
                HomeItem.PdfItem(pdfFile) as HomeItem
            }
        }
        .cachedIn(viewModelScope) // 🌟 Ye RAM ko optimize karta hai

    @OptIn(ExperimentalCoroutinesApi::class)
    private val physicalItemsFlow = _sortType.flatMapLatest { sort ->
        repository.getPdfsInPhysicalFolder(currentFolderId).map { pdfs ->
            val sortedPdfs = when(sort) {
                SortType.NAME_ASC -> pdfs.sortedBy { it.name.lowercase() }
                SortType.NAME_DESC -> pdfs.sortedByDescending { it.name.lowercase() }
                SortType.SIZE_DESC -> pdfs.sortedByDescending { it.sizeInBytes }
                SortType.SIZE_ASC -> pdfs.sortedBy { it.sizeInBytes }
                SortType.DATE_ASC -> pdfs.sortedBy { it.lastModified }
                SortType.DATE_DESC -> pdfs.sortedByDescending { it.lastModified }
            }
            sortedPdfs.map { HomeItem.PdfItem(it) }.toImmutableList()
        }
    }

    private val virtualItemsFlow = combine(
        repository.getManagedFolders(actualFolderId, currentFolderType == FolderType.SECURE_VAULT),
        repository.getManagedPdfs(actualFolderId, currentFolderType == FolderType.SECURE_VAULT)
    ) { folders, pdfs ->
        val folderItems = folders.map { HomeItem.FolderItem(it) }
        val pdfItems = pdfs.map { HomeItem.PdfItem(it) }
        (folderItems + pdfItems).toImmutableList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val itemsFlow = if (currentFolderType == FolderType.PHYSICAL_DEVICE) physicalItemsFlow else virtualItemsFlow

    private val breadcrumbsFlow = if (currentFolderType == FolderType.PHYSICAL_DEVICE) {
        flowOf(persistentListOf(Folder(folderId = currentFolderId, name = args.folderName)))
    } else {
        repository.getAllManagedFolders(isVault = currentFolderType == FolderType.SECURE_VAULT).map { allFolders ->
            val breadcrumbList = mutableListOf<Folder>()
            var curr = allFolders.find { it.folderId == actualFolderId }
            while (curr != null) {
                breadcrumbList.add(0, curr)
                curr = allFolders.find { it.folderId == curr.parentFolderId }
            }
            breadcrumbList.toImmutableList()
        }
    }

    val uiState: StateFlow<UnifiedFolderUiState> = combine(
        itemsFlow, breadcrumbsFlow, userPreferences.isFolderGridViewFlow, _sortType, _internalState
    ) { items, breadcrumbs, isGrid, sort, internal ->
        val isPhysical = currentFolderType == FolderType.PHYSICAL_DEVICE
        val isVault = currentFolderType == FolderType.SECURE_VAULT
        internal.copy(
            isLoading = false,
            items = items,
            breadcrumbs = breadcrumbs,
            isGridView = isGrid,
            sortType = sort,
            canCreateSubFolders = !isPhysical && !isVault,
            canImport = !isPhysical,
            canRenameOrDelete = !isPhysical
        )
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UnifiedFolderUiState())

    fun onAction(action: UnifiedFolderAction) {
        when (action) {
            is UnifiedFolderAction.OpenSheet -> {
                val initialText = if (action.state is UnifiedFolderSheetState.RenameDialog) action.state.currentName else ""
                _internalState.update { it.copy(activeSheetState = action.state, textInput = initialText) }
            }
            is UnifiedFolderAction.CloseSheet -> {
                _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None, textInput = "") }
            }
            is UnifiedFolderAction.OnTextInputChange -> {
                _internalState.update { it.copy(textInput = action.text) }
            }
            is UnifiedFolderAction.UpdateSortType -> {
                _sortType.value = action.type; onAction(UnifiedFolderAction.CloseSheet)
            }
            is UnifiedFolderAction.ToggleViewMode -> viewModelScope.launch { userPreferences.saveFolderGridViewPreference(!userPreferences.isFolderGridViewFlow.first()) }
            is UnifiedFolderAction.ToggleFavorite -> viewModelScope.launch { repository.toggleFavorite(action.pdfId, action.isFav) }

            is UnifiedFolderAction.ConfirmCreateFolder -> {
                val folderName = _internalState.value.textInput.trim()
                if (folderName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.createManagedFolder(folderName, actualFolderId, isVault = currentFolderType == FolderType.SECURE_VAULT)
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            _events.send(UnifiedFolderEvent.ShowSnackbar("Folder created"))
                        }
                    }
                }
            }

            is UnifiedFolderAction.ConfirmRename -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.RenameDialog ?: return
                val newName = _internalState.value.textInput.trim()
                if (newName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                    viewModelScope.launch(Dispatchers.IO) {
                        when (val item = state.item) {
                            is HomeItem.FolderItem -> repository.renameManagedFolder(item.folder.folderId, newName)
                            is HomeItem.PdfItem -> renamePdfUseCase(item.pdf, newName)
                        }
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                        }
                    }
                }
            }

            is UnifiedFolderAction.ConfirmDelete -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.DeleteConfirm ?: return
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val foldersToDelete = state.items.filterIsInstance<HomeItem.FolderItem>()
                    val pdfsToDelete = state.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf }
                    foldersToDelete.forEach { deleteFolderUseCase(it.folder.folderId) }
                    if (pdfsToDelete.isNotEmpty()) deletePdfsUseCase(pdfsToDelete)
                    withContext(Dispatchers.Main) {
                        _events.send(UnifiedFolderEvent.ClearMultiSelection)
                        _internalState.update { it.copy(isProcessing = false) }
                    }
                }
            }

            is UnifiedFolderAction.ConfirmMove -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.MovePicker ?: return
                if (action.targetFolderId == actualFolderId) {
                    _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None) }
                    return
                }
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val pdfIds = state.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf.id }
                    val folderIds = state.items.filterIsInstance<HomeItem.FolderItem>().map { it.folder.folderId }
                    if (pdfIds.isNotEmpty()) repository.movePdfsToVirtualFolder(pdfIds, action.targetFolderId, isVault = currentFolderType == FolderType.SECURE_VAULT)
                    folderIds.forEach { repository.moveFolderToVirtualFolder(it, action.targetFolderId, isVault = currentFolderType == FolderType.SECURE_VAULT) }
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(UnifiedFolderEvent.ClearMultiSelection)
                    }
                }
            }

            is UnifiedFolderAction.ImportFile -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = repository.importPdfFromUri(
                        uriString = action.uriString,
                        targetFolderId = actualFolderId,
                        isVault = currentFolderType == FolderType.SECURE_VAULT,
                        isPhysicalFolder = currentFolderType == FolderType.PHYSICAL_DEVICE
                    )
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        if (result.isSuccess) _events.send(UnifiedFolderEvent.ShowSnackbar("Imported Successfully"))
                        else _events.send(UnifiedFolderEvent.ShowSnackbar("Import Failed"))
                    }
                }
            }

            is UnifiedFolderAction.OpenAppPdfPicker -> {
                viewModelScope.launch {
                    val allPdfs = repository.getAllPdfs(SortType.DATE_DESC).first()
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.AppPdfPicker(allPdfs)) }
                    }
                }
            }

            is UnifiedFolderAction.MovePdfsToCurrentFolder -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    repository.movePdfsToVirtualFolder(action.pdfIds, actualFolderId, isVault = currentFolderType == FolderType.SECURE_VAULT)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(UnifiedFolderEvent.ShowSnackbar("Added successfully!"))
                    }
                }
            }

            is UnifiedFolderAction.ToggleVaultStatus -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val newVaultStatus = !action.pdf.isVault
                    repository.movePdfsToVirtualFolder(listOf(action.pdf.id), null, isVault = newVaultStatus)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(UnifiedFolderEvent.ShowSnackbar(if (newVaultStatus) "Secured in Vault" else "Restored to Public"))
                    }
                }
            }
        }
    }
}