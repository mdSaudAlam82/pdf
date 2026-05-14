package com.edu.pdf.presentation.home.components

import androidx.compose.runtime.Composable
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.presentation.common.PremiumFolderListItem

@Composable
fun HomeFolderListItem(
    folder: Folder,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onMoreOptionsClick: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    // 🌟 100% DRY Principle: Purana ganda UI hata diya, ab seedha Premium Universal Folder use ho raha hai
    PremiumFolderListItem(
        name = folder.name,
        itemCount = folder.pdfCount,
        isSelectionMode = isSelectionMode,
        isSelected = isSelected,
        showMoreOptions = true, // Home screen par 3 dots chahiye
        onClick = onClick,
        onLongClick = onLongClick,
        onMoreOptionsClick = onMoreOptionsClick
    )
}