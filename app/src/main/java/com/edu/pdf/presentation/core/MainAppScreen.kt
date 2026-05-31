package com.edu.pdf.presentation.core

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.edu.pdf.presentation.folders.UnifiedFolderAction
import com.edu.pdf.presentation.folders.UnifiedFolderSheetState
import com.edu.pdf.presentation.folders.UnifiedFolderViewModel
import com.edu.pdf.presentation.folders.foldersSection
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.HomeSheetState
import com.edu.pdf.presentation.home.HomeViewModel
import com.edu.pdf.presentation.home.homeSection
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.pdfviewer.pdfViewerSection
import com.edu.pdf.presentation.placeholder.placeholderSections
import com.edu.pdf.presentation.search.searchSection
import java.io.File

/**
 * 🌟 THE ATOMIC SELECTION CONTEXT
 * Frozen state of selection for zero-flicker animations.
 */
data class SelectionContext(
    val active: Boolean,
    val items: List<HomeItem>,
    val tabIndex: Int,
    val isHome: Boolean
)

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

    LaunchedEffect(externalPdfUri) {
        if (!externalPdfUri.isNullOrBlank()) {
            isExternalLaunch = true
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
    val homeViewModel: HomeViewModel = hiltViewModel(viewModelStoreOwner = activityViewModelStoreOwner)
    val unifiedFolderViewModel: UnifiedFolderViewModel = hiltViewModel(viewModelStoreOwner = activityViewModelStoreOwner)
    
    val homeState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val unifiedFolderState by unifiedFolderViewModel.uiState.collectAsStateWithLifecycle()
    
    val pagedPdfs = homeViewModel.pagedUncategorizedPdfsFlow.collectAsLazyPagingItems()
    val folderPagedPdfs = unifiedFolderViewModel.pagedPdfsFlow.collectAsLazyPagingItems()

    val isHomeRoute = destination?.hasRoute<Screen.Home>() == true
    val isFolderRoute = destination?.hasRoute<Screen.UnifiedFolder>() == true

    // 🚀 THE "STICKY" MASTER SELECTION: The Final Solution for Flicker
    var persistentContext by remember { mutableStateOf(SelectionContext(false, emptyList(), 1, false)) }

    val currentRawContext = remember(
        homeState.isSelectionMode, homeState.selectedIds, 
        unifiedFolderState.isSelectionMode, unifiedFolderState.selectedIds,
        isHomeRoute, isFolderRoute, homeState.currentTabIndex
    ) {
        if (isHomeRoute && homeState.isSelectionMode) {
            val items = (homeState.recentItems + homeState.currentFolders + pagedPdfs.itemSnapshotList.items + homeState.favoritePdfs.map { HomeItem.PdfItem(it) })
                .filter { it.id in homeState.selectedIds }.distinctBy { it.id }
            SelectionContext(true, items, homeState.currentTabIndex, true)
        } else if (isFolderRoute && unifiedFolderState.isSelectionMode) {
            val items = (unifiedFolderState.folders + folderPagedPdfs.itemSnapshotList.items)
                .filter { it.id in unifiedFolderState.selectedIds }.distinctBy { it.id }
            SelectionContext(true, items, 1, false)
        } else {
            persistentContext.copy(active = false)
        }
    }

    SideEffect {
        if (currentRawContext.active) {
            persistentContext = currentRawContext
        } else if (persistentContext.active) {
            persistentContext = persistentContext.copy(active = false)
        }
    }

    val isFullScreen = destination?.hasRoute<Screen.PdfViewer>() == true ||
            destination?.hasRoute<Screen.Vault>() == true ||
            destination?.hasRoute<Screen.Permission>() == true ||
            destination?.hasRoute<Screen.Search>() == true

    val shouldShowShellBar = if (isFolderRoute) persistentContext.active else !isTablet && !isFullScreen

    Row(modifier = Modifier.fillMaxSize()) {
        if (isTablet && !isFullScreen) {
            PremiumNavigationRail(navController = navController)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            contentWindowInsets = WindowInsets(0.dp),
            bottomBar = {
                AnimatedVisibility(
                    visible = shouldShowShellBar,
                    enter = slideInVertically(animationSpec = tween(150)) { it } + fadeIn(tween(100)),
                    exit = slideOutVertically(animationSpec = tween(150)) { it } + fadeOut(tween(100)),
                    label = "ShellBarVisibility"
                ) {
                    // 🌟 ELITE FIX: Yeh Box "hawa mein latkne" ki problem solve karega.
                    // Yeh ek solid patti (background) banayega jo system navigation bar tak jayegi.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface) // Yeh apki White Patti hai
                            .windowInsetsPadding(WindowInsets.navigationBars) // Yeh zameen (bottom) se chipka dega
                    ) {
                        AnimatedContent(
                            targetState = persistentContext.active,
                            transitionSpec = { fadeIn(tween(120)) togetherWith fadeOut(tween(120)) },
                            label = "BottomBarMorph"
                        ) { isSelectionActive ->
                            if (isSelectionActive) {
                                if (persistentContext.isHome) {
                                    SmartSelectionBottomBar(
                                        selectedItems = persistentContext.items,
                                        tabIndex = persistentContext.tabIndex,
                                        onDelete = { homeViewModel.onAction(HomeAction.ConfirmDelete(persistentContext.items)) },
                                        onMove = { homeViewModel.onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(persistentContext.items))) },
                                        onMerge = { Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show() },
                                        onShare = { shareItems(context, persistentContext.items) },
                                        onRemoveFromRecent = { homeViewModel.onAction(HomeAction.RemoveFromRecent(persistentContext.items)) },
                                        onUnfavorite = { homeViewModel.onAction(HomeAction.UnfavoritePdfs(persistentContext.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf })) }
                                    )
                                } else {
                                    SmartSelectionBottomBar(
                                        selectedItems = persistentContext.items,
                                        tabIndex = 1,
                                        onDelete = { unifiedFolderViewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(persistentContext.items))) },
                                        onMove = { unifiedFolderViewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(persistentContext.items))) },
                                        onMerge = { Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show() },
                                        onShare = { shareItems(context, persistentContext.items) },
                                        onRemoveFromRecent = {},
                                        onUnfavorite = {}
                                    )
                                }
                            } else {
                                PremiumBottomBar(navController = navController)
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startScreen,
                modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(300)) }
            ) {
                composable<Screen.Permission> {
                    PremiumPermissionScreen(onPermissionGranted = {
                        navController.navigate(Screen.Home) { popUpTo(Screen.Permission) { inclusive = true } }
                    })
                }

                homeSection(navController = navController, isTablet = isTablet, viewModel = homeViewModel)
                searchSection(navController = navController)
                pdfViewerSection(navController = navController, isExternalLaunch = { isExternalLaunch }, onExternalClosed = { isExternalLaunch = false })
                foldersSection(navController = navController, isTablet = isTablet, unifiedViewModel = unifiedFolderViewModel)
                placeholderSections(navController = navController, isTablet = isTablet)
            }
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
