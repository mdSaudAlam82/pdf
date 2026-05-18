package com.edu.pdf.presentation.pdfviewer.ai

import com.edu.pdf.domain.model.ChatMessage

// 1. STATE: Jo UI ko dikhega
data class AiChatState(
    val isOpen: Boolean = false,
    val isTyping: Boolean = false,
    val chatMessages: List<ChatMessage> = emptyList(),
    val error: String? = null
)

// 2. INTENT/ACTION: Jo UI user ke click karne par bhejega
sealed interface AiAction {
    data class ToggleChat(val open: Boolean) : AiAction
    data class SendMessage(val message: String, val pdfTextContext: String? = null) : AiAction
}