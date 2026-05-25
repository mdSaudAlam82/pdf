package com.edu.pdf.presentation.common.picker

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.presentation.common.PremiumBreadcrumbs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovePickerSheetRoute(
    folders: List<Folder> = emptyList(), // 🌟 Default value added
    onDismiss: () -> Unit,
    onTargetSelected: (String?) -> Unit,
    viewModel: MovePickerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // 🌟 ELITE FIX: Sirf tabhi update karo jab folders khali na ho (Home screen support)
    // Viewerscreen ke liye ViewModel ka apna 'init' block kaam karega
    LaunchedEffect(folders) {
        if (folders.isNotEmpty()) {
            viewModel.onAction(MovePickerAction.UpdateFolders(folders))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MovePickerEvent.MoveToTarget -> {
                    onTargetSelected(event.targetFolderId)
                    // 🌟 CLEAN TRANSITION: Close picker immediately
                    viewModel.onAction(MovePickerAction.NavigateTo(null))
                    onDismiss()
                }
                is MovePickerEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    MovePickerSheetContent(
        state = state,
        onAction = viewModel::onAction,
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovePickerSheetContent(
    state: MovePickerState,
    onAction: (MovePickerAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            if (state.currentParentId != null) {
                onAction(MovePickerAction.NavigateBack)
            } else {
                onDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // LATEST PREDICTIVE BACK HANDLER
        BackHandler(enabled = state.currentParentId != null) {
            onAction(MovePickerAction.NavigateBack)
        }

        Scaffold(
            // 🌟 STRICT FIX 1: Yahan se imePadding() HATA DIYA HAI!
            // Ab keyboard aane par button apni jagah par fix rahega.
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp) {
                    Column(modifier = Modifier.statusBarsPadding()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                            }
                            Text(
                                text = "Move to...",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                            )
                            IconButton(onClick = { onAction(MovePickerAction.ToggleCreateFolderDialog(true)) }) {
                                Icon(Icons.Default.CreateNewFolder, contentDescription = "New Folder", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        PremiumBreadcrumbs(
                            breadcrumbs = state.breadcrumbs,
                            rootName = "Home",
                            onNavigate = { folder -> onAction(MovePickerAction.NavigateTo(folder?.folderId)) },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        )
                    }
                }
            }
            // 🌟 STRICT FIX 2: Scaffold ka bottomBar hata diya gaya hai.
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()) // Sirf top padding di
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.subFolders.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No sub-folders here", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        // 🌟 STRICT FIX 3: Niche 120.dp ki jagah chhodi taaki list button ke upar aa sake
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(state.subFolders, key = { it.folderId }) { folder ->
                            com.edu.pdf.presentation.common.PremiumFolderListItem(
                                name = folder.name,
                                itemCount = folder.pdfCount,
                                showMoreOptions = false,
                                onClick = { onAction(MovePickerAction.NavigateTo(folder.folderId)) }
                            )
                        }
                    }
                }

                // 🌟 ELITE 2026 UI FIX: Floating Docked Button with Glass Fade
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = { onAction(MovePickerAction.ConfirmMoveHere) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Move Here", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }

        // ... Yahan se aapka if (state.isCreatingFolder) wala AlertDialog code aayega jo pehle se ekdum sahi hai ...
        if (state.isCreatingFolder) {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                keyboard?.show()
            }

            AlertDialog(
                onDismissRequest = { onAction(MovePickerAction.ToggleCreateFolderDialog(false)) },
                title = { Text("Create New Folder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = state.newFolderName,
                        onValueChange = { onAction(MovePickerAction.UpdateFolderName(it)) },
                        label = { Text("Folder Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                confirmButton = {
                    Button(onClick = { onAction(MovePickerAction.CreateAndEnterFolder) }, enabled = state.newFolderName.trim().isNotEmpty()) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(MovePickerAction.ToggleCreateFolderDialog(false)) }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}