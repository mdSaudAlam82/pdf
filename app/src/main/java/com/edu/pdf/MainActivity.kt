package com.edu.pdf

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.data.security.SecurityUtils
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
import com.edu.pdf.notification.PdfNotificationHelper
import com.edu.pdf.presentation.core.MainAppScreen
import com.edu.pdf.presentation.common.GlobalProgressViewModel
import com.edu.pdf.presentation.common.GlobalProgressEvent
import com.edu.pdf.presentation.common.ModernBulkMoveDialog
import com.edu.pdf.ui.theme.PdfTheme
import com.edu.pdf.worker.PdfDetectionWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var scanPdfsUseCase: ScanPdfsUseCase
    @Inject lateinit var notificationHelper: PdfNotificationHelper
    @Inject lateinit var repository: PdfRepository
    @Inject lateinit var userPreferences: UserPreferences

    private val progressViewModel: GlobalProgressViewModel by viewModels()
    private var externalPdfUri by mutableStateOf<String?>(null)
    private var autoNavigatePath by mutableStateOf<String?>(null)

    private val notificationPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Notifications disabled.", Toast.LENGTH_SHORT).show()
            }
        }

    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            lifecycleScope.launch(Dispatchers.IO) {
                val isLocked = userPreferences.isSyncLockedFlow.first()
                if (!isLocked) {
                    scanPdfsUseCase()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        SecurityUtils.wipeVaultTempStorage(this)
        enableEdgeToEdge()
        checkNotificationPermission()
        PdfDetectionWorker.enqueue(this)

        contentResolver.registerContentObserver(
            MediaStore.Files.getContentUri("external"),
            true,
            contentObserver
        )

        externalPdfUri = handleIncomingIntent(intent)

        // 🌟 LISTEN FOR MOVE COMPLETION
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                progressViewModel.events.collect { event ->
                    if (event is GlobalProgressEvent.OperationFinished) {
                        autoNavigatePath = event.targetPath
                        // 🌟 FINISH SYNC
                        scanPdfsUseCase()
                    }
                }
            }
        }

        setContent {
            PdfTheme {
                val progressState by progressViewModel.uiState.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainAppScreen(
                            externalPdfUri = externalPdfUri,
                            onPdfOpened = { externalPdfUri = null },
                            autoNavigatePath = autoNavigatePath,
                            onNavigateConsumed = { autoNavigatePath = null }
                        )

                        if (progressState.isVisible) {
                            ModernBulkMoveDialog(
                                current = progressState.current,
                                total = progressState.total,
                                isConfirmingCancel = progressState.isConfirmingCancel,
                                onCancelRequest = { progressViewModel.requestCancel() },
                                onCancelConfirm = { progressViewModel.confirmCancel() },
                                onCancelDismiss = { progressViewModel.dismissCancel() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        externalPdfUri = handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?): String? {
        if (intent == null) return null
        return when (intent.action) {
            Intent.ACTION_VIEW -> intent.data?.toString()
            else -> intent.getStringExtra("pdf_to_open")
        }
    }

    private fun checkNotificationPermission() {
        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
    }
}
