package com.edu.pdf.presentation.core

import android.os.Environment
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.PremiumNavigationRail
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
import kotlin.reflect.typeOf

fun hasStoragePermission(): Boolean {
    return Environment.isExternalStorageManager()
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    LocalContext.current

    val windowSizeClass = calculateWindowSizeClass(activity = androidx.activity.compose.LocalActivity.current ?: return)
    val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val startScreen: Screen = remember {
        if (hasStoragePermission()) Screen.Home else Screen.Permission
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isFullScreen = currentRoute?.contains(Screen.PdfViewer::class.simpleName ?: "") == true ||
            currentRoute?.contains(Screen.Vault::class.simpleName ?: "") == true ||
            currentRoute?.contains(Screen.Permission::class.simpleName ?: "") == true ||
            currentRoute?.contains(Screen.Search::class.simpleName ?: "") == true

    Row(modifier = Modifier.fillMaxSize()) {
        if (isTablet && !isFullScreen) {
            PremiumNavigationRail(navController = navController)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startScreen,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                composable<Screen.Permission> {
                    PremiumPermissionScreen(onPermissionGranted = {
                        navController.navigate(Screen.Home) { popUpTo(Screen.Permission) { inclusive = true } }
                    })
                }

                // 🌟 FIX: Sabko properly navController aur isTablet pass kar diya hai
                homeSection(navController = navController, isTablet = isTablet)
                searchSection(navController = navController)
                pdfViewerSection(navController = navController)
                foldersSection(navController = navController, isTablet = isTablet)
                placeholderSections(navController = navController, isTablet = isTablet)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun NavGraphBuilder.homeSection(navController: NavHostController, isTablet: Boolean) {
    composable<Screen.Home> {
        val paneNavigator = rememberListDetailPaneScaffoldNavigator<Any>()
        val scope = rememberCoroutineScope()

        NavigableListDetailPaneScaffold(
            navigator = paneNavigator,
            listPane = {
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreenWrapper(
                    viewModel = homeViewModel,
                    navController = navController,
                    onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
                    onFolderClick = { id, name, type ->
                        // 🌟 THE MAGIC FIX: Mobile aur Tablet ke liye alag logic
                        if (isTablet) {
                            // Agar Tablet hai, toh split-pane mein kholo
                            scope.launch {
                                paneNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    contentKey = Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type)
                                )
                            }
                        } else {
                            // Agar Phone hai, toh normal nav push karo (Ye step-by-step backstack banayega!)
                            navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type))
                        }
                    },
                    onSearchClick = { navController.navigate(Screen.Search) }
                )
            },
            detailPane = {
                val folderArgs = paneNavigator.currentDestination?.contentKey as? Screen.UnifiedFolder
                if (folderArgs != null) {
                    androidx.compose.runtime.key(folderArgs.folderId) {
                        UnifiedFolderScreen(
                            folderId = folderArgs.folderId,
                            folderName = folderArgs.folderName,
                            folderType = folderArgs.folderType,
                            onBack = {
                                scope.launch {
                                    if (paneNavigator.canNavigateBack()) {
                                        paneNavigator.navigateBack()
                                    } else {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
                            onFolderNavigate = { id, name, type ->
                                // 🌟 MAGIC FIX 2: Nested Sub-folders humesha navController mein push honge
                                // Isse aapko step-by-step back jane ka option milega.
                                navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type))
                            },
                            onBreadcrumbNavigate = { folder ->
                                scope.launch {
                                    if (folder == null) {
                                        if (paneNavigator.canNavigateBack()) paneNavigator.navigateBack()
                                    } else {
                                        paneNavigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            contentKey = Screen.UnifiedFolder(android.net.Uri.encode(folder.folderId), android.net.Uri.encode(folder.name), FolderType.VIRTUAL_HUB)
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

fun NavGraphBuilder.foldersSection(navController: NavHostController, isTablet: Boolean) {
    composable<Screen.Folders> {
        Scaffold(
            bottomBar = { if (!isTablet) PremiumBottomBar(navController) },
            contentWindowInsets = WindowInsets(0.dp)
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                FoldersScreen(
                    onFolderClick = { path, name ->
                        if (path == "vault_root") {
                            navController.navigate(Screen.Vault)
                        } else {
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

    composable<Screen.UnifiedFolder>(
        typeMap = mapOf(typeOf<FolderType>() to NavType.EnumType(FolderType::class.java)),
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<Screen.UnifiedFolder>()
        UnifiedFolderScreen(
            folderId = args.folderId,         // 🌟 FIX: Id pass ki
            folderName = args.folderName,     // 🌟 FIX: Name pass ki
            folderType = args.folderType,
            onBack = { navController.popBackStack() },
            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
            onFolderNavigate = { id, name, type ->
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
fun NavGraphBuilder.placeholderSections(navController: NavHostController, isTablet: Boolean) {
    composable<Screen.Tools> {
        Scaffold(
            topBar = { UniversalTopBar(title = "Tools") },
            bottomBar = { if (!isTablet) PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = "Tools coming soon...", color = Color.Gray)
            }
        }
    }

    composable<Screen.Settings> {
        Scaffold(
            topBar = { UniversalTopBar(title = "Settings") },
            bottomBar = { if (!isTablet) PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = "Settings coming soon...", color = Color.Gray)
            }
        }
    }
}