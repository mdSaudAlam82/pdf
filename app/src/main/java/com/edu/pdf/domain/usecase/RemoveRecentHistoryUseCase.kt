package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

/**
 * 🌟 MASHINE 6: RemoveRecentHistoryUseCase
 * Iska kaam hai Recent list se PDF ya Folder ko hatana (unka time 0 set karke).
 */
class RemoveRecentHistoryUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(items: List<HomeItem>) {
        items.forEach { item ->
            when (item) {
                is HomeItem.PdfItem -> repository.updateLastOpenedTime(item.pdf.id, 0L)
                is HomeItem.FolderItem -> repository.updateFolderLastOpenedTime(item.folder.folderId, 0L)
            }
        }
    }
}
