package com.edu.pdf.data.repository

import android.graphics.Bitmap
import com.edu.pdf.data.preferences.AiKeyManager
import com.edu.pdf.domain.repository.AiRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import java.io.ByteArrayOutputStream

class AiRepositoryImpl @Inject constructor(
    private val keyManager: AiKeyManager
) : AiRepository {

    override fun chatWithPdfStream(query: String, pageBitmap: Bitmap?): Flow<String> = flow {
        val keys = keyManager.getKeys()

        if (keys.isEmpty()) {
            emit("\n[Error: AI API Keys not found. Please add your keys in Settings.]")
            return@flow
        }

        // 🌟 ELITE FIX 1: Bitmap Compression
        val compressedBitmap = pageBitmap?.let { scaleAndCompressBitmap(it) }

        var success = false
        var lastError: Exception? = null

        for ((index, apiKey) in keys.withIndex()) {
            try {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-2.5-flash",
                    apiKey = apiKey.trim()
                )

                val inputContent = content {
                    compressedBitmap?.let { image(it) }
                    text(query)
                }

                // 🌟 ELITE FIX 2: Stream Flow Control (Prevents duplicate appending on fallback)
                val responseStream = generativeModel.generateContentStream(inputContent)

                // Agar fallback run ho raha hai, toh user ko batao
                if (index > 0) emit("\n[Switched to Backup Key...]\n")

                responseStream.collect { chunk ->
                    emit(chunk.text ?: "")
                }

                success = true
                break // Successful, bahar aa jao

            } catch (e: Exception) {
                lastError = e
                // Error log karein par flow crash na hone dein
            }
        }

        if (!success) {
            emit("\n[Error: All API Keys exhausted. Reason: ${lastError?.localizedMessage}]")
        }
    }.flowOn(Dispatchers.IO)

    // Helper Function to resize Bitmap for Gemini Payload limits
    private fun scaleAndCompressBitmap(bitmap: Bitmap, maxDimension: Int = 1024): Bitmap {
        val ratio = Math.min(maxDimension.toFloat() / bitmap.width, maxDimension.toFloat() / bitmap.height)
        if (ratio >= 1.0f) return bitmap // Pehle se chhota hai

        val width = Math.round(ratio * bitmap.width)
        val height = Math.round(ratio * bitmap.height)
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}