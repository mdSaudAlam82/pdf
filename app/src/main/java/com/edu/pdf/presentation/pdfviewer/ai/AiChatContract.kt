package com.edu.pdf.presentation.pdfviewer.ai

import androidx.compose.runtime.Immutable
import com.edu.pdf.domain.model.ChatMessage

// 🌟 WORLD STANDARD: Bitmap KABHI State mein nahi rahega
// Sirf Page Number track karo, Bitmap ViewModel ke andar WeakReference se handle hoga

@Immutable
data class AiChatState(
    val messages: List<ChatMessage> = emptyList(),
    val currentInput: String = "",
    val isAiThinking: Boolean = false,
    val isVisible: Boolean = false,
    val currentPageNumber: Int = 1,    // 🌟 Page number, Bitmap nahi
    val pdfName: String = "",           // 🌟 Context ke liye
    val suggestedPrompts: List<String> = listOf(
        "📝 Summarize this page",
        "🔍 Explain key concepts",
        "❓ What are the main points?",
        "🌐 Translate to Hindi"
    ),
    val errorMessage: String? = null
)

sealed interface AiChatAction {
    data class UpdateInput(val text: String) : AiChatAction
    data class SendMessage(val query: String) : AiChatAction
    data class OnSmartPromptClick(val prompt: String) : AiChatAction
    data class SetContext(
        val pageNumber: Int,
        val pdfName: String,
        val bitmapRef: android.graphics.Bitmap? // 🌟 Direct pass, State mein store nahi
    ) : AiChatAction
    data class SetVisibility(val visible: Boolean) : AiChatAction
    data object StopStreaming : AiChatAction
    data object ClearChat : AiChatAction
    data object DismissError : AiChatAction
}

sealed interface AiChatEvent {
    data class ShowError(val message: String) : AiChatEvent
    data object ScrollToBottom : AiChatEvent
}
