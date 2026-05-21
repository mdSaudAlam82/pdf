package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

/**
 * 🌟 MASHINE 3: ToggleFavoriteUseCase
 * Iska kaam hai sirf Favorite status (Dil wala icon) ko badalna.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(pdfId: String, isFavorite: Boolean) {
        repository.toggleFavorite(pdfId, isFavorite)
    }
}
