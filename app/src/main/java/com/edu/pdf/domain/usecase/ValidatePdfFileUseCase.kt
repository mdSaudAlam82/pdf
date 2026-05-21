package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import java.io.File
import javax.inject.Inject

/**
 * 🌟 MASHINE 7: ValidatePdfFileUseCase
 * Iska kaam hai file kholne se pehle uska "Health Check" karna.
 */
class ValidatePdfFileUseCase @Inject constructor(
    private val repository: PdfRepository,
    private val deletePdfsUseCase: DeletePdfsUseCase
) {
    suspend operator fun invoke(pdf: PdfFile): Result<Unit> {
        // 1. Check karo physical file hai ya nahi, ya MediaStore mein exist karti hai
        val exists = File(pdf.path).exists() || repository.checkFileExists(pdf.id)
        
        return if (exists) {
            Result.success(Unit)
        } else {
            // 2. Agar file nahi mili, toh database se bhi saaf kar do taaki user confuse na ho
            deletePdfsUseCase(listOf(pdf))
            Result.failure(Exception("File moved or deleted externally."))
        }
    }
}
