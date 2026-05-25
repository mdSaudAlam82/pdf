package com.edu.pdf.data.source

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume

class DeviceStorageDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val cryptoEngine: VaultCryptoEngine
) {
    fun getPdfProRootFolder(): String {
        val docsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOCUMENTS)
        val pdfProDir = File(docsDir, "PdfPro")
        if (!pdfProDir.exists()) {
            pdfProDir.mkdirs()
            scanFilesBatch(arrayOf(pdfProDir.absolutePath))
        }
        return pdfProDir.absolutePath
    }

    suspend fun createPhysicalFolder(name: String, parentPath: String?): String? = withContext(Dispatchers.IO) {
        val root = parentPath ?: getPdfProRootFolder()
        val newFolder = File(root, name)
        if (newFolder.exists()) return@withContext null
        return@withContext if (newFolder.mkdirs()) {
            scanFilesBatch(arrayOf(newFolder.absolutePath))
            newFolder.absolutePath
        } else null
    }

    /**
     * 🌟 THE INDUSTRY-STANDARD MOVE ENGINE
     * Uses Atomic Rename for speed and NIO Channels for cross-partition safety.
     * 100% Reliable across all Android devices.
     */
    suspend fun movePhysicalFile(sourcePath: String, targetFolderPath: String): String? = withContext(Dispatchers.IO) {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) return@withContext null

        val targetDir = File(targetFolderPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        if (sourceFile.parentFile?.absolutePath == targetDir.absolutePath) {
            return@withContext sourcePath
        }

        var targetFile = File(targetDir, sourceFile.name)
        val originalNameWithoutExt = sourceFile.nameWithoutExtension
        val originalExt = sourceFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
        var counter = 1

        while (targetFile.exists()) {
            targetFile = File(targetDir, "$originalNameWithoutExt ($counter)$originalExt")
            counter++
        }

        return@withContext try {
            // 🌟 1. ATOMIC RENAME (Pure Speed for Internal Storage)
            if (sourceFile.renameTo(targetFile)) {
                return@withContext targetFile.absolutePath
            }

            // 🌟 2. NIO HIGH-SPEED FALLBACK (For SD-Cards or partitions)
            if (sourceFile.isDirectory) {
                if (sourceFile.copyRecursively(targetFile, overwrite = true)) {
                    sourceFile.deleteRecursively()
                    targetFile.absolutePath
                } else null
            } else {
                sourceFile.inputStream().channel.use { src ->
                    targetFile.outputStream().channel.use { dst ->
                        src.transferTo(0, src.size(), dst)
                    }
                }
                if (targetFile.length() == sourceFile.length()) {
                    if (sourceFile.delete()) targetFile.absolutePath else null
                } else null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun deletePhysicalPath(path: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(path)
        if (!file.exists()) return@withContext true
        val success = file.deleteRecursively()
        if (success) syncWithMediaStore(path, null)
        return@withContext success
    }

    suspend fun syncWithMediaStore(oldPath: String?, newPath: String?) = withContext(Dispatchers.IO) {
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
            context, pathsToScan, null, null
        )
    }

    /**
     * 🌟 STABLE BULK SYNC
     * Notifies OS about multiple changes efficiently using a path array.
     */
    suspend fun syncWithMediaStoreBulk(paths: List<String>) = withContext(Dispatchers.IO) {
        if (paths.isNotEmpty()) {
            android.media.MediaScannerConnection.scanFile(context, paths.toTypedArray(), null, null)
        }
    }

    suspend fun processDevicePdfUpdates(
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

        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ? OR ${MediaStore.Files.FileColumns.DATA} LIKE '%.pdf'"
        val selectionArgs = arrayOf("application/pdf")

        context.contentResolver.query(collection, projection, selection, selectionArgs, null)?.use { cursor ->
            val tempBatch = mutableListOf<PdfFile>()
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val path = cursor.getString(dataCol) ?: continue
                if (path.contains("/secure_vault_core/") || path.contains("/.trash/")) continue

                val file = File(path)
                if (!file.exists()) continue

                val size = cursor.getLong(sizeCol)
                if (size > 0) {
                    val uriStr = ContentUris.withAppendedId(collection, cursor.getLong(idCol)).toString()
                    val parentPath = file.parentFile?.absolutePath

                    tempBatch.add(PdfFile(
                        id = uriStr,
                        name = cursor.getString(titleCol) ?: file.name,
                        path = path,
                        sizeInBytes = size,
                        lastModified = cursor.getLong(dateCol),
                        virtualParentId = parentPath
                    ))
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

    fun doesFileExist(fileUriOrPath: String): Boolean {
        return try {
            if (fileUriOrPath.startsWith("content://")) {
                context.contentResolver.openFileDescriptor(fileUriOrPath.toUri(), "r")?.use { true } ?: false
            } else {
                File(fileUriOrPath).exists()
            }
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
            if (file.exists() && movePhysicalFile(pdf.path, trashFolder.absolutePath) != null) {
                successfullyTrashedIds.add(pdf.id)
            } else if (!file.exists()) {
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
    suspend fun importFileFromUri(uri: Uri, isVault: Boolean, targetPhysicalPath: String? = null): PdfFile? = withContext(Dispatchers.IO) {
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
                val originalNameWithoutExt = tempFile.nameWithoutExtension
                val originalExt = tempFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
                var counter = 1
                while (tempFile.exists()) {
                    tempFile = File(publicDir, "$originalNameWithoutExt ($counter)$originalExt")
                    counter++
                }
                tempFile
            }

            contentResolver.openInputStream(uri)?.use { inputStream ->
                if (isVault) {
                    cryptoEngine.getEncryptedOutputStream(targetFile).use { encryptedOutput ->
                        inputStream.copyTo(encryptedOutput)
                    }
                } else {
                    targetFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }

            if (!isVault) {
                val realUri = suspendCancellableCoroutine<Uri?> { cont ->
                    android.media.MediaScannerConnection.scanFile(
                        context, arrayOf(targetFile.absolutePath), arrayOf("application/pdf")
                    ) { _, scannedUri -> cont.resume(scannedUri) }
                }
                val finalId = realUri?.toString() ?: targetFile.absolutePath

                return@withContext PdfFile(
                    id = finalId, name = targetFile.name, path = targetFile.absolutePath,
                    sizeInBytes = if (fileSize > 0) fileSize else targetFile.length(),
                    lastModified = System.currentTimeMillis(),
                    virtualParentId = targetFile.parentFile?.absolutePath
                )
            } else {
                return@withContext PdfFile(
                    id = targetFile.absolutePath, name = targetFile.name, path = targetFile.absolutePath,
                    sizeInBytes = if (fileSize > 0) fileSize else targetFile.length(),
                    lastModified = System.currentTimeMillis(), isVault = true
                )
            }
        } catch (_: Exception) {
            targetFile?.let { if (it.exists()) it.delete() }
            return@withContext null
        }
    }

    fun renamePhysicalFile(oldPath: String, newName: String): String? {
        val oldFile = File(oldPath)
        if (!oldFile.exists()) return null
        val parentDir = oldFile.parentFile ?: return null
        val targetFile = File(parentDir, newName)
        if (oldFile.name.equals(newName, ignoreCase = true) && oldFile.name != newName) {
            val tempFile = File(parentDir, newName + "_temp")
            if (oldFile.renameTo(tempFile)) {
                if (tempFile.renameTo(targetFile)) return targetFile.absolutePath
            }
        } else {
            if (!targetFile.exists() && oldFile.renameTo(targetFile)) {
                return targetFile.absolutePath
            }
        }
        return null
    }
}
