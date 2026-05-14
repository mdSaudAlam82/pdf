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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val listState = rememberLazyListState()

    // Auto-scroll logic waisa hi powerful rakha gaya hai
    LaunchedEffect(breadcrumbs.size) {
        if (breadcrumbs.isNotEmpty()) {
            listState.animateScrollToItem(breadcrumbs.size)
        } else {
            listState.animateScrollToItem(0)
        }
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // 🌟 Vertical padding thodi kam ki taaki sleek lage
        state = listState,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 1. Root Node (e.g., Home)
        item {
            EliteBreadcrumbItem(
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
                modifier = Modifier.padding(horizontal = 4.dp).size(16.dp), // 🌟 Icon thoda chota kiya for premium look
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            val isLast = folder == breadcrumbs.last()
            EliteBreadcrumbItem(
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
private fun EliteBreadcrumbItem(name: String, isLast: Boolean, onClick: () -> Unit) {
    // 🌟 THE MAGIC: Purane folders transparent honge, sirf current folder highlight hoga
    val bgColor = if (isLast) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val textColor = if (isLast) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val fontWeight = if (isLast) FontWeight.Bold else FontWeight.Medium

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)) // 🌟 Modern soft rectangle shape
            .background(bgColor)
            .clickable(enabled = !isLast, onClick = onClick)
            .padding(horizontal = if (isLast) 12.dp else 6.dp, vertical = 6.dp), // 🌟 Dynamic padding
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = textColor,
            fontWeight = fontWeight,
            fontSize = 14.sp, // 🌟 Sleek professional size
            maxLines = 1,
            overflow = TextOverflow.Ellipsis // 🌟 Lamba naam hone par '...' dikhayega
        )
    }
}