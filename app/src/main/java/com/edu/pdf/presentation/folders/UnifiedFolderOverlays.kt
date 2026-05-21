package com.edu.pdf.presentation.folders

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.folders.components.FolderMenuSheet
import com.edu.pdf.presentation.common.PdfActionBottomSheet
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
                        onDelete = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(listOf(item)))) },
                        onActionClick = { actionTitle ->
                            when (actionTitle) {
                                "Move to" -> onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(listOf(item))))

                                "Move to Vault", "Remove from Vault" -> {
                                    onAction(UnifiedFolderAction.ToggleVaultStatus(item.pdf))
                                }

                                // 🌟 STEP 2 FIX: Folders ke andar MVI Rename trigger
                                "Rename" -> {
                                    val baseName = item.pdf.name.removeSuffix(".pdf").removeSuffix(".PDF")
                                    onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.RenameDialog(item, baseName)))
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
            // 🌟 NAYA ELITE MVI PICKER FOR UNIFIED FOLDERS
            com.edu.pdf.presentation.common.picker.MovePickerSheetRoute(
                folders = foldersTree,
                onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                onTargetSelected = { targetFolderId ->
                    onAction(UnifiedFolderAction.ConfirmMove(targetFolderId))
                }
            )
        }

        is UnifiedFolderSheetState.CreateFolderDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current
            var hasRequestedFocus by remember { mutableStateOf(false) }

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
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                if (!hasRequestedFocus) {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                    hasRequestedFocus = true
                                }
                            },
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
            var hasRequestedFocus by remember { mutableStateOf(false) }

            // 🌟 WAPAS AAGAYA: Smart Auto-Select logic
            var textFieldValue by remember {
                mutableStateOf(
                    androidx.compose.ui.text.input.TextFieldValue(
                        text = state.textInput,
                        selection = androidx.compose.ui.text.TextRange(0, state.textInput.length)
                    )
                )
            }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(UnifiedFolderAction.CloseSheet)
                },
                title = { Text("Rename", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = {
                            textFieldValue = it
                            onAction(UnifiedFolderAction.OnTextInputChange(it.text))
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                if (!hasRequestedFocus) {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                    hasRequestedFocus = true
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        // 🌟 WAPAS AAGAYA: Clear 'X' Icon
                        trailingIcon = {
                            if (textFieldValue.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    textFieldValue = androidx.compose.ui.text.input.TextFieldValue("", androidx.compose.ui.text.TextRange.Zero)
                                    onAction(UnifiedFolderAction.OnTextInputChange(""))
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(UnifiedFolderAction.ConfirmRename)
                        },
                        enabled = textFieldValue.text.trim().isNotEmpty()
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
            com.edu.pdf.presentation.common.picker.GlobalPdfPickerSheet(
                onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                onPdfsSelected = { selectedIds ->
                    onAction(UnifiedFolderAction.MovePdfsToCurrentFolder(selectedIds))
                }
            )
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