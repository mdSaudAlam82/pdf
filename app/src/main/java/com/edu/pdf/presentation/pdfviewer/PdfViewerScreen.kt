package com.edu.pdf.presentation.pdfviewer

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.ViewConfiguration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.pdf.viewer.fragment.PdfViewerFragment
import com.edu.pdf.presentation.common.PdfActionBottomSheet
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.pdfviewer.ai.AiChatOverlayScreen
import com.edu.pdf.presentation.pdfviewer.ocr.OcrAction
import com.edu.pdf.presentation.pdfviewer.ocr.OcrSelectionOverlay
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PdfViewerScreen(
    onBack: () -> Unit,
    pdfPath: String? = null, // 🌟 NAYA: Support for Pane/Detail usage
    viewModel: PdfViewerViewModel = hiltViewModel(),
    ocrViewModel: com.edu.pdf.presentation.pdfviewer.ocr.OcrViewModel = hiltViewModel()
) {
    LaunchedEffect(pdfPath) {
        pdfPath?.let {
            viewModel.onAction(PdfViewerAction.Initialize(it))
        }
    }
    val context = LocalContext.current
    val activity = context as? AppCompatActivity ?: return

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ocrState by ocrViewModel.state.collectAsStateWithLifecycle()

    var showAiChat by remember { mutableStateOf(false) }
    var currentCapturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val pdfUri = uiState.pdfUri
    val window = activity.window
    val insetsController = remember(window) { WindowCompat.getInsetsController(window, window.decorView) }
    val touchSlop = remember { ViewConfiguration.get(context).scaledTouchSlop }
    val scope = rememberCoroutineScope()
    var tapJob by remember { mutableStateOf<Job?>(null) }
    val doubleTapTimeout = remember { ViewConfiguration.getDoubleTapTimeout().toLong() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PdfViewerEvent.NavigateBack -> {
                    insetsController.show(WindowInsetsCompat.Type.statusBars())
                    onBack()
                }
                is PdfViewerEvent.ShowToast -> {
                    android.widget.Toast.makeText(context, event.message, android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(uiState.isTopBarVisible) {
        if (uiState.isTopBarVisible) {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
        } else {
            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    BackHandler {
        if (showAiChat) {
            showAiChat = false
            currentCapturedBitmap = null
        } else if (ocrState.isLiveTextActive) {
            ocrViewModel.onAction(OcrAction.StopLiveText)
        } else {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
            onBack()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
        }
    }
    val containerId = remember { android.view.View.generateViewId() }

    val darkColorMatrix = remember { ColorMatrix(floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    )) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AnimatedVisibility(
                    visible = uiState.isTopBarVisible,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .statusBarsPadding()
                            .height(56.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { insetsController.show(WindowInsetsCompat.Type.statusBars()); onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            text = uiState.pdfFileName.ifEmpty { "Pro PDF Viewer" },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        IconButton(onClick = { viewModel.onAction(PdfViewerAction.ToggleNightMode) }) {
                            Icon(
                                imageVector = if (uiState.isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Night Mode",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = {
                                captureScreenNonBlocking(activity) { bitmap ->
                                    ocrViewModel.onAction(OcrAction.StartLiveText(bitmap))
                                }
                            }
                        ) {
                            Icon(Icons.Default.CenterFocusWeak, contentDescription = "Scan Text", tint = MaterialTheme.colorScheme.onSurface)
                        }

                        IconButton(
                            onClick = {
                                captureScreenNonBlocking(activity) { bitmap ->
                                    currentCapturedBitmap = bitmap
                                    showAiChat = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Ask AI",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(onClick = {
                            val fragment = activity.supportFragmentManager.findFragmentById(containerId) as? PdfViewerFragment
                            fragment?.isTextSearchActive = true
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        IconButton(onClick = { viewModel.onAction(PdfViewerAction.OpenSheet(PdfViewerSheetState.MoreActions)) }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                    while (true) {
                                        val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                                        val downTime = System.currentTimeMillis()
                                        var isTap = true
                                        do {
                                            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                                            if (event.changes.size > 1) {
                                                isTap = false
                                                if (uiState.isTopBarVisible) viewModel.onAction(PdfViewerAction.SetTopBarVisible(false))
                                            }
                                            val pos = event.changes.first().position
                                            if ((pos - down.position).getDistance() > touchSlop) {
                                                isTap = false
                                                if (uiState.isTopBarVisible) viewModel.onAction(PdfViewerAction.SetTopBarVisible(false))
                                            }
                                        } while (event.changes.any { it.pressed })
                                        val upTime = System.currentTimeMillis()
                                        if (isTap && (upTime - downTime) < 200) {
                                            if (tapJob?.isActive == true) {
                                                tapJob?.cancel()
                                            } else {
                                                tapJob = scope.launch {
                                                    delay(doubleTapTimeout)
                                                    viewModel.onAction(PdfViewerAction.ToggleTopBar)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                 {
                    if (pdfUri != null) {
                        androidx.compose.ui.viewinterop.AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { _ ->
                                val themedContext = androidx.appcompat.view.ContextThemeWrapper(
                                    activity,
                                    android.R.style.Theme_DeviceDefault_NoActionBar
                                )

                                val wrapper = android.widget.FrameLayout(themedContext)

                                val container = androidx.fragment.app.FragmentContainerView(themedContext).apply {
                                    id = containerId
                                    layoutParams = android.widget.FrameLayout.LayoutParams(
                                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                }
                                wrapper.addView(container)

                                val fragmentManager = activity.supportFragmentManager
                                var pdfFragment = fragmentManager.findFragmentById(containerId) as? PdfViewerFragment
                                if (pdfFragment == null) {
                                    pdfFragment = PdfViewerFragment()
                                    // 🌟 TABLET MASTERY: Stylus support hint
                                    fragmentManager.beginTransaction()
                                        .replace(containerId, pdfFragment)
                                        .commitAllowingStateLoss()
                                }

                                wrapper.post { pdfFragment.documentUri = pdfUri }
                                wrapper
                            },
                            update = { view ->
                                val fragmentManager = activity.supportFragmentManager
                                val fragment = fragmentManager.findFragmentById(containerId) as? PdfViewerFragment

                                if (fragment != null) {
                                    if (fragment.documentUri != pdfUri) {
                                        fragment.documentUri = pdfUri
                                    }
                                }

                                if (uiState.isNightMode) {
                                    val paint = android.graphics.Paint().apply {
                                        colorFilter = android.graphics.ColorMatrixColorFilter(darkColorMatrix.values)
                                    }
                                    view.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, paint)
                                } else {
                                    view.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                                }
                            },
                            onRelease = { _ ->
                                val fragmentManager = activity.supportFragmentManager
                                val fragment = fragmentManager.findFragmentById(containerId)
                                if (fragment != null) {
                                    fragmentManager.beginTransaction()
                                        .remove(fragment)
                                        .commitAllowingStateLoss()
                                }
                            }
                        )
                    }
                }
            }

            OcrSelectionOverlay(
                state = ocrState,
                onAction = { action -> ocrViewModel.onAction(action) }
            )

            AiChatOverlayScreen(
                isVisible = showAiChat,
                currentPageBitmap = currentCapturedBitmap,
                currentPageNumber = uiState.currentPageNumber,
                pdfName = uiState.pdfFileName,
                onDismiss = {
                    showAiChat = false
                    currentCapturedBitmap = null
                }
            )

            // 🌟 PURE MVI: UI is strictly driven by ViewModel's activeSheetState
            when (val sheetState = uiState.activeSheetState) {
                is PdfViewerSheetState.MoreActions -> {
                    if (uiState.pdfFile != null) {
                        PdfActionBottomSheet(
                            pdf = uiState.pdfFile!!,
                            onDismiss = { viewModel.onAction(PdfViewerAction.CloseSheet) },
                            onShare = {
                                viewModel.onAction(PdfViewerAction.CloseSheet)
                                sharePdf(context, uiState.pdfUri)
                            },
                            onFavoriteToggle = {
                                viewModel.onAction(PdfViewerAction.ToggleFavorite(!uiState.pdfFile!!.isFavorite))
                            },
                            onDelete = {
                                viewModel.onAction(PdfViewerAction.OpenSheet(PdfViewerSheetState.DeleteConfirmation))
                            },
                            onActionClick = { action ->
                                when (action) {
                                    "Rename" -> viewModel.onAction(PdfViewerAction.OpenSheet(PdfViewerSheetState.RenameDialog(uiState.pdfFileName)))
                                    "Move to" -> viewModel.onAction(PdfViewerAction.OpenSheet(PdfViewerSheetState.MovePicker))
                                    "Move to Vault", "Remove from Vault" -> viewModel.onAction(PdfViewerAction.ToggleVaultStatus)
                                    "Print" -> viewModel.onAction(PdfViewerAction.PrintFile(context))
                                    else -> Toast.makeText(context, "$action coming soon!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }

                is PdfViewerSheetState.MovePicker -> {
                    com.edu.pdf.presentation.common.picker.MovePickerSheetRoute(
                        folders = emptyList(),
                        onDismiss = { viewModel.onAction(PdfViewerAction.CloseSheet) },
                        onTargetSelected = { targetId ->
                            viewModel.onAction(PdfViewerAction.MoveToFolder(targetId))
                        }
                    )
                }

                is PdfViewerSheetState.RenameDialog -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.onAction(PdfViewerAction.CloseSheet) },
                        title = { Text("Rename PDF") },
                        text = {
                            TextField(
                                value = uiState.renameInput,
                                onValueChange = { viewModel.onAction(PdfViewerAction.UpdateRenameInput(it)) },
                                placeholder = { Text("Enter new name") }
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { viewModel.onAction(PdfViewerAction.ConfirmRename) }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.onAction(PdfViewerAction.CloseSheet) }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                is PdfViewerSheetState.DeleteConfirmation -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.onAction(PdfViewerAction.CloseSheet) },
                        title = { Text("Delete File?") },
                        text = { Text("Are you sure you want to permanently delete this PDF?") },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.onAction(PdfViewerAction.ConfirmDelete) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) { Text("Delete", color = MaterialTheme.colorScheme.onError) }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.onAction(PdfViewerAction.CloseSheet) }) { Text("Cancel") }
                        }
                    )
                }

                PdfViewerSheetState.None -> {}
            }
        }
    }
}

// 🌟 Helper for Share
private fun sharePdf(context: android.content.Context, uri: android.net.Uri?) {
    try {
        uri?.let { u ->
            if (u.scheme == "file") {
                val builder = android.os.StrictMode.VmPolicy.Builder()
                android.os.StrictMode.setVmPolicy(builder.build())
            }
            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(android.content.Intent.EXTRA_STREAM, u)
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(android.content.Intent.createChooser(shareIntent, "Share PDF"))
        }
    } catch (_: Exception) {
        android.widget.Toast.makeText(context, "Error: Unable to share file", android.widget.Toast.LENGTH_SHORT).show()
    }
}

// 🌟 THE 2026 NAVIGATION ENGINE: PdfViewer Section
fun NavGraphBuilder.pdfViewerSection(
    navController: NavHostController,
    isExternalLaunch: () -> Boolean,
    onExternalClosed: () -> Unit
) {
    composable<Screen.PdfViewer> { backStackEntry ->
        val route: Screen.PdfViewer = backStackEntry.toRoute()
        PdfViewerScreen(
            onBack = {
                if (isExternalLaunch()) {
                    onExternalClosed()
                    navController.popBackStack()
                } else {
                    navController.popBackStack()
                }
            }
        )
    }
}

private fun captureScreenNonBlocking(activity: Activity, onCaptured: (Bitmap) -> Unit) {
    val window = activity.window
    val view = window.decorView
    val bitmap = createBitmap(view.width, view.height)
    PixelCopy.request(window, bitmap, { copyResult ->
        if (copyResult == PixelCopy.SUCCESS) {
            onCaptured(bitmap)
        } else {
            bitmap.recycle()
        }
    }, Handler(Looper.getMainLooper()))
}
