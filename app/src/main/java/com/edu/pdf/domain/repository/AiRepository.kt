package com.edu.pdf.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    fun chatWithPdfStream(query: String, pageBitmap: Bitmap?): Flow<String>
}