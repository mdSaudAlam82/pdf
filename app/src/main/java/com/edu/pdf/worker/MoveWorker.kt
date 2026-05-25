package com.edu.pdf.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.notification.PdfNotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.ensureActive

@HiltWorker
class MoveWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PdfRepository,
    private val notificationHelper: PdfNotificationHelper,
    private val userPreferences: com.edu.pdf.data.preferences.UserPreferences
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val batchId = inputData.getLong(KEY_BATCH_ID, -1L)
        val folderIds = inputData.getStringArray(KEY_FOLDER_IDS) ?: emptyArray()
        val targetPath = inputData.getString(KEY_TARGET_PATH)
        val sourcePath = inputData.getString(KEY_SOURCE_PATH) // 🌟 Get the source
        val isVault = inputData.getBoolean(KEY_IS_VAULT, false)

        if (batchId == -1L && folderIds.isEmpty()) return@withContext Result.failure()

        val pdfsToMove = if (batchId != -1L) repository.getPdfsForWorkerBatch(batchId) else emptyList()
        val totalItems = pdfsToMove.size + folderIds.size
        var processedCount = 0

        if (totalItems == 0) return@withContext Result.success()

        // 🛡️ PERFORMANCE LOCK: Disable ContentObserver during bulk move
        userPreferences.setSyncLocked(true)

        try {
            // 🌟 1. MOVE FOLDERS
            for (folderId in folderIds) {
                ensureActive()
                repository.moveFolderToVirtualFolder(folderId, targetPath, isVault)
                processedCount++
                updateProgress(processedCount, totalItems, targetPath)
            }

            // 🌟 2. MOVE PDFS
            pdfsToMove.chunked(25).forEach { chunk ->
                ensureActive()
                val chunkIds = chunk.map { it.id }
                repository.movePdfsToVirtualFolder(chunkIds, targetPath, isVault)
                processedCount += chunk.size
                updateProgress(processedCount, totalItems, targetPath)
            }

            // 🧹 CLEANUP
            if (batchId != -1L) repository.markPdfsForWorker(pdfsToMove.map { it.id }, 0L)

            userPreferences.setSyncLocked(false)
            // 🌟 FINISH SUCCESS: Trigger navigation back to SOURCE
            val finalOutput = workDataOf(KEY_TARGET_PATH to sourcePath)
            Result.success(finalOutput)
        } catch (e: Exception) {
            if (batchId != -1L) repository.markPdfsForWorker(pdfsToMove.map { it.id }, 0L)
            userPreferences.setSyncLocked(false)
            Result.failure()
        }
    }

    private suspend fun updateProgress(current: Int, total: Int, targetPath: String?) {
        val progressData = workDataOf(
            KEY_PROGRESS_CURRENT to current,
            KEY_PROGRESS_TOTAL to total,
            KEY_TARGET_PATH to targetPath
        )
        setProgress(progressData)
    }

    companion object {
        const val KEY_BATCH_ID = "batch_id"
        const val KEY_FOLDER_IDS = "folder_ids"
        const val KEY_TARGET_PATH = "target_path"
        const val KEY_SOURCE_PATH = "source_path" // 🌟 NAYA: To return back home
        const val KEY_IS_VAULT = "is_vault"
        const val KEY_PROGRESS_CURRENT = "progress_current"
        const val KEY_PROGRESS_TOTAL = "progress_total"

        fun start(context: Context, batchId: Long, folderIds: List<String>, targetPath: String?, sourcePath: String?, isVault: Boolean) {
            val inputData = workDataOf(
                KEY_BATCH_ID to batchId,
                KEY_FOLDER_IDS to folderIds.toTypedArray(),
                KEY_TARGET_PATH to targetPath,
                KEY_SOURCE_PATH to sourcePath, // 🌟 Save where we came from
                KEY_IS_VAULT to isVault
            )

            val moveRequest = OneTimeWorkRequestBuilder<MoveWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "bulk_move_task",
                ExistingWorkPolicy.REPLACE, // 🌟 Ensure fresh start
                moveRequest
            )
        }
    }
}
