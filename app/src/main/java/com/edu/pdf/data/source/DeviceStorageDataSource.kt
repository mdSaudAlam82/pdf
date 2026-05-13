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

    // 🌟 THE ELITE FIX: Physical Folder Creation
    // 🌟 THE ELITE FIX: No more auto-appending (1), (2). UI will show error now.
    suspend fun createPhysicalFolder(name: String, parentPath: String?): String? = withContext(Dispatchers.IO) {
        val root = parentPath ?: getPdfProRootFolder()
        val newFolder = File(root, name)

        // Agar folder pehle se hai, toh null return karo taaki UI ko error bheja ja sake
        if (newFolder.exists()) return@withContext null

        return@withContext if (newFolder.mkdirs()) {
            scanFilesBatch(arrayOf(newFolder.absolutePath))
            newFolder.absolutePath
        } else null
    }

    suspend fun movePhysicalFile(sourcePath: String, targetFolderPath: String): String? = withContext(Dispatchers.IO) {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) return@withContext null

        val targetDir = File(targetFolderPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        var targetFile = File(targetDir, sourceFile.name)

        val originalNameWithoutExt = sourceFile.nameWithoutExtension
        val originalExt = sourceFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
        var counter = 1

        while (targetFile.exists()) {
            targetFile = File(targetDir, "$originalNameWithoutExt ($counter)$originalExt")
            counter++
        }

        return@withContext try {
            // 🌟 1. FAST PATH: OS-Level Atomic Move (1 मिलीसेकंड में पूरा फोल्डर/फाइल मूव होगा, बिना कॉपी किए)
            if (sourceFile.renameTo(targetFile)) {
                syncWithMediaStore(sourcePath, targetFile.absolutePath)
                return@withContext targetFile.absolutePath
            }

            // 🌟 2. FALLBACK PATH: अगर 'renameTo' फेल होता है (जैसे Internal Storage से SD Card में मूव करते वक़्त)
            if (sourceFile.isDirectory) {
                // 👉 यहाँ क्रैश होता था! अब हम फोल्डर के अंदर जाकर Recursive Copy करेंगे
                if (sourceFile.copyRecursively(targetFile, overwrite = true)) {
                    sourceFile.deleteRecursively() // कॉपी सफल होने पर ही पुराना उड़ाएंगे
                    syncWithMediaStore(sourcePath, targetFile.absolutePath)
                    targetFile.absolutePath
                } else {
                    targetFile.deleteRecursively() // Rollback (अगर फेल हुआ तो आधा-अधूरा काम हटा देंगे)
                    null
                }
            } else {
                // PDF Stream Copy
                sourceFile.inputStream().use { input ->
                    targetFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                if (targetFile.length() == sourceFile.length()) {
                    // 🌟 ELITE FIX: Android 11+ Safety! Agar purani file sach me delete nahi ho payi, toh duplication roko.
                    val isDeleted = sourceFile.delete()
                    if (isDeleted) {
                        syncWithMediaStore(sourcePath, targetFile.absolutePath)
                        return@withContext targetFile.absolutePath
                    } else {
                        // Purani file delete nahi hui, isliye nayi copy ko bhi uda do (Rollback)
                        targetFile.delete()
                        return@withContext null
                    }
                } else {
                    targetFile.delete() // Rollback due to size mismatch
                    return@withContext null
                }
            }
        } catch (_: Exception) {
            // कोई भी एरर आने पर कचरा साफ़ करेंगे (Rollback)
            if (sourceFile.isDirectory) targetFile.deleteRecursively() else targetFile.delete()
            null
        }
    }

    // 🌟 THE ELITE FIX: Deep Recursive Delete
    suspend fun deletePhysicalPath(path: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(path)
        if (!file.exists()) return@withContext true // Already deleted
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

    // 🌟 THE ELITE FIX: Time-Trap Removed for File Manager Renames
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

        // 🌟 FIX: DATE_MODIFIED wali condition hata di gayi hai.
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

                // Vault aur Trash ko ignore karo
                if (path.contains("/secure_vault_core/") || path.contains("/.trash/")) continue

                val file = File(path)
                if (!file.exists()) continue // MediaStore agar purani delete hui file dikhaye toh bacha lo

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

    // 🌟 THE ELITE FIX: Ab ye normal Path aur MediaStore URI dono ko samajh payega
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
                successfullyTrashedIds.add(pdf.id) // DB Cleanup
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
                    // 🌟 NAYA: Agar Vault me import ho raha hai, toh seedha Encrypt karo!
                    cryptoEngine.getEncryptedOutputStream(targetFile).use { encryptedOutput ->
                        inputStream.copyTo(encryptedOutput)
                    }
                } else {
                    // 🌟 NORMAL: Agar normal folder me import ho raha hai, toh normal copy karo
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
                    virtualParentId = targetFile.parentFile?.absolutePath // 🌟 Link to correct physical folder!
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
            // Case-only rename trick (Maths -> temp -> maths)
            val tempFile = File(parentDir, newName + "_temp")
            if (oldFile.renameTo(tempFile)) {
                if (tempFile.renameTo(targetFile)) return targetFile.absolutePath
            }
        } else {
            // Normal Rename (Collisions check implicitly handled by renameTo failing if exists)
            if (!targetFile.exists() && oldFile.renameTo(targetFile)) {
                return targetFile.absolutePath
            }
        }
        return null
    }

}