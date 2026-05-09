package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu.pdf.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val homeTabsList = listOf(
        Triple(stringResource(R.string.tab_recent), Icons.Default.Schedule, 0),
        Triple(stringResource(R.string.tab_all_files), Icons.Default.Description, 1),
        Triple(stringResource(R.string.tab_favorites), Icons.Default.Favorite, 2)
    )

    // 🌟 FIX: Switched to standard TabRow to access tabPositions safely.
    // Isse hum negative constraints wale Compose crash se bach jayenge.
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        containerColor = Color.Transparent,
        divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) },
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                val currentTab = tabPositions[selectedTabIndex]

                // 🌟 MILITARY GUARD: Jab tak tab ki width valid (0 se zyada) nahi hoti,
                // tab tak indicator animate nahi karega. No negative width = No crash!
                if (currentTab.width > 0.dp) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(currentTab),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    ) {
        homeTabsList.forEach { (title, icon, index) ->
            val isSelected = selectedTabIndex == index
            val tintColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, modifier = Modifier.size(18.dp), tint = tintColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = tintColor
                        )
                    }
                }
            )
        }
    }
}