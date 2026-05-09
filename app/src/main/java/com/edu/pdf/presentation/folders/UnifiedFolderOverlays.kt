package com.edu.pdf.presentation.folders

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.folders.components.FolderMenuSheet
import com.edu.pdf.presentation.home.HomeHierarchicalMovePickerSheet
import com.edu.pdf.presentation.home.components.PdfActionBottomSheet
import com.edu.pdf.presentation.home.components.SortBottomSheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnifiedFolderOverlays(
    state: UnifiedFolderUiState,
    foldersTree: List<com.edu.pdf.domain.model.Folder>,
    onAction: (UnifiedFolderAction) -> Unit
) {
    val context = LocalContext.current

    when (val sheetState = state.activeSheetState) {
        is UnifiedFolderSheetState.None -> {}

        is UnifiedFolderSheetState.SortPicker -> {
            SortBottomSheet(
                currentSort = state.sortType,
                onSortSelected = { onAction(UnifiedFolderAction.UpdateSortType(it)) },
                onDismiss = { onAction(UnifiedFolderAction.CloseSheet) }
            )
        }

        is UnifiedFolderSheetState.ItemMenu -> {
            when (val item = sheetState.item) {
                is HomeItem.FolderItem -> {
                    FolderMenuSheet(
                        folder = item.folder,
                        onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                        onRenameClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.RenameDialog(item, item.folder.name))) },
                        onMoveToClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(listOf(item)))) },
                        onDetailsClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DetailsDialog(item))) },
                        onDeleteClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(listOf(item)))) }
                    )
                }
                is HomeItem.PdfItem -> {
                    PdfActionBottomSheet(
                        pdf = item.pdf,
                        onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                        onFavoriteToggle = {
                            onAction(UnifiedFolderAction.ToggleFavorite(item.pdf.id, !item.pdf.isFavorite))
                            Toast.makeText(context, if (item.pdf.isFavorite) "Removed from Favorites" else "Added to Favorites", Toast.LENGTH_SHORT).show()
                            onAction(UnifiedFolderAction.CloseSheet)
                        },
                        onShare = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, item.pdf.id.toUri())
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDF"))
                            onAction(UnifiedFolderAction.CloseSheet)
                        },
                        onRenameConfirm = { newName ->
                            onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.RenameDialog(item, newName)))
                            onAction(UnifiedFolderAction.OnTextInputChange(newName))
                            onAction(UnifiedFolderAction.ConfirmRename)
                        },
                        onDelete = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(listOf(item)))) },
                        onDetails = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DetailsDialog(item))) },
                        onActionClick = { actionTitle ->
                            when (actionTitle) {
                                "Move to" -> onAction(
                                    UnifiedFolderAction.OpenSheet(
                                        UnifiedFolderSheetState.MovePicker(
                                            listOf(item)
                                        )
                                    )
                                )

                                // 🌟 WIRING: Dynamic Vault Clicks
                                "Move to Vault", "Remove from Vault" -> {
                                    onAction(UnifiedFolderAction.ToggleVaultStatus(item.pdf))
                                }

                                else -> {
                                    Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
                                    onAction(UnifiedFolderAction.CloseSheet)
                                }
                            }
                        }
                    )
                }
            }
        }

        is UnifiedFolderSheetState.MovePicker -> {
            HomeHierarchicalMovePickerSheet(
                folders = foldersTree,
                itemsBeingMoved = sheetState.items,
                onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                onFolderSelected = { onAction(UnifiedFolderAction.ConfirmMove(it)) },
                onLocalCreateFolder = { _, _ ->
                    Toast.makeText(context, "Please create folders from the main screen", Toast.LENGTH_SHORT).show()
                }
            )
        }

        is UnifiedFolderSheetState.CreateFolderDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current

            LaunchedEffect(Unit) {
                // 🌟 PRO FIX: No delay, frame ka wait karo
                androidx.compose.runtime.withFrameNanos { }
                focusRequester.requestFocus()
                keyboard?.show()
            }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(UnifiedFolderAction.CloseSheet)
                },
                title = { Text("New Folder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = state.textInput,
                        onValueChange = { onAction(UnifiedFolderAction.OnTextInputChange(it)) },
                        label = { Text("Folder Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(12.dp)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(UnifiedFolderAction.ConfirmCreateFolder)
                        },
                        enabled = state.textInput.trim().isNotEmpty()
                    ) { Text("Create", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(UnifiedFolderAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is UnifiedFolderSheetState.RenameDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current

            LaunchedEffect(Unit) {
                // 🌟 PRO FIX: No delay, frame ka wait karo
                androidx.compose.runtime.withFrameNanos { }
                focusRequester.requestFocus()
                keyboard?.show()
            }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(UnifiedFolderAction.CloseSheet)
                },
                title = { Text("Rename", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = state.textInput,
                        onValueChange = { onAction(UnifiedFolderAction.OnTextInputChange(it)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        shape = RoundedCornerShape(12.dp)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(UnifiedFolderAction.ConfirmRename)
                        },
                        enabled = state.textInput.trim().isNotEmpty()
                    ) { Text("Rename", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(UnifiedFolderAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is UnifiedFolderSheetState.DeleteConfirm -> {
            val itemCount = sheetState.items.size
            AlertDialog(
                onDismissRequest = { onAction(UnifiedFolderAction.CloseSheet) },
                title = { Text(if (itemCount > 1) "Delete $itemCount items?" else "Delete Item?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Text(
                        "Are you sure you want to permanently delete the selected item(s)? This action cannot be undone.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onAction(UnifiedFolderAction.ConfirmDelete) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Delete", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(UnifiedFolderAction.CloseSheet) }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is UnifiedFolderSheetState.DetailsDialog -> {
            val name = when (val item = sheetState.item) {
                is HomeItem.FolderItem -> item.folder.name
                is HomeItem.PdfItem -> item.pdf.name
            }
            val dateRaw = sheetState.item.lastModified
            val sizeStr = if (sheetState.item is HomeItem.PdfItem) {
                val bytes = sheetState.item.pdf.sizeInBytes
                if (bytes >= 1024 * 1024) String.format(Locale.US, "%.1f MB", bytes / (1024f * 1024f))
                else String.format(Locale.US, "%.1f KB", bytes / 1024f)
            } else "Folder Directory"

            val currentLocale = LocalConfiguration.current.locales[0]
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", currentLocale)
            val formattedDate = sdf.format(Date(if (dateRaw < 1000000000000L) dateRaw * 1000 else dateRaw))

            AlertDialog(
                onDismissRequest = { onAction(UnifiedFolderAction.CloseSheet) },
                title = { Text("Item Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Column {
                        DetailRow("Name", name)
                        DetailRow("Size/Type", sizeStr)
                        DetailRow("Modified", formattedDate)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onAction(UnifiedFolderAction.CloseSheet) }) {
                        Text("Close", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // 🌟 2026 PREMIUM FULL-SCREEN PICKER UX
        is UnifiedFolderSheetState.AppPdfPicker -> {
            var selectedIds by remember { mutableStateOf(setOf<String>()) }
            var searchQuery by remember { mutableStateOf("") }
            val haptic = LocalHapticFeedback.current

            // On-the-fly Instant Search
            val filteredPdfs = remember(searchQuery, sheetState.allPdfs) {
                if (searchQuery.isBlank()) sheetState.allPdfs
                else sheetState.allPdfs.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }

            Dialog(
                onDismissRequest = { onAction(UnifiedFolderAction.CloseSheet) },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false, // Takes full screen
                    decorFitsSystemWindows = false
                )
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp) {
                            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { onAction(UnifiedFolderAction.CloseSheet) }) {
                                        Icon(Icons.Default.Close, contentDescription = "Close")
                                    }
                                    Text("Select PDFs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

                                    AnimatedVisibility(visible = selectedIds.isNotEmpty()) {
                                        Button(
                                            onClick = { onAction(UnifiedFolderAction.MovePdfsToCurrentFolder(selectedIds.toList())) },
                                            modifier = Modifier.padding(end = 8.dp)
                                        ) {
                                            Text("Add (${selectedIds.size})", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                // Live Premium Search Bar
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
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
                            }
                        }
                    }
                ) { padding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding)
                    ) {
                        if (filteredPdfs.isEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                                    Text("No files found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        } else {
                            items(items = filteredPdfs, key = { it.id }) { pdf ->
                                val isSelected = selectedIds.contains(pdf.id)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            selectedIds = if (isSelected) selectedIds - pdf.id else selectedIds + pdf.id
                                        }
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = pdf.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = android.text.format.Formatter.formatShortFileSize(context, pdf.sizeInBytes),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}