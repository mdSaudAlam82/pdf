package com.edu.pdf.presentation.pdfviewer.ai

// Deprecated warning fixed
// Naya alpha import
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.domain.model.ChatMessage
import com.edu.pdf.domain.model.ChatRole

// ════════════════════════════════════════════════════════════
// 🌟 ENTRY POINT — Route Composable
// ════════════════════════════════════════════════════════════

@Composable
fun AiChatOverlayScreen(
    isVisible: Boolean,
    currentPageBitmap: android.graphics.Bitmap?,
    currentPageNumber: Int = 1,
    pdfName: String = "",
    onDismiss: () -> Unit,
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // 🌟 PDF context ViewModel ko do — Bitmap State mein NAHI jayega
    LaunchedEffect(isVisible, currentPageNumber, pdfName) {
        if (isVisible) {
            viewModel.onAction(
                AiChatAction.SetContext(
                    pageNumber = currentPageNumber,
                    pdfName = pdfName,
                    bitmapRef = currentPageBitmap
                )
            )
        }
    }

    // 🌟 Auto-scroll on new message
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            if (event is AiChatEvent.ScrollToBottom && state.messages.isNotEmpty()) {
                listState.animateScrollToItem(state.messages.lastIndex)
            }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(tween(durationMillis = 300)),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 280)
        ) + fadeOut(tween(durationMillis = 200))
    ) {
        // 🌟 IS COLUMN KO DEKHO: Iska modifier ab aisa dikhna chahiye
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // ── 1. TOP BAR (Iske andar ka code bilkul mat chhedna, ye same rahega) ──
            AiChatTopBar(
                pageNumber = state.currentPageNumber,
                pdfName = state.pdfName,
                onDismiss = {
                    keyboardController?.hide()
                    onDismiss()
                }
            )

            // ── 2. CONTENT AREA (Ye bhi bilkul same rahega) ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (state.messages.isEmpty()) {
                    AiEmptyState(
                        prompts = state.suggestedPrompts,
                        pdfName = state.pdfName,
                        onPromptClick = { prompt ->
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.onAction(AiChatAction.OnSmartPromptClick(prompt))
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AiMessageList(
                        messages = state.messages,
                        isThinking = state.isAiThinking,
                        listState = listState,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.errorMessage?.let { error ->
                    AiErrorBanner(
                        message = error,
                        onDismiss = { viewModel.onAction(AiChatAction.DismissError) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
                    )
                }
            }

            // ── 3. INPUT BAR (Ye bhi bilkul same rahega) ──
            AiInputSection(
                input = state.currentInput,
                isThinking = state.isAiThinking,
                onInputChange = {
                    viewModel.onAction(AiChatAction.UpdateInput(it))
                },
                onSendClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                    viewModel.onAction(AiChatAction.SendMessage(state.currentInput))
                },
                onStopClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onAction(AiChatAction.StopStreaming)
                },
                modifier = Modifier
            )
        }
    }
}


// ════════════════════════════════════════════════════════════
// TOP BAR
// ════════════════════════════════════════════════════════════

@Composable
private fun AiChatTopBar(
    pageNumber: Int,
    pdfName: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp), // Vertical space reduced from 6 to 2 (Slim Look!)
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close AI Chat",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                // 🌟 ELITE FIX: Yahan se AiIconPulse aur Spacer hata diya gaya hai!

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Read AI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (pdfName.isNotBlank()) {
                        val displayName = if (pdfName.length > 28) {
                            pdfName.take(28) + "…"
                        } else pdfName

                        Text(
                            text = "Page $pageNumber  ·  $displayName",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Powered by Gemini",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun AiMessageList(
    messages: List<ChatMessage>,
    isThinking: Boolean,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        // 🌟 ELITE FIX: Side padding hata di taaki full screen feel aaye
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Messages ke beech zyada saaf jagah
    ) {
        items(messages, key = { it.id }) { message ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(200)) + slideInVertically(initialOffsetY = { it / 4 }, animationSpec = tween(200))
            ) {
                AiMessageBubble(message = message)
            }
        }
        if (isThinking && messages.lastOrNull()?.isStreaming == false) {
            item(key = "typing_indicator") { TypingIndicator() }
        }
    }
}

// ════════════════════════════════════════════════════════════
// MESSAGE BUBBLE
// ════════════════════════════════════════════════════════════

@Composable
private fun AiMessageBubble(message: ChatMessage) {
    val isUser = message.role == ChatRole.USER

    // 🌟 Ek taraf se start hoga dono (Gemini Jaisa)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Sirf sides me thodi jagah
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    if (isUser) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isUser) {
                Text("U", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content Area (Bina kisi background dabbe ke)
        Column(modifier = Modifier.weight(1f)) {
            // Name Label (Jaise Gemini me hota hai)
            Text(
                text = if (isUser) "You" else "Read AI",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            if (!isUser && message.isStreaming && message.text.isEmpty()) {
                // Breathing Pulse Animation
                val infiniteTransition = rememberInfiniteTransition(label = "gemini_pulse")
                val pulseAlpha by infiniteTransition.animateFloat(0.4f, 1f, infiniteRepeatable(tween(700), RepeatMode.Reverse), "pulse_alpha")

                Row(modifier = Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) { index ->
                        val dotAlpha by infiniteTransition.animateFloat(0.2f, 1f, infiniteRepeatable(tween(400, delayMillis = index * 150), RepeatMode.Reverse), "dot_$index")
                        Box(modifier = Modifier.size(8.dp).alpha(pulseAlpha * dotAlpha).background(Brush.linearGradient(listOf(Color(0xFF81D4FA), Color(0xFFCE93D8))), CircleShape))
                    }
                }
            } else {
                // Simple, flat, beautiful text
                val displayText = when {
                    message.isStreaming && message.text.isNotEmpty() -> message.text + "▋"
                    else -> message.text
                }
                Text(
                    text = displayText,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 26.sp // Reading experience better karne ke liye
                )
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
// TYPING INDICATOR — 3 Dots
// ════════════════════════════════════════════════════════════

@Composable
private fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing_dots")
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Read AI", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(top = 4.dp)) {
                repeat(3) { dotIndex ->
                    val dotAlpha by infiniteTransition.animateFloat(0.25f, 1f, infiniteRepeatable(tween(500, delayMillis = dotIndex * 160), RepeatMode.Reverse), "dot_alpha")
                    Box(modifier = Modifier.size(7.dp).background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = dotAlpha), CircleShape))
                }
            }
        }
    }
}
// ════════════════════════════════════════════════════════════
// EMPTY STATE
// ════════════════════════════════════════════════════════════
@Composable
private fun AiEmptyState(
    prompts: List<String>,
    pdfName: String,
    onPromptClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 🌟 ELITE FIX: Magic Breathing & Floating Animation
    val infiniteTransition = rememberInfiniteTransition(label = "magic_logo_anim")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(1500, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "logo_scale"
    )

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -8f,
        animationSpec = infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "logo_float"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            // 🌟 Wapas Classic Padding (Thodi space keyboard ke liye)
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        // 🌟 Wapas Center Alignment
        verticalArrangement = Arrangement.Center
    ) {

        // 🌟 Purana Classic Logo (Bina Border) + Naya Breathing Animation
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationY = floatOffset
                }
                .size(84.dp)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Read AI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (pdfName.isNotBlank())
                "Ask me anything about\n\"${pdfName.take(30)}${if (pdfName.length > 30) "…" else ""}\""
            else
                "Ask me anything about this document",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Section header
        Text(
            text = "Suggested Questions",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        // 🌟 Suggestion chips
        prompts.take(4).forEach { prompt ->
            AiSuggestionChip(
                prompt = prompt,
                onClick = { onPromptClick(prompt) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun AiSuggestionChip(
    prompt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = prompt,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ════════════════════════════════════════════════════════════
// INPUT BAR
// ════════════════════════════════════════════════════════════

@Composable
private fun AiInputSection(
    input: String,
    isThinking: Boolean,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {

            // 🌟 Text Field — pill shape, no visible border
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                placeholder = {
                    Text(
                        text = "Message Read AI…",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(22.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.55f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.45f)
                ),
                shape = RoundedCornerShape(22.dp),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🌟 Send ↔ Stop toggle with animation
            AnimatedContent(
                targetState = isThinking,
                label = "send_stop_toggle",
                transitionSpec = {
                    (scaleIn(tween(180)) + fadeIn(tween(180))) togetherWith
                            (scaleOut(tween(120)) + fadeOut(tween(120)))
                }
            ) { thinking ->
                Box(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            color = when {
                                thinking -> MaterialTheme.colorScheme.error
                                input.isNotBlank() -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            }
                        )
                        .clickable(enabled = thinking || input.isNotBlank()) {
                            if (thinking) onStopClick() else onSendClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (thinking) Icons.Default.Stop
                        else Icons.AutoMirrored.Filled.Send,
                        contentDescription = if (thinking) "Stop" else "Send",
                        tint = if (input.isNotBlank() || thinking) Color.White
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
// ERROR BANNER
// ════════════════════════════════════════════════════════════

@Composable
private fun AiErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss error",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
