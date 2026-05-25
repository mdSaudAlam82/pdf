@file:Suppress("DEPRECATION")
package com.edu.pdf.presentation.home

import android.content.Intent
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.SmartSelectionBottomBar
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.home.components.HomeContent
import com.edu.pdf.presentation.home.components.HomeTabs
import com.edu.pdf.presentation.home.components.SelectionTopBar
import com.edu.pdf.presentation.navigation.Screen
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreenWrapper(
    viewModel: HomeViewModel,
    navController: NavHostController,
    onPdfClick: (String) -> Unit,
    onFolderClick: (String, String, com.edu.pdf.domain.model.FolderType) -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(Environment.isExternalStorageManager()) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagedPdfs = viewModel.pagedUncategorizedPdfsFlow.collectAsLazyPagingItems()
    val isSelectionMode = uiState.isSelectionMode
    val selectedPdfs = uiState.selectedIds

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        hasPermission = Environment.isExternalStorageManager()
        if (hasPermission) viewModel.onAction(HomeAction.RefreshData)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is HomeEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    is HomeEvent.NavigateToPdfViewer -> onPdfClick(event.path)
                    is HomeEvent.NavigateToFolder -> onFolderClick(event.folderId, event.folderName, event.type)
                }
            }
        }
    }

    if (!hasPermission) {
        PermissionScreen {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply { data = "package:${context.packageName}".toUri() }
            permissionLauncher.launch(intent)
        }
    } else {
        LaunchedEffect(Unit) {
            viewModel.onAction(HomeAction.Initialize)
        }
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
        } else {
            HomeScreenPure(
                state = uiState,
                isRefreshing = uiState.isRefreshing,
                isSelectionMode = isSelectionMode,
                selectedPdfs = selectedPdfs,
                pagedPdfs = pagedPdfs,
                navController = navController,
                onSearchClick = onSearchClick,
                onSelectionModeChange = { enabled -> viewModel.onAction(HomeAction.SetSelectionMode(enabled)) },
                onToggleSelection = { id -> viewModel.onAction(HomeAction.ToggleSelection(id)) },
                onSelectAll = { ids -> viewModel.onAction(HomeAction.SelectAll(ids)) },
                onAction = viewModel::onAction
            )
            HomeOverlays(state = uiState, foldersTree = uiState.foldersTree, onAction = viewModel::onAction)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreenPure(
    state: HomeUiState,
    isRefreshing: Boolean,
    isSelectionMode: Boolean,
    selectedPdfs: PersistentSet<String>,
    pagedPdfs: androidx.paging.compose.LazyPagingItems<HomeItem.PdfItem>,
    navController: NavHostController,
    onSearchClick: () -> Unit,
    onSelectionModeChange: (Boolean) -> Unit,
    onToggleSelection: (String) -> Unit,
    onSelectAll: (List<String>) -> Unit,
    onAction: (HomeAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var currentTab by rememberSaveable { mutableIntStateOf(1) }
    val context = LocalContext.current

    val activity = androidx.activity.compose.LocalActivity.current ?: return
    val windowSizeClass = androidx.compose.material3.windowsizeclass.calculateWindowSizeClass(activity = activity)
    val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        currentTab = pagerState.currentPage
    }

    val currentTabItems = remember(currentTab, state.recentItems, state.currentFolders, state.favoritePdfs, pagedPdfs.itemSnapshotList) {
        when (currentTab) {
            0 -> state.recentItems
            1 -> {
                val pagedList = pagedPdfs.itemSnapshotList.items
                state.currentFolders + pagedList
            }
            2 -> state.favoritePdfs.map { HomeItem.PdfItem(it) }
            else -> emptyList()
        }
    }

    BackHandler(enabled = isSelectionMode) {
        onSelectAll(emptyList())
        onSelectionModeChange(false)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                if (isSelectionMode) {
                    val currentTabTotalCount = when (currentTab) {
                        0 -> state.recentItems.size
                        1 -> state.currentFolders.size + pagedPdfs.itemCount
                        2 -> state.favoritePdfs.size
                        else -> 0
                    }

                    val isAllCurrentTabSelected = if (currentTabTotalCount == 0) false else {
                        if (currentTab == 1) {
                            selectedPdfs.size >= currentTabTotalCount && currentTabItems.all { it.id in selectedPdfs }
                        } else {
                            val tabIds = currentTabItems.map { it.id }
                            selectedPdfs.containsAll(tabIds) && tabIds.isNotEmpty()
                        }
                    }

                    SelectionTopBar(
                        selectedCount = selectedPdfs.size,
                        isAllSelected = isAllCurrentTabSelected,
                        onClearSelection = { onSelectionModeChange(false) },
                        onSelectAllToggle = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (isAllCurrentTabSelected) {
                                onSelectionModeChange(false)
                            } else {
                                onAction(HomeAction.SelectAllInTab(currentTab))
                            }
                        }
                    )
                } else {
                    UniversalTopBar(
                        title = "Hi Read",
                        isGridView = state.isGridView,
                        showSearch = true,
                        showCreateFolder = currentTab == 1,
                        showSort = currentTab != 0,
                        showSelectAll = true,
                        showToggleView = true,
                        onSelectAllClick = {
                            onSelectionModeChange(true)
                            onAction(HomeAction.SelectAllInTab(currentTab))
                        },
                        onSearchClick = onSearchClick,
                        onSortClick = { onAction(HomeAction.OpenSheet(HomeSheetState.SortPicker)) },
                        onToggleView = { onAction(HomeAction.ToggleViewMode) },
                        onCreateFolderClick = { onAction(HomeAction.OpenSheet(HomeSheetState.CreateFolderDialog())) },
                        scrollBehavior = scrollBehavior
                    )

                    HomeTabs(
                        selectedTabIndex = pagerState.currentPage,
                        onTabSelected = { index -> coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                    )
                }
            }
        },
        bottomBar = {
            // 🌟 PIXEL PERFECT SWAP: Only ONE bar exists at a time. No stacking.
            if (!isTablet) {
                if (isSelectionMode) {
                    val selectedIdsSet = remember(selectedPdfs) { selectedPdfs.toSet() }
                    val selectedItemsList = remember(currentTabItems, selectedIdsSet) {
                        if (selectedIdsSet.isEmpty()) emptyList() else currentTabItems.filter { it.id in selectedIdsSet }
                    }

                    SmartSelectionBottomBar(
                        selectedItems = selectedItemsList,
                        tabIndex = currentTab,
                        onDelete = { onAction(HomeAction.OpenSheet(HomeSheetState.DeleteConfirm(selectedItemsList))) },
                        onMove = { onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(selectedItemsList))) },
                        onMerge = { Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show() },
                        onShare = {
                            val pdfUris = selectedItemsList.mapNotNull { it as? HomeItem.PdfItem }.map { it.pdf.id.toUri() }
                            if (pdfUris.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                                    type = "application/pdf"
                                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, java.util.ArrayList(pdfUris))
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share PDFs via"))
                            }
                        },
                        onRemoveFromRecent = { onAction(HomeAction.RemoveFromRecent(selectedItemsList)) },
                        onUnfavorite = { onAction(HomeAction.UnfavoritePdfs(selectedItemsList.filterIsInstance<HomeItem.PdfItem>().map { it.pdf })) }
                    )
                } else {
                    // Standard Navigation Bar
                    PremiumBottomBar(navController = navController)
                }
            }
        }
    ) { paddingValues ->
        Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            HomeContent(
                state = state,
                isRefreshing = isRefreshing,
                isSelectionMode = isSelectionMode,
                selectedPdfs = selectedPdfs,
                pagedPdfs = pagedPdfs,
                paddingValues = PaddingValues(0.dp),
                pagerState = pagerState,
                onAction = onAction,
                onToggleSelection = onToggleSelection,
                onSelectionModeChange = onSelectionModeChange
            )
        }
    }
}

@Composable
fun PermissionScreen(onRequestPermission: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Storage Permission Required", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("To find and display all PDFs on your device, we need \"All Files Access\".", textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRequestPermission) { Text("Grant Permission") }
    }
}

// 🌟 THE 2026 NAVIGATION ENGINE: Home Section
fun NavGraphBuilder.homeSection(
    navController: NavHostController,
    isTablet: Boolean
) {
    composable<Screen.Home> {
        HomeScreenWrapper(
            viewModel = hiltViewModel(),
            navController = navController,
            onPdfClick = { path ->
                navController.navigate(Screen.PdfViewer(pdfPath = path))
            },
            onFolderClick = { id, name, type ->
                navController.navigate(
                    Screen.UnifiedFolder(
                        folderId = id,
                        folderName = name,
                        folderType = type
                    )
                )
            },
            onSearchClick = {
                navController.navigate(Screen.Search)
            }
        )
    }
}
