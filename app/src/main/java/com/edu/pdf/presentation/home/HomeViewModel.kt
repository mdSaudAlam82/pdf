package com.edu.pdf.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val recentItems: ImmutableList<HomeItem> = persistentListOf(),
    val currentFolderItems: ImmutableList<HomeItem> = persistentListOf(),
    val favoritePdfs: ImmutableList<PdfFile> = persistentListOf(),
    val breadcrumbs: ImmutableList<Folder> = persistentListOf(),
    val isGridView: Boolean = false,
    val sortType: SortType = SortType.DATE_DESC,
    val activeSheetState: HomeSheetState = HomeSheetState.None,
    val textInput: String = "",
    val isProcessing: Boolean = false
)

sealed interface HomeEvent {
    data class NavigateToPdf(val path: String) : HomeEvent
    data class NavigateToFolder(val folderId: String, val folderName: String) : HomeEvent
    data class ShowSnackbar(val message: String) : HomeEvent
    data object ClearMultiSelection : HomeEvent
}

sealed interface HomeAction {
    data class NavigateToVirtualFolder(val folder: Folder) : HomeAction
    data object NavigateUp : HomeAction
    data class OpenSheet(val state: HomeSheetState) : HomeAction
    data object CloseSheet : HomeAction
    data class OnTextInputChange(val text: String) : HomeAction
    data object ConfirmCreateFolder : HomeAction
    data object ConfirmRename : HomeAction
    data object ConfirmDelete : HomeAction
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
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val scanPdfsUseCase: ScanPdfsUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private val _sortType = MutableStateFlow(SortType.DATE_DESC)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _folderStack = MutableStateFlow<List<Folder>>(emptyList())
    private val currentFolderId = _folderStack.map { stack ->
        val id = stack.lastOrNull()?.folderId
        if (id.isNullOrBlank()) null else id
    }.distinctUntilChanged()

    private val _internalState = MutableStateFlow(
        HomeUiState(isLoading = true, activeSheetState = HomeSheetState.None, textInput = "")
    )

    private val unifiedItemsFlow = combine(
        currentFolderId.flatMapLatest { repoId -> repository.getManagedFolders(repoId, isVault = false) },
        currentFolderId.flatMapLatest { repoId -> repository.getManagedPdfs(repoId, isVault = false) }
    ) { folders, pdfs ->
        val folderItems = folders.map { HomeItem.FolderItem(it) }
        val pdfItems = pdfs.map { HomeItem.PdfItem(it) }
        (folderItems + pdfItems).toImmutableList()
    }.flowOn(Dispatchers.Default)

    val foldersTree: StateFlow<List<Folder>> = repository.getAllManagedFolders(isVault = false)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val favoritePdfsFlow = _sortType.flatMapLatest { sort ->
        repository.getFavoritePdfs(sort)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val recentItemsFlow = combine(
        repository.getRecentPdfs(),
        repository.getRecentFolders()
    ) { recentPdfs, recentFolders ->
        val pdfItems = recentPdfs.map { HomeItem.PdfItem(it) }
        val folderItems = recentFolders.map { HomeItem.FolderItem(it) }
        (pdfItems + folderItems)
            .sortedByDescending { item ->
                when (item) {
                    is HomeItem.PdfItem -> item.pdf.lastOpenedTime
                    is HomeItem.FolderItem -> item.folder.lastOpenedTime
                }
            }
            .take(50)
    }

    private val uiDataFlow = combine(
        recentItemsFlow,
        unifiedItemsFlow,
        favoritePdfsFlow
    ) { recent, unified, favs -> Triple(recent, unified, favs) }

    private val prefDataFlow = combine(
        userPreferences.isGridViewFlow,
        _sortType,
        _folderStack
    ) { isGrid, sort, stack -> Triple(isGrid, sort, stack) }

    val uiState: StateFlow<HomeUiState> = combine(
        uiDataFlow, prefDataFlow, _internalState
    ) { uiData, prefData, internal ->
        internal.copy(
            isLoading = false,
            recentItems = uiData.first.toImmutableList(),
            currentFolderItems = uiData.second,
            favoritePdfs = uiData.third.toImmutableList(),
            isGridView = prefData.first,
            sortType = prefData.second,
            breadcrumbs = prefData.third.toImmutableList()
        )
    }.flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        scanDeviceForData()
    }

    private fun scanDeviceForData() {
        viewModelScope.launch(Dispatchers.IO) { scanPdfsUseCase() }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.NavigateToVirtualFolder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateFolderLastOpenedTime(action.folder.folderId, System.currentTimeMillis())
                    withContext(Dispatchers.Main) {
                        _events.send(HomeEvent.NavigateToFolder(action.folder.folderId, action.folder.name))
                    }
                }
            }
            is HomeAction.NavigateUp -> {
                if (_folderStack.value.isNotEmpty()) {
                    _folderStack.value = _folderStack.value.dropLast(1)
                }
            }
            is HomeAction.OpenSheet -> {
                val initialText = if (action.state is HomeSheetState.RenameDialog) action.state.currentName else ""
                _internalState.update { it.copy(activeSheetState = action.state, textInput = initialText) }
            }
            is HomeAction.CloseSheet -> {
                _internalState.update { it.copy(activeSheetState = HomeSheetState.None, textInput = "") }
            }
            is HomeAction.OnTextInputChange -> {
                _internalState.update { it.copy(textInput = action.text) }
            }
            is HomeAction.UpdateSortType -> {
                _sortType.value = action.type
                onAction(HomeAction.CloseSheet)
            }
            is HomeAction.ConfirmCreateFolder -> {
                val folderName = _internalState.value.textInput.trim()
                if (folderName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.createManagedFolder(folderName, _folderStack.value.lastOrNull()?.folderId)
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            _events.send(HomeEvent.ShowSnackbar("Folder created"))
                        }
                    }
                }
            }
            is HomeAction.ConfirmRename -> {
                val state = _internalState.value.activeSheetState as? HomeSheetState.RenameDialog ?: return
                val newName = _internalState.value.textInput.trim()
                if (newName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
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
            is HomeAction.ConfirmDelete -> {
                val state = _internalState.value.activeSheetState as? HomeSheetState.DeleteConfirm ?: return
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val foldersToDelete = state.items.filterIsInstance<HomeItem.FolderItem>()
                    val pdfsToDelete = state.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf }
                    foldersToDelete.forEach { repository.deleteManagedFolder(it.folder.folderId) }
                    if (pdfsToDelete.isNotEmpty()) deletePdfsUseCase(pdfsToDelete)
                    withContext(Dispatchers.Main) {
                        _events.send(HomeEvent.ClearMultiSelection)
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ShowSnackbar("Items deleted successfully"))
                    }
                }
            }
            is HomeAction.ConfirmMove -> {
                val state = _internalState.value.activeSheetState as? HomeSheetState.MovePicker ?: return
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val pdfIds = state.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf.id }
                    val folderIds = state.items.filterIsInstance<HomeItem.FolderItem>().map { it.folder.folderId }
                    if (pdfIds.isNotEmpty()) repository.movePdfsToVirtualFolder(pdfIds, action.targetFolderId, isVault = false)
                    folderIds.forEach { repository.moveFolderToVirtualFolder(it, action.targetFolderId, isVault = false) }
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ClearMultiSelection)
                        _events.send(HomeEvent.ShowSnackbar("Moved successfully"))
                    }
                }
            }
            is HomeAction.CreateContextualFolder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.createManagedFolder(action.name, action.parentId)
                    withContext(Dispatchers.Main) {
                        _events.send(HomeEvent.ShowSnackbar("Folder created successfully"))
                    }
                }
            }
            is HomeAction.ToggleViewMode -> {
                viewModelScope.launch {
                    val currentGridState = userPreferences.isGridViewFlow.first()
                    userPreferences.saveGridViewPreference(!currentGridState)
                }
            }
            is HomeAction.RefreshData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _isRefreshing.value = true
                    scanPdfsUseCase()
                    delay(800)
                    _isRefreshing.value = false
                }
            }
            is HomeAction.ValidateAndOpenPdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (repository.checkFileExists(action.pdf.id)) {
                        repository.updateLastOpenedTime(action.pdf.id, System.currentTimeMillis())
                        _events.send(HomeEvent.NavigateToPdf(action.pdf.path))
                    } else {
                        _events.send(HomeEvent.ShowSnackbar("File moved or deleted by another app"))
                        scanDeviceForData()
                    }
                }
            }
            is HomeAction.ToggleFavorite -> viewModelScope.launch { repository.toggleFavorite(action.pdf.id, !action.pdf.isFavorite) }
            is HomeAction.RemoveFromRecent -> viewModelScope.launch(Dispatchers.IO) {
                action.items.forEach { item ->
                    when (item) {
                        is HomeItem.PdfItem -> repository.updateLastOpenedTime(item.pdf.id, 0L)
                        is HomeItem.FolderItem -> repository.updateFolderLastOpenedTime(item.folder.folderId, 0L)
                    }
                }
                _events.send(HomeEvent.ClearMultiSelection)
            }
            is HomeAction.UnfavoritePdfs -> viewModelScope.launch(Dispatchers.IO) {
                action.pdfs.forEach { repository.toggleFavorite(it.id, false) }
                _events.send(HomeEvent.ClearMultiSelection)
            }
            is HomeAction.ToggleVaultStatus -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val newVaultStatus = !action.pdf.isVault
                    repository.movePdfsToVirtualFolder(listOf(action.pdf.id), null, isVault = newVaultStatus)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ShowSnackbar(if (newVaultStatus) "Secured in Vault" else "Removed from Vault"))
                    }
                }
            }
        }
    }
}