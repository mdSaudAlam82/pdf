package com.edu.pdf.domain.usecase

import com.edu.pdf.data.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 🌟 MASHINE 9: UpdateUserPreferencesUseCase
 * Iska kaam hai app ki settings (Grid/List View) ko save karna.
 */
class UpdateUserPreferencesUseCase @Inject constructor(
    val userPreferences: UserPreferences
) {
    suspend fun toggleGridView() {
        // 🌟 UNIFIED FIX: Ab sirf ek hi switch hoga jo puri app ko control karega
        val current = userPreferences.isGridViewFlow.first()
        userPreferences.saveGridViewPreference(!current)
    }
}
