package com.edu.pdf.presentation.home

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Folder
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.folders.components.FolderMenuSheet
import com.edu.pdf.presentation.home.components.MoveFolderListItem
import com.edu.pdf.presentation.home.components.PdfActionBottomSheet
import com.edu.pdf.presentation.home.components.SortBottomSheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeOverlays(
    state: HomeUiState,
    foldersTree: List<com.edu.pdf.domain.model.Folder> = emptyList(),
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
            var localFolderName by rememberSaveable { mutableStateOf("") }
            var hasRequestedFocus by remember { mutableStateOf(false) }

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

            // 🌟 WAPAS AAGAYA: Smart Auto-Select logic
            var textFieldValue by remember {
                mutableStateOf(
                    androidx.compose.ui.text.input.TextFieldValue(
                        text = activeSheet.currentName,
                        selection = androidx.compose.ui.text.TextRange(0, activeSheet.currentName.length)
                    )
                )
            }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(HomeAction.CloseSheet)
                },
                title = { Text("Rename", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
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
                        // 🌟 WAPAS AAGAYA: Clear 'X' Icon
                        trailingIcon = {
                            if (textFieldValue.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    textFieldValue = androidx.compose.ui.text.input.TextFieldValue("", androidx.compose.ui.text.TextRange.Zero)
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
                            onAction(HomeAction.ConfirmRename(textFieldValue.text))
                        },
                        enabled = textFieldValue.text.trim().isNotEmpty(),
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
                        onClick = { onAction(HomeAction.ConfirmDelete) },
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
            HomeHierarchicalMovePickerSheet(
                folders = foldersTree,
                itemsBeingMoved = activeSheet.items,
                onDismiss = { onAction(HomeAction.CloseSheet) },
                onFolderSelected = { targetFolderId -> onAction(HomeAction.ConfirmMove(targetFolderId)) },
                onLocalCreateFolder = { name, parentId ->
                    onAction(HomeAction.CreateContextualFolder(name, parentId))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHierarchicalMovePickerSheet(
    folders: List<com.edu.pdf.domain.model.Folder>,
    itemsBeingMoved: List<HomeItem>,
    onDismiss: () -> Unit,
    onFolderSelected: (String?) -> Unit,
    onLocalCreateFolder: (String, String?) -> Unit
) {
    var showLocalNewFolderDialog by rememberSaveable { mutableStateOf(false) }
    var localNewFolderName by rememberSaveable { mutableStateOf("") }
    var currentParentId by rememberSaveable { mutableStateOf<String?>(null) }
    val haptic = LocalHapticFeedback.current

    // 🌟 THE ELITE FIX: Recursive filtering to prevent Cyclic Dependencies (Logic Untouched)
    val invalidFolderIds = remember(folders, itemsBeingMoved) {
        val movedFolderIds = itemsBeingMoved.filterIsInstance<HomeItem.FolderItem>().map { it.folder.folderId }
        val invalidSet = mutableSetOf<String>()

        fun addWithDescendants(folderId: String) {
            if (invalidSet.add(folderId)) {
                folders.filter { it.parentFolderId == folderId }.forEach { child ->
                    addWithDescendants(child.folderId)
                }
            }
        }
        movedFolderIds.forEach { addWithDescendants(it) }
        invalidSet
    }

    val currentFolders = remember(folders, currentParentId, invalidFolderIds) {
        folders.filter { it.parentFolderId == currentParentId && !invalidFolderIds.contains(it.folderId) }
            .sortedBy { it.name.lowercase() }
    }

    val breadcrumbs = remember(folders, currentParentId) {
        val list = mutableListOf<com.edu.pdf.domain.model.Folder>()
        var curr = folders.find { it.folderId == currentParentId }
        while (curr != null) {
            list.add(0, curr)
            val parentId = curr.parentFolderId
            curr = folders.find { it.folderId == parentId }
        }
        list
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false // 🌟 Edge to Edge Enabled
        )
    ) {
        BackHandler {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            if (currentParentId != null) {
                currentParentId = breadcrumbs.dropLast(1).lastOrNull()?.folderId
            } else {
                onDismiss()
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f), // Slightly transparent top
                    shadowElevation = 0.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth().systemBarsPadding()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                            }
                            Text(
                                text = "Move ${itemsBeingMoved.size} item" + if (itemsBeingMoved.size > 1) "s" else "",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                            )
                            IconButton(onClick = { showLocalNewFolderDialog = true }) {
                                Icon(Icons.Default.CreateNewFolder, contentDescription = "New Folder", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        // 🌟 Breadcrumbs Navigation Area
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (currentParentId == null) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                                modifier = Modifier.clickable {
                                    if (currentParentId != null) {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        currentParentId = null
                                    }
                                }
                            ) {
                                Text(
                                    text = "Home",
                                    fontWeight = if (currentParentId == null) FontWeight.Bold else FontWeight.Medium,
                                    color = if (currentParentId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }

                            com.edu.pdf.presentation.common.PremiumBreadcrumbs(
                                breadcrumbs = breadcrumbs,
                                rootName = "", // Empty to hide extra root
                                onNavigate = { folder ->
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    currentParentId = folder?.folderId
                                }
                            )
                        }
                    }
                }
            },
            // 🌟 PRO FIX: Bottom bar hata diya! Ab button screen ke upar float karega.
            bottomBar = {}
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp) // Scroll button ke piche tak jayega
                ) {
                    if (currentFolders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Folder, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("This folder is empty", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Tap the + icon to create a folder here", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    } else {
                        items(currentFolders, key = { it.folderId }) { folder ->
                            MoveFolderListItem(
                                folder = folder,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    currentParentId = folder.folderId
                                }
                            )
                        }
                    }
                }

                // 🌟 WORLD-CLASS FLOATING BUTTON WITH BLUR/GRADIENT
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .navigationBarsPadding() // Button safe area me rahega, gradient niche tak jayega
                ) {
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onFolderSelected(currentParentId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp), // Premium curve
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Move Here",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // ... Tumhara LocalNewFolderDialog ka code yahan aayega, usme koi change nahi hai ...
            if (showLocalNewFolderDialog) {
                val focusRequester = remember { FocusRequester() }
                val keyboard = LocalSoftwareKeyboardController.current

                LaunchedEffect(Unit) {
                    androidx.compose.runtime.withFrameNanos { }
                    focusRequester.requestFocus()
                    keyboard?.show()
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .pointerInput(Unit) { detectTapGestures { /* Block clicks */ } },
                    contentAlignment = Alignment.Center
                ) {
                    AlertDialog(
                        onDismissRequest = {
                            keyboard?.hide()
                            showLocalNewFolderDialog = false
                            localNewFolderName = ""
                        },
                        title = { Text("New Folder", fontWeight = FontWeight.Bold) },
                        text = {
                            OutlinedTextField(
                                value = localNewFolderName,
                                onValueChange = { localNewFolderName = it },
                                label = { Text("Folder Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                                shape = RoundedCornerShape(12.dp)
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    keyboard?.hide()
                                    if (localNewFolderName.trim().isNotEmpty()) {
                                        onLocalCreateFolder(localNewFolderName.trim(), currentParentId)
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                    showLocalNewFolderDialog = false
                                    localNewFolderName = ""
                                },
                                enabled = localNewFolderName.trim().isNotEmpty()
                            ) { Text("Create") }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                keyboard?.hide()
                                showLocalNewFolderDialog = false
                                localNewFolderName = ""
                            }) { Text("Cancel") }
                        },
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}