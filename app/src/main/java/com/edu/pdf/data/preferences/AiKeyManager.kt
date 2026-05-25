@file:Suppress("DEPRECATION")

package com.edu.pdf.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiKeyManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "ai_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveKeys(primary: String, fallback1: String, fallback2: String) {
        sharedPreferences.edit().apply {
            putString("KEY_PRIMARY", primary.trim())
            putString("KEY_FALLBACK_1", fallback1.trim())
            putString("KEY_FALLBACK_2", fallback2.trim())
            apply()
        }
    }

    fun getPrimaryKey(): String? = sharedPreferences.getString("KEY_PRIMARY", null)
    fun getFallbackKey1(): String? = sharedPreferences.getString("KEY_FALLBACK_1", null)
    fun getFallbackKey2(): String? = sharedPreferences.getString("KEY_FALLBACK_2", null)

    fun getKeys(): List<String> {
        val keys = mutableListOf<String>()
        getPrimaryKey()?.takeIf { it.isNotEmpty() }?.let { keys.add(it) }
        getFallbackKey1()?.takeIf { it.isNotEmpty() }?.let { keys.add(it) }
        getFallbackKey2()?.takeIf { it.isNotEmpty() }?.let { keys.add(it) }
        return keys
    }
}
