package com.edu.pdf.domain.ocr

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

// 🌟 DEFAULT: Sirf poora Block (Paragraph) lenge, aur ye check karenge usme kitni lines hain
data class OcrTextBlock(
    val text: String,
    val boundingBox: Rect?,
    val lineCount: Int
)

class TextRecognitionEngine @Inject constructor() {

    private val recognizer = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())

    suspend fun extractTextFromBitmap(bitmap: Bitmap): Result<List<OcrTextBlock>> = suspendCancellableCoroutine { continuation ->

        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                if (continuation.isActive) {
                    val extractedBlocks = mutableListOf<OcrTextBlock>()

                    // 🌟 RAW ML KIT DEFAULT: Google automatically columns ko alag Blocks me deta hai
                    for (block in visionText.textBlocks) {
                        extractedBlocks.add(
                            OcrTextBlock(
                                text = block.text, // Isme already saari lines sahi order me hoti hain (\n ke sath)
                                boundingBox = block.boundingBox,
                                lineCount = block.lines.size // Font size nikalne ke liye
                            )
                        )
                    }
                    continuation.resume(Result.success(extractedBlocks))
                }
            }
            .addOnFailureListener { exception ->
                if (continuation.isActive) {
                    continuation.resume(Result.failure(exception))
                }
            }
    }
}