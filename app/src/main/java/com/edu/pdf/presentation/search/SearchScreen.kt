package com.edu.pdf.presentation.search

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.airbnb.lottie.compose.*
import com.airbnb.lottie.RenderMode
import com.edu.pdf.R
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.presentation.common.PdfActionBottomSheet
import com.edu.pdf.presentation.home.components.PdfThumbnail
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.search.components.HighlightedText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onPdfClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query = uiState.query
    val results = uiState.results
    val history = uiState.history

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is SearchEvent.ShowSnackbar -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    TextField(
                        value = query,
                        onValueChange = { newValue ->
                            viewModel.onAction(SearchAction.OnQueryChange(newValue))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        placeholder = { Text("Search your PDFs...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            viewModel.onAction(SearchAction.SaveSearchQuery(query))
                            keyboardController?.hide()
                        }),
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = {
                                    viewModel.onAction(SearchAction.ClearSearch)
                                    focusRequester.requestFocus()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    )
                }
            }

            if (query.isBlank()) {
                ZeroStateView(
                    history = history,
                    onHistoryItemClick = { pastQuery -> viewModel.onAction(SearchAction.OnQueryChange(pastQuery)) },
                    onRemoveHistoryItem = { viewModel.onAction(SearchAction.RemoveHistoryItem(it)) },
                    onClearAll = { viewModel.onAction(SearchAction.ClearAllHistory) }
                )
            } else if (results.isEmpty()) {
                EmptyStateView(query = query)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(results, key = { it.id }) { pdf ->
                        SearchItemRow(
                            pdf = pdf,
                            query = query,
                            onClick = {
                                viewModel.onAction(SearchAction.SaveSearchQuery(query))
                                viewModel.onAction(SearchAction.MarkPdfAsOpened(pdf.id))
                                keyboardController?.hide()
                                onPdfClick(pdf.path)
                            },
                            onMoreClick = {
                                scope.launch {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    delay(200)
                                    viewModel.onAction(SearchAction.OpenSheet(SearchSheetState.PdfMenu(pdf)))
                                }
                            }
                        )
                    }
                }
            }
        }

        when (val sheetState = uiState.activeSheetState) {
            is SearchSheetState.PdfMenu -> {
                PdfActionBottomSheet(
                    pdf = sheetState.pdf,
                    onDismiss = { viewModel.onAction(SearchAction.CloseSheet) },
                    onFavoriteToggle = {
                        viewModel.onAction(SearchAction.ToggleFavorite(sheetState.pdf))
                    },
                    onShare = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_STREAM, sheetState.pdf.id.toUri())
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
                        viewModel.onAction(SearchAction.CloseSheet)
                    },
                    onDelete = {
                        viewModel.onAction(SearchAction.OpenSheet(SearchSheetState.DeleteConfirmation(sheetState.pdf)))
                    },
                    onActionClick = { actionTitle ->
                        when (actionTitle) {
                            "Rename" -> {
                                val baseName = sheetState.pdf.name.removeSuffix(".pdf").removeSuffix(".PDF")
                                viewModel.onAction(SearchAction.OpenSheet(SearchSheetState.RenameDialog(sheetState.pdf, baseName)))
                            }
                            "Move to" -> {
                                viewModel.onAction(SearchAction.OpenSheet(SearchSheetState.MovePicker(sheetState.pdf)))
                            }
                            "Move to Vault", "Remove from Vault" -> {
                                viewModel.onAction(SearchAction.ToggleVaultStatus(sheetState.pdf))
                                viewModel.onAction(SearchAction.CloseSheet)
                            }
                            else -> {
                                Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
                                viewModel.onAction(SearchAction.CloseSheet)
                            }
                        }
                    }
                )
            }

            is SearchSheetState.MovePicker -> {
                com.edu.pdf.presentation.common.picker.MovePickerSheetRoute(
                    onDismiss = { viewModel.onAction(SearchAction.CloseSheet) },
                    onTargetSelected = { targetId ->
                        viewModel.onAction(SearchAction.MoveToFolder(sheetState.pdf, targetId))
                    }
                )
            }

            is SearchSheetState.RenameDialog -> {
                val focusRequesterDialog = remember { FocusRequester() }
                var hasRequestedFocus by remember { mutableStateOf(false) }

                AlertDialog(
                    onDismissRequest = { viewModel.onAction(SearchAction.CloseSheet) },
                    title = { Text("Rename File", fontWeight = FontWeight.Bold) },
                    text = {
                        OutlinedTextField(
                            value = uiState.renameInput,
                            onValueChange = { viewModel.onAction(SearchAction.UpdateRenameInput(it)) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterDialog)
                                .onGloballyPositioned {
                                    if (!hasRequestedFocus) {
                                        focusRequesterDialog.requestFocus()
                                        keyboardController?.show()
                                        hasRequestedFocus = true
                                    }
                                },
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                if (uiState.renameInput.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.onAction(SearchAction.UpdateRenameInput(""))
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear text")
                                    }
                                }
                            }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                keyboardController?.hide()
                                if (uiState.renameInput.isNotBlank()) {
                                    viewModel.onAction(SearchAction.ConfirmRename)
                                }
                            }
                        ) { Text("Rename") }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.onAction(SearchAction.CloseSheet) }) { Text("Cancel") }
                    }
                )
            }

            is SearchSheetState.DeleteConfirmation -> {
                AlertDialog(
                    onDismissRequest = { viewModel.onAction(SearchAction.CloseSheet) },
                    title = { Text("Delete File?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                    text = {
                        Text(
                            "Are you sure you want to permanently delete '${sheetState.pdf.name}'? This action cannot be undone.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.onAction(SearchAction.ConfirmDeletePdf) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) { Text("Delete", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onError) }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.onAction(SearchAction.CloseSheet) }) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface
                )
            }

            SearchSheetState.None -> {}
        }
    }
}

@Composable
fun ZeroStateView(
    history: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onRemoveHistoryItem: (String) -> Unit,
    onClearAll: () -> Unit
) {
    var isHistoryExpanded by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 8.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            SmartSearchTipsCard()
        }

        if (history.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Searches", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        text = "Clear All",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onClearAll() }
                            .padding(4.dp)
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow))
                ) {
                    val visibleHistory = if (isHistoryExpanded) history else history.take(3)

                    visibleHistory.forEach { pastQuery ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onHistoryItemClick(pastQuery) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(pastQuery, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                            IconButton(onClick = { onRemoveHistoryItem(pastQuery) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    if (history.size > 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isHistoryExpanded = !isHistoryExpanded }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isHistoryExpanded) "Show Less" else "Show All",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isHistoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartSearchTipsCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Deep Search Enabled", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Typo Tolerance: Automatically adapts to minor spelling variations.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text("• Contextual Scan: Finds files instantly using partial or scattered keywords.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SearchItemRow(
    pdf: PdfFile,
    query: String,
    onClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            PdfThumbnail(pdf = pdf, modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.width(16.dp))
        HighlightedText(
            text = pdf.name,
            query = query,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onMoreClick, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun EmptyStateView(query: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_search))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                renderMode = RenderMode.HARDWARE,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No results found for", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("\"$query\"", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Check for typos or try a different word.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

// 🌟 THE 2026 NAVIGATION ENGINE: Search Section
fun NavGraphBuilder.searchSection(
    navController: NavHostController
) {
    composable<Screen.Search> {
        SearchScreen(
            onBackClick = { navController.popBackStack() },
            onPdfClick = { path ->
                navController.navigate(Screen.PdfViewer(pdfPath = path))
            }
        )
    }
}
