package com.edu.pdf.domain.model

import androidx.compose.runtime.Immutable

enum class ChatRole { USER, MODEL }

@Immutable
data class ChatMessage(
    val id: String,
    val role: ChatRole,
    val text: String,
    val isStreaming: Boolean = false
)