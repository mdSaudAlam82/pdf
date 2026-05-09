package com.edu.pdf.presentation.folders.vault

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: PdfRepository,
    private val cryptoEngine: VaultCryptoEngine,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val vaultPdfs = repository.getManagedPdfs(parentId = null, isVault = true)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 🌟 2026 FIX: RAM bachaane ke liye on-demand list loading
    private val _pickerPdfs = MutableStateFlow<List<PdfFile>?>(null)
    val pickerPdfs = _pickerPdfs.asStateFlow()

    val isGridView = userPreferences.isFolderGridViewFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _decryptionProgress = MutableStateFlow<Float?>(null)
    val decryptionProgress = _decryptionProgress.asStateFlow()

    fun loadPublicPdfsForPicker() {
        viewModelScope.launch(Dispatchers.IO) {
            _pickerPdfs.value = repository.getAllPdfs(com.edu.pdf.domain.model.SortType.DATE_DESC).first()
        }
    }

    fun closePicker() {
        _pickerPdfs.value = null
    }

    fun toggleViewMode() {
        viewModelScope.launch {
            val currentGridState = userPreferences.isFolderGridViewFlow.first()
            userPreferences.saveFolderGridViewPreference(!currentGridState)
        }
    }

    fun getDecryptedPathForViewing(pdfPath: String, onReady: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _decryptionProgress.value = 0f
            val lockedFile = File(pdfPath)

            val secureTempDir = File(context.cacheDir, "vault_temp_view")
            if (!secureTempDir.exists()) secureTempDir.mkdirs()
            secureTempDir.listFiles()?.forEach { it.delete() }

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
                                _decryptionProgress.value = copied / totalBytes
                                lastEmitTime = now
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    _decryptionProgress.value = null
                    onReady(tempFile.absolutePath)
                }
            } catch (e: Exception) {
                if (tempFile.exists()) tempFile.delete()
                withContext(Dispatchers.Main) { _decryptionProgress.value = null }
            }
        }
    }

    fun moveFromAppToVault(pdfIds: List<String>) { viewModelScope.launch(Dispatchers.IO) { repository.movePdfsToVirtualFolder(pdfIds, null, isVault = true) } }
    fun removeFromVault(pdfId: String) { viewModelScope.launch(Dispatchers.IO) { repository.movePdfsToVirtualFolder(listOf(pdfId), null, isVault = false) } }
    fun deletePdf(pdf: PdfFile) { viewModelScope.launch(Dispatchers.IO) { deletePdfsUseCase(listOf(pdf)) } }
    fun renamePdf(pdf: PdfFile, newName: String) { viewModelScope.launch(Dispatchers.IO) { renamePdfUseCase(pdf, newName) } }
}