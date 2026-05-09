@file:Suppress("DEPRECATION")
package com.edu.pdf.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.PremiumNavigationRail
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.home.components.ActionBottomBar
import com.edu.pdf.presentation.home.components.HomeContent
import com.edu.pdf.presentation.home.components.HomeTabs
import com.edu.pdf.presentation.home.components.SelectionTopBar
import com.edu.pdf.presentation.home.selection.SelectionViewModel
import com.edu.pdf.presentation.navigation.Screen
import kotlinx.collections.immutable.PersistentSet
import kotlinx.coroutines.launch


@Composable
fun HomeScreenWrapper(
    viewModel: HomeViewModel,
    selectionViewModel: SelectionViewModel = hiltViewModel(),
    navController: NavHostController,
    onPdfClick: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(Environment.isExternalStorageManager()) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isSelectionMode by selectionViewModel.isSelectionMode.collectAsStateWithLifecycle()
    val selectedPdfs by selectionViewModel.selectedPdfs.collectAsStateWithLifecycle()
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        hasPermission = Environment.isExternalStorageManager()
        if (hasPermission) viewModel.onAction(HomeAction.RefreshData)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                // 🌟 FIX 1 & 2: Added NavigateToPdf back, which fixes the "onPdfClick never used" warning
                is HomeEvent.NavigateToPdf -> onPdfClick(event.path)

                is HomeEvent.NavigateToFolder -> {
                    // 🌟 VIRTUAL HUB context ke saath navigate karein
                    navController.navigate(
                        Screen.UnifiedFolder(
                            folderId = event.folderId,
                            folderName = event.folderName,
                            folderType = com.edu.pdf.domain.model.FolderType.VIRTUAL_HUB
                        )
                    )
                }

                // 🌟 FIX 3: Added missing branches to make 'when' exhaustive
                is HomeEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                is HomeEvent.ClearMultiSelection -> selectionViewModel.setSelectionMode(false)
            }
        }
    }

    if (!hasPermission) {
        PermissionScreen {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply { data = "package:${context.packageName}".toUri() }
            permissionLauncher.launch(intent)
        }
    } else {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
        } else {
            HomeScreenPure(
                state = uiState,
                isRefreshing = isRefreshing,
                isSelectionMode = isSelectionMode,
                selectedPdfs = selectedPdfs,
                navController = navController,
                onSearchClick = onSearchClick,
                onSelectionModeChange = selectionViewModel::setSelectionMode,
                onToggleSelection = selectionViewModel::toggleSelection,
                onSelectAll = selectionViewModel::selectAll,
                onAction = viewModel::onAction
            )
            HomeOverlays(state = uiState, foldersTree = viewModel.foldersTree.collectAsStateWithLifecycle().value, onAction = viewModel::onAction)
        }
    }
}
@OptIn(
    androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class // 🌟 Ye line add karne se saari warning gayab ho jayengi
)
@Composable
fun HomeScreenPure(
    state: HomeUiState,
    isRefreshing: Boolean,
    isSelectionMode: Boolean,
    selectedPdfs: PersistentSet<String>,
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
    // 🌟 2026 ARCHITECT FIX: Screen Size Detection
    val windowSizeClass = calculateWindowSizeClass(activity = context as Activity)
    val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    // 🌟 GOD MODE ARCHITECTURE: Pager state ko upar shift kiya taaki Top Bar aur Content sync rahein
    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        currentTab = pagerState.currentPage
    }

    val currentTabItems = remember(currentTab, state.recentItems, state.currentFolderItems, state.favoritePdfs) {
        when (currentTab) {
            0 -> state.recentItems
            1 -> state.currentFolderItems
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
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                if (isSelectionMode) {
                    SelectionTopBar(
                        selectedCount = selectedPdfs.size,
                        totalCount = currentTabItems.size,
                        onClearSelection = {
                            onSelectAll(emptyList())
                            onSelectionModeChange(false)
                        },
                        onSelectAllToggle = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (selectedPdfs.size == currentTabItems.size) {
                                onSelectAll(emptyList())
                            } else {
                                onSelectAll(currentTabItems.map { it.id })
                            }
                        }
                    )
                } else {
                    UniversalTopBar(
                        title = if (state.breadcrumbs.isEmpty()) "Hi Read" else state.breadcrumbs.last().name,
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
                    if (state.breadcrumbs.isEmpty()) {
                        HomeTabs(
                            selectedTabIndex = pagerState.currentPage,
                            onTabSelected = { index -> coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                        )
                    }
                }
            }
        },
        bottomBar = {
            // ==========================================
            // 🌟 2026 TABLET LOGIC: Bottom Bar Visibility
            // ==========================================
            // Agar device Phone hai (!isTablet), tabhi ye neeche wala bar dikhega
            if (!isTablet) {
                AnimatedContent(
                    targetState = isSelectionMode,
                    transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
                    label = "BottomBarTransition"
                ) { selectionMode ->
                    if (selectionMode) {
                        // 🌟 GOD MODE UI CACHING (Tumhara original brilliant code)
                        val selectedIdsSet by remember(selectedPdfs) {
                            androidx.compose.runtime.derivedStateOf { selectedPdfs.toSet() }
                        }
                        val selectedItemsList by remember(currentTabItems, selectedIdsSet) {
                            androidx.compose.runtime.derivedStateOf {
                                if (selectedIdsSet.isEmpty()) emptyList()
                                else currentTabItems.filter { it.id in selectedIdsSet }
                            }
                        }

                        ActionBottomBar(
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
                        PremiumBottomBar(navController = navController)
                    }
                }
            }
        }
    ) { paddingValues ->
        // ==========================================
        // 🌟 2026 TABLET LOGIC: Side-by-Side Split View
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Scaffold ki padding Row par apply karni hai
        ) {
            // Agar Tablet hai aur user files select nahi kar raha hai, toh side mein Rail dikhao
            if (isTablet && !isSelectionMode) {
                PremiumNavigationRail(navController = navController)
            }

            HomeContent(
                state = state,
                isRefreshing = isRefreshing,
                isSelectionMode = isSelectionMode,
                selectedPdfs = selectedPdfs,
                // 🌟 PRO FIX: Kyunki padding Row le chuka hai, isko 0.dp deni hai warna layout uper-neeche katega
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
        Text("To find and display all PDFs on your device, we need \"All Files Access\".", textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRequestPermission) { Text("Grant Permission") }
    }
}