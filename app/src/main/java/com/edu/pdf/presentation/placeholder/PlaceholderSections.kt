package com.edu.pdf.presentation.placeholder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.placeholderSections(
    navController: NavHostController,
    isTablet: Boolean
) {
    composable<Screen.Tools> {
        Scaffold(
            topBar = { UniversalTopBar(title = "Tools") },
            bottomBar = { if (!isTablet) PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0.dp)
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tools Section Coming Soon")
            }
        }
    }
    
    composable<Screen.Settings> {
        SettingsScreen(
            isTablet = isTablet,
            navController = navController
        )
    }
}
