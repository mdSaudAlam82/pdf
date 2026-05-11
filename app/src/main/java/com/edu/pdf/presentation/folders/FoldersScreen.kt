package com.edu.pdf.presentation.folders

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.presentation.common.UniversalTopBar

// 🌟 PRO FIX: Context se Activity nikalne ka 100% safe tarika
fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
    onFolderClick: (String, String) -> Unit,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    // 🌟 EXACT FIX: Yahan 'viewModel.deviceFolders' ki jagah 'viewModel.uiState' se data nikalenge
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val folders = uiState.deviceFolders

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val activity = context.getActivity() as? FragmentActivity

    val biometricPrompt = remember(activity) {
        if (activity != null) {
            val executor = ContextCompat.getMainExecutor(activity)
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(activity, "Vault Locked: $errString", Toast.LENGTH_SHORT).show()
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                    onFolderClick("vault_root", "Private Vault")
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast.makeText(activity, "Fingerprint not recognized", Toast.LENGTH_SHORT).show()
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
        containerColor = MaterialTheme.colorScheme.background
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
            Text(
                text = "Physical Storage",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // 🌟 FIX: Loading state add kar diya taaki crash na ho
                if (uiState.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(folders, key = { it.absolutePath }) { folder ->
                        PhysicalFolderItem(
                            folderName = folder.name,
                            pdfCount = folder.pdfCount,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onFolderClick(folder.absolutePath, folder.name)
                            }
                        )
                    }
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
            name.contains("whatsapp") -> SmartFolderConfig(Icons.Rounded.ChatBubbleOutline, Color(0xFF25D366)) // WhatsApp Green
            name.contains("telegram") -> SmartFolderConfig(Icons.AutoMirrored.Rounded.Send, Color(0xFF0088CC)) // Telegram Blue
            name.contains("download") -> SmartFolderConfig(Icons.Rounded.Download, Color(0xFF2196F3)) // System Blue
            name.contains("dcim") || name.contains("camera") || name.contains("picture") || name.contains("screenshot") ->
                SmartFolderConfig(Icons.Rounded.PhotoCamera, Color(0xFFE91E63)) // Gallery Pink/Purple
            name.contains("document") -> SmartFolderConfig(Icons.Rounded.Description, Color(0xFFFF9800)) // Docs Orange
            name.contains("bluetooth") || name.contains("share") -> SmartFolderConfig(Icons.Rounded.Bluetooth, Color(0xFF3F51B5)) // Bluetooth Indigo
            name.contains("movie") || name.contains("video") -> SmartFolderConfig(Icons.Rounded.Movie, Color(0xFFF44336)) // Video Red
            else -> SmartFolderConfig(Icons.Rounded.Folder, defaultFolderColor) // 🌟 Standard Free-Form Theme Color
        }
    }
}
@Composable
fun PhysicalFolderItem(folderName: String, pdfCount: Int, onClick: () -> Unit) {
    // 🌟 Fetching the smart contextual design based on the folder's name
    val smartConfig = rememberSmartFolderConfig(folderName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp)) // 2026 standard squircle
                .background(smartConfig.color.copy(alpha = 0.15f)), // 15% opacity tint for premium glass look
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = smartConfig.icon,
                contentDescription = folderName,
                tint = smartConfig.color,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = folderName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$pdfCount PDFs",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}