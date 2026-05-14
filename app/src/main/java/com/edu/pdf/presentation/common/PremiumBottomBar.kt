package com.edu.pdf.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.edu.pdf.presentation.navigation.Screen

data class BottomNavItem<T : Any>(
    val title: String,
    val icon: ImageVector,
    val route: T
)

// 🌟 Ye Folders, Tools aur Settings me chalega
@Composable
fun PremiumBottomBar(navController: NavHostController) {
    // 🌟 SLIM FIX: NavigationBar hatakar Surface lagaya gaya
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding() // Edge to Edge!
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PremiumBottomBarItems(navController)
        }
    }
}

// 🌟 Ye Items dega jo hum HomeScreen (aur upar wale bar) me use karenge
@Composable
fun RowScope.PremiumBottomBarItems(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.Home),
        BottomNavItem("Folders", Icons.Default.Folder, Screen.Folders),
        BottomNavItem("Tools", Icons.Default.AutoFixHigh, Screen.Tools),
        BottomNavItem("Settings", Icons.Default.Settings, Screen.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val haptic = LocalHapticFeedback.current

    items.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
        val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

        // 🌟 Ziddi NavigationBarItem ko HATA DIYA gaya hai, ye Custom Column Slim hai!
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (!isSelected) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                .padding(vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(imageVector = item.icon, contentDescription = item.title, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.title, fontSize = 11.sp, color = color, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium)
        }
    }
}

// Tablet UI ke liye purana code same rakha gaya hai
@Composable
fun PremiumNavigationRail(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.Home),
        BottomNavItem("Folders", Icons.Default.Folder, Screen.Folders),
        BottomNavItem("Tools", Icons.Default.AutoFixHigh, Screen.Tools),
        BottomNavItem("Settings", Icons.Default.Settings, Screen.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

            NavigationRailItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}