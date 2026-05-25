package com.edu.pdf.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.data.preferences.AiKeyManager
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    primaryKey = keyManager.getPrimaryKey() ?: "",
                    fallbackKey1 = keyManager.getFallbackKey1() ?: "",
                    fallbackKey2 = keyManager.getFallbackKey2() ?: ""
                )
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.UpdatePrimaryKey -> _state.update { it.copy(primaryKey = action.key) }
            is SettingsAction.UpdateFallback1 -> _state.update { it.copy(fallbackKey1 = action.key) }
            is SettingsAction.UpdateFallback2 -> _state.update { it.copy(fallbackKey2 = action.key) }
            SettingsAction.SaveAndVerifyKeys -> verifyAndSave()
            SettingsAction.ClearMessage -> _state.update { it.copy(validationMessage = null) }
        }
    }

    private fun verifyAndSave() {
        val currentState = _state.value
        val primaryKeyTrimmed = currentState.primaryKey.trim()

        if (primaryKeyTrimmed.isBlank()) {
            _state.update { it.copy(validationMessage = "Primary Key is required!") }
            return
        }

        _state.update { it.copy(isVerifying = true, validationMessage = null) }

        viewModelScope.launch {
            // 🌟 2026 ELITE VERIFICATION: Real Handshake with Google Gemini
            val resultMessage = withContext(Dispatchers.IO) {
                try {
                    val model = GenerativeModel(
                        modelName = "gemini-1.5-flash",
                        apiKey = primaryKeyTrimmed
                    )
                    // Ping check
                    model.generateContent("Say 'Verified'")
                    "SUCCESS"
                } catch (e: Exception) {
                    e.localizedMessage ?: "Network error. Check connection."
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
                        validationMessage = "AI Engine Active & Secured! ✅"
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isVerifying = false,
                        isSuccess = false,
                        validationMessage = "Validation Failed: $resultMessage"
                    )
                }
            }
        }
    }
}
