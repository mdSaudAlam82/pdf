package com.edu.pdf.presentation.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
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
    data object AppPdfPicker : UnifiedFolderSheetState
}

sealed interface UnifiedFolderEvent {
    data class ShowSnackbar(val message: String) : UnifiedFolderEvent
    data object ClearMultiSelection : UnifiedFolderEvent
}

sealed interface UnifiedFolderAction {
    data class InitializeFolder(val id: String, val name: String, val type: FolderType) : UnifiedFolderAction
    data class ToggleSelection(val id: String) : UnifiedFolderAction
    data class SetSelectionMode(val enabled: Boolean) : UnifiedFolderAction
    data class SelectAll(val ids: List<String>) : UnifiedFolderAction
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
    data object SelectAllItems : UnifiedFolderAction
}

data class UnifiedFolderUiState(
    val isLoading: Boolean = true,
    val isProcessing: Boolean = false,
    val folderType: FolderType = FolderType.PHYSICAL_DEVICE,
    val folderId: String = "",
    val folderName: String = "",
    val folders: ImmutableList<HomeItem.FolderItem> = persistentListOf(),
    val breadcrumbs: ImmutableList<Folder> = persistentListOf(),
    val foldersTree: ImmutableList<Folder> = persistentListOf(), 
    val isSelectionMode: Boolean = false,
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val isGridView: Boolean = false,
    val sortType: SortType = SortType.DATE_DESC,
    val activeSheetState: UnifiedFolderSheetState = UnifiedFolderSheetState.None,
    val textInput: String = "",
    val canCreateSubFolders: Boolean = false,
    val canImport: Boolean = false,
    val canRenameOrDelete: Boolean = false,
    val shouldShowBreadcrumbs: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UnifiedFolderViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val moveItemsUseCase: MoveItemsUseCase,
    private val toggleVaultUseCase: ToggleVaultUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val importPdfUseCase: ImportPdfUseCase,
    private val updateUserPreferencesUseCase: UpdateUserPreferencesUseCase
) : ViewModel() {

    private val _currentFolderId = MutableStateFlow<String?>(null)
    private val _currentFolderName = MutableStateFlow("")
    private val _currentFolderType = MutableStateFlow(FolderType.PHYSICAL_DEVICE)

    private val _events = Channel<UnifiedFolderEvent>()
    val events = _events.receiveAsFlow()

    private val _sortType = MutableStateFlow(SortType.DATE_DESC)
    private val _internalState = MutableStateFlow(UnifiedFolderUiState())

    private fun initFolderData(id: String, name: String, type: FolderType) {
        val decodedId = android.net.Uri.decode(id)
        val decodedName = android.net.Uri.decode(name)
        val actualId = if (decodedId.isBlank() || decodedId == "root") null else decodedId

        _currentFolderId.value = actualId
        _currentFolderName.value = decodedName
        _currentFolderType.value = type
        _internalState.update { it.copy(folderId = decodedId, folderName = decodedName, folderType = type) }
    }

    val pagedPdfsFlow: Flow<PagingData<HomeItem.PdfItem>> = combine(_currentFolderId, _currentFolderType, _sortType) { id, type, sort ->
        Triple(id, type, sort)
    }.flatMapLatest { (id, type, sort) ->
        if (type == FolderType.PHYSICAL_DEVICE) {
            repository.getPaginatedPdfsInPhysicalFolder(id ?: "", sortType = sort)
        } else {
            repository.getPaginatedManagedPdfs(id, isVault = type == FolderType.SECURE_VAULT, sortType = sort)
        }
    }.map { pagingData -> pagingData.map { HomeItem.PdfItem(it) } }
        .cachedIn(viewModelScope)

    private val foldersFlow = combine(_currentFolderId, _currentFolderType, _sortType) { id, type, sort ->
        Triple(id, type, sort)
    }.flatMapLatest { (id, type, sort) ->
        if (type == FolderType.PHYSICAL_DEVICE) {
            flowOf(persistentListOf())
        } else {
            repository.getManagedFolders(id, isVault = type == FolderType.SECURE_VAULT).map { folders ->
                val sortedFolders = when (sort) {
                    SortType.NAME_ASC -> folders.sortedBy { it.name.lowercase() }
                    SortType.NAME_DESC -> folders.sortedByDescending { it.name.lowercase() }
                    SortType.DATE_DESC -> folders.sortedByDescending { it.createdAt }
                    SortType.DATE_ASC -> folders.sortedBy { it.createdAt }
                    SortType.SIZE_DESC -> folders.sortedByDescending { it.pdfCount }
                    SortType.SIZE_ASC -> folders.sortedBy { it.pdfCount }
                }
                sortedFolders.map { HomeItem.FolderItem(it) }.toImmutableList()
            }
        }
    }.flowOn(Dispatchers.Default)

    private val breadcrumbsFlow = combine(_currentFolderId, _currentFolderName, _currentFolderType) { id, name, type -> Triple(id, name, type) }
        .flatMapLatest { (id, name, type) ->
            if (type == FolderType.PHYSICAL_DEVICE) {
                flowOf(persistentListOf(Folder(folderId = id ?: "", name = name)))
            } else {
                repository.getAllManagedFolders(isVault = type == FolderType.SECURE_VAULT).map { allFolders ->
                    val breadcrumbList = mutableListOf<Folder>()
                    var curr = allFolders.find { it.folderId == id }
                    while (curr != null) {
                        breadcrumbList.add(0, curr)
                        curr = allFolders.find { it.folderId == curr.parentFolderId }
                    }
                    breadcrumbList.toImmutableList()
                }
            }
        }

    // 🌟 UNIVERSAL SYNC FOR FOLDERS
    private val prefsAndTypeFlow = combine(
        updateUserPreferencesUseCase.userPreferences.isGridViewFlow, // 🌟 MATCHED SOURCE
        _sortType, 
        _currentFolderType
    ) { isGrid, sort, type -> Triple(isGrid, sort, type) }

    private val heavyDatabaseFlow = combine(
        foldersFlow,
        breadcrumbsFlow,
        repository.getAllManagedFolders(isVault = false),
        prefsAndTypeFlow
    ) { folders, breadcrumbs, tree, prefs ->
        data class DbState(val folders: ImmutableList<HomeItem.FolderItem>, val breadcrumbs: ImmutableList<Folder>, val tree: List<Folder>, val prefs: Triple<Boolean, SortType, FolderType>)
        DbState(folders, breadcrumbs, tree, prefs)
    }.flowOn(Dispatchers.Default)

    val uiState: StateFlow<UnifiedFolderUiState> = combine(
        heavyDatabaseFlow,
        _internalState
    ) { dbData, internal ->
        val (isGrid, sort, type) = dbData.prefs
        val isPhysical = type == FolderType.PHYSICAL_DEVICE
        val isVault = type == FolderType.SECURE_VAULT

        internal.copy(
            isLoading = false,
            folders = dbData.folders,
            breadcrumbs = dbData.breadcrumbs,
            foldersTree = dbData.tree.toImmutableList(),
            isGridView = isGrid, // 🌟 SHARED SOURCE
            sortType = sort,
            canCreateSubFolders = !isPhysical && !isVault,
            canImport = !isPhysical,
            canRenameOrDelete = !isPhysical,
            shouldShowBreadcrumbs = !isPhysical
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UnifiedFolderUiState())

    fun onAction(action: UnifiedFolderAction) {
        when (action) {
            is UnifiedFolderAction.InitializeFolder -> initFolderData(action.id, action.name, action.type)
            is UnifiedFolderAction.ToggleSelection -> {
                val currentSelected = _internalState.value.selectedIds
                val newSelection = if (currentSelected.contains(action.id)) currentSelected.remove(action.id) else currentSelected.add(action.id)
                _internalState.update { it.copy(selectedIds = newSelection) }
            }
            is UnifiedFolderAction.SetSelectionMode -> _internalState.update { it.copy(isSelectionMode = action.enabled, selectedIds = if (!action.enabled) persistentSetOf() else it.selectedIds) }
            is UnifiedFolderAction.SelectAll -> _internalState.update { it.copy(selectedIds = action.ids.toPersistentSet()) }
            is UnifiedFolderAction.SelectAllItems -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val folderIds = uiState.value.folders.map { it.folder.folderId }
                    val type = _currentFolderType.value
                    val id = _currentFolderId.value

                    val pdfIds = if (type == FolderType.PHYSICAL_DEVICE) {
                        repository.getPhysicalFolderPdfIdsFast(id ?: "")
                    } else {
                        repository.getManagedPdfIdsFast(id, isVault = type == FolderType.SECURE_VAULT)
                    }

                    val allIds = (folderIds + pdfIds).toPersistentSet()
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(selectedIds = allIds) }
                    }
                }
            }
            is UnifiedFolderAction.OpenSheet -> _internalState.update { it.copy(activeSheetState = action.state, textInput = if (action.state is UnifiedFolderSheetState.RenameDialog) action.state.currentName else "") }
            is UnifiedFolderAction.CloseSheet -> _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None, textInput = "") }
            is UnifiedFolderAction.OnTextInputChange -> _internalState.update { it.copy(textInput = action.text) }
            is UnifiedFolderAction.UpdateSortType -> { _sortType.value = action.type; onAction(UnifiedFolderAction.CloseSheet) }
            
            is UnifiedFolderAction.ToggleViewMode -> viewModelScope.launch { 
                updateUserPreferencesUseCase.toggleGridView()
            }

            is UnifiedFolderAction.ToggleFavorite -> viewModelScope.launch { 
                toggleFavoriteUseCase(action.pdfId, action.isFav) 
            }

            is UnifiedFolderAction.ConfirmCreateFolder -> {
                val folderName = _internalState.value.textInput.trim()
                if (folderName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true) }
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = createFolderUseCase(folderName, _currentFolderId.value, isVault = _currentFolderType.value == FolderType.SECURE_VAULT)
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            if (result.isSuccess) {
                                _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None, textInput = "") }
                                _events.send(UnifiedFolderEvent.ShowSnackbar("Folder created"))
                            } else {
                                _events.send(UnifiedFolderEvent.ShowSnackbar(result.exceptionOrNull()?.message ?: "Error creating folder"))
                            }
                        }
                    }
                }
            }
            is UnifiedFolderAction.ConfirmRename -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.RenameDialog ?: return
                val newName = _internalState.value.textInput.trim()
                if (newName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true) }
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = when (val item = state.item) {
                            is HomeItem.FolderItem -> repository.renameManagedFolder(item.folder.folderId, newName)
                            is HomeItem.PdfItem -> Result.success(renamePdfUseCase(item.pdf, newName)).map{}
                        }
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            if (result.isSuccess) {
                                _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None, textInput = "") }
                                _events.send(UnifiedFolderEvent.ShowSnackbar("Renamed successfully"))
                            } else {
                                _events.send(UnifiedFolderEvent.ShowSnackbar(result.exceptionOrNull()?.message ?: "Error renaming folder"))
                            }
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
                    withContext(Dispatchers.Main) { _events.send(UnifiedFolderEvent.ClearMultiSelection); _internalState.update { it.copy(isProcessing = false) } }
                }
            }
            is UnifiedFolderAction.ConfirmMove -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.MovePicker ?: return
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    moveItemsUseCase(state.items, action.targetFolderId, isVault = _currentFolderType.value == FolderType.SECURE_VAULT)
                    withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) }; _events.send(UnifiedFolderEvent.ClearMultiSelection) }
                }
            }
            is UnifiedFolderAction.ImportFile -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    importPdfUseCase(action.uriString, _currentFolderId.value, isVault = _currentFolderType.value == FolderType.SECURE_VAULT)
                    withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) }; _events.send(UnifiedFolderEvent.ShowSnackbar("Imported Successfully")) }
                }
            }
            is UnifiedFolderAction.OpenAppPdfPicker -> _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.AppPdfPicker) }
            
            is UnifiedFolderAction.MovePdfsToCurrentFolder -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val items = action.pdfIds.map { id -> HomeItem.PdfItem(com.edu.pdf.domain.model.PdfFile(id = id, name = "", path = "", sizeInBytes = 0, lastModified = 0)) }
                    moveItemsUseCase(items, _currentFolderId.value, isVault = _currentFolderType.value == FolderType.SECURE_VAULT)
                    withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) }; _events.send(UnifiedFolderEvent.ShowSnackbar("Added successfully!")) }
                }
            }
            is UnifiedFolderAction.ToggleVaultStatus -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = toggleVaultUseCase(action.pdf)
                    withContext(Dispatchers.Main) { 
                        _internalState.update { it.copy(isProcessing = false) }
                        if (result.isSuccess) {
                            val msg = if (action.pdf.isVault) "Restored to Public" else "Secured in Vault"
                            _events.send(UnifiedFolderEvent.ShowSnackbar(msg)) 
                        }
                    }
                }
            }
        }
    }
}
