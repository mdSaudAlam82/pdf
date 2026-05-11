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
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.HomeViewModel
import com.edu.pdf.presentation.home.components.ActionBottomBar
import com.edu.pdf.presentation.home.components.SelectionTopBar
import com.edu.pdf.presentation.home.components.UnifiedGridItem
import com.edu.pdf.presentation.home.components.UnifiedListItem

@Composable
fun UnifiedFolderScreen(
    // 🌟 EXACT FIX: Ye 3 lines maine galti se delete kar di thi pichli baar!
    folderId: String? = null,
    folderName: String? = null,
    folderType: FolderType? = null,

    onBack: () -> Unit,
    onPdfClick: (String) -> Unit,
    onFolderNavigate: (String, String, FolderType) -> Unit,
    onBreadcrumbNavigate: (Folder?) -> Unit,
    viewModel: UnifiedFolderViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    // 🌟 EXACT FIX: Ye LaunchedEffect bhi wapas lana tha taaki PDFs load ho sakein
    LaunchedEffect(folderId, folderName, folderType) {
        if (folderId != null && folderName != null && folderType != null) {
            viewModel.initFolderData(folderId, folderName, folderType)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagedPhysicalItems = viewModel.pagedPhysicalItems.collectAsLazyPagingItems()
    val foldersTree by homeViewModel.foldersTree.collectAsStateWithLifecycle()

    val isSelectionMode = uiState.isSelectionMode
    val selectedPdfs = uiState.selectedIds

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ... ISKE NEECHE TUMHARA BAAKI KA CODE WAISE HI RAHEGA ...
    // (LaunchedEffect(viewModel.events...) se shuru hokar end tak)

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

    val selectedItems by remember(uiState.items, selectedPdfs, uiState.folderType) {
        derivedStateOf {
            if (selectedPdfs.isEmpty()) emptyList()
            else if (uiState.folderType == FolderType.PHYSICAL_DEVICE) {
                selectedPdfs.map { id -> HomeItem.PdfItem(PdfFile(id, "", id, 0L, 0L)) }
            } else {
                uiState.items.filter {
                    val itemId = if (it is HomeItem.FolderItem) it.folder.folderId else (it as HomeItem.PdfItem).pdf.id
                    itemId in selectedPdfs
                }
            }
        }
    }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.onAction(UnifiedFolderAction.ImportFile(it.toString())) }
    }

    // ✅ 2026 PRO FIX: Predictive Back Animation Enabled!
    // यह BackHandler सिर्फ तभी काम करेगा जब कोई फाइल सेलेक्टेड हो (isSelectionMode == true)
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
        topBar = {
            if (isSelectionMode) {
                SelectionTopBar(
                    selectedCount = selectedPdfs.size,
                    totalCount = if (uiState.folderType == FolderType.PHYSICAL_DEVICE) pagedPhysicalItems.itemCount else uiState.items.size,
                    onClearSelection = { viewModel.onAction(UnifiedFolderAction.SetSelectionMode(false)) },
                    onSelectAllToggle = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        val total = if (uiState.folderType == FolderType.PHYSICAL_DEVICE) pagedPhysicalItems.itemCount else uiState.items.size
                        if (selectedPdfs.size == total) {
                            viewModel.onAction(UnifiedFolderAction.SelectAll(emptyList()))
                        } else {
                            if (uiState.folderType == FolderType.PHYSICAL_DEVICE) {
                                val allIds = (0 until pagedPhysicalItems.itemCount).mapNotNull { index ->
                                    val item = pagedPhysicalItems.peek(index)
                                    if (item is HomeItem.PdfItem) item.pdf.id else if (item is HomeItem.FolderItem) item.folder.folderId else null
                                }
                                viewModel.onAction(UnifiedFolderAction.SelectAll(allIds))
                            } else {
                                val allIds = uiState.items.map { if (it is HomeItem.FolderItem) it.folder.folderId else (it as HomeItem.PdfItem).pdf.id }
                                viewModel.onAction(UnifiedFolderAction.SelectAll(allIds))
                            }
                        }
                    }
                )
            } else {
                UnifiedCustomTopBar(
                    title = uiState.folderName,
                    isGridView = uiState.isGridView,
                    canCreateSubFolders = uiState.canCreateSubFolders,
                    onBackClick = onBack,
                    onAddFolderClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId))) },
                    onSortClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.SortPicker)) },
                    onToggleView = { viewModel.onAction(UnifiedFolderAction.ToggleViewMode) },
                    onSelectClick = { viewModel.onAction(UnifiedFolderAction.SetSelectionMode(true)) }
                )
            }
        },
        bottomBar = {
            if (isSelectionMode) {
                ActionBottomBar(
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
        Column(modifier = Modifier.padding(paddingValues)) {

            if (uiState.breadcrumbs.isNotEmpty()) {
                com.edu.pdf.presentation.common.PremiumBreadcrumbs(
                    breadcrumbs = uiState.breadcrumbs,
                    onNavigate = { folder -> onBreadcrumbNavigate(folder) }
                )
            }

            if (uiState.items.isEmpty() && uiState.folderType != FolderType.PHYSICAL_DEVICE && !uiState.isLoading) {
                PremiumEmptyState(
                    canImport = uiState.canImport,
                    canCreateFolder = uiState.canCreateSubFolders,
                    onImportFromDeviceClick = { filePicker.launch("application/pdf") },
                    onImportFromAppClick = { viewModel.onAction(UnifiedFolderAction.OpenAppPdfPicker) },
                    onCreateFolderClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId))) }
                )
            } else {
                if (uiState.isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 110.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 120.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (uiState.folderType == FolderType.PHYSICAL_DEVICE) {
                            items(
                                count = pagedPhysicalItems.itemCount,
                                key = pagedPhysicalItems.itemKey { item ->
                                    when (item) {
                                        is HomeItem.FolderItem -> item.folder.folderId
                                        is HomeItem.PdfItem -> item.pdf.id
                                    }
                                }
                            ) { index ->
                                val item = pagedPhysicalItems[index]
                                if (item != null) {
                                    UnifiedGridItem(
                                        item = item, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                        onAction = { action ->
                                            when (action) {
                                                is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                                is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                                else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(item)))
                                            }
                                        },
                                        onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                                    )
                                }
                            }
                        } else {
                            // 🌟 YAHAN TYPE INFERENCE FIX KIYA
                            items(
                                items = uiState.items,
                                key = { item: HomeItem ->
                                    when (item) {
                                        is HomeItem.FolderItem -> item.folder.folderId
                                        is HomeItem.PdfItem -> item.pdf.id
                                    }
                                }
                            ) { item ->
                                UnifiedGridItem(
                                    item = item, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                    onAction = { action ->
                                        when (action) {
                                            is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                            is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                            else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(item)))
                                        }
                                    },
                                    onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
                        if (uiState.folderType == FolderType.PHYSICAL_DEVICE) {
                            items(
                                count = pagedPhysicalItems.itemCount,
                                key = pagedPhysicalItems.itemKey { item ->
                                    when (item) {
                                        is HomeItem.FolderItem -> item.folder.folderId
                                        is HomeItem.PdfItem -> item.pdf.id
                                    }
                                }
                            ) { index ->
                                val item = pagedPhysicalItems[index]
                                if (item != null) {
                                    UnifiedListItem(
                                        item = item, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                        onAction = { action ->
                                            when (action) {
                                                is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                                is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                                else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(item)))
                                            }
                                        },
                                        onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                                    )
                                }
                            }
                        } else {
                            // 🌟 YAHAN BHI FIX KIYA
                            items(
                                items = uiState.items,
                                key = { item: HomeItem ->
                                    when (item) {
                                        is HomeItem.FolderItem -> item.folder.folderId
                                        is HomeItem.PdfItem -> item.pdf.id
                                    }
                                }
                            ) { item ->
                                UnifiedListItem(
                                    item = item, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                    onAction = { action ->
                                        when (action) {
                                            is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                            is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                            else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(item)))
                                        }
                                    },
                                    onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                                )
                            }
                        }
                    }
                }
            }
        }
        UnifiedFolderOverlays(state = uiState, foldersTree = foldersTree, onAction = viewModel::onAction)
    }
}

@Composable
fun UnifiedCustomTopBar(
    title: String,
    isGridView: Boolean,
    canCreateSubFolders: Boolean,
    onBackClick: () -> Unit,
    onAddFolderClick: () -> Unit,
    onSortClick: () -> Unit,
    onToggleView: () -> Unit,
    onSelectClick: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { Icon(Icons.Default.Close, "Close") }
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f).padding(horizontal = 8.dp))

            if (canCreateSubFolders) IconButton(onClick = onAddFolderClick) { Icon(Icons.Default.CreateNewFolder, "New Folder") }

            Row {
                IconButton(onClick = onSortClick) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
                IconButton(onClick = onToggleView) { Icon(if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView, "Toggle View") }
                IconButton(onClick = onSelectClick) { Icon(Icons.Outlined.CheckBox, "Select") }
            }
        }
    }
}

@Composable
fun PremiumEmptyState(
    canImport: Boolean,
    canCreateFolder: Boolean,
    onImportFromDeviceClick: () -> Unit,
    onImportFromAppClick: () -> Unit,
    onCreateFolderClick: () -> Unit
) {
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
private fun ProEmptyStateCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}