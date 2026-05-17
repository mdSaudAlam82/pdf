package com.edu.pdf.data.security

import android.content.Context
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.streamingaead.StreamingAeadConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@Suppress("DEPRECATION")
class VaultCryptoEngine @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    init {
        StreamingAeadConfig.register()
    }

    // 🌟 2026 FIX: 'by lazy' ka matlab hai jab tak zaroorat nahi hogi, ye load nahi hoga.
    // Isse Main UI Thread kabhi freeze nahi hoga!
    private val keysetHandle: KeysetHandle by lazy {
        AndroidKeysetManager.Builder()
            .withSharedPref(context, "vault_keys", "vault_prefs")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM_HKDF_4KB")) // Upgraded to 256-bit
            .withMasterKeyUri("android-keystore://vault_master_key")
            .build()
            .keysetHandle
    }

    private val streamingAead: StreamingAead by lazy {
        keysetHandle.getPrimitive(StreamingAead::class.java)
    }

    private val aad = "pdf_pro_vault_secure_data_v1".toByteArray()

    fun getEncryptedOutputStream(destinationFile: File): OutputStream {
        return streamingAead.newEncryptingStream(destinationFile.outputStream(), aad)
    }

    fun getEncryptedInputStream(encryptedFile: File): InputStream {
        return streamingAead.newDecryptingStream(encryptedFile.inputStream(), aad)
    }

}