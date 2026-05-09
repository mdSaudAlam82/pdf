package com.edu.pdf.presentation.folders.vault

import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.presentation.home.components.PdfActionBottomSheet

// 🌟 THE ELITE FIX: Hilt-aware wrapper for Navigation Graph
@Composable
fun VaultScreen(
    onBack: () -> Unit,
    onPdfClick: (String) -> Unit,
    viewModel: VaultViewModel = hiltViewModel()
) {
    val vaultPdfs by viewModel.vaultPdfs.collectAsStateWithLifecycle()
    val pickerPdfs by viewModel.pickerPdfs.collectAsStateWithLifecycle()
    val decryptionProgress by viewModel.decryptionProgress.collectAsStateWithLifecycle()
    val isGridView by viewModel.isGridView.collectAsStateWithLifecycle()

    VaultScreenPure(
        vaultPdfs = vaultPdfs,
        pickerPdfs = pickerPdfs,
        decryptionProgress = decryptionProgress,
        isGridView = isGridView,
        onBack = onBack,
        onPdfClick = onPdfClick,
        onAction = viewModel::onAction
    )
}

// 🌟 THE ELITE FIX: Pure UI Component (Testable & Previewable)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreenPure(
    vaultPdfs: List<PdfFile>,
    pickerPdfs: List<PdfFile>?,
    decryptionProgress: Float?,
    isGridView: Boolean,
    onBack: () -> Unit,
    onPdfClick: (String) -> Unit,
    onAction: (VaultAction) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var selectedPdfForActions by remember { mutableStateOf<PdfFile?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Private Vault", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                        onAction(VaultAction.ToggleViewMode)
                    }) {
                        Icon(
                            imageVector = if (isGridView) Icons.AutoMirrored.Rounded.ViewList else Icons.Rounded.GridView,
                            contentDescription = "Toggle View"
                        )
                    }
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                        onAction(VaultAction.LoadPublicPdfs)
                    }) {
                        Icon(Icons.Rounded.Add, contentDescription = "Add PDF")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (vaultPdfs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Vault is empty", color = Color.Gray)
                }
            } else {
                if (isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(vaultPdfs, key = { it.id }) { pdf ->
                            VaultPdfGridItem(
                                pdf = pdf,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onAction(VaultAction.OpenPdf(pdf.path) { secureUri -> onPdfClick(secureUri) })
                                },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedPdfForActions = pdf
                                },
                                onMoreClick = { selectedPdfForActions = pdf }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(vaultPdfs, key = { it.id }) { pdf ->
                            VaultPdfListItem(
                                pdf = pdf,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onAction(VaultAction.OpenPdf(pdf.path) { secureUri -> onPdfClick(secureUri) })
                                },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedPdfForActions = pdf
                                },
                                onMoreClick = { selectedPdfForActions = pdf }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = decryptionProgress != null,
                enter = fadeIn(), exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .pointerInput(Unit) { detectTapGestures {} },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            progress = { decryptionProgress ?: 0f },
                            color = MaterialTheme.colorScheme.primary, strokeWidth = 6.dp, modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Unlocking Vault...", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("${((decryptionProgress ?: 0f) * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }

    if (pickerPdfs != null) {
        VaultAppPickerSheet(
            pdfs = pickerPdfs,
            onDismiss = { onAction(VaultAction.ClosePicker) },
            onPdfSelected = { pdf ->
                onAction(VaultAction.MoveToVault(listOf(pdf.id)))
                onAction(VaultAction.ClosePicker)
            }
        )
    }

    selectedPdfForActions?.let { pdf ->
        PdfActionBottomSheet(
            pdf = pdf,
            onDismiss = { selectedPdfForActions = null },
            onFavoriteToggle = { },
            onShare = { },
            onRenameConfirm = { newName ->
                onAction(VaultAction.RenamePdf(pdf, newName))
                selectedPdfForActions = null
            },
            onDelete = {
                onAction(VaultAction.DeletePdf(pdf))
                selectedPdfForActions = null
            },
            onDetails = { },
            onActionClick = { actionName ->
                if (actionName.contains("Restore", ignoreCase = true) || actionName.contains("Move", ignoreCase = true) || actionName.contains("Remove", ignoreCase = true)) {
                    onAction(VaultAction.RemoveFromVault(pdf.id))
                }
                selectedPdfForActions = null
            }
        )
    }
}

// ... Baaki code (VaultAppPickerSheet, VaultPdfListItem, VaultPdfGridItem) waise hi rehne dein ...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultAppPickerSheet(
    pdfs: List<PdfFile>,
    onDismiss: () -> Unit,
    onPdfSelected: (PdfFile) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp).heightIn(max = 400.dp)) {
            Text("Select PDF to Secure", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            if (pdfs.isEmpty()) {
                Text("No PDFs found outside vault", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(pdfs) { pdf ->
                        VaultPdfListItem(
                            pdf = pdf,
                            onClick = { onPdfSelected(pdf) },
                            onLongClick = {},
                            onMoreClick = {}
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VaultPdfListItem(pdf: PdfFile, onClick: () -> Unit, onLongClick: () -> Unit, onMoreClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.PictureAsPdf, null, tint = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(pdf.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(Formatter.formatShortFileSize(context, pdf.sizeInBytes), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onMoreClick) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "Options")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VaultPdfGridItem(pdf: PdfFile, onClick: () -> Unit, onLongClick: () -> Unit, onMoreClick: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            IconButton(onClick = onMoreClick, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Rounded.MoreVert, contentDescription = "Options", modifier = Modifier.size(20.dp))
            }
        }
        Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.PictureAsPdf, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(pdf.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(Formatter.formatShortFileSize(context, pdf.sizeInBytes), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}