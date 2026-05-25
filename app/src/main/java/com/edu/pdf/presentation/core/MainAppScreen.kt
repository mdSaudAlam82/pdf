package com.edu.pdf.presentation.core

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.presentation.common.PremiumNavigationRail
import com.edu.pdf.presentation.folders.foldersSection
import com.edu.pdf.presentation.home.homeSection
import com.edu.pdf.presentation.navigation.Screen
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
            navController.navigate(
                Screen.UnifiedFolder(
                    folderId = decodedPath, 
                    folderName = folderName, 
                    folderType = FolderType.VIRTUAL_HUB
                )
            )
            onNavigateConsumed()
        }
    }

    val activity = androidx.activity.compose.LocalActivity.current ?: return
    val windowSizeClass = calculateWindowSizeClass(activity = activity)
    val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val startScreen: Screen = remember {
        if (hasStoragePermission()) Screen.Home else Screen.Permission
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val isFullScreen = destination?.hasRoute<Screen.PdfViewer>() == true ||
            destination?.hasRoute<Screen.Vault>() == true ||
            destination?.hasRoute<Screen.Permission>() == true ||
            destination?.hasRoute<Screen.Search>() == true

    Row(modifier = Modifier.fillMaxSize()) {
        // 🌟 RESTORED: Tablets get the Navigation Rail
        if (isTablet && !isFullScreen) {
            PremiumNavigationRail(navController = navController)
        }

        // 🌟 ARCHITECTURE FIX: Shell NO LONGER manages bottom bar.
        // Each screen now manages its own bottom bar for pixel-perfect swapping.
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
                    com.edu.pdf.presentation.core.PremiumPermissionScreen(onPermissionGranted = {
                        navController.navigate(Screen.Home) { popUpTo(Screen.Permission) { inclusive = true } }
                    })
                }

                homeSection(navController = navController, isTablet = isTablet)
                searchSection(navController = navController)
                pdfViewerSection(
                    navController = navController,
                    isExternalLaunch = { isExternalLaunch },
                    onExternalClosed = { isExternalLaunch = false }
                )
                foldersSection(navController = navController, isTablet = isTablet)
                placeholderSections(navController = navController, isTablet = isTablet)
            }
        }
    }
}
