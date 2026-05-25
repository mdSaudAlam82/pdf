package com.edu.pdf.presentation.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ModernBulkMoveDialog(
    current: Int,
    total: Int,
    isConfirmingCancel: Boolean,
    onCancelRequest: () -> Unit,
    onCancelConfirm: () -> Unit,
    onCancelDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val targetProgress = if (total > 0) current.toFloat() / total else 0f
    
    // 🌟 THE 2026 LIQUID ENGINE: Precise linear interpolation for zero-jitter
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
        label = "LiquidProgress"
    )

    val currentPercentage = (animatedProgress * 100).toInt()
    var lastVibratedPercent by remember { mutableIntStateOf(0) }
    
    // 🌟 SYNCED TACTILE FEEDBACK
    LaunchedEffect(currentPercentage) {
        if (currentPercentage > lastVibratedPercent) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            lastVibratedPercent = currentPercentage
        }
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            // 🌟 ULTRA-COMPACT ELITE CARD (280dp Industry Standard)
            Card(
                modifier = Modifier.width(280.dp).wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Processing Files...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$current of $total",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🌟 SLIM-LINE PROGRESS (6dp Minimalist)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🌟 SEAMLESS NUMERICAL FLOW
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$currentPercentage",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            lineHeight = 32.sp
                        )
                        Text(
                            text = "%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🌟 MINIMALIST ACTION
                    TextButton(
                        onClick = onCancelRequest,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Stop Operation", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isConfirmingCancel) {
                AlertDialog(
                    onDismissRequest = onCancelDismiss,
                    title = { Text("Stop Now?", fontWeight = FontWeight.Bold) },
                    text = { Text("Progress will be lost. Continue?") },
                    confirmButton = {
                        Button(
                            onClick = onCancelConfirm,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) { Text("Stop", color = Color.White) }
                    },
                    dismissButton = {
                        TextButton(onClick = onCancelDismiss) { Text("Resume") }
                    },
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}
