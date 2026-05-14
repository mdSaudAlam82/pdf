package com.edu.pdf.presentation.common

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
    // 🌟 STRICT MVI: Logic yahin calculate hoga (Single Source of Truth)
    val folderCount = selectedItems.count { it is HomeItem.FolderItem }
    val pdfCount = selectedItems.count { it is HomeItem.PdfItem }
    val totalCount = selectedItems.size
    val hasFolderSelected = folderCount > 0

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding() // Edge-to-Edge, no fixed height
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            // 1. DELETE
            SmartActionItem(
                title = "Delete",
                icon = Icons.Default.Delete,
                enabled = totalCount > 0,
                disabledMessage = "Select items to delete",
                onClick = onDelete
            )

            // 2. CONTEXTUAL MIDDLE BUTTON
            when (tabIndex) {
                0 -> SmartActionItem(
                    title = "Remove",
                    icon = Icons.Default.HistoryToggleOff,
                    enabled = totalCount > 0,
                    disabledMessage = "Select items to remove",
                    onClick = onRemoveFromRecent
                )
                1 -> SmartActionItem(
                    title = "Move",
                    icon = Icons.AutoMirrored.Filled.DriveFileMove,
                    enabled = totalCount > 0,
                    disabledMessage = "Select items to move",
                    onClick = onMove
                )
                2 -> SmartActionItem(
                    title = "Unfavorite",
                    icon = Icons.Default.BookmarkRemove,
                    enabled = totalCount > 0,
                    disabledMessage = "Select items to unfavorite",
                    onClick = onUnfavorite
                )
            }

            // 3. MERGE (Smart Logic)
            val mergeEnabled = pdfCount >= 2 && !hasFolderSelected
            val mergeMsg = if (hasFolderSelected) "Cannot merge folders" else "Select at least 2 PDFs to merge"
            SmartActionItem(
                title = "Merge",
                icon = Icons.AutoMirrored.Filled.CallMerge,
                enabled = mergeEnabled,
                disabledMessage = mergeMsg,
                onClick = onMerge
            )

            // 4. SHARE (Smart Logic)
            val shareEnabled = totalCount > 0 && !hasFolderSelected
            val shareMsg = if (hasFolderSelected) "Cannot share folders" else "Select PDFs to share"
            SmartActionItem(
                title = "Share",
                icon = Icons.Default.Share,
                enabled = shareEnabled,
                disabledMessage = shareMsg,
                onClick = onShare
            )
        }
    }
}

@Composable
private fun RowScope.SmartActionItem(
    title: String,
    icon: ImageVector,
    enabled: Boolean,
    disabledMessage: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val animatedColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
        animationSpec = tween(durationMillis = 300),
        label = "ColorAnimation_$title"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable {
                if (enabled) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                } else {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast.makeText(context, disabledMessage, Toast.LENGTH_SHORT).show()
                }
            }
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = title, tint = animatedColor, modifier = Modifier.size(24.dp))
        Text(text = title, fontSize = 11.sp, color = animatedColor, fontWeight = FontWeight.Medium)
    }
}