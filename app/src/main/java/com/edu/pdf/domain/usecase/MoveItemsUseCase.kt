package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

/**
 * 🌟 MASHINE 1: MoveItemsUseCase
 * Iska kaam hai PDFs aur Folders ko unki purani jagah se nayi jagah bhej dena.
 */
class MoveItemsUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(items: List<HomeItem>, targetFolderId: String?, isVault: Boolean): Result<Unit> {
        return try {
            val pdfIds = items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf.id }
            val folderIds = items.filterIsInstance<HomeItem.FolderItem>().map { it.folder.folderId }

            // 1. Pehle PDFs ko move karo
            if (pdfIds.isNotEmpty()) {
                repository.movePdfsToVirtualFolder(pdfIds, targetFolderId, isVault)
            }

            // 2. Phir Folders ko move karo
            folderIds.forEach { folderId ->
                repository.moveFolderToVirtualFolder(folderId, targetFolderId, isVault)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
