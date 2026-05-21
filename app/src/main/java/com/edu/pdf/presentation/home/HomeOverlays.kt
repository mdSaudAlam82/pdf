package com.edu.pdf.presentation.home

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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun HomeOverlays(
    state: HomeUiState,
    foldersTree: List<com.edu.pdf.domain.model.Folder> = emptyList(), // 🌟 Yahan add karna tha
    onAction: (HomeAction) -> Unit
) {
    val context = LocalContext.current

    when (val activeSheet = state.activeSheetState) {
        is HomeSheetState.None -> { /* App is Idle - UI is completely clean */ }

        is HomeSheetState.SortPicker -> {
            SortBottomSheet(
                currentSort = state.sortType,
                onSortSelected = { type -> onAction(HomeAction.UpdateSortType(type)) },
                onDismiss = { onAction(HomeAction.CloseSheet) }
            )
        }

        is HomeSheetState.CreateFolderDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current
            var hasRequestedFocus by remember { mutableStateOf(false) }

            // 🌟 NAYA: UI ke andar apna local state
            var localFolderName by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(HomeAction.CloseSheet)
                },
                title = { Text("New Folder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = localFolderName,
                        onValueChange = { localFolderName = it },
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
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(HomeAction.ConfirmCreateFolder(localFolderName))
                        },
                        enabled = localFolderName.trim().isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Create", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(HomeAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is HomeSheetState.ItemMenu -> {
            when (val item = activeSheet.item) {
                is HomeItem.FolderItem -> {
                    FolderMenuSheet(
                        folder = item.folder,
                        onDismiss = { onAction(HomeAction.CloseSheet) },
                        onRenameClick = { onAction(HomeAction.OpenSheet(HomeSheetState.RenameDialog(item, item.folder.name))) },
                        onMoveToClick = { onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(listOf(item)))) },
                        onDetailsClick = { onAction(HomeAction.OpenSheet(HomeSheetState.DetailsDialog(item))) },
                        onDeleteClick = { onAction(HomeAction.OpenSheet(HomeSheetState.DeleteConfirm(listOf(item)))) }
                    )
                }
                is HomeItem.PdfItem -> {
                    PdfActionBottomSheet(
                        pdf = item.pdf,
                        onDismiss = { onAction(HomeAction.CloseSheet) },
                        onFavoriteToggle = {
                            onAction(HomeAction.ToggleFavorite(item.pdf))
                            Toast.makeText(context, if (item.pdf.isFavorite) "Removed from Favorites" else "Added to Favorites", Toast.LENGTH_SHORT).show()
                            onAction(HomeAction.CloseSheet)
                        },
                        onShare = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, item.pdf.id.toUri())
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDF"))
                            onAction(HomeAction.CloseSheet)
                        },
                        // Deleted onRenameConfirm completely to fix the parameter error
                        onDelete = { onAction(HomeAction.OpenSheet(HomeSheetState.DeleteConfirm(listOf(item)))) },
                        onActionClick = { actionTitle ->
                            when (actionTitle) {
                                "Move to" -> onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(listOf(item))))

                                "Move to Vault", "Remove from Vault" -> {
                                    onAction(HomeAction.ToggleVaultStatus(item.pdf))
                                }

                                "Rename" -> {
                                    val baseName = item.pdf.name.removeSuffix(".pdf").removeSuffix(".PDF")
                                    onAction(HomeAction.OpenSheet(HomeSheetState.RenameDialog(item, baseName)))
                                }

                                else -> {
                                    Toast.makeText(context, "Feature coming soon!", Toast.LENGTH_SHORT).show()
                                    onAction(HomeAction.CloseSheet)
                                }
                            }
                        }
                    )
                }
            }
        }

        is HomeSheetState.RenameDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current
            var hasRequestedFocus by remember { mutableStateOf(false) }

            // 🌟 NAYA: Rename ke liye local state
            var localRenameText by remember { mutableStateOf(activeSheet.currentName) }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(HomeAction.CloseSheet)
                },
                title = { Text("Rename", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = localRenameText,
                        onValueChange = { localRenameText = it },
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        trailingIcon = {
                            if (localRenameText.isNotEmpty()) {
                                IconButton(onClick = {
                                    localRenameText = ""
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
                            onAction(HomeAction.ConfirmRename(activeSheet.item, localRenameText))
                        },
                        enabled = localRenameText.trim().isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Rename", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(HomeAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is HomeSheetState.DeleteConfirm -> {
            val itemCount = activeSheet.items.size
            AlertDialog(
                onDismissRequest = { onAction(HomeAction.CloseSheet) },
                title = { Text(if (itemCount > 1) "Delete $itemCount items?" else "Delete Item?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Text(
                        "Are you sure you want to permanently delete the selected item(s)? This action cannot be undone.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onAction(HomeAction.ConfirmDelete(activeSheet.items)) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Delete", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(HomeAction.CloseSheet) }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is HomeSheetState.MovePicker -> {
            com.edu.pdf.presentation.common.picker.MovePickerSheetRoute(
                folders = foldersTree, // 🌟 बस ये एक लाइन ऐड करनी है!
                onDismiss = { onAction(HomeAction.CloseSheet) },
                onTargetSelected = { targetFolderId ->
                    onAction(HomeAction.ConfirmMove(targetFolderId))
                }
            )
        }

        is HomeSheetState.DetailsDialog -> {
            val name = when (val item = activeSheet.item) {
                is HomeItem.FolderItem -> item.folder.name
                is HomeItem.PdfItem -> item.pdf.name
            }
            val dateRaw = activeSheet.item.lastModified
            val sizeStr = if (activeSheet.item is HomeItem.PdfItem) {
                val bytes = activeSheet.item.pdf.sizeInBytes
                if (bytes >= 1024 * 1024) String.format(Locale.US, "%.1f MB", bytes / (1024f * 1024f))
                else String.format(Locale.US, "%.1f KB", bytes / 1024f)
            } else "Folder Directory"

            val currentLocale = LocalConfiguration.current.locales[0]
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", currentLocale)
            val formattedDate = sdf.format(Date(if (dateRaw < 1000000000000L) dateRaw * 1000 else dateRaw))

            AlertDialog(
                onDismissRequest = { onAction(HomeAction.CloseSheet) },
                title = { Text("Item Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Column {
                        DetailRow("Name", name)
                        DetailRow("Size/Type", sizeStr)
                        DetailRow("Modified", formattedDate)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onAction(HomeAction.CloseSheet) }) {
                        Text("Close", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
        is HomeSheetState.AppPdfPicker -> {
            com.edu.pdf.presentation.common.picker.GlobalPdfPickerSheet(
                onDismiss = { onAction(HomeAction.CloseSheet) },
                onPdfsSelected = { selectedIds ->
                    onAction(HomeAction.MovePdfsToCurrentFolder(selectedIds))
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

