package com.edu.pdf.presentation.common.picker

import android.text.format.DateUtils
import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets // 🌟 ADDED
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding // 🌟 ADDED
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.home.components.PdfThumbnail
import com.edu.pdf.presentation.search.components.HighlightedText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalPdfPickerSheet(
    onDismiss: () -> Unit,
    onPdfsSelected: (List<String>) -> Unit,
    viewModel: PdfPickerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    Dialog(
        onDismissRequest = {
            // 🌟 PRO FIX: Dialog ka apna Back System override kar diya!
            if (state.currentFolderId != null) {
                // Folder ke andar hain, toh ek step piche jao
                val parentFolder = if (state.breadcrumbs.size > 1) {
                    state.breadcrumbs[state.breadcrumbs.size - 2]
                } else {
                    null
                }
                viewModel.onAction(PdfPickerAction.NavigateToFolder(parentFolder))
            } else {
                // Root par hain, toh picker band karo
                viewModel.onAction(PdfPickerAction.ClearSelection)
                onDismiss()
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().imePadding(), // 🌟 EXACT FIX 1: Keyboard aane par size adjust hoga
            contentWindowInsets = WindowInsets(0.dp),       // 🌟 EXACT FIX 2: Dialog padding ko reset kiya
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp) {
                    Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                viewModel.onAction(PdfPickerAction.ClearSelection)
                                onDismiss()
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                            Text("Select PDFs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

                            AnimatedVisibility(visible = state.selectedIds.isNotEmpty()) {
                                Button(
                                    onClick = {
                                        onPdfsSelected(state.selectedIds.toList())
                                        viewModel.onAction(PdfPickerAction.ClearSelection)
                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Add (${state.selectedIds.size})", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // 🌟 Premium Search Bar
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { viewModel.onAction(PdfPickerAction.OnSearchQueryChange(it)) },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("Search files...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        if (state.searchQuery.isBlank() && state.breadcrumbs.isNotEmpty()) {
                            com.edu.pdf.presentation.common.PremiumBreadcrumbs(
                                breadcrumbs = state.breadcrumbs,
                                rootName = "Root",
                                onNavigate = { folder -> viewModel.onAction(PdfPickerAction.NavigateToFolder(folder)) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (state.items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (state.searchQuery.isBlank()) "Folder is empty" else "No matching PDFs", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                    items(items = state.items, key = { it.id }) { item ->
                        when (item) {
                            is HomeItem.FolderItem -> {
                                PickerFolderRow(
                                    folder = item.folder,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.onAction(PdfPickerAction.NavigateToFolder(item.folder))
                                    }
                                )
                            }
                            is HomeItem.PdfItem -> {
                                val isSelected = state.selectedIds.contains(item.pdf.id)
                                PickerPdfRow(
                                    pdf = item.pdf,
                                    searchQuery = state.searchQuery,
                                    isSelected = isSelected,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.onAction(PdfPickerAction.ToggleSelection(item.pdf.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 🌟 ZERO BLOAT UI COMPONENTS

@Composable
private fun PickerFolderRow(folder: Folder, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(48.dp).height(60.dp).clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Folder, contentDescription = "Folder", tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(36.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(folder.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
            Text("${folder.pdfCount} items", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
    }
}

@Composable
private fun PickerPdfRow(pdf: com.edu.pdf.domain.model.PdfFile, searchQuery: String, isSelected: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current
    val displaySize = remember(pdf.sizeInBytes) { Formatter.formatShortFileSize(context, pdf.sizeInBytes) }
    val displayDate = remember(pdf.lastModified) {
        DateUtils.getRelativeTimeSpanString(if (pdf.lastModified < 1000000000000L) pdf.lastModified * 1000 else pdf.lastModified, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(48.dp).height(60.dp).clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            PdfThumbnail(pdf = pdf, modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            HighlightedText(
                text = pdf.name,
                query = searchQuery, // 🌟 HIGHLIGHT MAGIC ENABLED!
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("$displayDate  •  $displaySize", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(
            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.Circle,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
    }
}