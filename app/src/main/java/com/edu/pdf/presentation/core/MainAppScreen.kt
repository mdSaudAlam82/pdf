package com.edu.pdf.presentation.core

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Alignment
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.PremiumNavigationRail
import com.edu.pdf.presentation.common.SmartSelectionBottomBar
import com.edu.pdf.presentation.core.components.AdaptivePdfLayout
import com.edu.pdf.presentation.folders.UnifiedFolderAction
import com.edu.pdf.presentation.folders.UnifiedFolderSheetState
import com.edu.pdf.presentation.folders.UnifiedFolderViewModel
import com.edu.pdf.presentation.folders.foldersSection
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.HomeSheetState
import com.edu.pdf.presentation.home.HomeViewModel
import com.edu.pdf.presentation.home.homeSection
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.pdfviewer.PdfViewerScreen
import com.edu.pdf.presentation.pdfviewer.pdfViewerSection
import com.edu.pdf.presentation.placeholder.placeholderSections
import com.edu.pdf.presentation.search.searchSection
import java.io.File


fun hasStoragePermission(): Boolean {
    return android.os.Environment.isExternalStorageManager()
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainAppScreen(
    externalPdfUri: String? = null,
    onPdfOpened: () -> Unit = {},
    autoNavigatePath: String? = null,
    onNavigateConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()
    var isExternalLaunch by remember { mutableStateOf(false) }
    var selectedPdfPath by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(externalPdfUri) {
        if (!externalPdfUri.isNullOrBlank()) {
            isExternalLaunch = true
            selectedPdfPath = externalPdfUri
            navController.navigate(Screen.PdfViewer(pdfPath = externalPdfUri))
            onPdfOpened()
        }
    }

    LaunchedEffect(autoNavigatePath) {
        if (!autoNavigatePath.isNullOrBlank()) {
            val decodedPath = Uri.encode(autoNavigatePath)
            val folderName = File(autoNavigatePath).name
            navController.navigate(Screen.UnifiedFolder(folderId = decodedPath, folderName = folderName, folderType = FolderType.VIRTUAL_HUB))
            onNavigateConsumed()
        }
    }

    val activity = androidx.activity.compose.LocalActivity.current ?: return
    val context = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(activity = activity)
    val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val startScreen: Screen = remember { if (hasStoragePermission()) Screen.Home else Screen.Permission }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val activityViewModelStoreOwner = activity as androidx.lifecycle.ViewModelStoreOwner
    val shellViewModel: ShellViewModel = hiltViewModel(viewModelStoreOwner = activityViewModelStoreOwner)
    val homeViewModel: HomeViewModel = hiltViewModel(viewModelStoreOwner = activityViewModelStoreOwner)
    val unifiedFolderViewModel: UnifiedFolderViewModel = hiltViewModel(viewModelStoreOwner = activityViewModelStoreOwner)
    
    val shellState by shellViewModel.uiState.collectAsStateWithLifecycle()
    val homeState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val unifiedFolderState by unifiedFolderViewModel.uiState.collectAsStateWithLifecycle()
    
    val pagedPdfs = homeViewModel.pagedUncategorizedPdfsFlow.collectAsLazyPagingItems()
    val folderPagedPdfs = unifiedFolderViewModel.pagedPdfsFlow.collectAsLazyPagingItems()

    val isHomeRoute = destination?.hasRoute<Screen.Home>() == true
    val isFolderRoute = destination?.hasRoute<Screen.UnifiedFolder>() == true
    val isFoldersTabRoute = destination?.hasRoute<Screen.Folders>() == true
    val isToolsRoute = destination?.hasRoute<Screen.Tools>() == true
    val isSettingsRoute = destination?.hasRoute<Screen.Settings>() == true

    // 🚀 THE GLOBAL BACK HANDLER: Clears selection before screen navigation
    BackHandler(enabled = shellState.isSelectionMode) {
        shellViewModel.onAction(ShellAction.ClearSelection)
    }

    // 🚀 THE BOTTOM BAR VISIBILITY ENGINE: Strictly reactive to routes
    LaunchedEffect(destination) {
        // VIP Tabs that SHOULD show bottom bar
        val isMainTab = isHomeRoute || isFoldersTabRoute || isToolsRoute || isSettingsRoute
        
        // Screens that MUST hide bottom bar
        val isViewer = destination?.hasRoute<Screen.PdfViewer>() == true
        val isSearch = destination?.hasRoute<Screen.Search>() == true
        val isPermission = destination?.hasRoute<Screen.Permission>() == true
        val isUnifiedFolder = destination?.hasRoute<Screen.UnifiedFolder>() == true
        
        val shouldShow = isMainTab && !isViewer && !isSearch && !isPermission && !isUnifiedFolder
        
        // 🌟 GOLD STANDARD FIX: Delay bottom bar visibility slightly when navigating BACK 
        // to match the screen exit animation and prevent "jumping".
        if (shouldShow && !shellState.isBottomBarVisible) {
            kotlinx.coroutines.delay(200) 
        }
        shellViewModel.onAction(ShellAction.SetBottomBarVisible(shouldShow))
    }

    // 🚀 THE "STICKY" MASTER SELECTION: Integrated with ShellViewModel
    val selectedItems = remember(
        shellState.selectedIds, homeState.recentItems, homeState.currentFolders, 
        unifiedFolderState.folders, isHomeRoute, isFolderRoute
    ) {
        val ids = shellState.selectedIds
        
        // Collect from all visible sources to resolve IDs to full items
        val allAvailableItems = homeState.recentItems + 
                               homeState.currentFolders + 
                               pagedPdfs.itemSnapshotList.items.filterNotNull() + 
                               homeState.favoritePdfs.map { HomeItem.PdfItem(it) } +
                               unifiedFolderState.folders +
                               folderPagedPdfs.itemSnapshotList.items.filterNotNull()
        
        allAvailableItems.filter { it.id in ids }.distinctBy { it.id }
    }

    val isFullScreen = destination?.hasRoute<Screen.PdfViewer>() == true ||
            destination?.hasRoute<Screen.Vault>() == true ||
            destination?.hasRoute<Screen.Permission>() == true ||
            destination?.hasRoute<Screen.Search>() == true


    Row(modifier = Modifier.fillMaxSize()) {
        if (isTablet && !isFullScreen) {
            PremiumNavigationRail(navController = navController)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            contentWindowInsets = WindowInsets(0.dp),
                bottomBar = {
                    val isSelectionActive = shellState.isSelectionMode
                    AnimatedVisibility(
                        visible = shellState.isBottomBarVisible || isSelectionActive,
                        enter = fadeIn(tween(250, easing = FastOutSlowInEasing)) + 
                                expandIn(animationSpec = tween(250), expandFrom = Alignment.BottomCenter),
                        exit = fadeOut(tween(200)) + 
                               shrinkOut(animationSpec = tween(200), shrinkTowards = Alignment.BottomCenter),
                        label = "ShellBarVisibility"
                    ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        AnimatedContent(
                            targetState = shellState.isSelectionMode,
                            transitionSpec = { fadeIn(tween(120)) togetherWith fadeOut(tween(120)) },
                            label = "BottomBarMorph"
                        ) { isSelectionActive ->
                            if (isSelectionActive) {
                                SmartSelectionBottomBar(
                                    selectedItems = selectedItems,
                                    tabIndex = if (isHomeRoute) homeState.currentTabIndex else 1,
                                    onDelete = { 
                                        if (isHomeRoute) homeViewModel.onAction(HomeAction.ConfirmDelete(selectedItems))
                                        else unifiedFolderViewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(selectedItems)))
                                    },
                                    onMove = { 
                                        if (isHomeRoute) homeViewModel.onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(selectedItems)))
                                        else unifiedFolderViewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(selectedItems)))
                                    },
                                    onMerge = { Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show() },
                                    onShare = { shareItems(context, selectedItems) },
                                    onRemoveFromRecent = { homeViewModel.onAction(HomeAction.RemoveFromRecent(selectedItems)) },
                                    onUnfavorite = { homeViewModel.onAction(HomeAction.UnfavoritePdfs(selectedItems.filterIsInstance<HomeItem.PdfItem>().map { it.pdf })) }
                                )
                            } else {
                                PremiumBottomBar(navController = navController)
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            AdaptivePdfLayout(
                listContent = {
                    NavHost(
                        navController = navController,
                        startDestination = startScreen,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        enterTransition = { 
                            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn(tween(300))
                        },
                        exitTransition = { 
                            slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(300)) + fadeOut(tween(300))
                        },
                        popEnterTransition = { 
                            slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(300)) + fadeIn(tween(300))
                        },
                        popExitTransition = { 
                            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) + fadeOut(tween(300))
                        }
                    ) {
                        composable<Screen.Permission> {
                            PremiumPermissionScreen(onPermissionGranted = {
                                navController.navigate(Screen.Home) { popUpTo(Screen.Permission) { inclusive = true } }
                            })
                        }

                        homeSection(
                            navController = navController, 
                            isTablet = isTablet, 
                            viewModel = homeViewModel, 
                            shellViewModel = shellViewModel,
                            onPdfClickOverride = { path ->
                                if (isTablet) {
                                    selectedPdfPath = path
                                } else {
                                    navController.navigate(Screen.PdfViewer(pdfPath = path))
                                }
                            }
                        )
                        searchSection(navController = navController)
                        pdfViewerSection(
                            navController = navController, 
                            isExternalLaunch = { isExternalLaunch }, 
                            onExternalClosed = { isExternalLaunch = false }
                        )
                        foldersSection(
                            navController = navController, 
                            isTablet = isTablet, 
                            unifiedViewModel = unifiedFolderViewModel, 
                            shellViewModel = shellViewModel,
                            onPdfClickOverride = { path ->
                                if (isTablet) {
                                    selectedPdfPath = path
                                } else {
                                    navController.navigate(Screen.PdfViewer(pdfPath = path))
                                }
                            }
                        )
                        placeholderSections(navController = navController, isTablet = isTablet)
                    }
                },
                detailContent = { path ->
                    PdfViewerScreen(
                        pdfPath = path,
                        onBack = { selectedPdfPath = null }
                    )
                },
                selectedPdfPath = selectedPdfPath,
                onPdfSelected = { selectedPdfPath = it },
                onBack = { selectedPdfPath = null },
                isTablet = isTablet,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun shareItems(context: android.content.Context, items: List<HomeItem>) {
    val pdfUris = items.mapNotNull { it as? HomeItem.PdfItem }.map { it.pdf.id.toUri() }
    if (pdfUris.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "application/pdf"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, java.util.ArrayList(pdfUris))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share PDFs via"))
    }
}
