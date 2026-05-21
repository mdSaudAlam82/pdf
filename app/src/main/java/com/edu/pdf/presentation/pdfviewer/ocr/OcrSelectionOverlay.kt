package com.edu.pdf.presentation.pdfviewer.ocr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun OcrSelectionOverlay(
    state: OcrState,
    onAction: (OcrAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!state.isLiveTextActive) return

    val context = LocalContext.current

    // UI Gesture States (Kept in Compose to prevent ViewModel bottlenecking at 60fps)
    var selectedIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragCurrent by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(if (state.isProcessing) Modifier.blur(8.dp) else Modifier)
            .background(if (state.isProcessing) Color(0x33000000) else Color.Transparent)
    ) {
        // 1. Sleek Red Progress Bar
        AnimatedVisibility(
            visible = state.isProcessing,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(500)),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                color = Color.Red,
                trackColor = Color.Transparent
            )
        }

        // 2. Custom Canvas Selection Engine
        if (!state.isProcessing && state.extractedTextBlocks.isNotEmpty()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { tapOffset ->
                                val tappedIndex = state.extractedTextBlocks.indexOfFirst { block ->
                                    block.boundingBox?.let { rect ->
                                        tapOffset.x >= rect.left && tapOffset.x <= rect.right &&
                                                tapOffset.y >= rect.top && tapOffset.y <= rect.bottom
                                    } ?: false
                                }

                                // 🌟 FIX: Assignment Lifted out of If (Clean Kotlin Style)
                                selectedIndices = if (tappedIndex != -1) {
                                    if (selectedIndices.contains(tappedIndex)) {
                                        selectedIndices - tappedIndex
                                    } else {
                                        selectedIndices + tappedIndex
                                    }
                                } else {
                                    emptySet()
                                }
                            }
                        )
                    }
                    // 🌟 ELITE FIX: Using Smart Stylus Modifier for Palm Rejection
                    .smartStylusSelection(
                        onSelectionStart = { offset ->
                            dragStart = offset
                            dragCurrent = offset
                            selectedIndices = emptySet()
                        },
                        onSelectionDrag = { currentPosition ->
                            dragCurrent = currentPosition
                            val start = dragStart ?: return@smartStylusSelection
                            val current = dragCurrent ?: return@smartStylusSelection

                            val dragRect = Rect(
                                left = min(start.x, current.x),
                                top = min(start.y, current.y),
                                right = max(start.x, current.x),
                                bottom = max(start.y, current.y)
                            )

                            val newSelected = mutableSetOf<Int>()
                            state.extractedTextBlocks.forEachIndexed { index, block ->
                                block.boundingBox?.let { rect ->
                                    val blockRect = Rect(
                                        left = rect.left.toFloat(),
                                        top = rect.top.toFloat(),
                                        right = rect.right.toFloat(),
                                        bottom = rect.bottom.toFloat()
                                    )
                                    if (dragRect.overlaps(blockRect)) {
                                        newSelected.add(index)
                                    }
                                }
                            }
                            selectedIndices = newSelected
                        },
                        onSelectionEnd = {
                            dragStart = null
                            dragCurrent = null
                        }
                    )
            ) {
                // Drawing Text Boxes
                state.extractedTextBlocks.forEachIndexed { index, block ->
                    block.boundingBox?.let { rect ->
                        val isSelected = selectedIndices.contains(index)
                        val boxColor = if (isSelected) Color(0x66007AFF) else Color(0x22007AFF)

                        drawRect(
                            color = boxColor,
                            topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
                            size = Size(rect.width().toFloat(), rect.height().toFloat())
                        )
                    }
                }

                // Drawing Drag Selection Square
                dragStart?.let { start ->
                    dragCurrent?.let { current ->
                        val left = min(start.x, current.x)
                        val top = min(start.y, current.y)
                        val width = abs(current.x - start.x)
                        val height = abs(current.y - start.y)

                        drawRect(
                            color = Color(0x44FFFFFF),
                            topLeft = Offset(left, top),
                            size = Size(width, height),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }
        }

        // 3. Floating Action Menu (Copy & Gemini)
        AnimatedVisibility(
            visible = selectedIndices.isNotEmpty(),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xE61C1C1E), shape = MaterialTheme.shapes.extraLarge)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // COPY BUTTON
                TextButton(
                    onClick = {
                        val textToCopy = state.extractedTextBlocks
                            .filterIndexed { index, _ -> selectedIndices.contains(index) }
                            .joinToString(separator = "\n") { it.text }

                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Copied Text", textToCopy)
                        clipboard.setPrimaryClip(clip)

                        Toast.makeText(context, "Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                        selectedIndices = emptySet()
                    }
                ) {
                    Text("Copy", color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(width = 1.dp, height = 24.dp).background(Color.Gray))
                Spacer(modifier = Modifier.width(4.dp))

                // ✨ ASK GEMINI BUTTON
                TextButton(
                    onClick = {
                        val textToAsk = state.extractedTextBlocks
                            .filterIndexed { index, _ -> selectedIndices.contains(index) }
                            .joinToString(separator = "\n") { it.text }

                        // 🌟 FIX: Unused Variable Warning (अब हम textToAsk का इस्तेमाल टोस्ट में कर रहे हैं)
                        Toast.makeText(context, "Asking Gemini: ${textToAsk.take(15)}...", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("✨ Ask Gemini", color = Color(0xFF34C759), fontWeight = FontWeight.Bold)
                }
            }
        }

        // 4. Close Button
        AnimatedVisibility(
            visible = !state.isProcessing && state.extractedTextBlocks.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = { onAction(OcrAction.StopLiveText) },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.8f), shape = MaterialTheme.shapes.small)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close Scanner", tint = Color.Black)
            }
        }
    }
}
fun Modifier.smartStylusSelection(
    onSelectionStart: (Offset) -> Unit,
    onSelectionDrag: (Offset) -> Unit,
    onSelectionEnd: () -> Unit
): Modifier = this.pointerInput(Unit) {
    awaitEachGesture {
        val downEvent = awaitFirstDown(requireUnconsumed = false)

        // 🌟 PALM REJECTION: Sirf Pen (Stylus) ya Finger Touch (Single Touch) allow hoga
        if (downEvent.type == PointerType.Stylus || downEvent.type == PointerType.Touch) {
            onSelectionStart(downEvent.position)

            var isDragging = true
            while (isDragging) {
                val event = awaitPointerEvent()
                val dragChange = event.changes.firstOrNull { it.id == downEvent.id && it.pressed }

                if (dragChange != null) {
                    onSelectionDrag(dragChange.position)
                    dragChange.consume() // Consume zaroori hai taaki peeche ka PDF scroll na ho
                } else {
                    isDragging = false
                    onSelectionEnd()
                }
            }
        }
    }
}