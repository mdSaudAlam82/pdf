package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class CreateFolderUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(name: String, parentId: String?, isVault: Boolean = false): Result<String> {
        if (name.isBlank()) return Result.failure(Exception("Folder name cannot be empty"))
        return repository.createManagedFolder(name, parentId, isVault)
    }
}