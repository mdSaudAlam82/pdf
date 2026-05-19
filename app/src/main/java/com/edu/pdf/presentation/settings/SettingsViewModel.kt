package com.edu.pdf.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.data.preferences.AiKeyManager
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SettingsUiState(
    val primaryKey: String = "",
    val fallbackKey1: String = "",
    val fallbackKey2: String = "",
    val isVerifying: Boolean = false,
    val validationMessage: String? = null,
    val isSuccess: Boolean = false
)

sealed interface SettingsAction {
    data class UpdatePrimaryKey(val key: String) : SettingsAction
    data class UpdateFallback1(val key: String) : SettingsAction
    data class UpdateFallback2(val key: String) : SettingsAction
    data object SaveAndVerifyKeys : SettingsAction
    data object ClearMessage : SettingsAction
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val keyManager: AiKeyManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    init {
        val savedKeys = keyManager.getKeys()
        _state.update {
            it.copy(
                primaryKey = savedKeys.getOrNull(0) ?: "",
                fallbackKey1 = savedKeys.getOrNull(1) ?: "",
                fallbackKey2 = savedKeys.getOrNull(2) ?: ""
            )
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.UpdatePrimaryKey -> _state.update { it.copy(primaryKey = action.key) }
            is SettingsAction.UpdateFallback1 -> _state.update { it.copy(fallbackKey1 = action.key) }
            is SettingsAction.UpdateFallback2 -> _state.update { it.copy(fallbackKey2 = action.key) }
            is SettingsAction.ClearMessage -> _state.update { it.copy(validationMessage = null) }
            is SettingsAction.SaveAndVerifyKeys -> verifyAndSave()
        }
    }

    private fun verifyAndSave() {
        val currentState = _state.value
        // 🌟 FIX 1: Hamesha trim() use karo taaki copy-paste ke hidden spaces hat jayein
        val primaryKeyTrimmed = currentState.primaryKey.trim()

        if (primaryKeyTrimmed.isBlank()) {
            _state.update { it.copy(validationMessage = "Primary Key is required!") }
            return
        }

        _state.update { it.copy(isVerifying = true, validationMessage = null) }

        viewModelScope.launch {
            // Hum boolean (true/false) ke bajaye String result lenge taaki error padh sakein
            val resultMessage = withContext(Dispatchers.IO) {
                try {
                    val model = GenerativeModel(
                        modelName = "gemini-2.5-flash",
                        apiKey = primaryKeyTrimmed
                    )
                    // "ping" ki jagah "Hello" bhejo, kabhi-kabhi too-short prompts block ho jate hain
                    model.generateContent("Hello")
                    "SUCCESS"
                } catch (e: Exception) {
                    // 🌟 FIX 2: Asli exception ka message pakdo taaki UI par dikha sakein
                    e.localizedMessage ?: "Unknown Network Error"
                }
            }

            if (resultMessage == "SUCCESS") {
                keyManager.saveKeys(
                    primaryKeyTrimmed,
                    currentState.fallbackKey1.trim(),
                    currentState.fallbackKey2.trim()
                )
                _state.update {
                    it.copy(
                        isVerifying = false,
                        isSuccess = true,
                        validationMessage = "AI Keys Verified & Secured! ✅"
                    )
                }
            } else {
                // Yahan tumhe sachai pata chalegi ki aakhir API key kyu nahi le raha tha!
                _state.update {
                    it.copy(
                        isVerifying = false,
                        isSuccess = false,
                        validationMessage = "Failed: $resultMessage ❌"
                    )
                }
            }
        }
    }
}