package com.edu.pdf.presentation.pdfviewer

import android.view.ViewConfiguration
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.compose.AndroidFragment
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel // 🌟 WARNING FIXED
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.pdf.viewer.fragment.PdfViewerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import androidx.compose.material.icons.filled.CenterFocusWeak
import com.edu.pdf.presentation.pdfviewer.ocr.OcrAction
import com.edu.pdf.presentation.pdfviewer.ocr.OcrSelectionOverlay
import androidx.core.graphics.createBitmap

@Composable
fun PdfViewerScreen(
    onBack: () -> Unit,
    viewModel: PdfViewerViewModel = hiltViewModel(),
            ocrViewModel: com.edu.pdf.presentation.pdfviewer.ocr.OcrViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? AppCompatActivity ?: return

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ocrState by ocrViewModel.state.collectAsStateWithLifecycle()

    val pdfUri = uiState.pdfUri

    val window = activity.window
    val insetsController = remember(window) { WindowCompat.getInsetsController(window, window.decorView) }
    val touchSlop = remember { ViewConfiguration.get(context).scaledTouchSlop }
    val scope = rememberCoroutineScope()
    var tapJob by remember { mutableStateOf<Job?>(null) }
    val doubleTapTimeout = remember { ViewConfiguration.getDoubleTapTimeout().toLong() }

    LaunchedEffect(uiState.isTopBarVisible) {
        if (uiState.isTopBarVisible) {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
        } else {
            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    // 🌟 CLEAN BACK HANDLER
    // 🌟 SMART BACK HANDLER: OCR -> Search -> Exit Screen
    BackHandler {
        if (ocrState.isLiveTextActive) {
            // 1. अगर OCR (नीले बक्से) चालू हैं, तो बैक दबाने पर पहले उन्हें बंद करो
            ocrViewModel.onAction(OcrAction.StopLiveText)
        } else if (uiState.isSearchActive) {
            // 2. अगर सर्च चालू है, तो उसे बंद करो
            viewModel.onAction(PdfViewerAction.ToggleSearch)
        } else {
            // 3. अगर सब कुछ बंद है, तो ही PDF Viewer से बाहर निकलो
            insetsController.show(WindowInsetsCompat.Type.statusBars())
            onBack()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
        }
    }

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
                            text = "Pro PDF Viewer",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { viewModel.onAction(PdfViewerAction.ToggleNightMode) }) {
                            Icon(
                                imageVector = if (uiState.isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Night Mode",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        // 🌟 NAYA: OCR Scanner Button (बिना DocumentScanner वाले आइकॉन के)
                        IconButton(
                            onClick = {
                                // बैकग्राउंड में फोटो लेंगे ताकि स्क्रीन फ्रीज़ ना हो!
                                captureScreenNonBlocking(activity) { bitmap ->
                                    ocrViewModel.onAction(OcrAction.StartLiveText(bitmap))
                                }
                            }
                        ) {
                            Icon(Icons.Default.CenterFocusWeak, contentDescription = "Scan Text", tint = MaterialTheme.colorScheme.onSurface)
                        }

                        IconButton(onClick = { viewModel.onAction(PdfViewerAction.ToggleSearch) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .then(
                            if (uiState.isSearchActive) Modifier
                            else Modifier.pointerInput(Unit) {
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
                ) {
                    if (pdfUri != null) {
                        AndroidFragment<PdfViewerFragment>(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { if (uiState.isNightMode) colorFilter = ColorFilter.colorMatrix(darkColorMatrix) },
                            onUpdate = { fragment ->
                                if (fragment.documentUri != pdfUri) {
                                    fragment.documentUri = pdfUri
                                }
                                try {
                                    if (fragment.lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.STARTED)) {
                                        fragment.isTextSearchActive = uiState.isSearchActive
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Failed to load PDF", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } // Column End
            // 🌟 LAYER 2: OCR OVERLAY (यह PDF के ऊपर सैंडविच की तरह आ जाएगा)
            OcrSelectionOverlay(
                state = ocrState,
                onAction = { action -> ocrViewModel.onAction(action) }
            )
        } // Box End
    }
}
private fun captureScreenNonBlocking(activity: Activity, onCaptured: (Bitmap) -> Unit) {
    val window = activity.window
    val view = window.decorView

    // मेमोरी में हाई-क्वालिटी फोटो की जगह बना रहे हैं
    val bitmap = createBitmap(view.width, view.height)

    // यह हार्डवेयर-एक्सेलेरेटेड (Hardware-Accelerated) है, इसलिए स्क्रीन नहीं अटकेगी
    PixelCopy.request(window, bitmap, { copyResult ->
        if (copyResult == PixelCopy.SUCCESS) {
            onCaptured(bitmap) // फोटो लेकर OCR को दे दी
        } else {
            bitmap.recycle() // अगर कोई एरर आया, तो मेमोरी से कचरा साफ़ कर दो (No memory leak!)
        }
    }, Handler(Looper.getMainLooper()))
}