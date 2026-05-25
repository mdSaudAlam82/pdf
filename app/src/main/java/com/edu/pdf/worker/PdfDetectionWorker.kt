package com.edu.pdf.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.notification.PdfNotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class PdfDetectionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PdfRepository,
    private val notificationHelper: PdfNotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("PdfDetectionWorker", "Worker started! Something changed in storage.")
        return try {
            // 1. Scan the device for new PDFs
            repository.scanAndSavePdfs()

            val latestPdfs = repository.getAllPdfs(SortType.DATE_DESC).first().take(10)
            val nowSeconds = System.currentTimeMillis() / 1000
            
            Log.d("PdfDetectionWorker", "Scanning top 10 newest PDFs in system")

            latestPdfs.forEach { pdf ->
                val ageSeconds = nowSeconds - pdf.lastModified
                Log.d("PdfDetectionWorker", "Analyzing: ${pdf.name}, Age: ${ageSeconds}s")

                // 🌟 Standard Notification Logic
                if (ageSeconds in 0..<60) {
                    Log.d("PdfDetectionWorker", "NOTIFYING: ${pdf.name}")
                    notificationHelper.showNewPdfNotification(pdf.name, pdf.path)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("PdfDetectionWorker", "Worker Error: ${e.message}")
            Result.retry()
        }
    }

    companion object {
        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .addContentUriTrigger(android.provider.MediaStore.Files.getContentUri("external"), true)
                .build()

            val request = OneTimeWorkRequestBuilder<PdfDetectionWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "PdfDetectionWork",
                ExistingWorkPolicy.KEEP,
                request
            )
        }
    }
}
