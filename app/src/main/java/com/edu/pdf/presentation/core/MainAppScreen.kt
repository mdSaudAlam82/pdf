package com.edu.pdf.presentation.core

import android.os.Environment
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.folders.FoldersScreen
import com.edu.pdf.presentation.folders.UnifiedFolderScreen
import com.edu.pdf.presentation.folders.vault.VaultScreen
import com.edu.pdf.presentation.home.HomeScreenWrapper
import com.edu.pdf.presentation.home.HomeViewModel
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.pdfviewer.PdfViewerScreen
import com.edu.pdf.presentation.search.SearchScreen
import kotlinx.coroutines.launch
import androidx.navigation.NavType
import kotlin.reflect.typeOf

// 🌟 GATEWAY HELPER: PURE Android 13+ (MinSdk 33) Logic
fun hasStoragePermission(): Boolean {
    return Environment.isExternalStorageManager()
}

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()

    // 🌟 THE BOUNCER LOGIC
    val startScreen: Screen = remember {
        if (hasStoragePermission()) Screen.Home else Screen.Permission
    }

    NavHost(
        navController = navController,
        startDestination = startScreen,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        // 🌟 1. PERMISSION ROUTE
        composable<Screen.Permission> {
            PremiumPermissionScreen(
                onPermissionGranted = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Permission) { inclusive = true }
                    }
                }
            )
        }

        homeSection(navController)
        searchSection(navController)
        pdfViewerSection(navController)
        foldersSection(navController)
        placeholderSections(navController)
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun NavGraphBuilder.homeSection(navController: NavHostController) {
    composable<Screen.Home> {
        val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
        val scope = rememberCoroutineScope()

        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreenWrapper(
                    viewModel = homeViewModel,
                    navController = navController,
                    onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
                    onSearchClick = { navController.navigate(Screen.Search) },

                    onFolderClick = { folderId, folderName, folderType ->
                        // 🌟 THE ELITE FIX 1: No manual Uri.encode! Type-Safe navigation handles it.
                        // 🌟 THE ELITE FIX 2: Direct navController use kiya taaki SavedStateHandle me
                        //    sahi data jaye aur Blank Screen (white page) ka issue permanently solve ho jaye!
                        navController.navigate(Screen.UnifiedFolder(folderId, folderName, folderType))
                    }
                )
            },
            detailPane = {
                val folderArgs = navigator.currentDestination?.contentKey as? Screen.UnifiedFolder

                if (folderArgs != null) {
                    androidx.compose.runtime.key(folderArgs.folderId) {
                        UnifiedFolderScreen(
                            onBack = {
                                scope.launch { if (navigator.canNavigateBack()) navigator.navigateBack() }
                            },
                            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
                            onFolderNavigate = { id, name, type ->
                                scope.launch {
                                    navigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        contentKey = Screen.UnifiedFolder(
                                            android.net.Uri.encode(id),
                                            android.net.Uri.encode(name),
                                            type
                                        )
                                    )
                                }
                            },
                            onBreadcrumbNavigate = { folder ->
                                scope.launch {
                                    if (folder == null) {
                                        if (navigator.canNavigateBack()) navigator.navigateBack()
                                    } else {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            contentKey = Screen.UnifiedFolder(
                                                android.net.Uri.encode(folder.folderId),
                                                android.net.Uri.encode(folder.name),
                                                FolderType.VIRTUAL_HUB
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Select a folder to view contents", color = Color.Gray)
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.searchSection(navController: NavHostController) {
    composable<Screen.Search> { SearchScreen(onBackClick = { navController.popBackStack() }, onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) }) }
}

fun NavGraphBuilder.pdfViewerSection(navController: NavHostController) {
    composable<Screen.PdfViewer> { backStackEntry -> backStackEntry.toRoute<Screen.PdfViewer>(); PdfViewerScreen(onBack = { navController.popBackStack() }) }
}

fun NavGraphBuilder.foldersSection(navController: NavHostController) {
    composable<Screen.Folders> {
        Scaffold(
            bottomBar = { PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.padding(bottom = padding.calculateBottomPadding())) {
                FoldersScreen(
                    onFolderClick = { path, name ->
                        if (path == "vault_root") {
                            navController.navigate(Screen.Vault)
                        } else {
                            // 🌟 FOLDER OPEN HOTE WAQT SLASHES KO ENCODE KIYA HAI
                            navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(path), android.net.Uri.encode(name), FolderType.PHYSICAL_DEVICE))
                        }
                    }
                )
            }
        }
    }

    composable<Screen.Vault>(
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
    ) {
        VaultScreen(
            onBack = { navController.popBackStack<Screen.Folders>(inclusive = false) },
            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) }
        )
    }

    // 🌟 YAHAN SE TYPE-MAP HATA DIYA GAYA HAI
    // ... Inside foldersSection()
    // 🌟 FIX: Restored the typeMap! Enums MUST have this to avoid navigation serialization crashes.
    composable<Screen.UnifiedFolder>(
        typeMap = mapOf(typeOf<FolderType>() to NavType.EnumType(FolderType::class.java)),
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
    ) { backStackEntry ->
// ... rest of the code ...
        val args = backStackEntry.toRoute<Screen.UnifiedFolder>()

        UnifiedFolderScreen(
            onBack = { navController.popBackStack() },
            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
            onFolderNavigate = { id, name, type ->
                // 🌟 ENCODE KARKE BHEJ RAHE HAIN
                navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type))
            },
            onBreadcrumbNavigate = { folder ->
                if (folder == null) {
                    if (args.folderType == FolderType.PHYSICAL_DEVICE) {
                        navController.popBackStack<Screen.Folders>(inclusive = false)
                    } else {
                        navController.popBackStack<Screen.Home>(inclusive = false)
                    }
                } else {
                    val bType = FolderType.VIRTUAL_HUB
                    navController.popBackStack(Screen.UnifiedFolder(android.net.Uri.encode(folder.folderId), android.net.Uri.encode(folder.name), bType), inclusive = false)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.placeholderSections(navController: NavHostController) {

    // 🌟 1. TOOLS SCREEN
    composable<Screen.Tools> {
        Scaffold(
            topBar = { UniversalTopBar(title = "Tools") },
            bottomBar = { PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = "Tools coming soon...", color = Color.Gray)
            }
        }
    }

    // 🌟 2. SETTINGS SCREEN
    composable<Screen.Settings> {
        Scaffold(
            topBar = { UniversalTopBar(title = "Settings") },
            bottomBar = { PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = "Settings coming soon...", color = Color.Gray)
            }
        }
    }
}