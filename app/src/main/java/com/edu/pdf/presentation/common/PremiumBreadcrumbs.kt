package com.edu.pdf.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.Folder

@Composable
fun PremiumBreadcrumbs(
    breadcrumbs: List<Folder>,
    rootName: String = "Home",
    onNavigate: (Folder?) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    // 🌟 ELITE FIX: Auto-Scroll State
    val listState = rememberLazyListState()

    // 🌟 THE MAGIC: Jab bhi breadcrumbs ki size badhegi, ye automatically last me slide ho jayega
    LaunchedEffect(breadcrumbs.size) {
        if (breadcrumbs.isNotEmpty()) {
            listState.animateScrollToItem(breadcrumbs.size) // Root node (0) + breadcrumbs
        } else {
            listState.animateScrollToItem(0)
        }
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        state = listState, // 🌟 State attach kar diya
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 1. Root Node (e.g., Home)
        item {
            BreadcrumbPill(
                name = rootName,
                isLast = breadcrumbs.isEmpty(),
                onClick = {
                    if (breadcrumbs.isNotEmpty()) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate(null)
                    }
                }
            )
        }

        // 2. Sub-folders (Dynamic Path)
        items(breadcrumbs) { folder ->
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 4.dp).size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            val isLast = folder == breadcrumbs.last()
            BreadcrumbPill(
                name = folder.name,
                isLast = isLast,
                onClick = {
                    if (!isLast) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate(folder)
                    }
                }
            )
        }
    }
}

@Composable
private fun BreadcrumbPill(name: String, isLast: Boolean, onClick: () -> Unit) {
    val bgColor = if (isLast) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val textColor = if (isLast) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val fontWeight = if (isLast) FontWeight.Bold else FontWeight.Medium

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .clickable(enabled = !isLast, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name, color = textColor, fontWeight = fontWeight, fontSize = 16.sp)
    }
}