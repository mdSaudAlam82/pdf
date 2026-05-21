package com.edu.pdf.presentation.folders.vault

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.data.security.SecurityUtils
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

// 🌟 STRICT MVI: Single State Object
data class VaultUiState(
    val vaultPdfs: ImmutableList<PdfFile> = persistentListOf(),
    val isPickerOpen: Boolean = false,
    val isGridView: Boolean = false,
    val decryptionProgress: Float? = null,
    val isLoading: Boolean = true
)

// 🌟 EVENTS: One-time navigation and Toasts
sealed interface VaultEvent {
    data class NavigateToViewer(val tempPath: String) : VaultEvent
    data class ShowSnackbar(val message: String) : VaultEvent
}

sealed interface VaultAction {
    data object OpenPicker : VaultAction
    data object ClosePicker : VaultAction
    data object ToggleViewMode : VaultAction
    data class OpenPdf(val pdfPath: String) : VaultAction
    data class MoveToVault(val pdfIds: List<String>) : VaultAction
    data class RemoveFromVault(val pdfId: String) : VaultAction
    data class DeletePdf(val pdf: PdfFile) : VaultAction
}

@HiltViewModel
class VaultViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: PdfRepository,
    private val cryptoEngine: VaultCryptoEngine,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _internalState = MutableStateFlow(VaultUiState())

    private val _events = Channel<VaultEvent>()
    val events = _events.receiveAsFlow()

    val uiState: StateFlow<VaultUiState> = combine(
        repository.getManagedPdfs(parentPath = null, isVault = true),
        userPreferences.isFolderGridViewFlow,
        _internalState
    ) { pdfs, isGrid, internal ->
        internal.copy(
            vaultPdfs = pdfs.toImmutableList(),
            isGridView = isGrid,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, VaultUiState())

    fun onAction(action: VaultAction) {
        when (action) {
            is VaultAction.OpenPicker -> _internalState.update { it.copy(isPickerOpen = true) }
            is VaultAction.ClosePicker -> _internalState.update { it.copy(isPickerOpen = false) }
            is VaultAction.ToggleViewMode -> {
                viewModelScope.launch {
                    val currentGridState = userPreferences.isFolderGridViewFlow.first()
                    userPreferences.saveFolderGridViewPreference(!currentGridState)
                }
            }
            is VaultAction.OpenPdf -> decryptAndOpenPdf(action.pdfPath)
            is VaultAction.MoveToVault -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.movePdfsToVirtualFolder(action.pdfIds, null, isVault = true)
                    _events.send(VaultEvent.ShowSnackbar("Added to Vault"))
                }
            }
            is VaultAction.RemoveFromVault -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.movePdfsToVirtualFolder(listOf(action.pdfId), null, isVault = false)
                    _events.send(VaultEvent.ShowSnackbar("Restored from Vault"))
                }
            }
            is VaultAction.DeletePdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deletePdfsUseCase(listOf(action.pdf))
                    _events.send(VaultEvent.ShowSnackbar("Deleted permanently"))
                }
            }
        }
    }

    private fun decryptAndOpenPdf(pdfPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _internalState.update { it.copy(decryptionProgress = 0f) }
            val lockedFile = File(pdfPath)

            val secureTempDir = File(context.cacheDir, "vault_temp_view")
            if (!secureTempDir.exists()) secureTempDir.mkdirs()
            // 🛡️ SECURITY WIPE: Purani viewing file delete karo naya kholne se pehle
            SecurityUtils.wipeVaultTempStorage(context)

            val tempFile = File(secureTempDir, "view_${System.currentTimeMillis()}.pdf")

            try {
                val totalBytes = lockedFile.length().toFloat().coerceAtLeast(1f)
                var copied = 0L

                cryptoEngine.getEncryptedInputStream(lockedFile).use { input ->
                    tempFile.outputStream().use { output ->
                        val buffer = ByteArray(64 * 1024)
                        var bytesRead: Int
                        var lastEmitTime = 0L

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            copied += bytesRead
                            val now = System.currentTimeMillis()
                            if (now - lastEmitTime > 50) {
                                _internalState.update { it.copy(decryptionProgress = copied / totalBytes) }
                                lastEmitTime = now
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    _internalState.update { it.copy(decryptionProgress = null) }
                    _events.send(VaultEvent.NavigateToViewer(tempFile.absolutePath))
                }
            } catch (_: Exception) {
                if (tempFile.exists()) tempFile.delete()
                withContext(Dispatchers.Main) {
                    _internalState.update { it.copy(decryptionProgress = null) }
                    _events.send(VaultEvent.ShowSnackbar("Decryption Failed!"))
                }
            }
        }
    }
}