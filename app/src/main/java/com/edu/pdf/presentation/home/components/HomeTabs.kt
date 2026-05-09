package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.height(48.dp),
        // 🌟 PRO FIX: Yeh bhi 100% Sheesha!
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) },
//...
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(selectedTabIndex)
                    .wrapContentSize(Alignment.BottomCenter)
            ) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    ) {
        homeTabsList.forEach { (title, icon, index) ->
            val isSelected = selectedTabIndex == index
            val itemColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, modifier = Modifier.size(18.dp), tint = itemColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = itemColor
                        )
                    }
                }
            )
        }
    }
}