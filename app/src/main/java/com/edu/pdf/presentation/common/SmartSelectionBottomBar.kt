package com.edu.pdf.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.HomeItem

@Composable
fun SmartSelectionBottomBar(
    selectedItems: List<HomeItem>,
    tabIndex: Int,
    onDelete: () -> Unit,
    onMove: () -> Unit,
    onMerge: () -> Unit,
    onShare: () -> Unit,
    onRemoveFromRecent: () -> Unit,
    onUnfavorite: () -> Unit
) {
    val folderCount = selectedItems.count { it is HomeItem.FolderItem }
    val pdfCount = selectedItems.count { it is HomeItem.PdfItem }
    val totalCount = selectedItems.size
    val hasFolderSelected = folderCount > 0

    val haptic = LocalHapticFeedback.current

    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. DELETE
            SelectionActionItem(
                title = "Delete",
                icon = Icons.Default.Delete,
                color = MaterialTheme.colorScheme.error,
                onClick = onDelete
            )

            // 2. CONTEXTUAL
            val (contextTitle, contextIcon, contextAction) = when (tabIndex) {
                0 -> Triple("Remove", Icons.Default.HistoryToggleOff, onRemoveFromRecent)
                2 -> Triple("Unfavorite", Icons.Default.BookmarkRemove, onUnfavorite)
                else -> Triple("Move", Icons.AutoMirrored.Filled.DriveFileMove, onMove)
            }
            SelectionActionItem(
                title = contextTitle,
                icon = contextIcon,
                onClick = contextAction
            )

            // 3. MERGE
            val mergeEnabled = pdfCount >= 2 && !hasFolderSelected
            SelectionActionItem(
                title = "Merge",
                icon = Icons.AutoMirrored.Filled.CallMerge,
                enabled = mergeEnabled,
                onClick = onMerge
            )

            // 4. SHARE
            val shareEnabled = totalCount > 0 && !hasFolderSelected
            SelectionActionItem(
                title = "Share",
                icon = Icons.Default.Share,
                enabled = shareEnabled,
                onClick = onShare
            )
        }
    }
}

@Composable
private fun RowScope.SelectionActionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val finalColor = if (enabled) color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(enabled = enabled) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(vertical = 2.dp), // 🌟 2px (2dp) EXACT PADDING
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = finalColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = title, fontSize = 11.sp, color = finalColor, fontWeight = FontWeight.Bold)
    }
}
