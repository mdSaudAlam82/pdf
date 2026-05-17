package com.edu.pdf.domain.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * 🌟 2026 Elite OCR Engine (Domain Layer)
 * Ye class Bitmap legi aur usme se text nikal kar degi.
 */
class TextRecognitionEngine @Inject constructor() {

    // 1. Model Initialize karna: Hum Devanagari (Hindi + English) wala model use kar rahe hain
    private val recognizer = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())

    /**
     * Suspend function - Ye background me run hoga aur UI ko block nahi karega.
     * @param bitmap PDF page ki image
     * @return Result<String> jisme ya toh text hoga, ya error
     */
    suspend fun extractTextFromBitmap(bitmap: Bitmap): Result<String> = suspendCancellableCoroutine { continuation ->

        // 2. Android ke Bitmap ko ML Kit ke 'InputImage' format me convert karna
        val image = InputImage.fromBitmap(bitmap, 0)

        // 3. ML Kit ko image process karne ke liye dena
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Agar text mil gaya aur coroutine abhi active hai, toh result wapas bhej do
                if (continuation.isActive) {
                    continuation.resume(Result.success(visionText.text))
                }
            }
            .addOnFailureListener { exception ->
                // Agar koi error aayi (jaise image clear nahi hai), toh failure bhej do
                if (continuation.isActive) {
                    continuation.resume(Result.failure(exception))
                }
            }
    }
}