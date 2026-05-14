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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.common.PremiumBottomBarItems
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.home.components.HomeContent
import com.edu.pdf.presentation.home.components.HomeTabs
import com.edu.pdf.presentation.home.components.SelectionTopBar
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

    // 🌟 MVI STRICT EVENT OBSERVER: Ye background me crash nahi hone dega
    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is HomeEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    is HomeEvent.NavigateToPdfViewer -> onPdfClick(event.path)
                    // 🌟 2. NAYA EVENT LISTEN KIYA
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
    pagedPdfs: androidx.paging.compose.LazyPagingItems<HomeItem.PdfItem>, // 🌟 FIX: Ye line miss ho gayi thi
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

    // 🌟 FIX: Paging items aur Folders ko mix karke select karne ke liye
    val currentTabItems = remember(currentTab, state.recentItems, state.currentFolders, state.favoritePdfs, pagedPdfs.itemSnapshotList) {
        when (currentTab) {
            0 -> state.recentItems
            1 -> {
                val pagedList = pagedPdfs.itemSnapshotList.items
                state.currentFolders + pagedList // Folders aur PDFs dono aayenge
            }
            2 -> state.favoritePdfs.map { HomeItem.PdfItem(it) }
            else -> emptyList()
        }
    }

    // 🌟 EXACT FIX: Ab BackHandler sirf Selection Mode me kaam aayega!
    BackHandler(enabled = isSelectionMode) {
        onSelectAll(emptyList())
        onSelectionModeChange(false)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                if (isSelectionMode) {
                    // 🌟 STEP 1: Current Tab me total kitne items hain?
                    val currentTabTotalCount = when (currentTab) {
                        0 -> state.recentItems.size
                        1 -> state.currentFolders.size + pagedPdfs.itemCount
                        2 -> state.favoritePdfs.size
                        else -> 0
                    }

                    // 🌟 STEP 2: Kya Current Tab ke saare items select ho chuke hain?
                    val isAllCurrentTabSelected = if (currentTabTotalCount == 0) false else {
                        if (currentTab == 1) {
                            // Paging Mode: Agar selection size total se zyada/barabar hai, aur jo screen pe dikh rahe hain wo sab selected hain
                            selectedPdfs.size >= currentTabTotalCount && currentTabItems.all { it.id in selectedPdfs }
                        } else {
                            // Memory Mode (Recent/Fav): Kya current tab ke saare ID selected map me hain?
                            val tabIds = currentTabItems.map { it.id }
                            selectedPdfs.containsAll(tabIds) && tabIds.isNotEmpty()
                        }
                    }

                    SelectionTopBar(
                        selectedCount = selectedPdfs.size, // Hamesha total count dikhayega
                        isAllSelected = isAllCurrentTabSelected, // Box RED hoga ya nahi?
                        onClearSelection = {
                            onSelectionModeChange(false)
                        },
                        onSelectAllToggle = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (isAllCurrentTabSelected) {
                                // Agar sab select the, to click karne par sab Deselect kar do
                                onSelectionModeChange(false)
                            } else {
                                // NAYA ACTION: Sirf Current Tab ke items MVI ke zariye select karo
                                onAction(HomeAction.SelectAllInTab(currentTab))
                            }
                        }
                    )
                } else {
                    // 🌟 FIX: Home screen ka title hamesha 'Hi Read' rahega aur Tabs hamesha dikhenge
                    UniversalTopBar(
                        title = "Hi Read",
                        isGridView = state.isGridView,
                        onSelectAllClick = {
                            onSelectionModeChange(true)
                            onSelectAll(currentTabItems.map { it.id })
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
            if (isSelectionMode || !isTablet) {
                androidx.compose.animation.AnimatedContent(
                    targetState = isSelectionMode,
                    label = "BottomBarSwapAnimation"
                ) { selectionActive ->
                    if (selectionActive) {
                        val selectedIdsSet by remember(selectedPdfs) { derivedStateOf { selectedPdfs.toSet() } }
                        val selectedItemsList by remember(currentTabItems, selectedIdsSet) {
                            derivedStateOf { if (selectedIdsSet.isEmpty()) emptyList() else currentTabItems.filter { it.id in selectedIdsSet } }
                        }

                        // 🌟 ELITE FIX: Hamara naya Master Bar! (No extra NavigationBar wrapper needed)
                        com.edu.pdf.presentation.common.SmartSelectionBottomBar(
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
                        if (!isTablet) {
                            // 🌟 NORMAL APP BAR: Iske andar already height 72dp aur padding set hai
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 0.dp,
                                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                                    PremiumBottomBarItems(navController = navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HomeContent(
                state = state,
                isRefreshing = isRefreshing,
                isSelectionMode = isSelectionMode,
                selectedPdfs = selectedPdfs,
                pagedPdfs = pagedPdfs, // 🌟 FIX: Ye pass karna zaroori hai
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