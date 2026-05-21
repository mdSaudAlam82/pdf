package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

/**
 * 🌟 MASHINE 5: MarkPdfAsOpenedUseCase
 * Iska kaam hai PDF khulne par uski date aur time database mein update karna.
 */
class MarkPdfAsOpenedUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(pdfId: String) {
        repository.updateLastOpenedTime(pdfId, System.currentTimeMillis())
    }
}
