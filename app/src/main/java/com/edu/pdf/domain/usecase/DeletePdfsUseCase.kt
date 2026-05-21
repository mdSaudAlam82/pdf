package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class DeletePdfsUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(pdfs: List<PdfFile>): Boolean {
        // 🌟 SMART FIX: Agar list khali hai, toh repository ko call mat karo
        if (pdfs.isEmpty()) return true

        return repository.deletePdfs(pdfs)
    }
}