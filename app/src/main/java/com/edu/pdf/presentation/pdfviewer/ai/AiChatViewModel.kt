package com.edu.pdf.presentation.pdfviewer.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.ChatMessage
import com.edu.pdf.domain.model.ChatRole
import com.edu.pdf.domain.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val aiRepository: AiRepository // Aapka purana AI repo
) : ViewModel() {

    private val _state = MutableStateFlow(AiChatState())
    val state: StateFlow<AiChatState> = _state.asStateFlow()

    fun onAction(action: AiAction) {
        when (action) {
            is AiAction.ToggleChat -> {
                _state.update { it.copy(isOpen = action.open) }
            }
            is AiAction.SendMessage -> {
                sendMessageToGemini(action.message, action.pdfTextContext)
            }
        }
    }

    private fun sendMessageToGemini(userText: String, pdfContext: String?) {
        val userMsg = ChatMessage(id = UUID.randomUUID().toString(), text = userText, role = ChatRole.USER)

        _state.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isTyping = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                // Yahan aapka AI repository call hoga
                aiRepository.chatWithPdfStream(userText, null).collect { chunk ->
                    // Handle stream update (Jaise aapke purane ViewModel me tha)
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isTyping = false) }
            } finally {
                _state.update { it.copy(isTyping = false) }
            }
        }
    }
}