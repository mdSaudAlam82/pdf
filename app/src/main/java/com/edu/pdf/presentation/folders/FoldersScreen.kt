package com.edu.pdf.presentation.folders

import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.UniversalTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
    navController: NavHostController, // 🌟 NAYA
    isTablet: Boolean, // 🌟 NAYA
    onFolderClick: (String, String) -> Unit,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val folders = uiState.deviceFolders

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val haptic = LocalHapticFeedback.current
    val activity = androidx.activity.compose.LocalActivity.current as? FragmentActivity

    val biometricPrompt = remember(activity) {
        if (activity != null) {
            val executor = ContextCompat.getMainExecutor(activity)
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                    onFolderClick("vault_root", "Private Vault")
                }
            })
        } else null
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Private Vault")
            .setSubtitle("Use your fingerprint or device PIN")
            .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    Scaffold(
        topBar = { UniversalTopBar(title = "Device Folders", scrollBehavior = scrollBehavior) },
        bottomBar = { if (!isTablet) PremiumBottomBar(navController) }, // 🌟 RESTORED
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            PremiumVaultCard(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    if (biometricPrompt != null) {
                        biometricPrompt.authenticate(promptInfo)
                    } else {
                        onFolderClick("vault_root", "Private Vault")
                    }
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(folders) { folder ->
                    PhysicalFolderItem(
                        folderName = folder.name,
                        pdfCount = folder.pdfCount,
                        onClick = { onFolderClick(folder.absolutePath, folder.name) }
                    )
                }
            }
        }
    }
}
@Composable
fun PremiumVaultCard(onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (pressed) 0.96f else 1f, label = "scale")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .pointerInput(Unit) {
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(requireUnconsumed = false)
                        waitForUpOrCancellation()
                        onClick()
                    }
                }
            },
        shadowElevation = 4.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.error
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Vault", tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Private Vault",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Military-grade secure storage",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

data class SmartFolderConfig(val icon: ImageVector, val color: Color)

@Composable
fun rememberSmartFolderConfig(folderName: String): SmartFolderConfig {
    val defaultFolderColor = MaterialTheme.colorScheme.tertiary

    return remember(folderName) {
        val name = folderName.lowercase()
        when {
            name.contains("whatsapp") -> SmartFolderConfig(Icons.Rounded.ChatBubbleOutline, Color(0xFF25D366))
            name.contains("telegram") -> SmartFolderConfig(Icons.AutoMirrored.Rounded.Send, Color(0xFF0088CC))
            name.contains("download") -> SmartFolderConfig(Icons.Rounded.Download, Color(0xFF2196F3))
            name.contains("dcim") || name.contains("camera") || name.contains("picture") || name.contains("screenshot") ->
                SmartFolderConfig(Icons.Rounded.PhotoCamera, Color(0xFFE91E63))
            name.contains("document") -> SmartFolderConfig(Icons.Rounded.Description, Color(0xFFFF9800))
            name.contains("bluetooth") || name.contains("share") -> SmartFolderConfig(Icons.Rounded.Bluetooth, Color(0xFF3F51B5))
            name.contains("movie") || name.contains("video") -> SmartFolderConfig(Icons.Rounded.Movie, Color(0xFFF44336))
            else -> SmartFolderConfig(Icons.Rounded.Folder, defaultFolderColor)
        }
    }
}

@Composable
fun PhysicalFolderItem(folderName: String, pdfCount: Int, onClick: () -> Unit) {
    val smartConfig = rememberSmartFolderConfig(folderName)

    com.edu.pdf.presentation.common.PremiumFolderListItem(
        name = folderName,
        itemCount = pdfCount,
        icon = smartConfig.icon,
        iconTint = smartConfig.color,
        showMoreOptions = false,
        onClick = onClick
    )
}
