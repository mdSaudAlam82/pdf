package com.edu.pdf.presentation.placeholder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.placeholderSections(
    navController: NavHostController,
    isTablet: Boolean
) {
    composable<Screen.Tools> {
        // 🌟 ARCHITECTURE MASTERPIECE: Pure Content
        Column(modifier = Modifier.fillMaxSize()) {
            UniversalTopBar(title = "Tools")
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Tools Section Coming Soon")
            }
        }
    }
    
    composable<Screen.Settings> {
        SettingsScreen()
    }
}
