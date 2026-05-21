package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

/**
 * 🌟 MASHINE 4: ImportPdfUseCase
 * Iska kaam hai bahar ki file (WhatsApp/Downloads) ko app ke andar lana.
 */
class ImportPdfUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(uriString: String, targetFolderId: String?, isVault: Boolean): Result<Unit> {
        return repository.importPdfFromUri(
            uriString = uriString,
            targetPath = targetFolderId,
            isVault = isVault,
            isPhysicalFolder = false // Managed system me import ho raha hai
        )
    }
}
