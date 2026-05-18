package com.edu.pdf.presentation.pdfviewer.ai // 🌟 Package ab ai folder me hai

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.edu.pdf.domain.model.ChatMessage
import com.edu.pdf.domain.model.ChatRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatOverlay(
    state: AiChatState,
    onAction: (AiAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // 🌟 FIX: Ab hum 'state.isOpen' check kar rahe hain
    BackHandler(enabled = state.isOpen) {
        focusManager.clearFocus()
        onAction(AiAction.ToggleChat(false)) // 🌟 FIX: Naya Action
    }

    LaunchedEffect(state.chatMessages.size) {
        if (state.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(state.chatMessages.size - 1)
        }
    }

    if (state.isOpen) { // 🌟 FIX
        LaunchedEffect(Unit) {
            delay(50)
            sheetState.expand()
        }

        ModalBottomSheet(
            onDismissRequest = {
                focusManager.clearFocus()
                onAction(AiAction.ToggleChat(false)) // 🌟 FIX
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .imePadding()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    ChatListSection(
                        messages = state.chatMessages,
                        isTyping = state.isTyping, // 🌟 FIX: Naya variable naam
                        errorMsg = state.error,    // 🌟 FIX: Naya variable naam
                        listState = listState
                    )
                }

                ChatInputBar(
                    inputText = inputText,
                    onInputChanged = { inputText = it },
                    isTyping = state.isTyping,
                    onSearchBarFocused = {
                        coroutineScope.launch {
                            sheetState.expand()
                        }
                    },
                    onSendClicked = {
                        if (inputText.isNotBlank() && !state.isTyping) {
                            onAction(AiAction.SendMessage(inputText)) // 🌟 FIX: Naya Action
                            inputText = ""
                            focusManager.clearFocus()
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatListSection(
    messages: List<ChatMessage>,
    isTyping: Boolean,
    errorMsg: String?,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            val isUser = message.role == ChatRole.USER
            val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
            val bgColor = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = alignment
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (isTyping) {
            item {
                Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI is typing...", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                }
            }
        }

        errorMsg?.let { error ->
            item {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    inputText: String,
    onInputChanged: (String) -> Unit,
    isTyping: Boolean,
    onSearchBarFocused: () -> Unit,
    onSendClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = inputText,
            onValueChange = onInputChanged,
            placeholder = { Text("Ask something...") },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onSearchBarFocused()
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 4
        )

        AnimatedVisibility(
            visible = inputText.isNotBlank() && !isTyping,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            IconButton(
                onClick = onSendClicked,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}