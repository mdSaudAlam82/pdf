package com.edu.pdf

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.edu.pdf.data.security.SecurityUtils
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
import com.edu.pdf.notification.PdfNotificationHelper
import com.edu.pdf.presentation.core.MainAppScreen
import com.edu.pdf.ui.theme.PdfTheme
import com.edu.pdf.worker.PdfDetectionWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var scanPdfsUseCase: ScanPdfsUseCase
    @Inject lateinit var notificationHelper: PdfNotificationHelper
    @Inject lateinit var repository: PdfRepository

    // 🌟 ELITE FIX: External PDF Uri ko hold karne ke liye state variable
    private var externalPdfUri by mutableStateOf<String?>(null)

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            PdfDetectionWorker.enqueue(this)
        }
    }

    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            this@MainActivity.lifecycleScope.launch(Dispatchers.IO) {
                if (uri != null) {
                    try {
                        contentResolver.query(
                            uri,
                            arrayOf(
                                MediaStore.Files.FileColumns.DISPLAY_NAME,
                                MediaStore.Files.FileColumns.DATA,
                                MediaStore.Files.FileColumns.SIZE,       // 🌟 NAYA: File ka size
                                MediaStore.Files.FileColumns.DATE_ADDED  // 🌟 NAYA: File kab bani
                            ),
                            null, null, null
                        )?.use { cursor ->
                            if (cursor.moveToFirst()) {
                                val name = cursor.getString(0) ?: "New Document"
                                val path = cursor.getString(1) ?: ""
                                val size = cursor.getLong(2)
                                val dateAdded = cursor.getLong(3) // Seconds me hota hai

                                val nowSeconds = System.currentTimeMillis() / 1000

                                // 🌟 ELITE FIX 2:
                                // 1. Name must end with .pdf
                                // 2. Size > 0 hona chahiye (Downloading/Khali file ignore hogi)
                                // 3. File pichle 60 seconds me add hui ho (Rename ki hui purani files ignore hongi)
                                if (name.endsWith(".pdf", ignoreCase = true) && size > 0 && (nowSeconds - dateAdded) < 60) {
                                    notificationHelper.showNewPdfNotification(name, path)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                scanPdfsUseCase()
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

        // 🌟 ELITE FIX: App khulne par check karo ki kya koi external PDF aaya hai
        externalPdfUri = handleIncomingIntent(intent)

        setContent {
            PdfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass the external PDF state to MainAppScreen
                    MainAppScreen(
                        externalPdfUri = externalPdfUri,
                        onPdfOpened = { externalPdfUri = null } // Reset state after opening
                    )
                }
            }
        }
    }

    // 🌟 ELITE FIX: Agar app pehle se background me khula ho aur naya PDF click kiya jaye
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        externalPdfUri = handleIncomingIntent(intent)
    }

    // Helper function intent data ko filter karne ke liye
    private fun handleIncomingIntent(intent: Intent?): String? {
        if (intent == null) return null

        // 1. Notification se aaya hua PDF check karo
        val notificationPath = intent.getStringExtra("pdf_to_open")
        if (!notificationPath.isNullOrBlank()) return notificationPath

        // 2. WhatsApp, File Manager, Chrome se aaya hua PDF check karo
        if (intent.action == Intent.ACTION_VIEW && intent.type == "application/pdf") {
            return intent.data?.toString()
        }

        return null
    }

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
    }
}