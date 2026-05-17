package com.edu.pdf.presentation.ocr

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun LiveTextOverlay(
    state: OcrUiState,
    onStopLiveText: () -> Unit
) {
    if (!state.isLiveTextActive) return

    BackHandler(enabled = true) {
        onStopLiveText()
    }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(100f)
    ) {
        state.capturedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Frozen Screen",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(PointerEventPass.Main).changes.forEach { it.consume() }
                        }
                    }
                }
        )

        SelectionContainer(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {

                // 🌟 RAW ML KIT DEFAULT RENDERING 🌟
                // 🌟 RAW ML KIT DEFAULT RENDERING 🌟
                state.extractedBlocks.forEach { block ->
                    val rect = block.boundingBox ?: return@forEach

                    val leftDp = with(density) { rect.left.toDp() }
                    val topDp = with(density) { rect.top.toDp() }

                    // 🌟 FIX 1: Width calculation add ki
                    val widthDp = with(density) { rect.width().toDp() }
                    val heightDp = with(density) { rect.height().toDp() }

                    // Basic Font Size Calculation
                    val lineCount = block.lineCount.coerceAtLeast(1)
                    val exactFontSize = with(density) { (heightDp / lineCount).toSp() }

                    BasicText(
                        // ML Kit ka default format (\n ke sath)
                        text = block.text,
                        modifier = Modifier
                            .offset(x = leftDp, y = topDp)
                            // 🌟 FIX 2: Strict size lagaya taaki left column right column par OVERLAP na kare!
                            // (Ye false selection ko hamesha ke liye rok dega)
                            .size(width = widthDp, height = heightDp),
                        style = TextStyle(
                            color = Color.Transparent,
                            fontSize = exactFontSize,
                            lineHeight = exactFontSize,
                            // 🌟 FIX 3: Android ke faltu margins hataye taaki text exact dabbe me fit baithe
                            platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)
                        ),
                        // 🌟 FIX 4: Text ko forced agli line me jane se roko. Jo ML Kit ne bola hai wahi rahega!
                        softWrap = false
                    )
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        IconButton(
            onClick = onStopLiveText,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Live Text",
                tint = Color.White
            )
        }
    }
}