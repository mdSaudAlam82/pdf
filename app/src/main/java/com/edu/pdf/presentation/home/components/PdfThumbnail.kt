package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.edu.pdf.domain.model.PdfFile

@Composable
fun PdfThumbnail(
    pdf: PdfFile,
    modifier: Modifier = Modifier
) {
    var isLocked by remember { mutableStateOf(false) }
    var isCorrupted by remember { mutableStateOf(false) }

    Box(modifier = modifier.background(Color(0xFFF0F0F0)), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pdf)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            onState = { state ->
                if (state is AsyncImagePainter.State.Error) {
                    val errorMsg = state.result.throwable.message
                    // 🌟 VAULT COMPLETELY REMOVED!
                    // Ab lock icon sirf tab aayega jab actual file me password laga ho.
                    if (errorMsg == "PDF_IS_LOCKED") {
                        isLocked = true
                    } else {
                        isCorrupted = true
                    }
                }
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // 🌟 SMART UI LOGIC
        if (isLocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Protected PDF",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        } else if (isCorrupted) {
            Icon(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = "Corrupted PDF",
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}