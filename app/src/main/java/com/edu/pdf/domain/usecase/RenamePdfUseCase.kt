package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class RenamePdfUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(pdf: PdfFile, newName: String): Boolean {
        // Yahan business logic aa sakta hai, jaise name validation
        if (newName.isBlank()) return false
        return repository.renamePdf(pdf, newName)
    }
}