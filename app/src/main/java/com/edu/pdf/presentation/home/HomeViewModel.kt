package com.edu.pdf.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.model.PdfFile
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface HomeSheetState {
    data object None : HomeSheetState
    data object SortPicker : HomeSheetState
    data class CreateFolderDialog(val parentId: String? = null) : HomeSheetState
    data class ItemMenu(val item: HomeItem) : HomeSheetState
    data class RenameDialog(val item: HomeItem, val currentName: String) : HomeSheetState
    data class DetailsDialog(val item: HomeItem) : HomeSheetState
    data class MovePicker(val items: List<HomeItem>) : HomeSheetState
    data class DeleteConfirm(val items: List<HomeItem>) : HomeSheetState
    data object AppPdfPicker : HomeSheetState
}

sealed interface HomeEvent {
    data class ShowSnackbar(val message: String) : HomeEvent
    data class NavigateToPdfViewer(val path: String) : HomeEvent
    data class NavigateToFolder(val folderId: String, val folderName: String, val type: com.edu.pdf.domain.model.FolderType) : HomeEvent
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val recentItems: ImmutableList<HomeItem> = persistentListOf(),
    val currentFolders: ImmutableList<HomeItem.FolderItem> = persistentListOf(),
    val favoritePdfs: ImmutableList<PdfFile> = persistentListOf(),
    val foldersTree: ImmutableList<Folder> = persistentListOf(),
    val isRefreshing: Boolean = false,
    val isSelectionMode: Boolean = false,
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val isGridView: Boolean = false,
    val sortType: SortType = SortType.DATE_DESC,
    val activeSheetState: HomeSheetState = HomeSheetState.None,
    val isProcessing: Boolean = false
)

sealed interface HomeAction {
    data object Initialize : HomeAction
    data class ToggleSelection(val id: String) : HomeAction
    data class SetSelectionMode(val enabled: Boolean) : HomeAction
    data class SelectAll(val ids: List<String>) : HomeAction
    data class NavigateToVirtualFolder(val folder: Folder) : HomeAction
    data class OpenSheet(val state: HomeSheetState) : HomeAction
    data object CloseSheet : HomeAction
    data class ConfirmCreateFolder(val folderName: String) : HomeAction
    data class ConfirmRename(val item: HomeItem, val newName: String) : HomeAction
    data class ConfirmDelete(val items: List<HomeItem>) : HomeAction
    data class ConfirmMove(val targetFolderId: String?) : HomeAction
    data class UpdateSortType(val type: SortType) : HomeAction
    data class CreateContextualFolder(val name: String, val parentId: String?) : HomeAction
    data object ToggleViewMode : HomeAction
    data object RefreshData : HomeAction
    data class ValidateAndOpenPdf(val pdf: PdfFile) : HomeAction
    data class ToggleFavorite(val pdf: PdfFile) : HomeAction
    data class ToggleVaultStatus(val pdf: PdfFile) : HomeAction
    data class RemoveFromRecent(val items: List<HomeItem>) : HomeAction
    data class UnfavoritePdfs(val pdfs: List<PdfFile>) : HomeAction
    data object OpenAppPdfPicker : HomeAction
    data class ImportFile(val uriString: String) : HomeAction
    data class MovePdfsToCurrentFolder(val pdfIds: List<String>) : HomeAction
    data class SelectAllInTab(val tabIndex: Int) : HomeAction
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val scanPdfsUseCase: ScanPdfsUseCase,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val moveItemsUseCase: MoveItemsUseCase,
    private val toggleVaultUseCase: ToggleVaultUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val importPdfUseCase: ImportPdfUseCase,
    private val removeRecentHistoryUseCase: RemoveRecentHistoryUseCase,
    private val validatePdfFileUseCase: ValidatePdfFileUseCase,
    private val markPdfAsOpenedUseCase: MarkPdfAsOpenedUseCase,
    private val updateUserPreferencesUseCase: UpdateUserPreferencesUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(HomeUiState(isLoading = true, activeSheetState = HomeSheetState.None))
    private var hasInitialized = false

    private val sortTypeFlow = _internalState.map { it.sortType }.distinctUntilChanged()

    private val currentFoldersFlow = sortTypeFlow.flatMapLatest { sort ->
        repository.getManagedFolders(null, isVault = false).map { folders ->
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
    }.flowOn(Dispatchers.Default)

    val pagedUncategorizedPdfsFlow = sortTypeFlow.flatMapLatest { sort ->
        repository.getAllPdfsPaged(sort).map { pagingData ->
            pagingData.map { HomeItem.PdfItem(it) }
        }
    }.cachedIn(viewModelScope)

    private val favoritePdfsFlow = sortTypeFlow.flatMapLatest { sort -> repository.getFavoritePdfs(sort) }

    private val recentItemsFlow = combine(repository.getRecentPdfs(), repository.getRecentFolders()) { recentPdfs, recentFolders ->
        val pdfItems = recentPdfs.map { HomeItem.PdfItem(it) }
        val folderItems = recentFolders.map { HomeItem.FolderItem(it) }
        (pdfItems + folderItems)
            .sortedByDescending { item -> if (item is HomeItem.PdfItem) item.pdf.lastOpenedTime else (item as HomeItem.FolderItem).folder.lastOpenedTime }
            .take(50)
    }

    private val uiDataFlow = combine(recentItemsFlow, currentFoldersFlow, favoritePdfsFlow) { recent, folders, favs ->
        Triple(recent, folders, favs)
    }

    // 🌟 UNIVERSAL SYNC UI STATE
    val uiState: StateFlow<HomeUiState> = combine(
        uiDataFlow,
        updateUserPreferencesUseCase.userPreferences.isGridViewFlow, // 🌟 SHARED SOURCE
        repository.getAllManagedFolders(isVault = false),
        _internalState
    ) { uiData, isGrid, tree, internal ->
        internal.copy(
            isLoading = false,
            recentItems = uiData.first.toImmutableList(),
            currentFolders = uiData.second,
            favoritePdfs = uiData.third.toImmutableList(),
            foldersTree = tree.toImmutableList(),
            isGridView = isGrid // 🌟 SHARED PREF
        )
    }.distinctUntilChanged().flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Initialize -> if (!hasInitialized) { viewModelScope.launch(Dispatchers.IO) { scanPdfsUseCase() }; hasInitialized = true }
            is HomeAction.ToggleSelection -> {
                val currentSelected = _internalState.value.selectedIds
                val newSelection = if (currentSelected.contains(action.id)) currentSelected.remove(action.id) else currentSelected.add(action.id)
                _internalState.update { it.copy(selectedIds = newSelection) }
            }
            is HomeAction.SetSelectionMode -> _internalState.update { it.copy(isSelectionMode = action.enabled, selectedIds = if (!action.enabled) persistentSetOf() else it.selectedIds) }
            is HomeAction.SelectAll -> _internalState.update { it.copy(selectedIds = action.ids.toPersistentSet()) }
            
            is HomeAction.SelectAllInTab -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val allIds = when (action.tabIndex) {
                        0 -> { 
                            val recentPdfs = repository.getRecentPdfs().first().map { it.id }
                            val recentFolders = repository.getRecentFolders().first().map { it.folderId }
                            recentPdfs + recentFolders
                        }
                        1 -> { 
                            // 🌟 2026 PRO: Database se saari 3,000+ IDs uthao
                            val folderIds = repository.getManagedFolders(null, isVault = false).first().map { it.folderId }
                            val pdfIds = repository.getUncategorizedPdfIdsFast()
                            folderIds + pdfIds
                        }
                        2 -> repository.getFavoritePdfIdsFast()
                        else -> emptyList()
                    }
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(selectedIds = allIds.toPersistentSet()) }
                    }
                }
            }
            
            is HomeAction.NavigateToVirtualFolder -> {
                viewModelScope.launch {
                    repository.updateFolderLastOpenedTime(action.folder.folderId, System.currentTimeMillis())
                    _events.send(HomeEvent.NavigateToFolder(action.folder.folderId, action.folder.name, com.edu.pdf.domain.model.FolderType.VIRTUAL_HUB))
                }
            }
            is HomeAction.OpenSheet -> _internalState.update { it.copy(activeSheetState = action.state) }
            is HomeAction.CloseSheet -> _internalState.update { it.copy(activeSheetState = HomeSheetState.None) }
            is HomeAction.UpdateSortType -> _internalState.update { it.copy(sortType = action.type, activeSheetState = HomeSheetState.None) }

            is HomeAction.ConfirmCreateFolder -> {
                if (action.folderName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                    viewModelScope.launch(Dispatchers.IO) {
                        createFolderUseCase(action.folderName, null)
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            _events.send(HomeEvent.ShowSnackbar("Folder created"))
                        }
                    }
                }
            }

            is HomeAction.ConfirmRename -> {
                if (action.newName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                    viewModelScope.launch(Dispatchers.IO) {
                        when (val item = action.item) {
                            is HomeItem.FolderItem -> repository.renameManagedFolder(item.folder.folderId, action.newName)
                            is HomeItem.PdfItem -> renamePdfUseCase(item.pdf, action.newName)
                        }
                        withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) } }
                    }
                }
            }

            is HomeAction.ConfirmDelete -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val foldersToDelete = action.items.filterIsInstance<HomeItem.FolderItem>()
                    val pdfsToDelete = action.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf }
                    foldersToDelete.forEach { deleteFolderUseCase(it.folder.folderId) }
                    if (pdfsToDelete.isNotEmpty()) deletePdfsUseCase(pdfsToDelete)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false, isSelectionMode = false, selectedIds = persistentSetOf()) }
                        _events.send(HomeEvent.ShowSnackbar("Items deleted successfully"))
                    }
                }
            }

            is HomeAction.ConfirmMove -> {
                if (_internalState.value.activeSheetState !is HomeSheetState.MovePicker) return
                
                val currentSelected = _internalState.value.selectedIds
                
                _internalState.update { 
                    it.copy(
                        isProcessing = true, 
                        activeSheetState = HomeSheetState.None,
                        isSelectionMode = false,
                        selectedIds = persistentSetOf()
                    ) 
                } 
                
                viewModelScope.launch(Dispatchers.IO) {
                    val pdfIds = currentSelected.filter { !it.startsWith("/") }.toSet()
                    val folderIds = currentSelected.filter { it.startsWith("/") }.toList()
                    
                    moveItemsUseCase(
                        selectedIds = pdfIds,
                        folderIds = folderIds,
                        targetFolderId = action.targetFolderId,
                        sourcePath = null,
                        isVault = false
                    )
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                    }
                }
            }

            is HomeAction.CreateContextualFolder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.createManagedFolder(action.name, action.parentId)
                    withContext(Dispatchers.Main) { _events.send(HomeEvent.ShowSnackbar("Folder created successfully")) }
                }
            }

            is HomeAction.ToggleViewMode -> viewModelScope.launch { 
                updateUserPreferencesUseCase.toggleGridView()
            }

            is HomeAction.RefreshData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _internalState.update { it.copy(isRefreshing = true) }
                    scanPdfsUseCase()
                    delay(800)
                    _internalState.update { it.copy(isRefreshing = false) }
                }
            }

            is HomeAction.ValidateAndOpenPdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    validatePdfFileUseCase(action.pdf).onSuccess {
                        markPdfAsOpenedUseCase(action.pdf.id)
                        withContext(Dispatchers.Main) { _events.send(HomeEvent.NavigateToPdfViewer(action.pdf.path)) }
                    }.onFailure { e ->
                        withContext(Dispatchers.Main) { _events.send(HomeEvent.ShowSnackbar(e.message ?: "File not found")) }
                    }
                }
            }

            is HomeAction.ToggleFavorite -> viewModelScope.launch { 
                toggleFavoriteUseCase(action.pdf.id, !action.pdf.isFavorite) 
            }

            is HomeAction.RemoveFromRecent -> viewModelScope.launch(Dispatchers.IO) {
                removeRecentHistoryUseCase(action.items)
                withContext(Dispatchers.Main) { _internalState.update { it.copy(isSelectionMode = false, selectedIds = persistentSetOf()) } }
            }

            is HomeAction.UnfavoritePdfs -> viewModelScope.launch(Dispatchers.IO) {
                action.pdfs.forEach { toggleFavoriteUseCase(it.id, false) }
                withContext(Dispatchers.Main) { _internalState.update { it.copy(isSelectionMode = false, selectedIds = persistentSetOf()) } }
            }

            is HomeAction.ToggleVaultStatus -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = toggleVaultUseCase(action.pdf)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        if (result.isSuccess) {
                            val msg = if (action.pdf.isVault) "Removed from Vault" else "Secured in Vault"
                            _events.send(HomeEvent.ShowSnackbar(msg))
                        }
                    }
                }
            }

            is HomeAction.ImportFile -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = importPdfUseCase(action.uriString, null, isVault = false)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ShowSnackbar(if (result.isSuccess) "Imported Successfully" else "Import Failed"))
                    }
                }
            }

            is HomeAction.MovePdfsToCurrentFolder -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    // 🌟 2026 PRO: Use IDs directly
                    moveItemsUseCase(
                        selectedIds = action.pdfIds.toSet(),
                        folderIds = emptyList(),
                        targetFolderId = null,
                        sourcePath = null, // 🌟 Home has no parent folder
                        isVault = false
                    )
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ShowSnackbar("Added successfully!"))
                    }
                }
            }

            is HomeAction.OpenAppPdfPicker -> {
                _internalState.update { it.copy(activeSheetState = HomeSheetState.AppPdfPicker) }
            }
        }
    }
}
