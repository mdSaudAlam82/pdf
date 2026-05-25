package com.edu.pdf.presentation.folders

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.common.SmartSelectionBottomBar
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.components.SelectionTopBar
import com.edu.pdf.presentation.home.components.UnifiedGridItem
import com.edu.pdf.presentation.home.components.UnifiedListItem
import com.edu.pdf.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnifiedFolderScreen(
    folderId: String? = null,
    folderName: String? = null,
    folderType: FolderType? = null,
    onBack: () -> Unit,
    onPdfClick: (String) -> Unit,
    onFolderNavigate: (String, String, FolderType) -> Unit,
    onBreadcrumbNavigate: (Folder?) -> Unit,
    viewModel: UnifiedFolderViewModel = hiltViewModel()
) {
    LaunchedEffect(folderId, folderName, folderType) {
        if (folderId != null && folderName != null && folderType != null) {
            viewModel.onAction(UnifiedFolderAction.InitializeFolder(folderId, folderName, folderType))
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagedPdfs = viewModel.pagedPdfsFlow.collectAsLazyPagingItems()

    val isSelectionMode = uiState.isSelectionMode
    val selectedPdfs = uiState.selectedIds

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is UnifiedFolderEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    is UnifiedFolderEvent.ClearMultiSelection -> viewModel.onAction(UnifiedFolderAction.SetSelectionMode(false))
                }
            }
        }
    }

    if (uiState.folderType == FolderType.SECURE_VAULT) {
        DisposableEffect(lifecycleOwner) {
            val activity = context as? ComponentActivity
            activity?.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
            val observer = LifecycleEventObserver { _, event -> if (event == Lifecycle.Event.ON_PAUSE) onBack() }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                activity?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    val selectedItems = remember(uiState.folders, selectedPdfs, pagedPdfs.itemSnapshotList) {
        if (selectedPdfs.isEmpty()) emptyList()
        else {
            val loadedPdfs = pagedPdfs.itemSnapshotList.items
            selectedPdfs.mapNotNull { id ->
                uiState.folders.find { it.folder.folderId == id } ?: loadedPdfs.find { it.pdf.id == id }
            }
        }
    }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.onAction(UnifiedFolderAction.ImportFile(it.toString())) }
    }
    BackHandler(enabled = isSelectionMode) {
        viewModel.onAction(UnifiedFolderAction.SetSelectionMode(false))
    }
    val onLongPressEnableSelection: (String) -> Unit = { id ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        if (!isSelectionMode) {
            viewModel.onAction(UnifiedFolderAction.SetSelectionMode(true))
            if (!selectedPdfs.contains(id)) viewModel.onAction(UnifiedFolderAction.ToggleSelection(id))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (isSelectionMode) {
                val totalItems = uiState.folders.size + pagedPdfs.itemCount
                val isAllSelected = totalItems > 0 && selectedPdfs.size >= totalItems

                SelectionTopBar(
                    selectedCount = selectedPdfs.size,
                    isAllSelected = isAllSelected,
                    onClearSelection = { viewModel.onAction(UnifiedFolderAction.SetSelectionMode(false)) },
                    onSelectAllToggle = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        if (isAllSelected) {
                            viewModel.onAction(UnifiedFolderAction.SelectAll(emptyList()))
                        } else {
                            viewModel.onAction(UnifiedFolderAction.SelectAllItems)
                        }
                    }
                )
            } else {
                val totalItems = uiState.folders.size + pagedPdfs.itemCount
                val isEmpty = totalItems == 0
                
                // 🌟 THE 2026 SMART HEADER LOGIC
                val isVirtual = uiState.folderType == FolderType.VIRTUAL_HUB
                val isVault = uiState.folderType == FolderType.SECURE_VAULT
                
                // Rule: If Breadcrumbs are visible (Virtual/Home), hide title and show 'X'
                // Else (Device), show title and show 'Back'
                val headerTitle = if (isVirtual) "" else uiState.folderName
                val navIcon = if (isVirtual || isVault) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack

                com.edu.pdf.presentation.common.UniversalTopBar(
                    title = headerTitle,
                    navigationIcon = navIcon,
                    onBackClick = onBack,
                    isGridView = uiState.isGridView,
                    showSearch = false, 
                    showCreateFolder = uiState.canCreateSubFolders,
                    showSort = totalItems > 1, 
                    showToggleView = !isEmpty, 
                    showSelectAll = !isEmpty,
                    onSelectAllClick = {
                        viewModel.onAction(UnifiedFolderAction.SetSelectionMode(true))
                        viewModel.onAction(UnifiedFolderAction.SelectAllItems)
                    },
                    onSortClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.SortPicker)) },
                    onToggleView = { viewModel.onAction(UnifiedFolderAction.ToggleViewMode) },
                    onCreateFolderClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId))) }
                )
            }
        },
        bottomBar = {
            if (isSelectionMode) {
                SmartSelectionBottomBar(
                    selectedItems = selectedItems,
                    tabIndex = 1,
                    onDelete = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(selectedItems))) },
                    onMove = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(selectedItems))) },
                    onMerge = { Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show() },
                    onShare = {
                        val pdfUris = selectedItems.mapNotNull { it as? HomeItem.PdfItem }.map { it.pdf.id.toUri() }
                        if (pdfUris.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                                type = "application/pdf"
                                putParcelableArrayListExtra(Intent.EXTRA_STREAM, java.util.ArrayList(pdfUris))
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDFs via"))
                        }
                    },
                    onRemoveFromRecent = {},
                    onUnfavorite = {}
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {

                if (uiState.shouldShowBreadcrumbs) {
                    com.edu.pdf.presentation.common.PremiumBreadcrumbs(
                        breadcrumbs = uiState.breadcrumbs,
                        onNavigate = { folder -> onBreadcrumbNavigate(folder) }
                    )
                }

                val isEmpty = uiState.folders.isEmpty() && pagedPdfs.itemCount == 0
                val shouldShowEmptyState = uiState.isDataLoaded && isEmpty && !uiState.isLoading

                if (shouldShowEmptyState) {
                    PremiumEmptyState(
                        canImport = uiState.canImport,
                        canCreateFolder = uiState.canCreateSubFolders,
                        onImportFromDeviceClick = { filePicker.launch("application/pdf") },
                        onImportFromAppClick = { viewModel.onAction(UnifiedFolderAction.OpenAppPdfPicker) },
                        onCreateFolderClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId))) }
                    )
                } else if (uiState.isDataLoaded) {
                    if (uiState.isGridView) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 110.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 120.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(items = uiState.folders, key = { it.folder.folderId }) { folder ->
                                UnifiedGridItem(
                                    item = folder, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                    onAction = { action ->
                                        if (action is HomeAction.NavigateToVirtualFolder) {
                                            onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                        } else {
                                            viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(folder)))
                                        }
                                    },
                                    onToggleSelection = { id -> viewModel.onAction(UnifiedFolderAction.ToggleSelection(id)) }, onLongPress = onLongPressEnableSelection
                                )
                            }

                            items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                                val pdfItem = pagedPdfs[index]
                                if (pdfItem != null) {
                                    UnifiedGridItem(
                                        item = pdfItem, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                        onAction = { action ->
                                            if (action is HomeAction.ValidateAndOpenPdf) {
                                                onPdfClick(action.pdf.path)
                                            } else {
                                                viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(pdfItem)))
                                            }
                                        },
                                        onToggleSelection = { id -> viewModel.onAction(UnifiedFolderAction.ToggleSelection(id)) }, onLongPress = onLongPressEnableSelection
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
                            items(items = uiState.folders, key = { it.folder.folderId }) { folder ->
                                UnifiedListItem(
                                    item = folder, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                    onAction = { action ->
                                        if (action is HomeAction.NavigateToVirtualFolder) {
                                            onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                        } else {
                                            viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(folder)))
                                        }
                                    },
                                    onToggleSelection = { id -> viewModel.onAction(UnifiedFolderAction.ToggleSelection(id)) }, onLongPress = onLongPressEnableSelection
                                )
                            }

                            items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                                val pdfItem = pagedPdfs[index]
                                if (pdfItem != null) {
                                    UnifiedListItem(
                                        item = pdfItem, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                        onAction = { action ->
                                            if (action is HomeAction.ValidateAndOpenPdf) {
                                                onPdfClick(action.pdf.path)
                                            } else {
                                                viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(pdfItem)))
                                            }
                                        },
                                        onToggleSelection = { id -> viewModel.onAction(UnifiedFolderAction.ToggleSelection(id)) }, onLongPress = onLongPressEnableSelection
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        UnifiedFolderOverlays(state = uiState, foldersTree = uiState.foldersTree, onAction = viewModel::onAction)
    }
}

@Composable
fun PremiumEmptyState(canImport: Boolean, canCreateFolder: Boolean, onImportFromDeviceClick: () -> Unit, onImportFromAppClick: () -> Unit, onCreateFolderClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (canImport) {
                ProEmptyStateCard(title = "Add from this app", icon = Icons.Default.Home, iconTint = MaterialTheme.colorScheme.error, onClick = onImportFromAppClick)
                ProEmptyStateCard(title = "Add from device", icon = Icons.Default.PhoneAndroid, iconTint = MaterialTheme.colorScheme.primary, onClick = onImportFromDeviceClick)
            }
            if (canCreateFolder) {
                ProEmptyStateCard(title = "Create folder", icon = Icons.Default.Folder, iconTint = MaterialTheme.colorScheme.tertiary, onClick = onCreateFolderClick)
            }
        }
    }
}

@Composable
private fun ProEmptyStateCard(title: String, icon: ImageVector, iconTint: Color, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// 🌟 THE 2026 NAVIGATION ENGINE: Folders Section
fun NavGraphBuilder.foldersSection(
    navController: NavHostController,
    isTablet: Boolean
) {
    composable<Screen.Folders> {
        FoldersScreen(
            navController = navController,
            isTablet = isTablet,
            onFolderClick = { id, name ->
                val encodedId = Uri.encode(id)
                navController.navigate(Screen.UnifiedFolder(folderId = encodedId, folderName = name, folderType = FolderType.PHYSICAL_DEVICE))
            }
        )
    }

    composable<Screen.UnifiedFolder> { backStackEntry ->
        val route: Screen.UnifiedFolder = backStackEntry.toRoute()
        UnifiedFolderScreen(
            folderId = route.folderId,
            folderName = route.folderName,
            folderType = route.folderType,
            onBack = { navController.popBackStack() },
            onPdfClick = { path ->
                navController.navigate(Screen.PdfViewer(pdfPath = path))
            },
            onFolderNavigate = { id, name, type ->
                val encodedId = Uri.encode(id)
                navController.navigate(Screen.UnifiedFolder(folderId = encodedId, folderName = name, folderType = type))
            },
            onBreadcrumbNavigate = { folder ->
                if (folder == null) {
                    navController.popBackStack(Screen.Folders, inclusive = false)
                } else {
                    val encodedId = Uri.encode(folder.folderId)
                    navController.navigate(Screen.UnifiedFolder(folderId = encodedId, folderName = folder.name, folderType = route.folderType))
                }
            }
        )
    }
}
