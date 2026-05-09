package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class ScanPdfsUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke() = repository.scanAndSavePdfs()
}