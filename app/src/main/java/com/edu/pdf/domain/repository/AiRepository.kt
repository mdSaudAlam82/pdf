package com.edu.pdf.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    // Ye function user ka text aur current page ki photo dono accept karega
    fun chatWithPdfStream(query: String, pageBitmap: Bitmap?): Flow<String>
}