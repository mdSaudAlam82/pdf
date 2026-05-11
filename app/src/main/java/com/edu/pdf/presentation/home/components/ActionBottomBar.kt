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
import androidx.compose.ui.res.stringResource
import com.edu.pdf.R

@Composable
fun RowScope.ActionBottomBarItems(
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

    ActionItem(
        title = stringResource(R.string.action_delete),
        icon = Icons.Default.Delete,
        enabled = totalCount > 0,
        disabledMessage = stringResource(R.string.msg_select_delete),
        onClick = onDelete
    )

    when (tabIndex) {
        0 -> ActionItem(
            title = stringResource(R.string.action_remove),
            icon = Icons.Default.HistoryToggleOff,
            enabled = totalCount > 0,
            disabledMessage = stringResource(R.string.msg_select_remove),
            onClick = onRemoveFromRecent
        )
        1 -> ActionItem(
            title = stringResource(R.string.action_move),
            icon = Icons.AutoMirrored.Filled.DriveFileMove,
            enabled = totalCount > 0,
            disabledMessage = stringResource(R.string.msg_select_move),
            onClick = onMove
        )
        2 -> ActionItem(
            title = stringResource(R.string.action_unfav),
            icon = Icons.Default.BookmarkRemove,
            enabled = totalCount > 0,
            disabledMessage = stringResource(R.string.msg_select_unfav),
            onClick = onUnfavorite
        )
    }

    val mergeEnabled = pdfCount >= 2 && !hasFolderSelected
    val mergeMsg = if (hasFolderSelected) stringResource(R.string.msg_no_merge_folder) else stringResource(R.string.msg_select_2_pdf)

    ActionItem(
        title = stringResource(R.string.action_merge),
        icon = Icons.AutoMirrored.Filled.CallMerge,
        enabled = mergeEnabled,
        disabledMessage = mergeMsg,
        onClick = onMerge
    )

    val shareEnabled = totalCount > 0 && !hasFolderSelected
    val shareMsg = if (hasFolderSelected) stringResource(R.string.msg_no_share_folder) else stringResource(R.string.msg_select_pdf_share)

    ActionItem(
        title = stringResource(R.string.action_share),
        icon = Icons.Default.Share,
        enabled = shareEnabled,
        disabledMessage = shareMsg,
        onClick = onShare
    )
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