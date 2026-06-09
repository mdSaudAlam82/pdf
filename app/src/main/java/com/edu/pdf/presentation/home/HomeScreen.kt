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
import androidx.compose.foundation.pager.PagerState
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
import com.edu.pdf.presentation.core.ShellAction
import com.edu.pdf.presentation.core.ShellViewModel

@Composable
fun HomeScreenWrapper(
    viewModel: HomeViewModel,
    shellViewModel: ShellViewModel,
    navController: NavHostController,
    onPdfClick: (String) -> Unit,
    onFolderClick: (String, String, com.edu.pdf.domain.model.FolderType) -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shellState by shellViewModel.uiState.collectAsStateWithLifecycle()
    val pagedPdfs = viewModel.pagedUncategorizedPdfsFlow.collectAsLazyPagingItems()

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

    LaunchedEffect(Unit) {
        viewModel.onAction(HomeAction.Initialize)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) 
        }
    } else {
        HomeScreenPure(
            state = uiState,
            shellState = shellState,
            isRefreshing = uiState.isRefreshing,
            pagedPdfs = pagedPdfs,
            onSearchClick = onSearchClick,
                onSelectionModeChange = { enabled -> shellViewModel.onAction(ShellAction.SetSelectionMode(enabled)) },
                onToggleSelection = { id -> shellViewModel.onAction(ShellAction.ToggleSelection(id)) },
                onAction = viewModel::onAction,
            onShellAction = shellViewModel::onAction,
            viewModel = viewModel
        )
        HomeOverlays(state = uiState, foldersTree = uiState.foldersTree, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPure(
    state: HomeUiState,
    shellState: com.edu.pdf.presentation.core.ShellUiState,
    isRefreshing: Boolean,
    pagedPdfs: androidx.paging.compose.LazyPagingItems<HomeItem.PdfItem>,
    onSearchClick: () -> Unit,
    onSelectionModeChange: (Boolean) -> Unit,
    onToggleSelection: (String) -> Unit,
    onAction: (HomeAction) -> Unit,
    onShellAction: (ShellAction) -> Unit,
    viewModel: HomeViewModel
) {
    val haptic = LocalHapticFeedback.current
    val isSelectionMode = shellState.isSelectionMode
    val selectedPdfs = shellState.selectedIds

    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onAction(HomeAction.UpdateTabIndex(pagerState.currentPage))
    }

    val currentTabItems = remember(pagerState.currentPage, state.recentItems, state.currentFolders, state.favoritePdfs, pagedPdfs.itemSnapshotList) {
        when (pagerState.currentPage) {
            0 -> state.recentItems
            1 -> {
                val pagedList = pagedPdfs.itemSnapshotList.items
                state.currentFolders + pagedList
            }
            2 -> state.favoritePdfs.map { HomeItem.PdfItem(it) }
            else -> emptyList()
        }
    }

    // Selection handled by Global BackHandler in MainAppScreen

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
                val currentTabTotalCount = when (pagerState.currentPage) {
                    0 -> state.recentItems.size
                    1 -> state.currentFolders.size + pagedPdfs.itemCount
                    2 -> state.favoritePdfs.size
                    else -> 0
                }

                val isAllCurrentTabSelected = if (currentTabTotalCount == 0) false else {
                    if (pagerState.currentPage == 1) {
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
                            viewModel.onAction(HomeAction.SelectAllInTab(pagerState.currentPage))
                        }
                    }
                )
            } else {
                UniversalTopBar(
                    title = "Hi Read",
                    isGridView = state.isGridView,
                    showSearch = true,
                    showCreateFolder = pagerState.currentPage == 1,
                    showSort = pagerState.currentPage != 0,
                    showSelectAll = true,
                    showToggleView = true,
                    onSelectAllClick = {
                        onSelectionModeChange(true)
                        viewModel.onAction(HomeAction.SelectAllInTab(pagerState.currentPage))
                    },
                    onSearchClick = onSearchClick,
                    onSortClick = { onAction(HomeAction.OpenSheet(HomeSheetState.SortPicker)) },
                    onToggleView = { onAction(HomeAction.ToggleViewMode) },
                    onCreateFolderClick = { onAction(HomeAction.OpenSheet(HomeSheetState.CreateFolderDialog())) },
                    scrollBehavior = scrollBehavior
                )

                HomeTabs(
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = { index -> 
                        coroutineScope.launch { 
                            pagerState.animateScrollToPage(index) 
                        } 
                    }
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
            paddingValues = PaddingValues(0.dp), // 🌟 Handled by Shell NavHost
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
    viewModel: HomeViewModel,
    shellViewModel: ShellViewModel,
    onPdfClickOverride: ((String) -> Unit)? = null
) {
    composable<Screen.Home> {
        HomeScreenWrapper(
            viewModel = viewModel,
            shellViewModel = shellViewModel,
            navController = navController,
            onPdfClick = { path ->
                if (onPdfClickOverride != null) {
                    onPdfClickOverride(path)
                } else {
                    navController.navigate(Screen.PdfViewer(pdfPath = path))
                }
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
