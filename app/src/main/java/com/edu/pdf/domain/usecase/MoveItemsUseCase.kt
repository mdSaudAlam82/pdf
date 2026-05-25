package com.edu.pdf.domain.usecase

import android.content.Context
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.worker.MoveWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MoveItemsUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: PdfRepository
) {
    suspend operator fun invoke(
        selectedIds: Set<String>, 
        folderIds: List<String>, 
        targetFolderId: String?, 
        sourcePath: String?, // 🌟 NAYA: To return back home
        isVault: Boolean
    ): Result<Unit> {
        return try {
            if (selectedIds.size > 5 || folderIds.size > 5) {
                val batchId = System.currentTimeMillis()
                repository.markPdfsForWorker(selectedIds.toList(), batchId)
                
                // Handover both target and source
                MoveWorker.start(context, batchId, folderIds, targetFolderId, sourcePath, isVault)
                return Result.success(Unit)
            }

            // Small moves (Synchronous)
            if (selectedIds.isNotEmpty()) {
                repository.movePdfsToVirtualFolder(selectedIds.toList(), targetFolderId, isVault)
            }
            folderIds.forEach { repository.moveFolderToVirtualFolder(it, targetFolderId, isVault) }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
