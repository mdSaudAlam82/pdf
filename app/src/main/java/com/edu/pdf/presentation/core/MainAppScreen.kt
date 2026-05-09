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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.edu.pdf.presentation.home.selection.SelectionViewModel
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.pdfviewer.PdfViewerScreen
import com.edu.pdf.presentation.search.SearchScreen

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

fun NavGraphBuilder.homeSection(navController: NavHostController) {
    composable<Screen.Home> {
        // 🌟 LAZY INJECTION: ViewModel tabhi banega jab permission mil jayegi aur Home khulega!
        // Isse app crash ya hang bilkul nahi hoga.
        val homeViewModel: HomeViewModel = hiltViewModel()
        val selectionViewModel: SelectionViewModel = hiltViewModel()

        HomeScreenWrapper(
            viewModel = homeViewModel,
            selectionViewModel = selectionViewModel,
            navController = navController,
            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
            onSearchClick = { navController.navigate(Screen.Search) }
        )
    }
}

fun NavGraphBuilder.searchSection(navController: NavHostController) {
    composable<Screen.Search> { SearchScreen(onBackClick = { navController.popBackStack() }, onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) }) }
}

fun NavGraphBuilder.pdfViewerSection(navController: NavHostController) {
    composable<Screen.PdfViewer> { backStackEntry -> backStackEntry.toRoute<Screen.PdfViewer>(); PdfViewerScreen(onBack = { navController.popBackStack() }) }
}

// ... MainAppScreen.kt ke andar foldersSection block dhundhein aur isse replace karein ...

fun NavGraphBuilder.foldersSection(navController: NavHostController) {
    composable<Screen.Folders> {
        Scaffold(
            bottomBar = { PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background // 🌟 Background color match kiya
        ) { padding ->
            // 🌟 GOD MODE FIX: Double Padding (Mota Top Bar) hatane ke liye
            // Hum sirf `bottom = padding.calculateBottomPadding()` use kar rahe hain.
            Box(modifier = Modifier.padding(bottom = padding.calculateBottomPadding())) {
                FoldersScreen(
                    onFolderClick = { path, name ->
                        if (path == "vault_root") {
                            navController.navigate(Screen.Vault)
                        } else {
                            navController.navigate(Screen.UnifiedFolder(path, name, FolderType.PHYSICAL_DEVICE))
                        }
                    }
                )
            }
        }
    }

    // 🌟 (Iske neeche tumhara Screen.Vault aur Screen.UnifiedFolder wala code waisa ka waisa hi rahega)

    // 🌟 NAYA: Dedicated Vault Navigation Block
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
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<Screen.UnifiedFolder>()

        UnifiedFolderScreen(
            onBack = { navController.popBackStack() },
            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
            onFolderNavigate = { id, name, type -> navController.navigate(Screen.UnifiedFolder(id, name, type)) },
            onBreadcrumbNavigate = { folder ->
                if (folder == null) {
                    if (args.folderType == FolderType.PHYSICAL_DEVICE) {
                        navController.popBackStack<Screen.Folders>(inclusive = false)
                    } else {
                        navController.popBackStack<Screen.Home>(inclusive = false)
                    }
                } else {
                    val bType = FolderType.VIRTUAL_HUB
                    navController.popBackStack(Screen.UnifiedFolder(folder.folderId, folder.name, bType), inclusive = false)
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