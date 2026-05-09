package com.edu.pdf.presentation.home.components

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.*
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
fun ActionBottomBar(
    selectedItems: List<HomeItem>,
    tabIndex: Int,
    onDelete: () -> Unit,
    onMove: () -> Unit,
    onMerge: () -> Unit,
    onShare: () -> Unit,
    onRemoveFromRecent: () -> Unit,
    onUnfavorite: () -> Unit
) {
    // 🌟 SMART ENGINE: Checks exactly what user selected
    val folderCount = selectedItems.count { it is HomeItem.FolderItem }
    val pdfCount = selectedItems.count { it is HomeItem.PdfItem }
    val totalCount = selectedItems.size
    val hasFolderSelected = folderCount > 0

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. DELETE
            ActionItem(
                title = "Delete",
                icon = Icons.Default.Delete,
                enabled = totalCount > 0,
                disabledMessage = "Select items to delete",
                onClick = onDelete
            )

            // 2. DYNAMIC TAB ACTION
            when (tabIndex) {
                0 -> ActionItem("Remove", Icons.Default.HistoryToggleOff, enabled = totalCount > 0, disabledMessage = "Select items to remove", onClick = onRemoveFromRecent)
                1 -> ActionItem("Move", Icons.AutoMirrored.Filled.DriveFileMove, enabled = totalCount > 0, disabledMessage = "Select items to move", onClick = onMove)
                2 -> ActionItem("Unfav", Icons.Default.BookmarkRemove, enabled = totalCount > 0, disabledMessage = "Select items to unfavorite", onClick = onUnfavorite)
            }

            // 3. MERGE (🌟 ALWAYS VISIBLE, but becomes colorless if folders are selected)
            val mergeEnabled = pdfCount >= 2 && !hasFolderSelected
            val mergeMsg = if (hasFolderSelected) "Folders cannot be merged" else "Select at least 2 PDFs to merge"
            ActionItem(
                title = "Merge",
                icon = Icons.AutoMirrored.Filled.CallMerge,
                enabled = mergeEnabled,
                disabledMessage = mergeMsg,
                onClick = onMerge
            )

            // 4. SHARE (🌟 ALWAYS VISIBLE, but becomes colorless if folders are selected)
            val shareEnabled = totalCount > 0 && !hasFolderSelected
            val shareMsg = if (hasFolderSelected) "Folders cannot be shared directly" else "Select PDFs to share"
            ActionItem(
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
private fun RowScope.ActionItem(
    title: String,
    icon: ImageVector,
    enabled: Boolean,
    disabledMessage: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // 🌟 THE "COLORLESS" UX MAGIC
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
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = title, tint = animatedColor, modifier = Modifier.size(24.dp))
        Text(text = title, fontSize = 11.sp, color = animatedColor, fontWeight = FontWeight.Medium)
    }
}