// FILE: VaultDataSource.kt
package com.edu.pdf.data.source

import android.content.Context
import android.os.Environment
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class VaultDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val cryptoEngine: VaultCryptoEngine // 🌟 100% Tink Crypto Engine
) {

    // =======================================================
    // 🔒 1. MOVE TO VAULT (Encrypt with Tink)
    // =======================================================
    suspend fun moveToInternalVault(pdf: PdfFile, onOriginalDelete: suspend (String) -> Unit): String? = withContext(Dispatchers.IO) {
        val originalFile = File(pdf.path)
        if (!originalFile.exists()) return@withContext null

        val vaultDir = File(context.filesDir, "secure_vault_core")
        if (!vaultDir.exists()) vaultDir.mkdirs()

        // Storage Check
        // Storage Check (Modern Android Standard)
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as android.os.storage.StorageManager
        val uuid = storageManager.getUuidForPath(vaultDir)
        if (storageManager.getAllocatableBytes(uuid) < originalFile.length() + (50 * 1024 * 1024)) {
            throw Exception("Storage Full! Cannot secure this file.")
        }

        val finalSecureFile = File(vaultDir, "${UUID.randomUUID()}.locked")
        val tmpSecureFile = File(vaultDir, "${finalSecureFile.name}.tmp")

        return@withContext try {
            // 🌟 NAYA: Tink se Encrypt karke likhna
            originalFile.inputStream().use { input ->
                cryptoEngine.getEncryptedOutputStream(tmpSecureFile).use { encryptedOutput ->
                    input.copyTo(encryptedOutput)
                }
            }

            if (tmpSecureFile.renameTo(finalSecureFile)) {
                onOriginalDelete(originalFile.absolutePath)

                // 🌟 ELITE SECURITY FIX: चेक करो कि फाइल सच में डिलीट हुई या नहीं!
                val isDeleted = originalFile.delete()
                if (!isDeleted && originalFile.exists()) {
                    // अगर OS ने डिलीट करने से रोक दिया, तो Vault वाली कॉपी भी डिलीट कर दो (Rollback)
                    finalSecureFile.delete()
                    throw Exception("OS prevented deletion! File is still public.")
                }

                finalSecureFile.absolutePath
            } else {
                tmpSecureFile.delete()
                null
            }
        } catch (e: Exception) {
            tmpSecureFile.delete()
            throw e // Error UI तक भेजो
        }
    }

    // =======================================================
    // 🔓 2. RESTORE FROM VAULT (Decrypt with Tink)
    // =======================================================
    suspend fun restoreFromInternalVault(securePath: String, originalName: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        val secureFile = File(securePath)
        if (!secureFile.exists()) return@withContext null

        val docsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val pdfProDir = File(docsDir, "PdfPro")
        if (!pdfProDir.exists()) pdfProDir.mkdirs()

        var targetFile = File(pdfProDir, originalName)
        var counter = 1
        val nameWithoutExt = targetFile.nameWithoutExtension
        val ext = targetFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }

        while (targetFile.exists()) {
            targetFile = File(pdfProDir, "$nameWithoutExt ($counter)$ext")
            counter++
        }

        val tmpTargetFile = File(pdfProDir, "${targetFile.name}.tmp")

        return@withContext try {
            // 🌟 NAYA: Tink se Decrypt karke wapas bahar nikalna
            cryptoEngine.getEncryptedInputStream(secureFile).use { decryptedInput ->
                tmpTargetFile.outputStream().use { output ->
                    decryptedInput.copyTo(output)
                }
            }

            if (tmpTargetFile.renameTo(targetFile)) {
                secureFile.delete()
                Pair(targetFile.absolutePath, targetFile.absolutePath)
            } else {
                tmpTargetFile.delete()
                null
            }
        } catch (_: Exception) {
            tmpTargetFile.delete()
            null
        }
    }

    // =======================================================
    // 👁️ 3. VIEW INSIDE VAULT (Stream to PDF Viewer)
    // =======================================================
    fun getSecureVaultStreamUri(encryptedPath: String): String {
        return encryptedPath
    }
}