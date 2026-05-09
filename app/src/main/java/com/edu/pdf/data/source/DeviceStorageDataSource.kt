package com.edu.pdf.data.source

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.edu.pdf.domain.model.PdfFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject
import kotlin.coroutines.resume
class DeviceStorageDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun getPdfProRootFolder(): String {
        val docsDir =
            android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOCUMENTS)
        val pdfProDir = File(docsDir, "PdfPro")
        if (!pdfProDir.exists()) {
            pdfProDir.mkdirs()
            scanFilesBatch(arrayOf(pdfProDir.absolutePath))
        }
        return pdfProDir.absolutePath
    }
    suspend fun processDevicePdfUpdates(
        lastSyncTime: Long,
        onNewPdfsBatch: suspend (List<PdfFile>) -> Unit
    ) = withContext(Dispatchers.IO) {
        val collection = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED
        )
        val selection = "(${MediaStore.Files.FileColumns.MIME_TYPE} = ? OR ${MediaStore.Files.FileColumns.DATA} LIKE '%.pdf') AND ${MediaStore.Files.FileColumns.DATE_MODIFIED} > ?"
        val selectionArgs = arrayOf("application/pdf", (lastSyncTime / 1000).toString())

        context.contentResolver.query(collection, projection, selection, selectionArgs, null)?.use { cursor ->
            val tempBatch = mutableListOf<PdfFile>()
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val path = cursor.getString(dataCol) ?: continue
                if (path.contains("/secure_vault_core/")) continue // Vault files skip karo

                val size = cursor.getLong(sizeCol)
                if (size > 0) {
                    val uriStr = ContentUris.withAppendedId(collection, cursor.getLong(idCol)).toString()
                    tempBatch.add(PdfFile(uriStr, cursor.getString(titleCol) ?: File(path).name, path, size, cursor.getLong(dateCol)))
                }

                if (tempBatch.size >= 200) {
                    onNewPdfsBatch(tempBatch.toList())
                    tempBatch.clear()
                    yield()
                }
            }
            if (tempBatch.isNotEmpty()) onNewPdfsBatch(tempBatch)
        }
    }

    suspend fun movePhysicalFile(sourcePath: String, targetFolderPath: String): String? =
        withContext(Dispatchers.IO) {
            val sourceFile = File(sourcePath)
            if (!sourceFile.exists()) return@withContext null
            val targetDir = File(targetFolderPath)
            if (!targetDir.exists()) targetDir.mkdirs()
            var targetFile = File(targetDir, sourceFile.name)
            var counter = 1
            while (targetFile.exists()) {
                val nameWithoutExt = sourceFile.nameWithoutExtension
                val ext = sourceFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
                targetFile = File(targetDir, "$nameWithoutExt ($counter)$ext")
                counter++
            }
            return@withContext try {
                Files.move(Paths.get(sourceFile.absolutePath), Paths.get(targetFile.absolutePath))
                syncWithMediaStore(sourcePath, targetFile.absolutePath)
                targetFile.absolutePath
            } catch (_: Exception) {
                null
            }
        }

    suspend fun syncWithMediaStore(oldPath: String?, newPath: String?) =
        withContext(Dispatchers.IO) {
            oldPath?.let { path ->
                try {
                    context.contentResolver.delete(
                        MediaStore.Files.getContentUri("external"),
                        "${MediaStore.Files.FileColumns.DATA} = ?",
                        arrayOf(path)
                    )
                } catch (_: Exception) {}
            }
            val pathsToScan = listOfNotNull(oldPath, newPath).toTypedArray()
            if (pathsToScan.isNotEmpty()) android.media.MediaScannerConnection.scanFile(
                context,
                pathsToScan,
                null,
                null
            )
        }

    fun doesFileExist(fileUri: String): Boolean {
        return try {
            context.contentResolver.openFileDescriptor(fileUri.toUri(), "r")?.use { true } ?: false
        } catch (_: Exception) {
            false
        }
    }

    suspend fun moveToTrash(pdfs: List<PdfFile>): List<String> = withContext(Dispatchers.IO) {
        val successfullyTrashedIds = mutableListOf<String>()
        val trashFolder = File(context.getExternalFilesDir(null), ".trash")
        if (!trashFolder.exists()) trashFolder.mkdirs()

        for (pdf in pdfs) {
            val file = File(pdf.path)
            if (file.exists() && file.renameTo(
                    File(
                        trashFolder,
                        "${System.currentTimeMillis()}_${file.name}"
                    )
                )
            ) {
                try {
                    context.contentResolver.delete(pdf.id.toUri(), null, null)
                } catch (_: Exception) {}
                successfullyTrashedIds.add(pdf.id)
            } else {
                successfullyTrashedIds.add(pdf.id)
            }
        }
        return@withContext successfullyTrashedIds
    }

    fun scanFilesBatch(paths: Array<String>) {
        if (paths.isEmpty()) return
        android.media.MediaScannerConnection.scanFile(context, paths, null, null)
    }

    @SuppressLint("UnsanitizedFilenameFromContentProvider")
    suspend fun importFileFromUri(
        uri: Uri,
        isVault: Boolean,
        targetPhysicalPath: String? = null
    ): PdfFile? = withContext(Dispatchers.IO) {
        var targetFile: File? = null
        try {
            val contentResolver = context.contentResolver
            var fileName = "Imported_PDF_${System.currentTimeMillis()}.pdf"
            var fileSize = 0L
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                    if (nameIndex != -1) fileName = cursor.getString(nameIndex) ?: fileName
                    if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
                }
            }

            targetFile = if (isVault) {
                val vaultDir = File(context.filesDir, "secure_vault_core")
                if (!vaultDir.exists()) vaultDir.mkdirs()
                File(vaultDir, "${java.util.UUID.randomUUID()}.locked")
            } else {
                val publicDir = if (targetPhysicalPath != null && File(targetPhysicalPath).exists()) {
                    File(targetPhysicalPath)
                } else {
                    File(getPdfProRootFolder())
                }
                var tempFile = File(publicDir, fileName)
                var counter = 1
                while (tempFile.exists()) {
                    val nameWithoutExt = tempFile.nameWithoutExtension
                    val ext = tempFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
                    tempFile = File(publicDir, "$nameWithoutExt ($counter)$ext")
                    counter++
                }
                tempFile
            }

            contentResolver.openInputStream(uri)?.use { inputStream ->
                targetFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            if (!isVault) {
                val realUri = suspendCancellableCoroutine<Uri?> { cont ->
                    android.media.MediaScannerConnection.scanFile(
                        context,
                        arrayOf(targetFile.absolutePath),
                        arrayOf("application/pdf")
                    ) { _, scannedUri ->
                        cont.resume(scannedUri)
                    }
                }

                val finalId = realUri?.toString() ?: targetFile.absolutePath

                return@withContext PdfFile(
                    id = finalId,
                    name = targetFile.name,
                    path = targetFile.absolutePath,
                    sizeInBytes = if (fileSize > 0) fileSize else targetFile.length(),
                    lastModified = System.currentTimeMillis()
                )
            } else {
                return@withContext PdfFile(
                    id = targetFile.absolutePath,
                    name = targetFile.name,
                    path = targetFile.absolutePath,
                    sizeInBytes = if (fileSize > 0) fileSize else targetFile.length(),
                    lastModified = System.currentTimeMillis(),
                    isVault = true
                )
            }
        } catch (e: kotlinx.coroutines.CancellationException) {
            targetFile?.let { if (it.exists()) it.delete() }
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            targetFile?.let { if (it.exists()) it.delete() }
            return@withContext null
        }
    }
}