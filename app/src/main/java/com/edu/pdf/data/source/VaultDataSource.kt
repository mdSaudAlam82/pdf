package com.edu.pdf.data.source

import android.content.Context
import android.net.Uri
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume

// 🌟 2026 ELITE ARCHITECTURE: 100% Dedicated Security Data Source
class VaultDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context, // 🌟 NAYA: Added @param:
    private val cryptoEngine: VaultCryptoEngine
) {
    // 🌟 Private helper to get public folder
    private fun getPdfProRootFolder(): String {
        val docsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOCUMENTS)
        val pdfProDir = File(docsDir, "PdfPro")
        if (!pdfProDir.exists()) pdfProDir.mkdirs()
        return pdfProDir.absolutePath
    }

    fun getTrueInternalVaultDir(): File {
        val vaultDir = File(context.filesDir, "secure_vault_core")
        if (!vaultDir.exists()) vaultDir.mkdirs()
        return vaultDir
    }

    fun getSecureVaultStreamUri(encryptedPath: String): String {
        val encodedPath = Uri.encode(encryptedPath)
        return "content://${context.packageName}.vault.streamer/stream?path=$encodedPath"
    }

    suspend fun moveToInternalVault(pdf: PdfFile, onSyncNeeded: suspend (String) -> Unit): String? = withContext(Dispatchers.IO) {
        val sourceFile = File(pdf.path)
        if (!sourceFile.exists()) return@withContext null

        val vaultDir = getTrueInternalVaultDir()
        val secureFileName = "${java.util.UUID.randomUUID()}.locked"
        val destFile = File(vaultDir, secureFileName)

        return@withContext try {
            sourceFile.inputStream().use { input ->
                cryptoEngine.getEncryptedOutputStream(destFile).use { output ->
                    cryptoEngine.secureCopy(input, output)
                }
            }
            if (sourceFile.delete()) {
                onSyncNeeded(sourceFile.absolutePath) // 🌟 Repository ko bolenge ki sync karwa de
                destFile.absolutePath
            } else {
                destFile.delete()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (destFile.exists()) destFile.delete()
            null
        }
    }

    suspend fun restoreFromInternalVault(pdfPath: String, originalName: String): Pair<String, String>? =
        withContext(Dispatchers.IO) {
            val lockedFile = File(pdfPath)
            if (!lockedFile.exists()) return@withContext null

            val publicFolder = File(getPdfProRootFolder())
            var destFile = File(publicFolder, originalName)

            var counter = 1
            while (destFile.exists()) {
                val nameWithoutExt = destFile.nameWithoutExtension
                val ext = destFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
                destFile = File(publicFolder, "$nameWithoutExt ($counter)$ext")
                counter++
            }

            return@withContext try {
                cryptoEngine.getEncryptedInputStream(lockedFile).use { input ->
                    destFile.outputStream().use { output ->
                        cryptoEngine.secureCopy(input, output)
                    }
                }

                if (lockedFile.delete()) {
                    val realUri = suspendCancellableCoroutine<Uri?> { cont ->
                        android.media.MediaScannerConnection.scanFile(context, arrayOf(destFile.absolutePath), arrayOf("application/pdf")) { _, scannedUri ->
                            cont.resume(scannedUri)
                        }
                    }
                    val finalId = realUri?.toString() ?: destFile.absolutePath
                    Pair(finalId, destFile.absolutePath)
                } else {
                    destFile.delete()
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (destFile.exists()) destFile.delete()
                null
            }
        }
}