package com.edu.pdf.domain.ocr

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

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
                    val extractedLines = mutableListOf<OcrTextBlock>()

                    for (block in visionText.textBlocks) {
                        for (line in block.lines) {
                            extractedLines.add(
                                OcrTextBlock(
                                    text = line.text,
                                    boundingBox = line.boundingBox,
                                    lineCount = 1
                                )
                            )
                        }
                    }

                    continuation.resume(Result.success(extractedLines))
                }
            }
            .addOnFailureListener { exception ->
                if (continuation.isActive) {
                    continuation.resume(Result.failure(exception))
                }
            }

        continuation.invokeOnCancellation {
        }
    }
}