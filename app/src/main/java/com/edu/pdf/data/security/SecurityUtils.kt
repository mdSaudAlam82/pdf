package com.edu.pdf.data.security

import android.content.Context
import java.io.File

object SecurityUtils {
    // Ye function app start hone par aur vault band hone par temporary decrypted files ko delete karega
    fun wipeVaultTempStorage(context: Context) {
        val tempDir = File(context.cacheDir, "vault_temp_view")
        if (tempDir.exists()) {
            tempDir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }
}