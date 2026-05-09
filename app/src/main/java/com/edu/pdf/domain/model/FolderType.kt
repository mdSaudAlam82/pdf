package com.edu.pdf.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * 🌟 ELITE ARCHITECTURE:
 * Ye batayega ki Universal Screen ko konsa data load karna hai aur
 * konse actions (rename/delete) allow karne hain.
 */
@Keep // 🌟 PRO FIX: Naam change hone se bachayega
@Serializable // 🌟 PRO FIX: Compose Navigation crash hone se bachayega
enum class FolderType {
    PHYSICAL_DEVICE, // Jaise Android ka Download folder
    VIRTUAL_HUB,     // App ke andar banaya gaya folder
    SECURE_VAULT     // Private Vault
}