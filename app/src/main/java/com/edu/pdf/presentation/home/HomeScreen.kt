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
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.home.components.HomeContent
import com.edu.pdf.presentation.home.components.HomeTabs
import com.edu.pdf.presentation.home.components.SelectionTopBar
import com.edu.pdf.presentation.navigation.Screen
import kotlinx.collections.immutable.PersistentSet
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
                onSearchClick = onSearchClick,
                onSelectionModeChange = { enabled -> viewModel.onAction(HomeAction.SetSelectionMode(enabled)) },
                onToggleSelection = { id -> viewModel.onAction(HomeAction.ToggleSelection(id)) },
                onSelectAll = { ids -> viewModel.onAction(HomeAction.SelectAll(ids)) },
                onAction = viewModel::onAction,
                viewModel = viewModel
            )
            HomeOverlays(state = uiState, foldersTree = uiState.foldersTree, onAction = viewModel::onAction)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPure(
    state: HomeUiState,
    isRefreshing: Boolean,
    isSelectionMode: Boolean,
    selectedPdfs: PersistentSet<String>,
    pagedPdfs: androidx.paging.compose.LazyPagingItems<HomeItem.PdfItem>,
    onSearchClick: () -> Unit,
    onSelectionModeChange: (Boolean) -> Unit,
    onToggleSelection: (String) -> Unit,
    onSelectAll: (List<String>) -> Unit,
    onAction: (HomeAction) -> Unit,
    viewModel: HomeViewModel
) {
    val haptic = LocalHapticFeedback.current
    var currentTab by rememberSaveable { mutableIntStateOf(1) }

    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        currentTab = pagerState.currentPage
        viewModel.onAction(HomeAction.UpdateTabIndex(pagerState.currentPage))
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

    // 🌟 ARCHITECTURE MASTERPIECE: Pure Content View (No Scaffold!)
    // The Shell (MainAppScreen) handles the Scaffold.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        // TOP BAR SECTION
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

        // CONTENT SECTION
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

    // 🌟 SELECTION BAR REMOVED FROM HERE
    // It is now managed by the Shell (MainAppScreen) in the same physical footprint as the Bottom Bar.
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

fun NavGraphBuilder.homeSection(
    navController: NavHostController,
    isTablet: Boolean,
    viewModel: HomeViewModel
) {
    composable<Screen.Home> {
        HomeScreenWrapper(
            viewModel = viewModel,
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
