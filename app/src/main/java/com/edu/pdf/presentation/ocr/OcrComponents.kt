package com.edu.pdf.presentation.ocr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * 🌟 1. The Premium Laser Scanning Animation (2026 Standard)
 */
@Composable
fun OcrScanningOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")

    // 🌟 ऊपर से नीचे जाने वाला एनीमेशन (0f से 1f तक)
    val scanPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_line"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // डार्क ग्लास इफ़ेक्ट
    ) {
        // 🌟 चमकती हुई लेज़र लाइन
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.TopCenter)
                .offset { IntOffset(x = 0, y = (scanPosition * 800).dp.roundToPx()) } // 🌟 Lambda Offset (Zero recomposition) // स्क्रीन की हाइट के हिसाब से नीचे जाएगा
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent
                        )
                    )
                )
        )

        // 🌟 बीच में लोडिंग टेक्स्ट
        Text(
            text = "Extracting Text...",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * 🌟 2. The Full-Page OCR Result Sheet
 */
/**
 * 🌟 2. The Full-Page OCR Result Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartOcrPeekSheet(
    state: OcrUiState,
    onAction: (OcrAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 🌟 FIX 1: Warning ख़त्म! अब हम Android का 'Native Clipboard' यूज़ करेंगे (यह कभी Deprecate नहीं होता)
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    val heightFraction = remember { Animatable(0f) }
    var hasVibrated by remember { mutableStateOf(false) }

    BackHandler(enabled = state.isSheetOpen) {
        onAction(OcrAction.CloseSheet)
    }

    if (!state.isSheetOpen && heightFraction.value == 0f) return

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val totalHeightDp = maxHeight
        val safeStatusPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 2.dp
        val maxFraction = 1f - (safeStatusPadding / totalHeightDp)

        val cornerRadius = if (heightFraction.value >= maxFraction - 0.02f) 0.dp else 24.dp

        LaunchedEffect(state.isSheetOpen, state.isLoading) {
            if (state.isSheetOpen && !state.isLoading) {
                heightFraction.animateTo(
                    targetValue = maxFraction,
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            } else if (!state.isSheetOpen) {
                heightFraction.animateTo(0f, animationSpec = tween(250, easing = FastOutSlowInEasing))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(heightFraction.value)
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.98f))
                .pointerInput(maxFraction) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (heightFraction.value < 0.25f) {
                                    onAction(OcrAction.CloseSheet)
                                } else if (heightFraction.value > 0.7f) {
                                    heightFraction.animateTo(maxFraction, tween(250, easing = FastOutSlowInEasing))
                                } else {
                                    heightFraction.animateTo(0.45f, tween(250, easing = FastOutSlowInEasing))
                                }
                                hasVibrated = false
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            val dragFraction = dragAmount / constraints.maxHeight.toFloat()
                            val newFraction = (heightFraction.value - dragFraction).coerceIn(0f, maxFraction)
                            heightFraction.snapTo(newFraction)

                            if (newFraction >= (maxFraction - 0.01f) && !hasVibrated) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                hasVibrated = true
                            } else if (newFraction < maxFraction - 0.05f) {
                                hasVibrated = false
                            }
                        }
                    }
                }
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.width(36.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)))
            }

            if (state.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${state.errorMessage}", color = MaterialTheme.colorScheme.error)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
                    AnimatedVisibility(visible = heightFraction.value > 0.8f, enter = fadeIn(), exit = fadeOut()) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Scanned Text",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                FilledTonalIconButton(
                                    onClick = {
                                        // 🌟 Native Clipboard का इस्तेमाल
                                        val clip = ClipData.newPlainText("Scanned Text", state.extractedText)
                                        clipboardManager.setPrimaryClip(clip)
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy All", modifier = Modifier.size(18.dp))
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    SelectionContainer {
                        Text(
                            text = state.extractedText.ifBlank { "No text found on this page." },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 30.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                            modifier = Modifier
                                .padding(bottom = 80.dp)
                                // 🌟 FIX 2: Haptic Tracker (अब कोई Warning नहीं)
                                .pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        while (true) {
                                            // 'val down =' हटा दिया गया है, इसलिए कोई "Unused variable" वार्निंग नहीं आएगी
                                            awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)

                                            val upEvent = withTimeoutOrNull(400) {
                                                waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                            }

                                            // अगर 400ms तक उंगली नहीं उठी, मतलब Long Press (Selection) हुआ है!
                                            if (upEvent == null) {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                // लगातार वाइब्रेट होने से रोकने के लिए उंगली उठने का इंतज़ार करो
                                                do {
                                                    val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                                                } while (event.changes.any { it.pressed })
                                            }
                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}