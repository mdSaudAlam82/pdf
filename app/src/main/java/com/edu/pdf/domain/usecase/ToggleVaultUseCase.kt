package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

/**
 * 🌟 MASHINE 2: ToggleVaultUseCase
 * Iska kaam hai file ko "Public" se "Private" (Vault) karna aur wapas nikalna.
 */
class ToggleVaultUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(pdf: PdfFile): Result<Unit> {
        return try {
            val newVaultStatus = !pdf.isVault
            // Vault me dalne par target folder hamesha null (Root) rehta hai
            repository.movePdfsToVirtualFolder(listOf(pdf.id), null, isVault = newVaultStatus)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
