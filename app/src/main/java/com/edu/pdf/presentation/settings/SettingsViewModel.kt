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
        if (currentState.primaryKey.isBlank()) {
            _state.update { it.copy(validationMessage = "Primary Key is required!") }
            return
        }

        _state.update { it.copy(isVerifying = true, validationMessage = null) }

        viewModelScope.launch {
            val isValid = withContext(Dispatchers.IO) {
                try {
                    val model = GenerativeModel(modelName = "gemini-1.5-flash", apiKey = currentState.primaryKey)
                    model.generateContent("ping")
                    true
                } catch (e: Exception) {
                    false
                }
            }

            if (isValid) {
                keyManager.saveKeys(currentState.primaryKey, currentState.fallbackKey1, currentState.fallbackKey2)
                _state.update { it.copy(isVerifying = false, isSuccess = true, validationMessage = "AI Keys Verified & Secured! ✅") }
            } else {
                _state.update { it.copy(isVerifying = false, isSuccess = false, validationMessage = "Invalid Primary Key. ❌") }
            }
        }
    }
}