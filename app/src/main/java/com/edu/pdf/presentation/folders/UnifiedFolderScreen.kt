package com.edu.pdf.presentation.folders

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.HomeViewModel
import com.edu.pdf.presentation.home.components.ActionBottomBar
import com.edu.pdf.presentation.home.components.SelectionTopBar
import com.edu.pdf.presentation.home.components.UnifiedGridItem
import com.edu.pdf.presentation.home.components.UnifiedListItem
import com.edu.pdf.presentation.home.selection.SelectionViewModel
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun UnifiedFolderScreen(
    onBack: () -> Unit,
    onPdfClick: (String) -> Unit,
    onFolderNavigate: (String, String, FolderType) -> Unit,
    onBreadcrumbNavigate: (Folder?) -> Unit,
    viewModel: UnifiedFolderViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    selectionViewModel: SelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagedPhysicalItems = viewModel.pagedPhysicalItems.collectAsLazyPagingItems()
    val foldersTree by homeViewModel.foldersTree.collectAsStateWithLifecycle()
    val isSelectionMode by selectionViewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedPdfs by selectionViewModel.selectedPdfs.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 🌟 GOD MODE SECURITY: Anti-Screenshot & Auto-Background Kickout
    if (uiState.folderType == FolderType.SECURE_VAULT) {
        DisposableEffect(lifecycleOwner) {
            val activity = context as? ComponentActivity
            // 1. Block Screenshots & Screen Recording
            activity?.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)

            // 2. Auto-Lock Engine
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {
                    onBack() // App minimize hote hi bahar nikal do
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                // 3. Screenshot dobara allow kar do
                activity?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
    // 🌟 GOD MODE UI CACHING
    val selectedIdsSet by remember(selectedPdfs) {
        androidx.compose.runtime.derivedStateOf { selectedPdfs.toSet() }
    }
    val selectedItems by remember(uiState.items, selectedIdsSet) {
        androidx.compose.runtime.derivedStateOf {
            if (selectedIdsSet.isEmpty()) emptyList()
            else uiState.items.filter { it.id in selectedIdsSet }
        }
    }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.onAction(UnifiedFolderAction.ImportFile(it.toString())) }
    }

    BackHandler {
        if (isSelectionMode) {
            selectionViewModel.clearSelection()
            selectionViewModel.setSelectionMode(false)
        } else {
            onBack()
        }
    }

    val onLongPressEnableSelection: (String) -> Unit = { id ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        if (!isSelectionMode) {
            selectionViewModel.setSelectionMode(true)
            if (!selectedPdfs.contains(id)) selectionViewModel.toggleSelection(id)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isSelectionMode) {
                SelectionTopBar(
                    selectedCount = selectedPdfs.size,
                    totalCount = uiState.items.size,
                    onClearSelection = {
                        selectionViewModel.clearSelection()
                        selectionViewModel.setSelectionMode(false)
                    },
                    onSelectAllToggle = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        if (selectedPdfs.size == uiState.items.size) selectionViewModel.clearSelection()
                        else selectionViewModel.selectAll(uiState.items.map { it.id })
                    }
                )
            } else {
                UnifiedCustomTopBar(
                    title = uiState.folderName,
                    isGridView = uiState.isGridView,
                    canCreateSubFolders = uiState.canCreateSubFolders,
                    isEmpty = uiState.items.isEmpty(),
                    onBackClick = onBack,
                    onAddFolderClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId))) },
                    onSortClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.SortPicker)) },
                    onToggleView = { viewModel.onAction(UnifiedFolderAction.ToggleViewMode) },
                    onSelectClick = {
                        selectionViewModel.setSelectionMode(true)
                        selectionViewModel.selectAll(uiState.items.map { it.id })
                    }
                    // 🌟 ELITE UX: Import is NO LONGER in the top bar!
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
                    onMerge = {
                        Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        // 🌟 2026 PRO FIX: Type inference aur toUri error fixed
                        val pdfUris = selectedItems.mapNotNull { it as? com.edu.pdf.domain.model.HomeItem.PdfItem }.map { it.pdf.id.toUri() }
                        if (pdfUris.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                                type = "application/pdf"
                                putParcelableArrayListExtra(Intent.EXTRA_STREAM, java.util.ArrayList(pdfUris))
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDFs via"))
                        } else {
                            Toast.makeText(context, "Please select at least one PDF", Toast.LENGTH_SHORT).show()
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
                UnifiedBreadcrumbs(
                    breadcrumbs = uiState.breadcrumbs,
                    onBreadcrumbClick = { folder -> onBreadcrumbNavigate(folder) }
                )
            }

            if (uiState.items.isEmpty() && !uiState.isLoading) {
                // 🌟 ELITE UX: The NEW Pro Empty State!
                PremiumEmptyState(
                    canImport = uiState.canImport,
                    canCreateFolder = uiState.canCreateSubFolders,
                    onImportFromDeviceClick = { filePicker.launch("application/pdf") },
                    onImportFromAppClick = {
                        viewModel.onAction(UnifiedFolderAction.OpenAppPdfPicker)

                        // 🌟 NEXT PHASE: Ye humein app ke main PDF list pe le jayega (Move UI)
                        viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.None))
                        Toast.makeText(context, "Select files from Home to move here", Toast.LENGTH_LONG).show()
                    },
                    onCreateFolderClick = {
                        viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId)))
                    }
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
                        items(uiState.items, key = { it.id }) { item ->
                            UnifiedGridItem(
                                item = item, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                onAction = { action ->
                                    when (action) {
                                        is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                        is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                        else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(item)))
                                    }
                                },
                                onToggleSelection = { selectionViewModel.toggleSelection(it) }, onLongPress = onLongPressEnableSelection
                            )
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
                        // 🌟 2026 PRO UX: Agar ye Paging wala data hai (Physical Folder)
                        if (uiState.folderType == FolderType.PHYSICAL_DEVICE) {
                            items(
                                count = pagedPhysicalItems.itemCount,
                                key = { index -> pagedPhysicalItems[index]?.id ?: index }
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
                                        onToggleSelection = { selectionViewModel.toggleSelection(it) }, onLongPress = onLongPressEnableSelection
                                    )
                                }
                            }
                        } else {
                            // Virtual aur Vault Folders ke liye purana method
                            items(uiState.items, key = { it.id }) { item ->
                                UnifiedListItem(
                                    item = item, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                    onAction = { action ->
                                        when (action) {
                                            is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                            is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                            else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(item)))
                                        }
                                    },
                                    onToggleSelection = { selectionViewModel.toggleSelection(it) }, onLongPress = onLongPressEnableSelection
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

// 🌟 CLEAN TOP BAR
@Composable
fun UnifiedCustomTopBar(
    title: String,
    isGridView: Boolean,
    canCreateSubFolders: Boolean,
    isEmpty: Boolean,
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

            // Hides these icons automatically if folder is empty!
            AnimatedVisibility(visible = !isEmpty, enter = fadeIn(), exit = fadeOut()) {
                Row {
                    IconButton(onClick = onSortClick) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
                    IconButton(onClick = onToggleView) { Icon(if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView, "Toggle View") }
                    IconButton(onClick = onSelectClick) { Icon(Icons.Outlined.CheckBox, "Select") }
                }
            }
        }
    }
}

// 🌟 PREMIUM EMPTY STATE WITH IMPORT BUTTON
// 🌟 2026 PRO UX: Premium Empty State with 3 Action Cards
@Composable
fun PremiumEmptyState(
    canImport: Boolean,
    canCreateFolder: Boolean,
    onImportFromDeviceClick: () -> Unit,
    onImportFromAppClick: () -> Unit,
    onCreateFolderClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Options List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (canImport) {
                // Option 1: Add from this app
                ProEmptyStateCard(
                    title = "Add from this app",
                    icon = Icons.Default.Home,
                    iconTint = MaterialTheme.colorScheme.error,
                    onClick = onImportFromAppClick
                )

                // Option 2: Add from device
                ProEmptyStateCard(
                    title = "Add from device",
                    icon = Icons.Default.PhoneAndroid,
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = onImportFromDeviceClick
                )
            }

            if (canCreateFolder) {
                // Option 3: Create folder
                ProEmptyStateCard(
                    title = "Create folder",
                    icon = Icons.Default.Folder,
                    iconTint = MaterialTheme.colorScheme.tertiary, // Premium Yellow
                    onClick = onCreateFolderClick
                )
            }
        }
    }
}

@Composable
private fun ProEmptyStateCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun UnifiedBreadcrumbs(
    breadcrumbs: List<Folder>,
    onBreadcrumbClick: (Folder?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Home",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onBreadcrumbClick(null) }.padding(4.dp)
        )
        breadcrumbs.forEach { folder ->
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            val isLast = folder == breadcrumbs.last()
            Text(
                text = folder.name,
                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Medium,
                color = if (isLast) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(enabled = !isLast) { onBreadcrumbClick(folder) }
            )
        }
    }
}