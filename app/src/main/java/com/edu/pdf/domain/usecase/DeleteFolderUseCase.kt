package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(folderId: String): Result<Unit> {
        return repository.deleteManagedFolder(folderId)
    }
}