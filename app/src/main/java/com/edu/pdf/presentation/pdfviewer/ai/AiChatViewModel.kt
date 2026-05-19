package com.edu.pdf.presentation.pdfviewer.ai

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.ChatMessage
import com.edu.pdf.domain.model.ChatRole
import com.edu.pdf.domain.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AiChatState())
    val state = _state.asStateFlow()

    private val _events = Channel<AiChatEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var streamingJob: Job? = null

    // 🌟 WORLD STANDARD: WeakReference — Bitmap GC ke liye available rahega
    // State mein nahi, isliye NO memory leak
    private var currentBitmapRef: WeakReference<Bitmap?> = WeakReference(null)

    fun onAction(action: AiChatAction) {
        when (action) {
            is AiChatAction.UpdateInput ->
                _state.update { it.copy(currentInput = action.text) }

            is AiChatAction.SetContext -> {
                // 🌟 Bitmap WeakReference mein store karo — State mein NAHI
                currentBitmapRef = WeakReference(action.bitmapRef)
                _state.update {
                    it.copy(
                        currentPageNumber = action.pageNumber,
                        pdfName = action.pdfName
                    )
                }
            }

            is AiChatAction.SetVisibility ->
                _state.update { it.copy(isVisible = action.visible) }

            is AiChatAction.ClearChat ->
                _state.update { it.copy(messages = emptyList(), errorMessage = null) }

            is AiChatAction.StopStreaming -> stopStreaming()

            is AiChatAction.SendMessage ->
                handleSendMessage(action.query, currentBitmapRef.get())

            is AiChatAction.OnSmartPromptClick ->
                handleSendMessage(action.prompt, currentBitmapRef.get())

            is AiChatAction.DismissError ->
                _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun handleSendMessage(query: String, bitmap: Bitmap?) {
        if (query.isBlank()) return

        // 🌟 Smart System Context: PDF name aur page number Gemini ko batao
        val contextualQuery = buildString {
            if (_state.value.pdfName.isNotBlank()) {
                append("[PDF: ${_state.value.pdfName}, Page ${_state.value.currentPageNumber}]\n")
            }
            append(query)
        }

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            role = ChatRole.USER,
            text = query  // UI mein sirf user ki query dikhao, context hidden
        )

        _state.update {
            it.copy(
                messages = it.messages + userMsg,
                currentInput = "",
                isAiThinking = true,
                errorMessage = null
            )
        }

        triggerScrollToBottom()

        val aiMsgId = UUID.randomUUID().toString()
        val aiPlaceholder = ChatMessage(
            id = aiMsgId,
            role = ChatRole.MODEL,
            text = "",
            isStreaming = true
        )
        _state.update { it.copy(messages = it.messages + aiPlaceholder) }

        streamingJob?.cancel()
        streamingJob = viewModelScope.launch {
            try {
                aiRepository.chatWithPdfStream(contextualQuery, bitmap).collect { chunk ->
                    if (chunk.isNotEmpty()) {
                        _state.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages.map { msg ->
                                    if (msg.id == aiMsgId) msg.copy(text = msg.text + chunk)
                                    else msg
                                }
                            )
                        }
                        triggerScrollToBottom()
                    }
                }
                finalizeAiMessage(aiMsgId)
            } catch (e: Exception) {
                finalizeAiMessage(aiMsgId)
                val errorText = when {
                    e.message?.contains("API key", ignoreCase = true) == true ->
                        "API Key invalid. Go to Settings → Add Gemini Key."
                    e.message?.contains("network", ignoreCase = true) == true ->
                        "No internet connection."
                    else -> "AI failed: ${e.localizedMessage}"
                }
                _state.update { it.copy(errorMessage = errorText) }
            }
        }
    }

    private fun stopStreaming() {
        streamingJob?.cancel()
        _state.value.messages.lastOrNull { it.isStreaming }?.let {
            finalizeAiMessage(it.id)
        }
    }

    private fun finalizeAiMessage(aiMsgId: String) {
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages.map { msg ->
                    if (msg.id == aiMsgId) msg.copy(isStreaming = false) else msg
                },
                isAiThinking = false
            )
        }
    }

    private fun triggerScrollToBottom() {
        viewModelScope.launch {
            _events.send(AiChatEvent.ScrollToBottom)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 🌟 ViewModel clear hone par WeakReference bhi clear
        currentBitmapRef.clear()
    }
}
