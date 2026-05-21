# PROJECT CONTEXT SUMMARY

## 1. FOLDER STRUCTURE
``
Folder PATH listing
Volume serial number is DC19-0CB1
C:\USERS\SAUD\ANDROIDSTUDIOPROJECTS\PDF\APP\SRC\MAIN
|   AndroidManifest.xml
|   
+---java
|   \---com
|       \---edu
|           \---pdf
|               |   MainActivity.kt
|               |   PdfApplication.kt
|               |   
|               +---data
|               |   +---coil
|               |   |       PdfThumbnailFetcher.kt
|               |   |       
|               |   +---local
|               |   |   |   PdfDatabase.kt
|               |   |   |   
|               |   |   +---dao
|               |   |   |       PdfDao.kt
|               |   |   |       SearchHistoryDao.kt
|               |   |   |       
|               |   |   \---entity
|               |   |           FolderEntity.kt
|               |   |           PdfEntity.kt
|               |   |           PdfFtsEntity.kt
|               |   |           SearchHistoryEntity.kt
|               |   |           
|               |   +---mapper
|               |   |       PdfMapper.kt
|               |   |       
|               |   +---preferences
|               |   |       AiKeyManager.kt
|               |   |       UserPreferences.kt
|               |   |       
|               |   +---repository
|               |   |       AiRepositoryImpl.kt
|               |   |       PdfRepositoryImpl.kt
|               |   |       
|               |   +---security
|               |   |       SecurityUtils.kt
|               |   |       VaultCryptoEngine.kt
|               |   |       
|               |   \---source
|               |           DeviceStorageDataSource.kt
|               |           VaultDataSource.kt
|               |           
|               +---di
|               |       DatabaseModule.kt
|               |       RepositoryModule.kt
|               |       UseCaseModule.kt
|               |       
|               +---domain
|               |   +---model
|               |   |       ChatMessage.kt
|               |   |       Folder.kt
|               |   |       FolderType.kt
|               |   |       HomeItem.kt
|               |   |       PdfFile.kt
|               |   |       SortType.kt
|               |   |       
|               |   +---ocr
|               |   |       TextRecognitionEngine.kt
|               |   |       
|               |   +---repository
|               |   |       AiRepository.kt
|               |   |       PdfRepository.kt
|               |   |       
|               |   \---usecase
|               |           CreateFolderUseCase.kt
|               |           DeleteFolderUseCase.kt
|               |           DeletePdfsUseCase.kt
|               |           RenamePdfUseCase.kt
|               |           ScanPdfsUseCase.kt
|               |           
|               +---notification
|               |       PdfNotificationHelper.kt
|               |       
|               +---presentation
|               |   +---common
|               |   |   |   PremiumBottomBar.kt
|               |   |   |   PremiumBreadcrumbs.kt
|               |   |   |   PremiumFolderComponents.kt
|               |   |   |   SmartSelectionBottomBar.kt
|               |   |   |   UniversalTopBar.kt
|               |   |   |   
|               |   |   \---picker
|               |   |           GlobalPdfPickerSheet.kt
|               |   |           MovePickerSheet.kt
|               |   |           MovePickerState.kt
|               |   |           MovePickerViewModel.kt
|               |   |           PdfPickerViewModel.kt
|               |   |           
|               |   +---core
|               |   |       MainAppScreen.kt
|               |   |       PremiumPermissionScreen.kt
|               |   |       
|               |   +---folders
|               |   |   |   FoldersScreen.kt
|               |   |   |   FoldersViewModel.kt
|               |   |   |   UnifiedFolderOverlays.kt
|               |   |   |   UnifiedFolderScreen.kt
|               |   |   |   UnifiedFolderViewModel.kt
|               |   |   |   
|               |   |   +---components
|               |   |   |       FolderMenuSheet.kt
|               |   |   |       
|               |   |   \---vault
|               |   |           VaultScreen.kt
|               |   |           VaultViewModel.kt
|               |   |           
|               |   +---home
|               |   |   |   HomeOverlays.kt
|               |   |   |   HomeScreen.kt
|               |   |   |   HomeViewModel.kt
|               |   |   |   
|               |   |   \---components
|               |   |           EmptyStateView.kt
|               |   |           HomeContent.kt
|               |   |           HomeFolderGridItem.kt
|               |   |           HomeFolderListItem.kt
|               |   |           HomeTabs.kt
|               |   |           MoveFolderListItem.kt
|               |   |           PdfActionBottomSheet.kt
|               |   |           PdfGridItem.kt
|               |   |           PdfListItem.kt
|               |   |           PdfThumbnail.kt
|               |   |           SelectionTopBar.kt
|               |   |           SortBottomSheet.kt
|               |   |           
|               |   +---navigation
|               |   |       Screen.kt
|               |   |       
|               |   +---ocr
|               |   |       OcrComponents.kt
|               |   |       OcrContract.kt
|               |   |       OcrViewModel.kt
|               |   |       
|               |   +---pdfviewer
|               |   |   |   PdfViewerScreen.kt
|               |   |   |   PdfViewerViewModel.kt
|               |   |   |   
|               |   |   +---ai
|               |   |   |       AiChatBottomSheet.kt
|               |   |   |       AiChatContract.kt
|               |   |   |       AiChatViewModel.kt
|               |   |   |       
|               |   |   \---ocr
|               |   |           OcrContract.kt
|               |   |           OcrSelectionOverlay.kt
|               |   |           OcrViewModel.kt
|               |   |           
|               |   +---search
|               |   |   |   SearchScreen.kt
|               |   |   |   SearchViewModel.kt
|               |   |   |   
|               |   |   \---components
|               |   |           HighlightedText.kt
|               |   |           
|               |   \---settings
|               |           SettingsScreen.kt
|               |           SettingsViewModel.kt
|               |           
|               +---receiver
|               |       FileDetectionReceiver.kt
|               |       
|               +---ui
|               |   \---theme
|               |           Color.kt
|               |           Theme.kt
|               |           Type.kt
|               |           
|               \---worker
|                       PdfDetectionWorker.kt
|                       
\---res
    +---drawable
    |       empty_state_illustration.png
    |       ic_launcher_background.xml
    |       ic_launcher_foreground.xml
    |       premium_folder1.png
    |       
    +---mipmap-anydpi
    |       ic_launcher.xml
    |       ic_launcher_round.xml
    |       
    +---mipmap-hdpi
    |       ic_launcher.webp
    |       ic_launcher_round.webp
    |       
    +---mipmap-mdpi
    |       ic_launcher.webp
    |       ic_launcher_round.webp
    |       
    +---mipmap-xhdpi
    |       ic_launcher.webp
    |       ic_launcher_round.webp
    |       
    +---mipmap-xxhdpi
    |       ic_launcher.webp
    |       ic_launcher_round.webp
    |       
    +---mipmap-xxxhdpi
    |       ic_launcher.webp
    |       ic_launcher_round.webp
    |       
    +---raw
    |       empty_search.json
    |       
    +---values
    |       colors.xml
    |       strings.xml
    |       themes.xml
    |       
    \---xml
            backup_rules.xml
            data_extraction_rules.xml
            locales_config.xml
            
``n
## 2. ANDROID MANIFEST
FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\AndroidManifest.xml
``xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" tools:ignore="AllFilesAccessPolicy,ScopedStorage" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <application
        android:name=".PdfApplication"
        android:allowBackup="false"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:enableOnBackInvokedCallback="true"
        android:localeConfig="@xml/locales_config"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.App.Starting">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize" android:theme="@style/Theme.Material3.DayNight.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="application/pdf" />
            </intent-filter>
        </activity>
        <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="${applicationId}.androidx-startup"
            android:exported="false"
            tools:node="merge">
            <!-- 🌟 HILT FIX: Disable default initializer to use Hilt's custom configuration -->
            <meta-data
                android:name="androidx.work.WorkManagerInitializer"
                android:value="androidx.startup"
                tools:node="remove" />
        </provider>

        <receiver android:name=".receiver.FileDetectionReceiver" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.MEDIA_SCANNER_SCAN_FILE" />
                <data android:scheme="file" />
            </intent-filter>
        </receiver>
    </application>
</manifest>

``n
## 3. KOTLIN SOURCE CODE
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\MainActivity.kt
``kotlin
package com.edu.pdf

import android.Manifest
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Build
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
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.presentation.core.MainAppScreen
import com.edu.pdf.ui.theme.PdfTheme
import com.edu.pdf.worker.PdfDetectionWorker
import com.edu.pdf.notification.PdfNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var scanPdfsUseCase: ScanPdfsUseCase
    @Inject lateinit var notificationHelper: PdfNotificationHelper
    @Inject lateinit var repository: com.edu.pdf.domain.repository.PdfRepository

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            PdfDetectionWorker.enqueue(this)
        }
    }

    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            lifecycleScope.launch(Dispatchers.IO) {
                // 1. Scan the device
                scanPdfsUseCase()
                
                // 🌟 INSTANT NOTIFICATION: Jab app khula ho, turant check karo
                val latest = repository.getAllPdfs(SortType.DATE_DESC).first().firstOrNull()
                latest?.let { pdf ->
                    val ageSec = (System.currentTimeMillis() / 1000) - pdf.lastModified
                    if (ageSec < 10) { // Sirf wahi jo abhi-abhi aayi hai (Instant)
                        notificationHelper.showNewPdfNotification(pdf.name, pdf.path)
                    }
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

        // 🌟 DEEP LINK: Click handler will be in the Navigation system
        // But we catch the path here
        val pdfPathToOpen = intent.getStringExtra("pdf_to_open")

        setContent {
            PdfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppScreen()
                }
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\PdfApplication.kt
``kotlin
package com.edu.pdf

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.key.Keyer
import coil3.memory.MemoryCache
import coil3.util.DebugLogger
import com.edu.pdf.data.coil.PdfThumbnailFetcher
import com.edu.pdf.domain.model.PdfFile
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PdfApplication : Application(), SingletonImageLoader.Factory, Configuration.Provider {
    
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    override fun newImageLoader(context: android.content.Context): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(PdfThumbnailFetcher.Factory(context))
                add(Keyer<PdfFile> { pdf, _ -> pdf.id })
            }
            .logger(DebugLogger())
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("pdf_thumbnails_coil"))
                    .maxSizeBytes(100L * 1024 * 1024)
                    .build()
            }
            .build()
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\coil\PdfThumbnailFetcher.kt
``kotlin
package com.edu.pdf.data.coil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import coil3.size.Dimension
import com.edu.pdf.domain.model.PdfFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import java.io.File
import java.io.FileOutputStream

private val renderDispatcher: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(3)

class PdfThumbnailFetcher(
    private val context: Context,
    private val pdf: PdfFile,
    private val options: Options
) : Fetcher {

    override suspend fun fetch(): FetchResult? = withContext(renderDispatcher) {
        if (pdf.isVault) return@withContext null

        val file = File(pdf.path)
        if (!file.exists() || file.length() == 0L) return@withContext null

        val cacheFolder = File(context.cacheDir, "smart_pdf_thumbnails")
        if (!cacheFolder.exists()) cacheFolder.mkdirs()

        // 🌟 THE ELITE FIX: ID aur LastModified dono use karo, taaki PDF edit hone par Thumbnail turant update ho jaye!
        val uniqueId = java.util.UUID.nameUUIDFromBytes((pdf.id + pdf.lastModified).toByteArray()).toString()
        val thumbFileName = "$uniqueId.webp"
        val cachedThumbFile = File(cacheFolder, thumbFileName)


        if (cachedThumbFile.exists() && cachedThumbFile.length() > 500) {
            return@withContext SourceFetchResult(
                source = ImageSource(
                    file = cachedThumbFile.toOkioPath(),
                    fileSystem = FileSystem.SYSTEM
                ),
                mimeType = "image/webp",
                dataSource = DataSource.DISK
            )
        }
        ensureActive()


        val reqWidth = options.size.width
        val targetWidth = if (reqWidth is Dimension.Pixels && reqWidth.px > 0) reqWidth.px else 300
        val boundedWidth = targetWidth.coerceIn(150, 500)


        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        var page: PdfRenderer.Page? = null

        try {

            pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(pfd)

            if (renderer.pageCount <= 0) return@withContext null

            page = renderer.openPage(0)
            ensureActive()


            if (page.width <= 0 || page.height <= 0) {
                return@withContext null
            }

            val aspectRatio = page.height.toFloat() / page.width.toFloat()


            val height = (boundedWidth * aspectRatio).toInt().coerceAtLeast(1)


            // 🌟 ELITE FIX: Android OS से एक नया कागज़ (Bitmap) माँगो और उसे एकदम सफ़ेद (White) कर दो
            val bitmap = createBitmap(boundedWidth, height, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(Color.WHITE)

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)


            FileOutputStream(cachedThumbFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 85, out)
            }
            bitmap.recycle()

            return@withContext SourceFetchResult(
                source = ImageSource(
                    file = cachedThumbFile.toOkioPath(),
                    fileSystem = FileSystem.SYSTEM
                ),
                mimeType = "image/webp",
                dataSource = DataSource.DISK
            )
        } catch (_: SecurityException) {

            runCatching { if (cachedThumbFile.exists()) cachedThumbFile.delete() }
            throw SecurityException("PDF_IS_LOCKED")
        } catch (e: Exception) {
            e.printStackTrace()

            runCatching { if (cachedThumbFile.exists()) cachedThumbFile.delete() }
            throw e
        } finally {
            try {
                page?.close()
                renderer?.close()
                pfd?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class Factory(private val context: Context) : Fetcher.Factory<PdfFile> {
        override fun create(data: PdfFile, options: Options, imageLoader: ImageLoader): Fetcher {
            return PdfThumbnailFetcher(context, data, options)
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\local\PdfDatabase.kt
``kotlin
package com.edu.pdf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edu.pdf.data.local.dao.PdfDao
import com.edu.pdf.data.local.dao.SearchHistoryDao
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.PdfEntity
import com.edu.pdf.data.local.entity.PdfFtsEntity
import com.edu.pdf.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        PdfEntity::class,
        SearchHistoryEntity::class,
        PdfFtsEntity::class,
        FolderEntity::class
    ],
    version = 10, // 🌟 NAYA: Bumped from 9 to 10
    exportSchema = true
)
abstract class PdfDatabase : RoomDatabase() {
    abstract val pdfDao: PdfDao
    abstract val searchHistoryDao: SearchHistoryDao
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\local\dao\PdfDao.kt
``kotlin
package com.edu.pdf.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.PdfEntity
import kotlinx.coroutines.flow.Flow

data class FolderWithCount(
    @Embedded val folder: FolderEntity,
    val pdfCount: Int
)

@Dao
interface PdfDao {

    // ==========================================
    // 🧹 PHASE 3: GHOST CLEANUP WEAPONS (New Safety Net)
    // ==========================================
    @Query("DELETE FROM managed_folders WHERE absolutePath NOT IN (:existingPaths) AND isVault = 0")
    suspend fun deleteStaleFolders(existingPaths: List<String>)

    @Query("DELETE FROM pdf_table WHERE path NOT IN (:existingPaths) AND isVault = 0")
    suspend fun deleteStalePdfs(existingPaths: List<String>)

    @Query("SELECT absolutePath FROM managed_folders WHERE isVault = 0")
    suspend fun getAllStoredFolderPaths(): List<String>


    // ==========================================
    // 📁 PHYSICAL FOLDERS (MANAGED HUB & VAULT)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolder(folder: FolderEntity)

    // 🌟 ELITE FIX: Grouping by ID (New Primary Key) taaki count accurate rahe
    // 🌟 ELITE FIX: Ab ye PDF + Sub-folders dono ko ginega (34 files + 1 folder = 35 items)
    @Query("""
        SELECT f.*, 
        ( (SELECT COUNT(id) FROM pdf_table WHERE parentPath = f.absolutePath) + 
          (SELECT COUNT(id) FROM managed_folders WHERE parentPath = f.absolutePath) ) as pdfCount
        FROM managed_folders f 
        WHERE f.parentPath IS :parentPath AND f.isVault = :isVault 
        ORDER BY f.name ASC
    """)
    fun getFoldersByParentWithCount(parentPath: String?, isVault: Boolean): Flow<List<FolderWithCount>>

    @Query("""
        SELECT f.*, 
        ( (SELECT COUNT(id) FROM pdf_table WHERE parentPath = f.absolutePath) + 
          (SELECT COUNT(id) FROM managed_folders WHERE parentPath = f.absolutePath) ) as pdfCount
        FROM managed_folders f 
        WHERE f.isVault = :isVault 
        ORDER BY f.name ASC
    """)
    fun getAllFoldersWithCount(isVault: Boolean): Flow<List<FolderWithCount>>

    @Query("""
        SELECT f.*, 
        ( (SELECT COUNT(id) FROM pdf_table WHERE parentPath = f.absolutePath) + 
          (SELECT COUNT(id) FROM managed_folders WHERE parentPath = f.absolutePath) ) as pdfCount
        FROM managed_folders f 
        WHERE f.lastOpenedTime > 0 AND f.isVault = 0 
        ORDER BY f.lastOpenedTime DESC LIMIT 50
    """)
    fun getRecentFoldersWithCount(): Flow<List<FolderWithCount>>

    @Query("UPDATE managed_folders SET name = :newName, absolutePath = :newPath, parentPath = :newParentPath WHERE absolutePath = :oldPath")
    suspend fun renameFolder(oldPath: String, newName: String, newPath: String, newParentPath: String?)

    @Query("DELETE FROM managed_folders WHERE absolutePath = :path")
    suspend fun deleteFolder(path: String)

    @Query("UPDATE managed_folders SET parentPath = :newParentPath, absolutePath = :newAbsolutePath, isVault = :isVault WHERE absolutePath = :oldAbsolutePath")
    suspend fun moveFolder(oldAbsolutePath: String, newAbsolutePath: String, newParentPath: String?, isVault: Boolean)

    @Query("UPDATE managed_folders SET lastOpenedTime = :time WHERE absolutePath = :path")
    suspend fun updateFolderLastOpenedTime(path: String, time: Long)

    // ==========================================
    // 🌪️ ELITE CASCADE RENAME ENGINE (Tumhara Original Smart Logic!)
    // ==========================================
    // फोल्डर और उसके सब-फोल्डर्स का नाम बदलने का नया कोड
    @Query("""
        UPDATE managed_folders 
        SET 
            absolutePath = :newPath || SUBSTR(absolutePath, LENGTH(:oldPath) + 1),
            parentPath = CASE 
                WHEN parentPath = :oldPath THEN :newPath
                WHEN parentPath LIKE :oldPath || '/%' THEN :newPath || SUBSTR(parentPath, LENGTH(:oldPath) + 1)
                ELSE parentPath 
            END,
            name = CASE WHEN absolutePath = :oldPath THEN :newName ELSE name END
        WHERE absolutePath = :oldPath OR absolutePath LIKE :oldPath || '/%'
    """)
    suspend fun cascadeRenameFolders(oldPath: String, newPath: String, newName: String)

    @Query("""
        UPDATE pdf_table 
        SET 
            path = :newPath || SUBSTR(path, LENGTH(:oldPath) + 1),
            parentPath = CASE 
                WHEN parentPath = :oldPath THEN :newPath
                WHEN parentPath LIKE :oldPath || '/%' THEN :newPath || SUBSTR(parentPath, LENGTH(:oldPath) + 1)
                ELSE parentPath
            END
        WHERE parentPath = :oldPath OR parentPath LIKE :oldPath || '/%' OR path LIKE :oldPath || '/%'
    """)
    suspend fun cascadeRenamePdfs(oldPath: String, newPath: String)


    // ==========================================
    // 📄 PDF FILES (CORE)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPdf(pdf: PdfEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPdfs(pdfs: List<PdfEntity>)

    @Query("SELECT * FROM pdf_table WHERE parentPath IS :parentPath AND isVault = :isVault ORDER BY lastModified DESC")
    fun getPdfsByParent(parentPath: String?, isVault: Boolean): Flow<List<PdfEntity>>

    @Query("SELECT * FROM pdf_table WHERE parentPath IS :parentPath AND isVault = :isVault ORDER BY lastModified DESC")
    fun getPaginatedPdfsByParent(parentPath: String?, isVault: Boolean): PagingSource<Int, PdfEntity>
    @Query("SELECT * FROM pdf_table WHERE lastOpenedTime > 0 AND isVault = 0 ORDER BY lastOpenedTime DESC LIMIT 50")
    fun getRecentPdfs(): Flow<List<PdfEntity>>

    @RawQuery(observedEntities = [PdfEntity::class])
    fun getSortedPdfs(query: SupportSQLiteQuery): Flow<List<PdfEntity>>

    @RawQuery(observedEntities = [PdfEntity::class])
    fun getSortedPdfsPaged(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    @Query("UPDATE pdf_table SET lastOpenedTime = :time WHERE id = :pdfId")
    suspend fun updateLastOpenedTime(pdfId: String, time: Long)

    @Query("UPDATE pdf_table SET isFavorite = :isFav WHERE id = :pdfId")
    suspend fun updateFavoriteStatus(pdfId: String, isFav: Boolean)

    @Query("DELETE FROM pdf_table WHERE id IN (:ids)")
    suspend fun deletePdfsByIds(ids: List<String>)

    @Query("DELETE FROM pdf_table WHERE path IN (:paths)")
    suspend fun deletePdfsByPaths(paths: List<String>)

    @Query("UPDATE pdf_table SET name = :newName, path = :newPath WHERE id = :pdfId")
    suspend fun updatePdfNameAndPath(pdfId: String, newName: String, newPath: String)

    @Query("UPDATE pdf_table SET parentPath = :newParentPath, isVault = :isVault WHERE id = :pdfId")
    suspend fun movePdfToFolder(pdfId: String, newParentPath: String?, isVault: Boolean)

    @Query("UPDATE pdf_table SET id = :newId, path = :newPath WHERE id = :oldId")
    suspend fun updatePdfIdAndPath(oldId: String, newId: String, newPath: String)

    @Query("SELECT id FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfIds(): List<String>

    @Query("SELECT path FROM pdf_table WHERE isVault = 0")
    suspend fun getAllPublicPdfPaths(): List<String>

    @Query("SELECT * FROM pdf_table WHERE id IN (:ids)")
    suspend fun getPdfsByIds(ids: List<String>): List<PdfEntity>


    // ==========================================
    // 🔍 FTS SEARCH & PAGING ENGINE
    // ==========================================
    // ==========================================
    // 🔍 SMART SEARCH & PAGING ENGINE
    // ==========================================
    @Query("""
        SELECT * FROM pdf_table 
        WHERE name LIKE '%' || :query || '%' AND isVault = 0
        ORDER BY lastModified DESC
        LIMIT 50
    """)
    fun searchPdfsInDatabase(query: String): Flow<List<PdfEntity>>

    @Query("INSERT INTO pdf_fts_table(pdf_fts_table) VALUES('optimize')")
    suspend fun optimizeSearchIndex()

    @Query("""
        SELECT * FROM pdf_table 
        WHERE name LIKE '%' || :query || '%' AND isVault = :isVault 
        ORDER BY lastModified DESC 
        LIMIT 50
    """)
    suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfEntity>

    @Query("""
        SELECT * FROM pdf_table 
        WHERE path LIKE :folderPath || '/%' 
        AND path NOT LIKE :folderPath || '/%/%' 
        AND isVault = 0 
        ORDER BY lastModified DESC
    """)
    fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfEntity>>

    @Query("""
        SELECT * FROM pdf_table 
        WHERE path LIKE :folderPath || '/%' 
        AND path NOT LIKE :folderPath || '/%/%' 
        AND isVault = 0 
        ORDER BY lastModified DESC
    """)
    fun getPaginatedPdfsInPhysicalFolder(folderPath: String): PagingSource<Int, PdfEntity>
    // ==========================================
    // 🌪️ MISSING CASCADE DELETE ENGINE
    // ==========================================
    // फोल्डर्स को डिलीट करने के लिए नया सुरक्षित कोड
    @Query("""
        DELETE FROM managed_folders 
        WHERE absolutePath = :path OR absolutePath LIKE :path || '/%'
    """)
    suspend fun cascadeDeleteFolders(path: String)

    @Query("""
        DELETE FROM pdf_table 
        WHERE parentPath = :path OR parentPath LIKE :path || '/%' OR path LIKE :path || '/%'
    """)
    suspend fun cascadeDeletePdfs(path: String)

    // FILE: com/edu/pdf/data/local/dao/PdfDao.kt

    // 🌟 ELITE FIX: Home Screen के लिए Paging 3 Query
    @RawQuery(observedEntities = [PdfEntity::class])
    fun getUncategorizedPdfsPaged(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    // 🌟 ELITE MVI FIX: Dynamic Paging 3 Sorting
    @RawQuery(observedEntities = [PdfEntity::class])
    fun getPaginatedPdfsByParentRaw(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    @RawQuery(observedEntities = [PdfEntity::class])
    fun getPaginatedPdfsInPhysicalFolderRaw(query: SupportSQLiteQuery): PagingSource<Int, PdfEntity>

    // 🌟 FAST SELECT ALL ENGINE (Sirf ID nikalega, Zero Memory Full)
    @Query("SELECT id FROM pdf_table WHERE isVault = 0 AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))")
    suspend fun getUncategorizedPdfIdsFast(): List<String>

    @Query("SELECT id FROM pdf_table WHERE isFavorite = 1 AND isVault = 0")
    suspend fun getFavoritePdfIdsFast(): List<String>

    @Query("SELECT id FROM pdf_table WHERE parentPath IS :parentPath AND isVault = :isVault")
    suspend fun getManagedPdfIdsFast(parentPath: String?, isVault: Boolean): List<String>

    @Query("SELECT id FROM pdf_table WHERE path LIKE :folderPath || '/%' AND path NOT LIKE :folderPath || '/%/%' AND isVault = 0")
    suspend fun getPhysicalFolderPdfIdsFast(folderPath: String): List<String>


}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\local\dao\SearchHistoryDao.kt
``kotlin
package com.edu.pdf.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.pdf.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Upsert
    suspend fun insertSearchQuery(searchHistory: SearchHistoryEntity)

    @Query("SELECT * FROM search_history_table ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history_table")
    suspend fun clearAllHistory()

    @Query("DELETE FROM search_history_table WHERE `query` = :query")
    suspend fun deleteSearchQuery(query: String)
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\local\entity\FolderEntity.kt
``kotlin
package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Keep
@Entity(
    tableName = "managed_folders",
    indices = [
        Index(value = ["absolutePath"], unique = true), // 🌟 Path unique rahega par PK nahi
        Index(value = ["parentPath"]),
        Index(value = ["isVault"])
    ]
)
data class FolderEntity(
    // 🌟 ELITE FIX: Ab ID primary key hai. Path change hone par bhi ye ID nahi badlegi.
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val absolutePath: String,
    val name: String,
    val parentPath: String? = null,
    val isVault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastOpenedTime: Long = 0L
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\local\entity\PdfEntity.kt
``kotlin
package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Keep
@Entity(
    tableName = "pdf_table",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["path"], unique = true), // 🌟 Path badlega, par unique hona chahiye
        Index(value = ["parentPath"]), // 🌟 Sync engine ke liye super-fast lookup
        Index(value = ["isVault"]),
        Index(value = ["lastOpenedTime"])
    ]
)
data class PdfEntity(
    @PrimaryKey(autoGenerate = true) val roomId: Long = 0,
    val id: String, // MediaStore ID ya UUID
    val name: String,
    val path: String,
    val sizeInBytes: Long,
    val lastModified: Long,
    val isFavorite: Boolean = false,
    val lastOpenedTime: Long = 0L,
    val parentPath: String? = null,
    val isVault: Boolean = false
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\local\entity\PdfFtsEntity.kt
``kotlin
package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Fts4 // 🌟 Aapka sahi pakda hua FTS4
@Keep
@Fts4(contentEntity = PdfEntity::class)
@Entity(tableName = "pdf_fts_table")
data class PdfFtsEntity(
    val name: String
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\local\entity\SearchHistoryEntity.kt
``kotlin
package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity(tableName = "search_history_table")
data class SearchHistoryEntity(
    @PrimaryKey val query: String,
    val timestamp: Long
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\mapper\PdfMapper.kt
``kotlin
package com.edu.pdf.data.mapper

import com.edu.pdf.data.local.dao.FolderWithCount
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.PdfEntity
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.PdfFile

fun PdfEntity.toDomainModel(): PdfFile {
    return PdfFile(
        id = id,
        name = name,
        path = path,
        sizeInBytes = sizeInBytes,
        lastModified = lastModified,
        isFavorite = isFavorite,
        lastOpenedTime = lastOpenedTime,
        virtualParentId = parentPath, // 🌟 ELITE FIX: Ab parentPath hi tumhara 'virtualParentId' variable banega
        isVault = isVault
    )
}

fun PdfFile.toEntity(): PdfEntity {
    return PdfEntity(
        id = id,
        name = name,
        path = path,
        sizeInBytes = sizeInBytes,
        lastModified = lastModified,
        isFavorite = isFavorite,
        lastOpenedTime = lastOpenedTime,
        parentPath = virtualParentId, // 🌟 UI se aane wala ID asal me Path hai
        isVault = isVault
    )
}

fun FolderEntity.toDomainModel(pdfCount: Int = 0): Folder {
    return Folder(
        folderId = absolutePath, // 🌟 ELITE FIX: UI jise 'folderId' bol raha hai, wo asal me 'absolutePath' hai
        name = name,
        parentFolderId = parentPath,
        isVault = isVault,
        createdAt = createdAt,
        pdfCount = pdfCount,
        lastOpenedTime = lastOpenedTime
    )
}

fun FolderWithCount.toDomainModel(): Folder {
    return Folder(
        folderId = folder.absolutePath,
        name = folder.name,
        parentFolderId = folder.parentPath,
        isVault = folder.isVault,
        createdAt = folder.createdAt,
        pdfCount = pdfCount,
        lastOpenedTime = folder.lastOpenedTime
    )
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\preferences\AiKeyManager.kt
``kotlin
package com.edu.pdf.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION") // Hides Jetpack Security deprecation warnings
@Singleton
class AiKeyManager @Inject constructor(
    @param:ApplicationContext private val context: Context // Fixed Kotlin 2.x Warning
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "ai_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveKeys(primary: String, fallback1: String, fallback2: String) {
        sharedPreferences.edit().apply {
            putString("KEY_PRIMARY", primary.trim())
            putString("KEY_FALLBACK_1", fallback1.trim())
            putString("KEY_FALLBACK_2", fallback2.trim())
            apply()
        }
    }

    fun getKeys(): List<String> {
        val keys = mutableListOf<String>()
        sharedPreferences.getString("KEY_PRIMARY", "")?.takeIf { it.isNotEmpty() }?.let { keys.add(it) }
        sharedPreferences.getString("KEY_FALLBACK_1", "")?.takeIf { it.isNotEmpty() }?.let { keys.add(it) }
        sharedPreferences.getString("KEY_FALLBACK_2", "")?.takeIf { it.isNotEmpty() }?.let { keys.add(it) }
        return keys
    }

    fun hasAnyKey(): Boolean = getKeys().isNotEmpty()
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\preferences\UserPreferences.kt
``kotlin
package com.edu.pdf.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "pdf_user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val IS_GRID_VIEW = booleanPreferencesKey("is_grid_view")
        val IS_FOLDER_GRID_VIEW = booleanPreferencesKey("is_folder_grid_view")
        val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        val IS_INITIAL_SCAN_COMPLETED = booleanPreferencesKey("is_initial_scan_completed")
    }

    val isGridViewFlow: Flow<Boolean> = dataStore.data.map { it[IS_GRID_VIEW] ?: false }
    val isFolderGridViewFlow: Flow<Boolean> = dataStore.data.map { it[IS_FOLDER_GRID_VIEW] ?: false }

    suspend fun saveGridViewPreference(isGrid: Boolean) = dataStore.edit { it[IS_GRID_VIEW] = isGrid }
    suspend fun saveFolderGridViewPreference(isGrid: Boolean) = dataStore.edit { it[IS_FOLDER_GRID_VIEW] = isGrid }

    suspend fun getLastSyncTime(): Long = dataStore.data.map { it[LAST_SYNC_TIME] ?: 0L }.first()
    suspend fun updateLastSyncTime(time: Long) = dataStore.edit { it[LAST_SYNC_TIME] = time }

    suspend fun isInitialScanCompleted(): Boolean = dataStore.data.map { it[IS_INITIAL_SCAN_COMPLETED] ?: false }.first()
    suspend fun setInitialScanCompleted(completed: Boolean) = dataStore.edit { it[IS_INITIAL_SCAN_COMPLETED] = completed }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\repository\AiRepositoryImpl.kt
``kotlin
package com.edu.pdf.data.repository

import android.graphics.Bitmap
import com.edu.pdf.data.preferences.AiKeyManager
import com.edu.pdf.domain.repository.AiRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import java.io.ByteArrayOutputStream

class AiRepositoryImpl @Inject constructor(
    private val keyManager: AiKeyManager
) : AiRepository {

    override fun chatWithPdfStream(query: String, pageBitmap: Bitmap?): Flow<String> = flow {
        val keys = keyManager.getKeys()

        if (keys.isEmpty()) {
            emit("\n[Error: AI API Keys not found. Please add your keys in Settings.]")
            return@flow
        }

        // 🌟 ELITE FIX 1: Bitmap Compression
        val compressedBitmap = pageBitmap?.let { scaleAndCompressBitmap(it) }

        var success = false
        var lastError: Exception? = null

        for ((index, apiKey) in keys.withIndex()) {
            try {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-2.5-flash",
                    apiKey = apiKey.trim()
                )

                val inputContent = content {
                    compressedBitmap?.let { image(it) }
                    text(query)
                }

                // 🌟 ELITE FIX 2: Stream Flow Control (Prevents duplicate appending on fallback)
                val responseStream = generativeModel.generateContentStream(inputContent)

                // Agar fallback run ho raha hai, toh user ko batao
                if (index > 0) emit("\n[Switched to Backup Key...]\n")

                responseStream.collect { chunk ->
                    emit(chunk.text ?: "")
                }

                success = true
                break // Successful, bahar aa jao

            } catch (e: Exception) {
                lastError = e
                // Error log karein par flow crash na hone dein
            }
        }

        if (!success) {
            emit("\n[Error: All API Keys exhausted. Reason: ${lastError?.localizedMessage}]")
        }
    }.flowOn(Dispatchers.IO)

    // Helper Function to resize Bitmap for Gemini Payload limits
    private fun scaleAndCompressBitmap(bitmap: Bitmap, maxDimension: Int = 1024): Bitmap {
        val ratio = Math.min(maxDimension.toFloat() / bitmap.width, maxDimension.toFloat() / bitmap.height)
        if (ratio >= 1.0f) return bitmap // Pehle se chhota hai

        val width = Math.round(ratio * bitmap.width)
        val height = Math.round(ratio * bitmap.height)
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\repository\PdfRepositoryImpl.kt
``kotlin
package com.edu.pdf.data.repository

import androidx.core.net.toUri
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import com.edu.pdf.data.local.dao.PdfDao
import com.edu.pdf.data.local.dao.SearchHistoryDao
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.SearchHistoryEntity
import com.edu.pdf.data.mapper.toDomainModel
import com.edu.pdf.data.mapper.toEntity
import com.edu.pdf.data.source.DeviceStorageDataSource
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import javax.inject.Inject

class PdfRepositoryImpl @Inject constructor(
    private val pdfDao: PdfDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val deviceStorage: DeviceStorageDataSource,
    private val vaultStorage: com.edu.pdf.data.source.VaultDataSource,
    private val userPreferences: com.edu.pdf.data.preferences.UserPreferences
) : PdfRepository {

    override fun getManagedFolders(parentPath: String?, isVault: Boolean): Flow<List<Folder>> {
        // 🌟 Simple clean query: Null will match Null
        return pdfDao.getFoldersByParentWithCount(parentPath, isVault).map { list -> list.map { it.toDomainModel() } }
    }

    override fun getAllManagedFolders(isVault: Boolean): Flow<List<Folder>> {
        return pdfDao.getAllFoldersWithCount(isVault).map { list -> list.map { it.toDomainModel() } }
    }

    override fun getManagedPdfs(parentPath: String?, isVault: Boolean): Flow<List<PdfFile>> {
        return pdfDao.getPdfsByParent(parentPath, isVault).map { list -> list.map { it.toDomainModel() } }
    }

    // 🌟 THE ELITE FIX: Physical Folder Creation Sync
    override suspend fun createManagedFolder(name: String, parentPath: String?, isVault: Boolean): Result<String> {
        return try {
            val root = parentPath ?: deviceStorage.getPdfProRootFolder()
            val targetFile = File(root, name)

            // 🛑 YAHAN ROK LIYA: Agar us exact jagah par same naam hai
            if (targetFile.exists()) {
                return Result.failure(Exception("A folder named '$name' already exists here!"))
            }

            // 1. Pehle Physical OS me folder banao
            val newPhysicalPath = deviceStorage.createPhysicalFolder(name, parentPath)
            if (newPhysicalPath != null) {
                // 2. Agar physical ban gaya, toh Database me Index karo
                pdfDao.insertFolder(FolderEntity(absolutePath = newPhysicalPath, name = name, parentPath = parentPath, isVault = isVault))
                Result.success(newPhysicalPath)
            } else {
                Result.failure(Exception("Failed to create physical folder. Check permissions."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun renameManagedFolder(oldPath: String, newName: String): Result<Unit> {
        val oldFile = File(oldPath)
        val parentDir = oldFile.parentFile ?: return Result.failure(Exception("Invalid folder path"))
        val targetFile = File(parentDir, newName)

        // 🛑 YAHAN ROK LIYA: (Maths aur maths ko alag rakhne ke liye ignoreCase = true lagaya hai)
        if (targetFile.exists() && !oldFile.name.equals(newName, ignoreCase = true)) {
            return Result.failure(Exception("A folder named '$newName' already exists here!"))
        }

        val newPhysicalPath = deviceStorage.renamePhysicalFile(oldPath, newName)

        return if (newPhysicalPath != null) {
            pdfDao.cascadeRenameFolders(oldPath, newPhysicalPath, newName)
            pdfDao.cascadeRenamePdfs(oldPath, newPhysicalPath)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Rename physically failed"))
        }
    }

    // 🌟 THE ELITE FIX: Deep Recursive Delete
    override suspend fun deleteManagedFolder(path: String): Result<Unit> = withContext(Dispatchers.IO) {
        // 1. Delete from OS permanently
        val success = deviceStorage.deletePhysicalPath(path)
        if (success) {
            // 🌟 ELITE FIX 1: Ek sath folder aur uske andar ka sab kuch DB se clear karo
            pdfDao.cascadeDeleteFolders(path)
            pdfDao.cascadeDeletePdfs(path)
            return@withContext Result.success(Unit)
        }
        return@withContext Result.failure(Exception("Could not delete folder physically"))
    }

    override suspend fun moveFolderToVirtualFolder(folderPath: String, targetPath: String?, isVault: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        val target = targetPath ?: deviceStorage.getPdfProRootFolder()

        // 🌟 ELITE FIX 2: INCEPTION LOOP GUARD (BULLETPROOF)
        // Check if we are moving folder into itself or its children
        val normalizedSource = File(folderPath).absolutePath
        val normalizedTarget = File(target).absolutePath
        
        if (normalizedTarget == normalizedSource || normalizedTarget.startsWith("$normalizedSource/")) {
            return@withContext Result.failure(Exception("Cannot move a folder into itself or its own sub-folder!"))
        }

        val newPhysicalPath = deviceStorage.movePhysicalFile(folderPath, target)

        return@withContext if (newPhysicalPath != null) {
            // 1. अंदर के सारे बच्चे (Sub-folders & PDFs) का पाथ अपडेट करो
            val newName = File(newPhysicalPath).name
            pdfDao.cascadeRenameFolders(folderPath, newPhysicalPath, newName)
            pdfDao.cascadeRenamePdfs(folderPath, newPhysicalPath)

            // 🌟 2. ELITE FIX: जिस फोल्डर को मूव किया है, उसका अपना 'parentPath' भी अपडेट करो!
            val newParentForMovedFolder = if (target == deviceStorage.getPdfProRootFolder()) null else target

            // 🌟 ELITE FIX: cascadeRenameFolders ने पाथ बदल दिया है,
            // इसलिए अब हम फोल्डर को उसके 'नये' पाथ से ही ढूंढेंगे!
            pdfDao.moveFolder(
                oldAbsolutePath = newPhysicalPath, // ✅ बस folderPath को हटाकर newPhysicalPath कर दो!
                newAbsolutePath = newPhysicalPath,
                newParentPath = newParentForMovedFolder,
                isVault = isVault
            )

            // 3. Move होने के बाद उसे Recent से हटाओ
            pdfDao.updateFolderLastOpenedTime(newPhysicalPath, 0L)

            // 🌟 SMART REFRESH: MediaStore ko turant batao taaki UI update ho jaye
            deviceStorage.syncWithMediaStore(folderPath, newPhysicalPath)

            Result.success(Unit)
        } else {
            Result.failure(Exception("Move physically failed. Check storage permissions or collision."))
        }
    }

    override suspend fun movePdfsToVirtualFolder(pdfIds: List<String>, targetPath: String?, isVault: Boolean): Int {
        var movedCount = 0
        val targetPhysicalDir = targetPath ?: deviceStorage.getPdfProRootFolder()

        pdfIds.chunked(200).forEach { chunkedIds ->
            val pdfsToMove = pdfDao.getPdfsByIds(chunkedIds)
            pdfsToMove.forEach { pdfToMove ->
                val pdfId = pdfToMove.id
                if (isVault && !pdfToMove.isVault) {
                    val securePath = vaultStorage.moveToInternalVault(pdfToMove.toDomainModel()) { oldPath ->
                        deviceStorage.syncWithMediaStore(oldPath, null)
                    }
                    if (securePath != null) {
                        pdfDao.updatePdfNameAndPath(pdfId, pdfToMove.name, securePath)
                        pdfDao.movePdfToFolder(pdfId, null, isVault = true)
                        movedCount++
                    }
                } else if (!isVault && pdfToMove.isVault) {
                    val restoredData = vaultStorage.restoreFromInternalVault(pdfToMove.path, pdfToMove.name)
                    if (restoredData != null) {
                        val (newId, newPath) = restoredData
                        pdfDao.updatePdfIdAndPath(oldId = pdfId, newId = newId, newPath = newPath)
                        pdfDao.movePdfToFolder(newId, targetPhysicalDir, isVault = false)
                        movedCount++
                    }
                } else {
                    // 🌟 NORMAL MOVE: Move in OS first, then update DB
                    val newPhysicalPath = deviceStorage.movePhysicalFile(pdfToMove.path, targetPhysicalDir)
                    if (newPhysicalPath != null) {
                        pdfDao.updatePdfNameAndPath(pdfId, pdfToMove.name, newPhysicalPath)
                        pdfDao.movePdfToFolder(pdfId, targetPath, isVault)
                        
                        // 🌟 SMART REFRESH: MediaStore sync for each file
                        deviceStorage.syncWithMediaStore(pdfToMove.path, newPhysicalPath)

                        movedCount++
                    }
                }
            }
            yield()
        }
        return movedCount
    }

    override suspend fun importPdfFromUri(uriString: String, targetPath: String?, isVault: Boolean, isPhysicalFolder: Boolean): Result<Unit> {
        return try {
            val uri = uriString.toUri()
            val importedFile = deviceStorage.importFileFromUri(uri, isVault, targetPath)

            if (importedFile != null) {
                val newEntity = com.edu.pdf.data.local.entity.PdfEntity(
                    id = importedFile.id,
                    name = importedFile.name,
                    path = importedFile.path,
                    sizeInBytes = importedFile.sizeInBytes,
                    lastModified = importedFile.lastModified,
                    parentPath = targetPath, // 🌟 Accurate Path Mapping
                    isVault = isVault,
                    isFavorite = false
                )
                withContext(NonCancellable) { pdfDao.insertPdf(newEntity) }
                Result.success(Unit)
            } else {
                Result.failure(Exception("File copying failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🌟 THE ELITE FIX: Flawless Scanning Engine
    // 🌟 THE ELITE FIX: Flawless Scanning Engine (PDFs + Folders Both)
    override suspend fun scanAndSavePdfs() {
        withContext(Dispatchers.IO) {

            // 🧹 1. GHOST CLEANUP FOR PDFS
            // Jo PDFs bahar se delete/rename hui hain, unhe hatao
            val allPublicPaths = pdfDao.getAllPublicPdfPaths()
            val stalePaths = allPublicPaths.filter { path -> !File(path).exists() }

            if (stalePaths.isNotEmpty()) {
                stalePaths.chunked(100).forEach { chunk ->
                    pdfDao.deletePdfsByPaths(chunk)
                    yield()
                }
            }

            // 🧹 2. 🌟 NAYA: GHOST CLEANUP FOR FOLDERS
            // Jo Folders bahar se delete/rename hue hain, unka purana naam DB se hatao
            val allFolderPaths = pdfDao.getAllStoredFolderPaths()
            val staleFolderPaths = allFolderPaths.filter { path -> !File(path).exists() }

            if (staleFolderPaths.isNotEmpty()) {
                staleFolderPaths.forEach { path ->
                    pdfDao.deleteFolder(path)
                }
            }

            // 📁 3. Physical Folders (Naye folders) ko DB me dalo
            syncPhysicalFoldersWithDb()

            // 📄 4. PURE DEVICE SCAN (Nayi PDFs ko DB me dalo)
            deviceStorage.processDevicePdfUpdates { chunk ->
                pdfDao.insertAllPdfs(chunk.map { it.toEntity().copy(isVault = false) })
                yield()
            }

            userPreferences.setInitialScanCompleted(true)
            userPreferences.updateLastSyncTime(System.currentTimeMillis())
        }
    }

    override suspend fun renamePdf(pdf: PdfFile, newName: String): Boolean {
        // Automatically .pdf extension laga dega agar user bhool gaya
        val finalName = if (newName.endsWith(".pdf", ignoreCase = true)) newName else "$newName.pdf"
        val newPhysicalPath = deviceStorage.renamePhysicalFile(pdf.path, finalName)

        if (newPhysicalPath != null) {
            pdfDao.updatePdfNameAndPath(pdf.id, finalName, newPhysicalPath)
            return true
        }
        return false
    }

    override suspend fun deletePdfs(pdfs: List<PdfFile>): Boolean {
        val trashedIds = deviceStorage.moveToTrash(pdfs)
        if (trashedIds.isNotEmpty()) {
            trashedIds.chunked(100).forEach { chunk -> pdfDao.deletePdfsByIds(chunk); yield() }
        }
        return true
    }

    override fun getSecureVaultStreamUri(encryptedPath: String): String = vaultStorage.getSecureVaultStreamUri(encryptedPath)
    override fun getRecentPdfs(): Flow<List<PdfFile>> = pdfDao.getRecentPdfs().map { list -> list.map { it.toDomainModel() } }

    private fun getSortQuery(baseQuery: String, sortType: SortType): String {
        val orderBy = when (sortType) {
            SortType.NAME_ASC -> "name ASC"
            SortType.NAME_DESC -> "name DESC"
            SortType.DATE_DESC -> "lastModified DESC"
            SortType.DATE_ASC -> "lastModified ASC"
            SortType.SIZE_DESC -> "sizeInBytes DESC"
            SortType.SIZE_ASC -> "sizeInBytes ASC"
        }
        return "$baseQuery ORDER BY $orderBy"
    }

    override fun getAllPdfs(sortType: SortType): Flow<List<PdfFile>> {
        val query = SimpleSQLiteQuery(getSortQuery("SELECT * FROM pdf_table WHERE isVault = 0", sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }
    override fun getUncategorizedPdfs(sortType: SortType): Flow<List<PdfFile>> {
        val baseQuery = """
            SELECT * FROM pdf_table 
            WHERE isVault = 0 
            AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))
        """.trimIndent()

        val query = SimpleSQLiteQuery(getSortQuery(baseQuery, sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }
    // 🌟 THE ELITE FIX: Database se Paging 3 ke page mangwane ka logic
    // 🌟 THE ELITE FIX: Database se Paging 3 ke page mangwane ka logic
    override fun getAllPdfsPaged(sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val baseQuery = """
            SELECT * FROM pdf_table 
            WHERE isVault = 0 
            AND (parentPath IS NULL OR parentPath NOT IN (SELECT absolutePath FROM managed_folders WHERE isVault = 0))
        """.trimIndent()

        val query = SimpleSQLiteQuery(getSortQuery(baseQuery, sortType))

        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false),
            pagingSourceFactory = { pdfDao.getUncategorizedPdfsPaged(query) }
        ).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }

    override fun getFavoritePdfs(sortType: SortType): Flow<List<PdfFile>> {
        val query = SimpleSQLiteQuery(getSortQuery("SELECT * FROM pdf_table WHERE isFavorite = 1 AND isVault = 0", sortType))
        return pdfDao.getSortedPdfs(query).map { list -> list.map { it.toDomainModel() } }
    }

    override fun searchPdfs(query: String): Flow<List<PdfFile>> = pdfDao.searchPdfsInDatabase(query).map { list -> list.map { it.toDomainModel() } }
    override suspend fun checkFileExists(fileId: String): Boolean = deviceStorage.doesFileExist(fileId)
    override suspend fun updateLastOpenedTime(pdfId: String, time: Long) = pdfDao.updateLastOpenedTime(pdfId, time)
    override suspend fun toggleFavorite(pdfId: String, isFavorite: Boolean) = pdfDao.updateFavoriteStatus(pdfId, isFavorite)
    override fun getRecentFolders(): Flow<List<Folder>> = pdfDao.getRecentFoldersWithCount().map { list -> list.map { it.toDomainModel() } }
    override suspend fun updateFolderLastOpenedTime(path: String, time: Long) = pdfDao.updateFolderLastOpenedTime(path, time)
    override fun getRecentSearchQueries(): Flow<List<String>> = searchHistoryDao.getRecentSearches().map { list -> list.map { it.query } }
    override suspend fun saveSearchQuery(query: String) { if (query.isNotBlank()) searchHistoryDao.insertSearchQuery(SearchHistoryEntity(query.trim(), System.currentTimeMillis())) }
    override suspend fun deleteSearchQuery(query: String) = searchHistoryDao.deleteSearchQuery(query)
    override suspend fun clearAllSearchHistory() = searchHistoryDao.clearAllHistory()
    override suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfFile> = pdfDao.searchPdfsFast(query, isVault).map { it.toDomainModel() }
    override fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfFile>> = pdfDao.getPdfsInPhysicalFolder(folderPath).map { list -> list.map { it.toDomainModel() } }

    override fun getPaginatedPdfsInPhysicalFolder(folderPath: String, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val baseQuery = "SELECT * FROM pdf_table WHERE path LIKE ? || '/%' AND path NOT LIKE ? || '/%/%' AND isVault = 0"
        val fullQueryString = getSortQuery(baseQuery, sortType)
        val query = SimpleSQLiteQuery(fullQueryString, arrayOf(folderPath, folderPath))

        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false),
            pagingSourceFactory = { pdfDao.getPaginatedPdfsInPhysicalFolderRaw(query) }
        ).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }

    // 🌟 ELITE FIX: Ye function physical storage scan karke pehle se bane folders ko Database me wapas layega
    // 🌟 ELITE FIX: Ye function physical storage scan karke pehle se bane folders ko Database me wapas layega
    private suspend fun syncPhysicalFoldersWithDb() {
        val rootPath = deviceStorage.getPdfProRootFolder()
        val rootDir = File(rootPath)

        if (rootDir.exists() && rootDir.isDirectory) {
            rootDir.walkTopDown()
                .filter { it.isDirectory && it.absolutePath != rootPath }
                .forEach { folder ->
                    // 🌟 ELITE FIX: Agar iska parent 'PdfPro' root hai, toh parentPath NULL rakho
                    val parentFile = folder.parentFile?.absolutePath
                    val normalizedParent = if (parentFile == rootPath) null else parentFile

                    // बस इतना ही रखना है! कोई -1 वाला लॉजिक नहीं। (PdfDao में IGNORE अपना काम खुद कर लेगा)
                    pdfDao.insertFolder(
                        FolderEntity(
                            absolutePath = folder.absolutePath,
                            name = folder.name,
                            parentPath = normalizedParent,
                            isVault = false
                        )
                    )
                }
        }
    }
    override fun getPaginatedManagedPdfs(parentPath: String?, isVault: Boolean, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>> {
        val isVaultInt = if (isVault) 1 else 0
        val parentCondition = if (parentPath == null) "parentPath IS NULL" else "parentPath = ?"
        val baseQuery = "SELECT * FROM pdf_table WHERE $parentCondition AND isVault = $isVaultInt"

        val fullQueryString = getSortQuery(baseQuery, sortType)

        val query = if (parentPath == null) {
            SimpleSQLiteQuery(fullQueryString)
        } else {
            SimpleSQLiteQuery(fullQueryString, arrayOf(parentPath))
        }

        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(pageSize = 30, prefetchDistance = 15, enablePlaceholders = false),
            pagingSourceFactory = { pdfDao.getPaginatedPdfsByParentRaw(query) }
        ).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }

    override suspend fun getUncategorizedPdfIdsFast() = pdfDao.getUncategorizedPdfIdsFast()
    override suspend fun getFavoritePdfIdsFast() = pdfDao.getFavoritePdfIdsFast()
    override suspend fun getManagedPdfIdsFast(parentPath: String?, isVault: Boolean) = pdfDao.getManagedPdfIdsFast(parentPath, isVault)
    override suspend fun getPhysicalFolderPdfIdsFast(folderPath: String) = pdfDao.getPhysicalFolderPdfIdsFast(folderPath)
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\security\SecurityUtils.kt
``kotlin
package com.edu.pdf.data.security

import android.content.Context
import java.io.File

object SecurityUtils {
    // Ye function app start hone par aur vault band hone par temporary decrypted files ko delete karega
    fun wipeVaultTempStorage(context: Context) {
        val tempDir = File(context.cacheDir, "vault_temp_view")
        if (tempDir.exists()) {
            tempDir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\security\VaultCryptoEngine.kt
``kotlin
package com.edu.pdf.data.security

import android.content.Context
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.streamingaead.StreamingAeadConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@Suppress("DEPRECATION")
class VaultCryptoEngine @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    init {
        StreamingAeadConfig.register()
    }

    // 🌟 2026 FIX: 'by lazy' ka matlab hai jab tak zaroorat nahi hogi, ye load nahi hoga.
    // Isse Main UI Thread kabhi freeze nahi hoga!
    private val keysetHandle: KeysetHandle by lazy {
        AndroidKeysetManager.Builder()
            .withSharedPref(context, "vault_keys", "vault_prefs")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM_HKDF_4KB")) // Upgraded to 256-bit
            .withMasterKeyUri("android-keystore://vault_master_key")
            .build()
            .keysetHandle
    }

    private val streamingAead: StreamingAead by lazy {
        keysetHandle.getPrimitive(StreamingAead::class.java)
    }

    private val aad = "pdf_pro_vault_secure_data_v1".toByteArray()

    fun getEncryptedOutputStream(destinationFile: File): OutputStream {
        return streamingAead.newEncryptingStream(destinationFile.outputStream(), aad)
    }

    fun getEncryptedInputStream(encryptedFile: File): InputStream {
        return streamingAead.newDecryptingStream(encryptedFile.inputStream(), aad)
    }

}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\source\DeviceStorageDataSource.kt
``kotlin
package com.edu.pdf.data.source

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume

class DeviceStorageDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val cryptoEngine: VaultCryptoEngine
) {
    fun getPdfProRootFolder(): String {
        val docsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOCUMENTS)
        val pdfProDir = File(docsDir, "PdfPro")
        if (!pdfProDir.exists()) {
            pdfProDir.mkdirs()
            scanFilesBatch(arrayOf(pdfProDir.absolutePath))
        }
        return pdfProDir.absolutePath
    }

    // 🌟 THE ELITE FIX: Physical Folder Creation
    // 🌟 THE ELITE FIX: No more auto-appending (1), (2). UI will show error now.
    suspend fun createPhysicalFolder(name: String, parentPath: String?): String? = withContext(Dispatchers.IO) {
        val root = parentPath ?: getPdfProRootFolder()
        val newFolder = File(root, name)

        // Agar folder pehle se hai, toh null return karo taaki UI ko error bheja ja sake
        if (newFolder.exists()) return@withContext null

        return@withContext if (newFolder.mkdirs()) {
            scanFilesBatch(arrayOf(newFolder.absolutePath))
            newFolder.absolutePath
        } else null
    }

    suspend fun movePhysicalFile(sourcePath: String, targetFolderPath: String): String? = withContext(Dispatchers.IO) {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) return@withContext null

        val targetDir = File(targetFolderPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        // 🌟 SMART POLISH: Agar source aur target same hain, toh kuch mat karo!
        if (sourceFile.parentFile?.absolutePath == targetDir.absolutePath) {
            return@withContext sourcePath
        }

        var targetFile = File(targetDir, sourceFile.name)

        val originalNameWithoutExt = sourceFile.nameWithoutExtension
        val originalExt = sourceFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
        var counter = 1

        while (targetFile.exists()) {
            targetFile = File(targetDir, "$originalNameWithoutExt ($counter)$originalExt")
            counter++
        }

        return@withContext try {
            // 🌟 1. FAST PATH: OS-Level Atomic Move (1 मिलीसेकंड में पूरा फोल्डर/फाइल मूव होगा, बिना कॉपी किए)
            if (sourceFile.renameTo(targetFile)) {
                syncWithMediaStore(sourcePath, targetFile.absolutePath)
                return@withContext targetFile.absolutePath
            }

            // 🌟 2. FALLBACK PATH: अगर 'renameTo' फेल होता है (जैसे Internal Storage से SD Card में मूव करते वक़्त)
            if (sourceFile.isDirectory) {
                // 👉 यहाँ क्रैश होता था! अब हम फोल्डर के अंदर जाकर Recursive Copy करेंगे
                if (sourceFile.copyRecursively(targetFile, overwrite = true)) {
                    sourceFile.deleteRecursively() // कॉपी सफल होने पर ही पुराना उड़ाएंगे
                    syncWithMediaStore(sourcePath, targetFile.absolutePath)
                    targetFile.absolutePath
                } else {
                    targetFile.deleteRecursively() // Rollback (अगर फेल हुआ तो आधा-अधूरा काम हटा देंगे)
                    null
                }
            } else {
                // PDF Stream Copy
                sourceFile.inputStream().use { input ->
                    targetFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                if (targetFile.length() == sourceFile.length()) {
                    // 🌟 ELITE FIX: Android 11+ Safety! Agar purani file sach me delete nahi ho payi, toh duplication roko.
                    val isDeleted = sourceFile.delete()
                    if (isDeleted) {
                        syncWithMediaStore(sourcePath, targetFile.absolutePath)
                        return@withContext targetFile.absolutePath
                    } else {
                        // Purani file delete nahi hui, isliye nayi copy ko bhi uda do (Rollback)
                        targetFile.delete()
                        return@withContext null
                    }
                } else {
                    targetFile.delete() // Rollback due to size mismatch
                    return@withContext null
                }
            }
        } catch (_: Exception) {
            // कोई भी एरर आने पर कचरा साफ़ करेंगे (Rollback)
            if (sourceFile.isDirectory) targetFile.deleteRecursively() else targetFile.delete()
            null
        }
    }

    // 🌟 THE ELITE FIX: Deep Recursive Delete
    suspend fun deletePhysicalPath(path: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(path)
        if (!file.exists()) return@withContext true // Already deleted
        val success = file.deleteRecursively()
        if (success) syncWithMediaStore(path, null)
        return@withContext success
    }

    suspend fun syncWithMediaStore(oldPath: String?, newPath: String?) = withContext(Dispatchers.IO) {
        oldPath?.let { path ->
            try {
                context.contentResolver.delete(
                    MediaStore.Files.getContentUri("external"),
                    "${MediaStore.Files.FileColumns.DATA} = ?",
                    arrayOf(path)
                )
            } catch (_: Exception) {}
        }
        val pathsToScan = listOfNotNull(oldPath, newPath).toTypedArray()
        if (pathsToScan.isNotEmpty()) android.media.MediaScannerConnection.scanFile(
            context, pathsToScan, null, null
        )
    }

    // 🌟 THE ELITE FIX: Time-Trap Removed for File Manager Renames
    suspend fun processDevicePdfUpdates(
        onNewPdfsBatch: suspend (List<PdfFile>) -> Unit
    ) = withContext(Dispatchers.IO) {
        val collection = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED
        )

        // 🌟 FIX: DATE_MODIFIED wali condition hata di gayi hai.
        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ? OR ${MediaStore.Files.FileColumns.DATA} LIKE '%.pdf'"
        val selectionArgs = arrayOf("application/pdf")

        context.contentResolver.query(collection, projection, selection, selectionArgs, null)?.use { cursor ->
            val tempBatch = mutableListOf<PdfFile>()
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val path = cursor.getString(dataCol) ?: continue

                // Vault aur Trash ko ignore karo
                if (path.contains("/secure_vault_core/") || path.contains("/.trash/")) continue

                val file = File(path)
                if (!file.exists()) continue // MediaStore agar purani delete hui file dikhaye toh bacha lo

                val size = cursor.getLong(sizeCol)
                if (size > 0) {
                    val uriStr = ContentUris.withAppendedId(collection, cursor.getLong(idCol)).toString()
                    val parentPath = file.parentFile?.absolutePath

                    tempBatch.add(PdfFile(
                        id = uriStr,
                        name = cursor.getString(titleCol) ?: file.name,
                        path = path,
                        sizeInBytes = size,
                        lastModified = cursor.getLong(dateCol),
                        virtualParentId = parentPath
                    ))
                }

                if (tempBatch.size >= 200) {
                    onNewPdfsBatch(tempBatch.toList())
                    tempBatch.clear()
                    yield()
                }
            }
            if (tempBatch.isNotEmpty()) onNewPdfsBatch(tempBatch)
        }
    }

    // 🌟 THE ELITE FIX: Ab ye normal Path aur MediaStore URI dono ko samajh payega
    fun doesFileExist(fileUriOrPath: String): Boolean {
        return try {
            if (fileUriOrPath.startsWith("content://")) {
                context.contentResolver.openFileDescriptor(fileUriOrPath.toUri(), "r")?.use { true } ?: false
            } else {
                File(fileUriOrPath).exists()
            }
        } catch (_: Exception) {
            false
        }
    }

    suspend fun moveToTrash(pdfs: List<PdfFile>): List<String> = withContext(Dispatchers.IO) {
        val successfullyTrashedIds = mutableListOf<String>()
        val trashFolder = File(context.getExternalFilesDir(null), ".trash")
        if (!trashFolder.exists()) trashFolder.mkdirs()

        for (pdf in pdfs) {
            val file = File(pdf.path)
            if (file.exists() && movePhysicalFile(pdf.path, trashFolder.absolutePath) != null) {
                successfullyTrashedIds.add(pdf.id)
            } else if (!file.exists()) {
                successfullyTrashedIds.add(pdf.id) // DB Cleanup
            }
        }
        return@withContext successfullyTrashedIds
    }

    fun scanFilesBatch(paths: Array<String>) {
        if (paths.isEmpty()) return
        android.media.MediaScannerConnection.scanFile(context, paths, null, null)
    }

    @SuppressLint("UnsanitizedFilenameFromContentProvider")
    suspend fun importFileFromUri(uri: Uri, isVault: Boolean, targetPhysicalPath: String? = null): PdfFile? = withContext(Dispatchers.IO) {
        var targetFile: File? = null
        try {
            val contentResolver = context.contentResolver
            var fileName = "Imported_PDF_${System.currentTimeMillis()}.pdf"
            var fileSize = 0L
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                    if (nameIndex != -1) fileName = cursor.getString(nameIndex) ?: fileName
                    if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
                }
            }

            targetFile = if (isVault) {
                val vaultDir = File(context.filesDir, "secure_vault_core")
                if (!vaultDir.exists()) vaultDir.mkdirs()
                File(vaultDir, "${java.util.UUID.randomUUID()}.locked")
            } else {
                val publicDir = if (targetPhysicalPath != null && File(targetPhysicalPath).exists()) {
                    File(targetPhysicalPath)
                } else {
                    File(getPdfProRootFolder())
                }
                var tempFile = File(publicDir, fileName)

                val originalNameWithoutExt = tempFile.nameWithoutExtension
                val originalExt = tempFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }
                var counter = 1

                while (tempFile.exists()) {
                    tempFile = File(publicDir, "$originalNameWithoutExt ($counter)$originalExt")
                    counter++
                }
                tempFile
            }

            contentResolver.openInputStream(uri)?.use { inputStream ->
                if (isVault) {
                    // 🌟 NAYA: Agar Vault me import ho raha hai, toh seedha Encrypt karo!
                    cryptoEngine.getEncryptedOutputStream(targetFile).use { encryptedOutput ->
                        inputStream.copyTo(encryptedOutput)
                    }
                } else {
                    // 🌟 NORMAL: Agar normal folder me import ho raha hai, toh normal copy karo
                    targetFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }

            if (!isVault) {
                val realUri = suspendCancellableCoroutine<Uri?> { cont ->
                    android.media.MediaScannerConnection.scanFile(
                        context, arrayOf(targetFile.absolutePath), arrayOf("application/pdf")
                    ) { _, scannedUri -> cont.resume(scannedUri) }
                }
                val finalId = realUri?.toString() ?: targetFile.absolutePath

                return@withContext PdfFile(
                    id = finalId, name = targetFile.name, path = targetFile.absolutePath,
                    sizeInBytes = if (fileSize > 0) fileSize else targetFile.length(),
                    lastModified = System.currentTimeMillis(),
                    virtualParentId = targetFile.parentFile?.absolutePath // 🌟 Link to correct physical folder!
                )
            } else {
                return@withContext PdfFile(
                    id = targetFile.absolutePath, name = targetFile.name, path = targetFile.absolutePath,
                    sizeInBytes = if (fileSize > 0) fileSize else targetFile.length(),
                    lastModified = System.currentTimeMillis(), isVault = true
                )
            }
        } catch (_: Exception) {
            targetFile?.let { if (it.exists()) it.delete() }
            return@withContext null
        }
    }
    fun renamePhysicalFile(oldPath: String, newName: String): String? {
        val oldFile = File(oldPath)
        if (!oldFile.exists()) return null

        val parentDir = oldFile.parentFile ?: return null
        val targetFile = File(parentDir, newName)

        if (oldFile.name.equals(newName, ignoreCase = true) && oldFile.name != newName) {
            // Case-only rename trick (Maths -> temp -> maths)
            val tempFile = File(parentDir, newName + "_temp")
            if (oldFile.renameTo(tempFile)) {
                if (tempFile.renameTo(targetFile)) return targetFile.absolutePath
            }
        } else {
            // Normal Rename (Collisions check implicitly handled by renameTo failing if exists)
            if (!targetFile.exists() && oldFile.renameTo(targetFile)) {
                return targetFile.absolutePath
            }
        }
        return null
    }

}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\data\source\VaultDataSource.kt
``kotlin
// FILE: VaultDataSource.kt
package com.edu.pdf.data.source

import android.content.Context
import android.os.Environment
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class VaultDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val cryptoEngine: VaultCryptoEngine // 🌟 100% Tink Crypto Engine
) {

    // =======================================================
    // 🔒 1. MOVE TO VAULT (Encrypt with Tink)
    // =======================================================
    suspend fun moveToInternalVault(pdf: PdfFile, onOriginalDelete: suspend (String) -> Unit): String? = withContext(Dispatchers.IO) {
        val originalFile = File(pdf.path)
        if (!originalFile.exists()) return@withContext null

        val vaultDir = File(context.filesDir, "secure_vault_core")
        if (!vaultDir.exists()) vaultDir.mkdirs()

        // Storage Check
        // Storage Check (Modern Android Standard)
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as android.os.storage.StorageManager
        val uuid = storageManager.getUuidForPath(vaultDir)
        if (storageManager.getAllocatableBytes(uuid) < originalFile.length() + (50 * 1024 * 1024)) {
            throw Exception("Storage Full! Cannot secure this file.")
        }

        val finalSecureFile = File(vaultDir, "${UUID.randomUUID()}.locked")
        val tmpSecureFile = File(vaultDir, "${finalSecureFile.name}.tmp")

        return@withContext try {
            // 🌟 NAYA: Tink se Encrypt karke likhna
            originalFile.inputStream().use { input ->
                cryptoEngine.getEncryptedOutputStream(tmpSecureFile).use { encryptedOutput ->
                    input.copyTo(encryptedOutput)
                }
            }

            if (tmpSecureFile.renameTo(finalSecureFile)) {
                onOriginalDelete(originalFile.absolutePath)

                // 🌟 ELITE SECURITY FIX: चेक करो कि फाइल सच में डिलीट हुई या नहीं!
                val isDeleted = originalFile.delete()
                if (!isDeleted && originalFile.exists()) {
                    // अगर OS ने डिलीट करने से रोक दिया, तो Vault वाली कॉपी भी डिलीट कर दो (Rollback)
                    finalSecureFile.delete()
                    throw Exception("OS prevented deletion! File is still public.")
                }

                finalSecureFile.absolutePath
            } else {
                tmpSecureFile.delete()
                null
            }
        } catch (e: Exception) {
            tmpSecureFile.delete()
            throw e // Error UI तक भेजो
        }
    }

    // =======================================================
    // 🔓 2. RESTORE FROM VAULT (Decrypt with Tink)
    // =======================================================
    suspend fun restoreFromInternalVault(securePath: String, originalName: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        val secureFile = File(securePath)
        if (!secureFile.exists()) return@withContext null

        val docsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val pdfProDir = File(docsDir, "PdfPro")
        if (!pdfProDir.exists()) pdfProDir.mkdirs()

        var targetFile = File(pdfProDir, originalName)
        var counter = 1
        val nameWithoutExt = targetFile.nameWithoutExtension
        val ext = targetFile.extension.let { if (it.isNotEmpty()) ".$it" else "" }

        while (targetFile.exists()) {
            targetFile = File(pdfProDir, "$nameWithoutExt ($counter)$ext")
            counter++
        }

        val tmpTargetFile = File(pdfProDir, "${targetFile.name}.tmp")

        return@withContext try {
            // 🌟 NAYA: Tink se Decrypt karke wapas bahar nikalna
            cryptoEngine.getEncryptedInputStream(secureFile).use { decryptedInput ->
                tmpTargetFile.outputStream().use { output ->
                    decryptedInput.copyTo(output)
                }
            }

            if (tmpTargetFile.renameTo(targetFile)) {
                secureFile.delete()
                Pair(targetFile.absolutePath, targetFile.absolutePath)
            } else {
                tmpTargetFile.delete()
                null
            }
        } catch (_: Exception) {
            tmpTargetFile.delete()
            null
        }
    }

    // =======================================================
    // 👁️ 3. VIEW INSIDE VAULT (Stream to PDF Viewer)
    // =======================================================
    fun getSecureVaultStreamUri(encryptedPath: String): String {
        return encryptedPath
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\di\DatabaseModule.kt
``kotlin
package com.edu.pdf.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.edu.pdf.data.local.PdfDatabase
import com.edu.pdf.data.local.dao.PdfDao
import com.edu.pdf.data.local.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE managed_folders ADD COLUMN lastOpenedTime INTEGER NOT NULL DEFAULT 0")
        }
    }

    @Provides
    @Singleton
    fun providePdfDatabase(@ApplicationContext context: Context): PdfDatabase {
        return Room.databaseBuilder(
            context,
            PdfDatabase::class.java,
            "pdf_master_db"
        )
            .addMigrations(MIGRATION_9_10) // 🌟 Apply the migration
            .build()
    }

    @Provides
    @Singleton
    fun providePdfDao(database: PdfDatabase): PdfDao {
        return database.pdfDao
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: PdfDatabase): SearchHistoryDao {
        return database.searchHistoryDao
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\di\RepositoryModule.kt
``kotlin
package com.edu.pdf.di

import com.edu.pdf.data.repository.PdfRepositoryImpl
import com.edu.pdf.domain.repository.PdfRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused") // 🌟 2026 Standard: Tells IDE that Hilt handles this, so don't show "unused" warnings
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPdfRepository(
        impl: PdfRepositoryImpl
    ): PdfRepository

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        impl: com.edu.pdf.data.repository.AiRepositoryImpl
    ): com.edu.pdf.domain.repository.AiRepository
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\di\UseCaseModule.kt
``kotlin
package com.edu.pdf.di

import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.CreateFolderUseCase // 🌟 NAYA IMPORT
import com.edu.pdf.domain.usecase.DeleteFolderUseCase
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideScanPdfsUseCase(repository: PdfRepository): ScanPdfsUseCase {
        return ScanPdfsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeletePdfsUseCase(repository: PdfRepository): DeletePdfsUseCase {
        return DeletePdfsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRenamePdfUseCase(repository: PdfRepository): RenamePdfUseCase {
        return RenamePdfUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateFolderUseCase(repository: PdfRepository): CreateFolderUseCase {
        return CreateFolderUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideDeleteFolderUseCase(repository: PdfRepository): DeleteFolderUseCase {
        return DeleteFolderUseCase(repository)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\model\ChatMessage.kt
``kotlin
package com.edu.pdf.domain.model

import androidx.compose.runtime.Immutable

enum class ChatRole { USER, MODEL }

@Immutable
data class ChatMessage(
    val id: String,
    val role: ChatRole,
    val text: String,
    val isStreaming: Boolean = false
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\model\Folder.kt
``kotlin
package com.edu.pdf.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Folder(
    val folderId: String,
    val name: String,
    val parentFolderId: String? = null,
    val pdfCount: Int = 0,
    val isVault: Boolean = false,
    val createdAt: Long = 0L,
    val lastOpenedTime: Long = 0L
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\model\FolderType.kt
``kotlin
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
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\model\HomeItem.kt
``kotlin
package com.edu.pdf.domain.model

import androidx.compose.runtime.Immutable

/**
 * 🌟 GOD MODE WRAPPER:
 * Ye interface Compose ko batayega ki list me Folder aa raha hai ya PDF.
 * Dono ka common parameter 'id' aur 'lastModified' hai taaki sorting flawless ho.
 */
@Immutable
sealed interface HomeItem {
    val id: String
    val lastModified: Long

    data class FolderItem(val folder: Folder) : HomeItem {
        override val id: String = folder.folderId
        override val lastModified: Long = folder.createdAt
    }

    data class PdfItem(val pdf: PdfFile) : HomeItem {
        override val id: String = pdf.id
        override val lastModified: Long = pdf.lastModified
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\model\PdfFile.kt
``kotlin
package com.edu.pdf.domain.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable

@Keep
@Immutable
data class PdfFile(
    val id: String,
    val name: String,
    val path: String,
    val sizeInBytes: Long,
    val lastModified: Long,
    val isFavorite: Boolean = false,
    val lastOpenedTime: Long = 0L,
    val virtualParentId: String? = null, // 🌟 NAYA: Ye file kis folder me hai?
    val isVault: Boolean = false         // 🌟 NAYA: Kya ye file locked hai?
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\model\SortType.kt
``kotlin
package com.edu.pdf.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class SortType {
    NAME_ASC, NAME_DESC, DATE_DESC, DATE_ASC, SIZE_DESC, SIZE_ASC
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\ocr\TextRecognitionEngine.kt
``kotlin
package com.edu.pdf.domain.ocr

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

data class OcrTextBlock(
    val text: String,
    val boundingBox: Rect?,
    val lineCount: Int
)

class TextRecognitionEngine @Inject constructor() {
    private val recognizer = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())

    suspend fun extractTextFromBitmap(bitmap: Bitmap): Result<List<OcrTextBlock>> = suspendCancellableCoroutine { continuation ->
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                if (continuation.isActive) {
                    val extractedLines = mutableListOf<OcrTextBlock>()

                    for (block in visionText.textBlocks) {
                        for (line in block.lines) {
                            extractedLines.add(
                                OcrTextBlock(
                                    text = line.text,
                                    boundingBox = line.boundingBox,
                                    lineCount = 1
                                )
                            )
                        }
                    }

                    continuation.resume(Result.success(extractedLines))
                }
            }
            .addOnFailureListener { exception ->
                if (continuation.isActive) {
                    continuation.resume(Result.failure(exception))
                }
            }

        continuation.invokeOnCancellation {
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\repository\AiRepository.kt
``kotlin
package com.edu.pdf.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    // Ye function user ka text aur current page ki photo dono accept karega
    fun chatWithPdfStream(query: String, pageBitmap: Bitmap?): Flow<String>
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\repository\PdfRepository.kt
``kotlin
package com.edu.pdf.domain.repository

import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface PdfRepository {
    // 🌟 ELITE FIX: Ab parameters IDs nahi, balki Paths hain
    fun getManagedFolders(parentPath: String?, isVault: Boolean = false): Flow<List<Folder>>
    fun getAllManagedFolders(isVault: Boolean = false): Flow<List<Folder>>
    fun getManagedPdfs(parentPath: String?, isVault: Boolean = false): Flow<List<PdfFile>>

    suspend fun createManagedFolder(name: String, parentPath: String?, isVault: Boolean = false): Result<String>
    suspend fun renameManagedFolder(oldPath: String, newName: String): Result<Unit>
    suspend fun deleteManagedFolder(path: String): Result<Unit>

    suspend fun importPdfFromUri(uriString: String, targetPath: String?, isVault: Boolean, isPhysicalFolder: Boolean = false): Result<Unit>
    suspend fun movePdfsToVirtualFolder(pdfIds: List<String>, targetPath: String?, isVault: Boolean): Int
    suspend fun moveFolderToVirtualFolder(folderPath: String, targetPath: String?, isVault: Boolean): Result<Unit>

    fun getRecentPdfs(): Flow<List<PdfFile>>

    fun getUncategorizedPdfs(sortType: SortType): Flow<List<PdfFile>>
    fun getAllPdfs(sortType: SortType): Flow<List<PdfFile>>

    // 🌟 THE ELITE FIX: Home Screen Paging Support
    fun getAllPdfsPaged(sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>>
    fun getFavoritePdfs(sortType: SortType): Flow<List<PdfFile>>
    fun searchPdfs(query: String): Flow<List<PdfFile>>
    suspend fun scanAndSavePdfs()
    suspend fun checkFileExists(fileId: String): Boolean
    suspend fun updateLastOpenedTime(pdfId: String, time: Long)
    suspend fun toggleFavorite(pdfId: String, isFavorite: Boolean)
    suspend fun renamePdf(pdf: PdfFile, newName: String): Boolean
    suspend fun deletePdfs(pdfs: List<PdfFile>): Boolean

    fun getRecentSearchQueries(): Flow<List<String>>
    suspend fun saveSearchQuery(query: String)
    suspend fun deleteSearchQuery(query: String)
    suspend fun clearAllSearchHistory()

    fun getRecentFolders(): Flow<List<Folder>>
    suspend fun updateFolderLastOpenedTime(path: String, time: Long)
    suspend fun searchPdfsFast(query: String, isVault: Boolean): List<PdfFile>
    fun getSecureVaultStreamUri(encryptedPath: String): String

    fun getPdfsInPhysicalFolder(folderPath: String): Flow<List<PdfFile>>
    // इन दोनों को पुराने वाले से REPLACE कर दें
    fun getPaginatedPdfsInPhysicalFolder(folderPath: String, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>>

    fun getPaginatedManagedPdfs(parentPath: String?, isVault: Boolean = false, sortType: SortType): Flow<androidx.paging.PagingData<PdfFile>>

    suspend fun getUncategorizedPdfIdsFast(): List<String>
    suspend fun getFavoritePdfIdsFast(): List<String>
    suspend fun getManagedPdfIdsFast(parentPath: String?, isVault: Boolean): List<String>
    suspend fun getPhysicalFolderPdfIdsFast(folderPath: String): List<String>

}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\usecase\CreateFolderUseCase.kt
``kotlin
package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class CreateFolderUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(name: String, parentId: String?, isVault: Boolean = false): Result<String> {
        if (name.isBlank()) return Result.failure(Exception("Folder name cannot be empty"))
        return repository.createManagedFolder(name, parentId, isVault)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\usecase\DeleteFolderUseCase.kt
``kotlin
package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(folderId: String): Result<Unit> {
        return repository.deleteManagedFolder(folderId)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\usecase\DeletePdfsUseCase.kt
``kotlin
package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class DeletePdfsUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(pdfs: List<PdfFile>): Boolean {
        // 🌟 SMART FIX: Agar list khali hai, toh repository ko call mat karo
        if (pdfs.isEmpty()) return true

        return repository.deletePdfs(pdfs)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\usecase\RenamePdfUseCase.kt
``kotlin
package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class RenamePdfUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke(pdf: PdfFile, newName: String): Boolean {
        // Yahan business logic aa sakta hai, jaise name validation
        if (newName.isBlank()) return false
        return repository.renamePdf(pdf, newName)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\domain\usecase\ScanPdfsUseCase.kt
``kotlin
package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import javax.inject.Inject

class ScanPdfsUseCase @Inject constructor(
    private val repository: PdfRepository
) {
    suspend operator fun invoke() = repository.scanAndSavePdfs()
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\notification\PdfNotificationHelper.kt
``kotlin
package com.edu.pdf.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.edu.pdf.MainActivity
import com.edu.pdf.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val CHANNEL_ID = "pdf_file_updates" // 🌟 STANDARD CHANNEL

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "PDF File Updates", 
                NotificationManager.IMPORTANCE_DEFAULT // 🌟 STANDARD IMPORTANCE
            ).apply {
                description = "Notifies when a PDF is added or changed"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNewPdfNotification(fileName: String, filePath: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("pdf_to_open", filePath)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            filePath.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setContentTitle("PDF Update")
            .setContentText(fileName)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 🌟 STANDARD PRIORITY
            .setDefaults(NotificationCompat.DEFAULT_ALL) // 🌟 SYSTEM SOUND/VIBRATE
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(filePath.hashCode(), notification)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\PremiumBottomBar.kt
``kotlin
package com.edu.pdf.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.edu.pdf.presentation.navigation.Screen

data class BottomNavItem<T : Any>(
    val title: String,
    val icon: ImageVector,
    val route: T
)

// 🌟 Ye Folders, Tools aur Settings me chalega
@Composable
fun PremiumBottomBar(navController: NavHostController) {
    // 🌟 SLIM FIX: NavigationBar hatakar Surface lagaya gaya
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding() // Edge to Edge!
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PremiumBottomBarItems(navController)
        }
    }
}

// 🌟 Ye Items dega jo hum HomeScreen (aur upar wale bar) me use karenge
@Composable
fun RowScope.PremiumBottomBarItems(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.Home),
        BottomNavItem("Folders", Icons.Default.Folder, Screen.Folders),
        BottomNavItem("Tools", Icons.Default.AutoFixHigh, Screen.Tools),
        BottomNavItem("Settings", Icons.Default.Settings, Screen.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val haptic = LocalHapticFeedback.current

    items.forEach { item ->
        val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
        val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

        // 🌟 Ziddi NavigationBarItem ko HATA DIYA gaya hai, ye Custom Column Slim hai!
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (!isSelected) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                .padding(vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(imageVector = item.icon, contentDescription = item.title, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.title, fontSize = 11.sp, color = color, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium)
        }
    }
}

// Tablet UI ke liye purana code same rakha gaya hai
@Composable
fun PremiumNavigationRail(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.Home),
        BottomNavItem("Folders", Icons.Default.Folder, Screen.Folders),
        BottomNavItem("Tools", Icons.Default.AutoFixHigh, Screen.Tools),
        BottomNavItem("Settings", Icons.Default.Settings, Screen.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

            NavigationRailItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\PremiumBreadcrumbs.kt
``kotlin
package com.edu.pdf.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.Folder

@Composable
fun PremiumBreadcrumbs(
    breadcrumbs: List<Folder>,
    rootName: String = "Home",
    onNavigate: (Folder?) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()

    // Auto-scroll logic waisa hi powerful rakha gaya hai
    LaunchedEffect(breadcrumbs.size) {
        if (breadcrumbs.isNotEmpty()) {
            listState.animateScrollToItem(breadcrumbs.size)
        } else {
            listState.animateScrollToItem(0)
        }
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // 🌟 Vertical padding thodi kam ki taaki sleek lage
        state = listState,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 1. Root Node (e.g., Home)
        item {
            EliteBreadcrumbItem(
                name = rootName,
                isLast = breadcrumbs.isEmpty(),
                onClick = {
                    if (breadcrumbs.isNotEmpty()) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate(null)
                    }
                }
            )
        }

        // 2. Sub-folders (Dynamic Path)
        items(breadcrumbs) { folder ->
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 4.dp).size(16.dp), // 🌟 Icon thoda chota kiya for premium look
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            val isLast = folder == breadcrumbs.last()
            EliteBreadcrumbItem(
                name = folder.name,
                isLast = isLast,
                onClick = {
                    if (!isLast) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate(folder)
                    }
                }
            )
        }
    }
}

@Composable
private fun EliteBreadcrumbItem(name: String, isLast: Boolean, onClick: () -> Unit) {
    // 🌟 THE MAGIC: Purane folders transparent honge, sirf current folder highlight hoga
    val bgColor = if (isLast) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val textColor = if (isLast) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val fontWeight = if (isLast) FontWeight.Bold else FontWeight.Medium

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)) // 🌟 Modern soft rectangle shape
            .background(bgColor)
            .clickable(enabled = !isLast, onClick = onClick)
            .padding(horizontal = if (isLast) 12.dp else 6.dp, vertical = 6.dp), // 🌟 Dynamic padding
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = textColor,
            fontWeight = fontWeight,
            fontSize = 14.sp, // 🌟 Sleek professional size
            maxLines = 1,
            overflow = TextOverflow.Ellipsis // 🌟 Lamba naam hone par '...' dikhayega
        )
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\PremiumFolderComponents.kt
``kotlin
package com.edu.pdf.presentation.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PremiumFolderListItem(
    name: String,
    itemCount: Int,
    icon: ImageVector = Icons.Default.Folder,
    iconTint: Color = Color(0xFFFFC107), // Premium Yellow
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    showMoreOptions: Boolean = false, // True for Home Screen (3 dots), False for Picker (Chevron >)
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onMoreOptionsClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // 🌟 EXACT FIX: Yahan se `.clip(RoundedCornerShape(12.dp))` hata diya gaya hai!
            // Ab background aur touch effect ekdum plain aur flat (edge-to-edge) aayega.
            .run {
                if (onLongClick != null) combinedClickable(onClick = onClick, onLongClick = onLongClick)
                else clickable { onClick() }
            }
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🌟 SMART ICON LOGIC: Agar normal folder hai to Nayi Photo lagao, warna WhatsApp/Camera ka icon chalne do!
        if (icon == Icons.Default.Folder || icon == Icons.Rounded.Folder) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.edu.pdf.R.drawable.premium_folder1),
                contentDescription = name,
                modifier = Modifier.size(52.dp)
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = iconTint,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$itemCount items",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Smart Action Button Logic
        if (isSelectionMode) {
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        } else if (showMoreOptions && onMoreOptionsClick != null) {
            IconButton(onClick = onMoreOptionsClick, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\SmartSelectionBottomBar.kt
``kotlin
package com.edu.pdf.presentation.common

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.HomeItem

@Composable
fun SmartSelectionBottomBar(
    selectedItems: List<HomeItem>,
    tabIndex: Int,
    onDelete: () -> Unit,
    onMove: () -> Unit,
    onMerge: () -> Unit,
    onShare: () -> Unit,
    onRemoveFromRecent: () -> Unit,
    onUnfavorite: () -> Unit
) {
    // 🌟 STRICT MVI: Logic yahin calculate hoga (Single Source of Truth)
    val folderCount = selectedItems.count { it is HomeItem.FolderItem }
    val pdfCount = selectedItems.count { it is HomeItem.PdfItem }
    val totalCount = selectedItems.size
    val hasFolderSelected = folderCount > 0

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth().navigationBarsPadding() // Edge-to-Edge, no fixed height
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            // 1. DELETE
            SmartActionItem(
                title = "Delete",
                icon = Icons.Default.Delete,
                enabled = totalCount > 0,
                disabledMessage = "Select items to delete",
                onClick = onDelete
            )

            // 2. CONTEXTUAL MIDDLE BUTTON
            when (tabIndex) {
                0 -> SmartActionItem(
                    title = "Remove",
                    icon = Icons.Default.HistoryToggleOff,
                    enabled = totalCount > 0,
                    disabledMessage = "Select items to remove",
                    onClick = onRemoveFromRecent
                )
                1 -> SmartActionItem(
                    title = "Move",
                    icon = Icons.AutoMirrored.Filled.DriveFileMove,
                    enabled = totalCount > 0,
                    disabledMessage = "Select items to move",
                    onClick = onMove
                )
                2 -> SmartActionItem(
                    title = "Unfavorite",
                    icon = Icons.Default.BookmarkRemove,
                    enabled = totalCount > 0,
                    disabledMessage = "Select items to unfavorite",
                    onClick = onUnfavorite
                )
            }

            // 3. MERGE (Smart Logic)
            val mergeEnabled = pdfCount >= 2 && !hasFolderSelected
            val mergeMsg = if (hasFolderSelected) "Cannot merge folders" else "Select at least 2 PDFs to merge"
            SmartActionItem(
                title = "Merge",
                icon = Icons.AutoMirrored.Filled.CallMerge,
                enabled = mergeEnabled,
                disabledMessage = mergeMsg,
                onClick = onMerge
            )

            // 4. SHARE (Smart Logic)
            val shareEnabled = totalCount > 0 && !hasFolderSelected
            val shareMsg = if (hasFolderSelected) "Cannot share folders" else "Select PDFs to share"
            SmartActionItem(
                title = "Share",
                icon = Icons.Default.Share,
                enabled = shareEnabled,
                disabledMessage = shareMsg,
                onClick = onShare
            )
        }
    }
}

@Composable
private fun RowScope.SmartActionItem(
    title: String,
    icon: ImageVector,
    enabled: Boolean,
    disabledMessage: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val animatedColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
        animationSpec = tween(durationMillis = 300),
        label = "ColorAnimation_$title"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable {
                if (enabled) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                } else {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast.makeText(context, disabledMessage, Toast.LENGTH_SHORT).show()
                }
            }
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = title, tint = animatedColor, modifier = Modifier.size(24.dp))
        Text(text = title, fontSize = 11.sp, color = animatedColor, fontWeight = FontWeight.Medium)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\UniversalTopBar.kt
``kotlin
package com.edu.pdf.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalTopBar(
    title: String,
    currentTab: Int = 1,
    isGridView: Boolean = false,
    onSelectAllClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    onSortClick: (() -> Unit)? = null,
    onToggleView: (() -> Unit)? = null,
    onCreateFolderClick: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            if (onSearchClick != null) IconButton(onClick = onSearchClick) { Icon(Icons.Default.Search, "Search") }
            if (onCreateFolderClick != null) IconButton(onClick = onCreateFolderClick) { Icon(Icons.Default.CreateNewFolder, "Create Folder") }
            if (onSortClick != null && currentTab != 0) IconButton(onClick = onSortClick) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
            if (onToggleView != null) IconButton(onClick = onToggleView) { Icon(if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView, "Toggle View") }
            if (onSelectAllClick != null) IconButton(onClick = onSelectAllClick) { Icon(Icons.Outlined.CheckBox, "Select Files") }
        },
        windowInsets = TopAppBarDefaults.windowInsets,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            // 🌟 PRO FIX: 100% Sheesha! Ab color parent Column se aayega.
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            scrolledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\picker\GlobalPdfPickerSheet.kt
``kotlin
package com.edu.pdf.presentation.common.picker

import android.text.format.DateUtils
import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.home.components.PdfThumbnail
import com.edu.pdf.presentation.search.components.HighlightedText

@OptIn(ExperimentalMaterial3Api::class)
// 1. THE ROUTE (ये सिर्फ ViewModel को होल्ड करेगा)
@Composable
fun GlobalPdfPickerSheet(
    onDismiss: () -> Unit,
    onPdfsSelected: (List<String>) -> Unit,
    viewModel: PdfPickerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 🌟 NAYA: ViewModel ka Event sunkar sheet dismiss karega
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is PdfPickerEvent.Dismiss -> onDismiss()
            }
        }
    }

    GlobalPdfPickerSheetContent(
        state = state,
        onPdfsSelected = onPdfsSelected,
        onAction = viewModel::onAction
    )
}

// 2. THE PURE UI (इसे ViewModel का कोई आईडिया नहीं है)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalPdfPickerSheetContent(
    state: PdfPickerState,
    onPdfsSelected: (List<String>) -> Unit,
    onAction: (PdfPickerAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Dialog(
        // 🌟 MVI STRICT FIX: Saara kachra delete! Bas action pass kiya.
        onDismissRequest = { onAction(PdfPickerAction.NavigateBack) },
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().imePadding(), // 🌟 EXACT FIX 1: Keyboard aane par size adjust hoga
            contentWindowInsets = WindowInsets(0.dp),       // 🌟 EXACT FIX 2: Dialog padding ko reset kiya
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp) {
                    Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { onAction(PdfPickerAction.CloseSheet) }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                            Text("Select PDFs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

                            AnimatedVisibility(visible = state.selectedIds.isNotEmpty()) {
                                Button(
                                    onClick = {
                                        onPdfsSelected(state.selectedIds.toList())
                                        onAction(PdfPickerAction.CloseSheet) // 🌟 NAYA
                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Add (${state.selectedIds.size})", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // 🌟 Premium Search Bar
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { onAction(PdfPickerAction.OnSearchQueryChange(it)) },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("Search files...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        if (state.searchQuery.isBlank() && state.breadcrumbs.isNotEmpty()) {
                            com.edu.pdf.presentation.common.PremiumBreadcrumbs(
                                breadcrumbs = state.breadcrumbs,
                                rootName = "Root",
                                onNavigate = { folder -> onAction(PdfPickerAction.NavigateToFolder(folder)) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (state.items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (state.searchQuery.isBlank()) "Folder is empty" else "No matching PDFs", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                    items(items = state.items, key = { it.id }) { item ->
                        when (item) {
                            is HomeItem.FolderItem -> {
                                PickerFolderRow(
                                    folder = item.folder,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onAction(PdfPickerAction.NavigateToFolder(item.folder))
                                    }
                                )
                            }
                            is HomeItem.PdfItem -> {
                                val isSelected = state.selectedIds.contains(item.pdf.id)
                                PickerPdfRow(
                                    pdf = item.pdf,
                                    searchQuery = state.searchQuery,
                                    isSelected = isSelected,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onAction(PdfPickerAction.ToggleSelection(item.pdf.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 🌟 ZERO BLOAT UI COMPONENTS

// ✅ YAHAN PASTE KAREIN
@Composable
private fun PickerFolderRow(folder: Folder, onClick: () -> Unit) {
    com.edu.pdf.presentation.common.PremiumFolderListItem(
        name = folder.name,
        itemCount = folder.pdfCount,
        showMoreOptions = false,
        onClick = onClick
    )
}

@Composable
private fun PickerPdfRow(pdf: com.edu.pdf.domain.model.PdfFile, searchQuery: String, isSelected: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current
    val displaySize = remember(pdf.sizeInBytes) { Formatter.formatShortFileSize(context, pdf.sizeInBytes) }
    val displayDate = remember(pdf.lastModified) {
        DateUtils.getRelativeTimeSpanString(if (pdf.lastModified < 1000000000000L) pdf.lastModified * 1000 else pdf.lastModified, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(48.dp).height(60.dp).clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            PdfThumbnail(pdf = pdf, modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            HighlightedText(
                text = pdf.name,
                query = searchQuery, // 🌟 HIGHLIGHT MAGIC ENABLED!
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("$displayDate  •  $displaySize", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(
            // 🌟 CONSISTENCY FIX: Yahan bhi Square checkbox aayega
            imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\picker\MovePickerSheet.kt
``kotlin
package com.edu.pdf.presentation.common.picker

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.presentation.common.PremiumBreadcrumbs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovePickerSheetRoute(
    folders: List<Folder>,
    onDismiss: () -> Unit,
    onTargetSelected: (String?) -> Unit,
    viewModel: MovePickerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(folders) {
        viewModel.onAction(MovePickerAction.UpdateFolders(folders))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MovePickerEvent.MoveToTarget -> {
                    onTargetSelected(event.targetFolderId)
                    // Move hone ke baad Root par reset karega
                    viewModel.onAction(MovePickerAction.NavigateTo(null))
                    onDismiss()
                }
                is MovePickerEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    MovePickerSheetContent(
        state = state,
        onAction = viewModel::onAction,
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovePickerSheetContent(
    state: MovePickerState,
    onAction: (MovePickerAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            if (state.currentParentId != null) {
                onAction(MovePickerAction.NavigateBack)
            } else {
                onDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // LATEST PREDICTIVE BACK HANDLER
        BackHandler(enabled = state.currentParentId != null) {
            onAction(MovePickerAction.NavigateBack)
        }

        Scaffold(
            // 🌟 STRICT FIX 1: Yahan se imePadding() HATA DIYA HAI!
            // Ab keyboard aane par button apni jagah par fix rahega.
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp) {
                    Column(modifier = Modifier.statusBarsPadding()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                            }
                            Text(
                                text = "Move to...",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                            )
                            IconButton(onClick = { onAction(MovePickerAction.ToggleCreateFolderDialog(true)) }) {
                                Icon(Icons.Default.CreateNewFolder, contentDescription = "New Folder", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        PremiumBreadcrumbs(
                            breadcrumbs = state.breadcrumbs,
                            rootName = "Home",
                            onNavigate = { folder -> onAction(MovePickerAction.NavigateTo(folder?.folderId)) },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        )
                    }
                }
            }
            // 🌟 STRICT FIX 2: Scaffold ka bottomBar hata diya gaya hai.
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()) // Sirf top padding di
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.subFolders.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No sub-folders here", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        // 🌟 STRICT FIX 3: Niche 120.dp ki jagah chhodi taaki list button ke upar aa sake
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(state.subFolders, key = { it.folderId }) { folder ->
                            com.edu.pdf.presentation.common.PremiumFolderListItem(
                                name = folder.name,
                                itemCount = folder.pdfCount,
                                showMoreOptions = false,
                                onClick = { onAction(MovePickerAction.NavigateTo(folder.folderId)) }
                            )
                        }
                    }
                }

                // 🌟 ELITE 2026 UI FIX: Floating Docked Button with Glass Fade
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = { onAction(MovePickerAction.ConfirmMoveHere) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Move Here", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }

        // ... Yahan se aapka if (state.isCreatingFolder) wala AlertDialog code aayega jo pehle se ekdum sahi hai ...
        if (state.isCreatingFolder) {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                keyboard?.show()
            }

            AlertDialog(
                onDismissRequest = { onAction(MovePickerAction.ToggleCreateFolderDialog(false)) },
                title = { Text("Create New Folder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = state.newFolderName,
                        onValueChange = { onAction(MovePickerAction.UpdateFolderName(it)) },
                        label = { Text("Folder Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                confirmButton = {
                    Button(onClick = { onAction(MovePickerAction.CreateAndEnterFolder) }, enabled = state.newFolderName.trim().isNotEmpty()) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(MovePickerAction.ToggleCreateFolderDialog(false)) }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\picker\MovePickerState.kt
``kotlin
package com.edu.pdf.presentation.common.picker

import com.edu.pdf.domain.model.Folder

// Pure Immutable State
data class MovePickerState(
    val allFolders: List<Folder> = emptyList(), // Holds the entire folder tree
    val currentParentId: String? = null,
    val breadcrumbs: List<Folder> = emptyList(),
    val subFolders: List<Folder> = emptyList(),
    val isCreatingFolder: Boolean = false,
    val newFolderName: String = "",
    val isLoading: Boolean = false
)

// Actions triggered from UI
sealed interface MovePickerAction {
    data class UpdateFolders(val folders: List<Folder>) : MovePickerAction // Receives folders from Home
    data class NavigateTo(val folderId: String?) : MovePickerAction

    data object NavigateBack : MovePickerAction
    data class UpdateFolderName(val name: String) : MovePickerAction
    data class ToggleCreateFolderDialog(val show: Boolean) : MovePickerAction
    data object CreateAndEnterFolder : MovePickerAction
    data object ConfirmMoveHere : MovePickerAction
}

// One-time Events to UI
sealed interface MovePickerEvent {
    data class MoveToTarget(val targetFolderId: String?) : MovePickerEvent
    data class ShowSnackbar(val message: String) : MovePickerEvent
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\picker\MovePickerViewModel.kt
``kotlin
package com.edu.pdf.presentation.common.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovePickerViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovePickerState())
    val state = _state.asStateFlow()

    private val _events = Channel<MovePickerEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: MovePickerAction) {
        when (action) {
            is MovePickerAction.UpdateFolders -> {
                _state.update { currentState ->
                    val subFolders = action.folders.filter { it.parentFolderId == currentState.currentParentId && !it.isVault }.sortedBy { it.name.lowercase() }
                    val breadcrumbs = calculateBreadcrumbs(action.folders, currentState.currentParentId)
                    currentState.copy(allFolders = action.folders, subFolders = subFolders, breadcrumbs = breadcrumbs, isLoading = false)
                }
            }
            is MovePickerAction.NavigateTo -> {
                _state.update { currentState ->
                    val subFolders = currentState.allFolders.filter { it.parentFolderId == action.folderId && !it.isVault }.sortedBy { it.name.lowercase() }
                    val breadcrumbs = calculateBreadcrumbs(currentState.allFolders, action.folderId)
                    currentState.copy(currentParentId = action.folderId, subFolders = subFolders, breadcrumbs = breadcrumbs)
                }
            }
            // 🌟 LATEST 2026 MVI LOGIC: Ek step piche jane ka calculation
            is MovePickerAction.NavigateBack -> {
                _state.update { currentState ->
                    // Breadcrumbs se pichle folder ki ID nikaalo (dropLast(1) karke)
                    val parentId = currentState.breadcrumbs.dropLast(1).lastOrNull()?.folderId

                    val subFolders = currentState.allFolders.filter { it.parentFolderId == parentId && !it.isVault }.sortedBy { it.name.lowercase() }
                    val breadcrumbs = calculateBreadcrumbs(currentState.allFolders, parentId)
                    currentState.copy(currentParentId = parentId, subFolders = subFolders, breadcrumbs = breadcrumbs)
                }
            }
            is MovePickerAction.UpdateFolderName -> {
                _state.update { it.copy(newFolderName = action.name) }
            }
            is MovePickerAction.ToggleCreateFolderDialog -> {
                _state.update { it.copy(isCreatingFolder = action.show, newFolderName = "") }
            }
            is MovePickerAction.CreateAndEnterFolder -> createAndEnterFolder()
            is MovePickerAction.ConfirmMoveHere -> {
                viewModelScope.launch {
                    _events.send(MovePickerEvent.MoveToTarget(_state.value.currentParentId))
                }
            }
        }
    }

    private fun calculateBreadcrumbs(allFolders: List<Folder>, currentId: String?): List<Folder> {
        val breadcrumbs = mutableListOf<Folder>()
        var curr = allFolders.find { it.folderId == currentId }
        while (curr != null) {
            breadcrumbs.add(0, curr)
            curr = allFolders.find { it.folderId == curr.parentFolderId }
        }
        return breadcrumbs
    }

    private fun createAndEnterFolder() {
        val name = _state.value.newFolderName.trim()
        if (name.isBlank()) return

        val currentParent = _state.value.currentParentId

        viewModelScope.launch(Dispatchers.IO) {
            // 🌟 FIX: डायलॉग को खुला रहने दो, सिर्फ लोडिंग ऑन करो
            _state.update { it.copy(isLoading = true) }
            val result = repository.createManagedFolder(name, currentParent, isVault = false)

            result.onSuccess { newFolderId ->
                onAction(MovePickerAction.NavigateTo(newFolderId))
                // 🌟 FIX: जब सच में फोल्डर बन जाए, तभी डायलॉग को बंद करो (isCreatingFolder = false)
                _state.update { it.copy(isCreatingFolder = false, newFolderName = "", isLoading = false) }
            }.onFailure { e ->
                // 🌟 FIX: अगर एरर आया, तो बस लोडिंग बंद होगी और टोस्ट आएगा, डायलॉग/कीबोर्ड खुला रहेगा!
                _state.update { it.copy(isLoading = false) }
                _events.send(MovePickerEvent.ShowSnackbar(e.message ?: "Error creating folder"))
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\common\picker\PdfPickerViewModel.kt
``kotlin
package com.edu.pdf.presentation.common.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 🌟 1. STRICT MVI STATE (No separate variables)
data class PdfPickerState(
    val currentFolderId: String? = null,
    val breadcrumbs: List<Folder> = emptyList(),
    val items: List<HomeItem> = emptyList(),
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

sealed interface PdfPickerAction {
    data class NavigateToFolder(val folder: Folder?) : PdfPickerAction
    data class OnSearchQueryChange(val query: String) : PdfPickerAction
    data class ToggleSelection(val pdfId: String) : PdfPickerAction
    data object ClearSelection : PdfPickerAction
    // 🌟 MVI STRICT FIX: Naye actions
    data object NavigateBack : PdfPickerAction
    data object CloseSheet : PdfPickerAction
}

// 🌟 MVI STRICT FIX: ViewModel se UI ko band karne ka event
sealed interface PdfPickerEvent {
    data object Dismiss : PdfPickerEvent
}

@HiltViewModel
class PdfPickerViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    // 🌟 THE ELITE FIX: Single Source of Truth
    private val _state = MutableStateFlow(PdfPickerState())
    val state = _state.asStateFlow()
    // 🌟 NAYA: Event Channel
    private val _events = Channel<PdfPickerEvent>()
    val events = _events.receiveAsFlow()
    private var searchJob: Job? = null
    private var dataLoadJob: Job? = null

    init {
        loadFolderData(null) // Load root folder initially
    }

    // 🌟 THE REDUCER: UI sirf Intents bhejta hai, aur ye function unhe handle karta hai
    fun onAction(action: PdfPickerAction) {
        when (action) {
            is PdfPickerAction.NavigateToFolder -> {
                val folderId = action.folder?.folderId
                _state.update { it.copy(currentFolderId = folderId, searchQuery = "", isLoading = true) }
                loadFolderData(folderId)
            }
            is PdfPickerAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query, isLoading = true) }
                executeSearch(action.query)
            }
            is PdfPickerAction.ToggleSelection -> {
                _state.update { currentState ->
                    val currentSelection = currentState.selectedIds
                    val newSelection = if (currentSelection.contains(action.pdfId)) {
                        currentSelection.remove(action.pdfId)
                    } else {
                        currentSelection.add(action.pdfId)
                    }
                    currentState.copy(selectedIds = newSelection)
                }
            }
            is PdfPickerAction.ClearSelection -> {
                _state.update { it.copy(selectedIds = persistentSetOf()) }
            }
            // 🌟 NAYA: UI se saara back calculation yahan aa gaya
            // 🌟 PURE MVI ELITE FIX: 3-Stage Navigation State
            is PdfPickerAction.NavigateBack -> {
                val currentBreadcrumbs = _state.value.breadcrumbs
                if (currentBreadcrumbs.size > 1) {
                    val parentFolder = currentBreadcrumbs[currentBreadcrumbs.size - 2]
                    onAction(PdfPickerAction.NavigateToFolder(parentFolder))
                } else if (currentBreadcrumbs.size == 1) {
                    onAction(PdfPickerAction.NavigateToFolder(null))
                } else {

                    onAction(PdfPickerAction.CloseSheet)
                }
            }
            is PdfPickerAction.CloseSheet -> {
                onAction(PdfPickerAction.ClearSelection)
                viewModelScope.launch { _events.send(PdfPickerEvent.Dismiss) }
            }
        }
    }

    private fun loadFolderData(folderId: String?) {
        dataLoadJob?.cancel()
        dataLoadJob = viewModelScope.launch(Dispatchers.IO) {

            val foldersFlow = repository.getManagedFolders(folderId, isVault = false)

            // 🌟 ELITE FIX: Agar Picker Root par hai, toh pure phone ki PDF lao!
            val pdfsFlow = if (folderId == null) {
                repository.getAllPdfs(com.edu.pdf.domain.model.SortType.DATE_DESC)
            } else {
                repository.getManagedPdfs(folderId, isVault = false)
            }

            combine(
                foldersFlow,
                pdfsFlow,
                repository.getAllManagedFolders(isVault = false)
            ) { folders, pdfs, allFolders ->
                // Generate Breadcrumbs
                val breadcrumbList = mutableListOf<Folder>()
                var curr = allFolders.find { it.folderId == folderId }
                while (curr != null) {
                    breadcrumbList.add(0, curr)
                    curr = allFolders.find { it.folderId == curr.parentFolderId }
                }

                val items = folders.map { HomeItem.FolderItem(it) }.sortedBy { it.folder.name.lowercase() } +
                        pdfs.map { HomeItem.PdfItem(it) }.sortedByDescending { it.pdf.lastModified }

                Pair(breadcrumbList, items)
            }.collect { (breadcrumbs, items) ->
                _state.update { it.copy(breadcrumbs = breadcrumbs, items = items, isLoading = false) }
            }
        }
    }

    private fun executeSearch(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            loadFolderData(_state.value.currentFolderId)
            return
        }

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300) // 🌟 PRO FIX: Debounce! User ke type karte hi DB crash hone se bachayega
            repository.searchPdfsFast(query, isVault = false).let { pdfs ->
                val items = pdfs.map { HomeItem.PdfItem(it) }
                _state.update { it.copy(items = items, isLoading = false) }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\core\MainAppScreen.kt
``kotlin
package com.edu.pdf.presentation.core

import android.os.Environment
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.presentation.common.PremiumBottomBar
import com.edu.pdf.presentation.common.PremiumNavigationRail
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.folders.FoldersScreen
import com.edu.pdf.presentation.folders.UnifiedFolderScreen
import com.edu.pdf.presentation.folders.vault.VaultScreen
import com.edu.pdf.presentation.home.HomeScreenWrapper
import com.edu.pdf.presentation.home.HomeViewModel
import com.edu.pdf.presentation.navigation.Screen
import com.edu.pdf.presentation.pdfviewer.PdfViewerScreen
import com.edu.pdf.presentation.search.SearchScreen
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

fun hasStoragePermission(): Boolean {
    return Environment.isExternalStorageManager()
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    LocalContext.current

    val windowSizeClass = calculateWindowSizeClass(activity = androidx.activity.compose.LocalActivity.current ?: return)
    val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val startScreen: Screen = remember {
        if (hasStoragePermission()) Screen.Home else Screen.Permission
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isFullScreen = currentRoute?.contains(Screen.PdfViewer::class.simpleName ?: "") == true ||
            currentRoute?.contains(Screen.Vault::class.simpleName ?: "") == true ||
            currentRoute?.contains(Screen.Permission::class.simpleName ?: "") == true ||
            currentRoute?.contains(Screen.Search::class.simpleName ?: "") == true

    Row(modifier = Modifier.fillMaxSize()) {
        if (isTablet && !isFullScreen) {
            PremiumNavigationRail(navController = navController)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startScreen,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                composable<Screen.Permission> {
                    PremiumPermissionScreen(onPermissionGranted = {
                        navController.navigate(Screen.Home) { popUpTo(Screen.Permission) { inclusive = true } }
                    })
                }

                // 🌟 FIX: Sabko properly navController aur isTablet pass kar diya hai
                homeSection(navController = navController, isTablet = isTablet)
                searchSection(navController = navController)
                pdfViewerSection(navController = navController)
                foldersSection(navController = navController, isTablet = isTablet)
                placeholderSections(navController = navController, isTablet = isTablet)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun NavGraphBuilder.homeSection(navController: NavHostController, isTablet: Boolean) {
    composable<Screen.Home> {
        val paneNavigator = rememberListDetailPaneScaffoldNavigator<Any>()
        val scope = rememberCoroutineScope()

        // 🌟 2026 ELITE FIX: System Back Button handler for Tablet
        // Ye line ensure karegi ki tablet par back dabane se sirf detail pane band ho, app nahi
        androidx.activity.compose.BackHandler(enabled = paneNavigator.canNavigateBack()) {
            scope.launch {
                paneNavigator.navigateBack()
            }
        }

        NavigableListDetailPaneScaffold(
            navigator = paneNavigator,
            listPane = {
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreenWrapper(
                    viewModel = homeViewModel,
                    navController = navController,
                    onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
                    onFolderClick = { id, name, type ->
                        // 🌟 THE MAGIC FIX: Mobile aur Tablet ke liye alag logic
                        if (isTablet) {
                            // Agar Tablet hai, toh split-pane mein kholo
                            scope.launch {
                                paneNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    contentKey = Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type)
                                )
                            }
                        } else {
                            // Agar Phone hai, toh normal nav push karo (Ye step-by-step backstack banayega!)
                            navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type))
                        }
                    },
                    onSearchClick = { navController.navigate(Screen.Search) }
                )
            },
            detailPane = {
                val folderArgs = paneNavigator.currentDestination?.contentKey as? Screen.UnifiedFolder
                if (folderArgs != null) {
                    androidx.compose.runtime.key(folderArgs.folderId) {
                        UnifiedFolderScreen(
                            folderId = folderArgs.folderId,
                            folderName = folderArgs.folderName,
                            folderType = folderArgs.folderType,
                            onBack = {
                                scope.launch {
                                    if (paneNavigator.canNavigateBack()) {
                                        paneNavigator.navigateBack()
                                    } else {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
                            onFolderNavigate = { id, name, type ->
                                // 🌟 MAGIC FIX 2: Nested Sub-folders humesha navController mein push honge
                                // Isse aapko step-by-step back jane ka option milega.
                                navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type))
                            },
                            onBreadcrumbNavigate = { folder ->
                                scope.launch {
                                    if (folder == null) {
                                        if (paneNavigator.canNavigateBack()) paneNavigator.navigateBack()
                                    } else {
                                        paneNavigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            contentKey = Screen.UnifiedFolder(android.net.Uri.encode(folder.folderId), android.net.Uri.encode(folder.name), FolderType.VIRTUAL_HUB)
                                        )
                                    }
                                }
                            }
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Select a folder to view contents", color = Color.Gray)
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.searchSection(navController: NavHostController) {
    composable<Screen.Search> { SearchScreen(onBackClick = { navController.popBackStack() }, onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) }) }
}

fun NavGraphBuilder.pdfViewerSection(navController: NavHostController) {
    composable<Screen.PdfViewer> { backStackEntry -> backStackEntry.toRoute<Screen.PdfViewer>(); PdfViewerScreen(onBack = { navController.popBackStack() }) }
}

fun NavGraphBuilder.foldersSection(navController: NavHostController, isTablet: Boolean) {
    composable<Screen.Folders> {
        Scaffold(
            bottomBar = { if (!isTablet) PremiumBottomBar(navController) },
            contentWindowInsets = WindowInsets(0.dp)
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                FoldersScreen(
                    onFolderClick = { path, name ->
                        if (path == "vault_root") {
                            navController.navigate(Screen.Vault)
                        } else {
                            navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(path), android.net.Uri.encode(name), FolderType.PHYSICAL_DEVICE))
                        }
                    }
                )
            }
        }
    }

    composable<Screen.Vault>(
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
    ) {
        VaultScreen(
            onBack = { navController.popBackStack<Screen.Folders>(inclusive = false) },
            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) }
        )
    }

    composable<Screen.UnifiedFolder>(
        typeMap = mapOf(typeOf<FolderType>() to NavType.EnumType(FolderType::class.java)),
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<Screen.UnifiedFolder>()

        UnifiedFolderScreen(
            folderId = args.folderId,
            folderName = args.folderName,
            folderType = args.folderType,
            onBack = { navController.popBackStack() },
            onPdfClick = { path -> navController.navigate(Screen.PdfViewer(pdfPath = path)) },
            onFolderNavigate = { id, name, type ->
                navController.navigate(Screen.UnifiedFolder(android.net.Uri.encode(id), android.net.Uri.encode(name), type))
            },
            onBreadcrumbNavigate = { folder ->
                if (folder == null) {
                    if (args.folderType == FolderType.PHYSICAL_DEVICE) {
                        navController.popBackStack<Screen.Folders>(inclusive = false)
                    } else {
                        navController.popBackStack<Screen.Home>(inclusive = false)
                    }
                } else {
                    val bType = FolderType.VIRTUAL_HUB
                    navController.popBackStack(Screen.UnifiedFolder(android.net.Uri.encode(folder.folderId), android.net.Uri.encode(folder.name), bType), inclusive = false)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.placeholderSections(navController: NavHostController, isTablet: Boolean) {
    composable<Screen.Tools> {
        Scaffold(
            topBar = { UniversalTopBar(title = "Tools") },
            bottomBar = { if (!isTablet) PremiumBottomBar(navController) },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = "Tools coming soon...", color = Color.Gray)
            }
        }
    }

    // Purana 'coming soon' wala code replace karein isse:
    composable<Screen.Settings> {
        com.edu.pdf.presentation.settings.SettingsScreen(
            isTablet = isTablet,
            navController = navController
        )
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\core\PremiumPermissionScreen.kt
``kotlin
package com.edu.pdf.presentation.core // 🌟 Tumhara naya refactored package!

import android.content.Intent
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.core.net.toUri

@Composable
fun PremiumPermissionScreen(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (Environment.isExternalStorageManager()) {
                    onPermissionGranted()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        // Fallback launcher just in case LifecycleObserver misses it
        if (Environment.isExternalStorageManager()) {
            onPermissionGranted()
        }
    }

    val requestPermission = {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = "package:${context.packageName}".toUri()
            }
            permissionLauncher.launch(intent)
        } catch (_: Exception) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            permissionLauncher.launch(intent)
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FolderSpecial, contentDescription = null, modifier = Modifier.size(50.dp), tint = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("File Access Required", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "To provide a seamless PDF experience, Hi Read needs access to your device storage.",
                textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PermissionFeatureRow(Icons.Default.Search, "Auto-Scan", "Finds all PDFs on your device automatically.")
                PermissionFeatureRow(Icons.Default.Security, "Private Vault", "Allows securing and encrypting sensitive files.")
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = requestPermission,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Grant Permission", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun PermissionFeatureRow(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)) }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\FoldersScreen.kt
``kotlin
package com.edu.pdf.presentation.folders

import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.PhotoCamera
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.presentation.common.UniversalTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
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
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        // 🌟 EXACT FIX: Humne 'padding' ko tod diya hai!
        Column(
            modifier = Modifier
                .fillMaxSize()
                // 🌟 Sirf Top bar ki padding li, bottom ki padding HATA di! (Edge-to-Edge)
                .padding(top = padding.calculateTopPadding())
        ) {

            // 🌟 1. FIXED VAULT CARD
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

            // 🌟 2. SCROLLING FOLDERS
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                // 🌟 PERFECT NATIVE FEEL: Ab list ekdum screen ke aakhri chhor (edge) tak jayegi.
                // Aakhri folder navigation bar ke piche na chhupe, isliye sirf bottom mein system bar jitni jagah di hai.
                contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
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
// ✅ YAHAN PASTE KAREIN
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
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\FoldersViewModel.kt
``kotlin
package com.edu.pdf.presentation.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject

data class DeviceFolder(val name: String, val absolutePath: String, val pdfCount: Int)

// 🌟 STRICT MVI: State Definition
data class FoldersUiState(
    val deviceFolders: ImmutableList<DeviceFolder> = persistentListOf(),
    val isLoading: Boolean = true
)

// 🌟 STRICT MVI: Actions (Currently basic, but ready to scale)
sealed interface FoldersAction {
    // Agar future me pull-to-refresh ya sort add karna ho to yahan aayega
    data object RefreshFolders : FoldersAction
}

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    // 🌟 SINGLE SOURCE OF TRUTH
    val uiState: StateFlow<FoldersUiState> = repository.getAllPdfs(SortType.NAME_ASC)
        .map { pdfs ->
            val groupedFolders = pdfs.groupBy { File(it.path).parentFile?.absolutePath ?: "Unknown" }
                .map { (path, list) -> DeviceFolder(File(path).name, path, list.size) }
                .sortedBy { it.name.lowercase() }
                .toImmutableList() // 🌟 Immutable List for Compose Performance

            FoldersUiState(
                deviceFolders = groupedFolders,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FoldersUiState(isLoading = true)
        )

    fun onAction(action: FoldersAction) {
        when (action) {
            is FoldersAction.RefreshFolders -> {
                // Future scalability: Jab pull-to-refresh add karoge
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\UnifiedFolderOverlays.kt
``kotlin
package com.edu.pdf.presentation.folders

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.folders.components.FolderMenuSheet
import com.edu.pdf.presentation.home.components.PdfActionBottomSheet
import com.edu.pdf.presentation.home.components.SortBottomSheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnifiedFolderOverlays(
    state: UnifiedFolderUiState,
    foldersTree: List<com.edu.pdf.domain.model.Folder>,
    onAction: (UnifiedFolderAction) -> Unit
) {
    val context = LocalContext.current

    when (val sheetState = state.activeSheetState) {
        is UnifiedFolderSheetState.None -> {}

        is UnifiedFolderSheetState.SortPicker -> {
            SortBottomSheet(
                currentSort = state.sortType,
                onSortSelected = { onAction(UnifiedFolderAction.UpdateSortType(it)) },
                onDismiss = { onAction(UnifiedFolderAction.CloseSheet) }
            )
        }

        is UnifiedFolderSheetState.ItemMenu -> {
            when (val item = sheetState.item) {
                is HomeItem.FolderItem -> {
                    FolderMenuSheet(
                        folder = item.folder,
                        onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                        onRenameClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.RenameDialog(item, item.folder.name))) },
                        onMoveToClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(listOf(item)))) },
                        onDetailsClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DetailsDialog(item))) },
                        onDeleteClick = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(listOf(item)))) }
                    )
                }
                is HomeItem.PdfItem -> {
                    PdfActionBottomSheet(
                        pdf = item.pdf,
                        onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                        onFavoriteToggle = {
                            onAction(UnifiedFolderAction.ToggleFavorite(item.pdf.id, !item.pdf.isFavorite))
                            Toast.makeText(context, if (item.pdf.isFavorite) "Removed from Favorites" else "Added to Favorites", Toast.LENGTH_SHORT).show()
                            onAction(UnifiedFolderAction.CloseSheet)
                        },
                        onShare = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, item.pdf.id.toUri())
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDF"))
                            onAction(UnifiedFolderAction.CloseSheet)
                        },
                        onDelete = { onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(listOf(item)))) },
                        onActionClick = { actionTitle ->
                            when (actionTitle) {
                                "Move to" -> onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(listOf(item))))

                                "Move to Vault", "Remove from Vault" -> {
                                    onAction(UnifiedFolderAction.ToggleVaultStatus(item.pdf))
                                }

                                // 🌟 STEP 2 FIX: Folders ke andar MVI Rename trigger
                                "Rename" -> {
                                    val baseName = item.pdf.name.removeSuffix(".pdf").removeSuffix(".PDF")
                                    onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.RenameDialog(item, baseName)))
                                }

                                else -> {
                                    Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
                                    onAction(UnifiedFolderAction.CloseSheet)
                                }
                            }
                        }
                    )
                }
            }
        }

        is UnifiedFolderSheetState.MovePicker -> {
            // 🌟 NAYA ELITE MVI PICKER FOR UNIFIED FOLDERS
            com.edu.pdf.presentation.common.picker.MovePickerSheetRoute(
                folders = foldersTree,
                onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                onTargetSelected = { targetFolderId ->
                    onAction(UnifiedFolderAction.ConfirmMove(targetFolderId))
                }
            )
        }

        is UnifiedFolderSheetState.CreateFolderDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current
            var hasRequestedFocus by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(UnifiedFolderAction.CloseSheet)
                },
                title = { Text("New Folder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = state.textInput,
                        onValueChange = { onAction(UnifiedFolderAction.OnTextInputChange(it)) },
                        label = { Text("Folder Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                if (!hasRequestedFocus) {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                    hasRequestedFocus = true
                                }
                            },
                        shape = RoundedCornerShape(12.dp)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(UnifiedFolderAction.ConfirmCreateFolder)
                        },
                        enabled = state.textInput.trim().isNotEmpty()
                    ) { Text("Create", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(UnifiedFolderAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is UnifiedFolderSheetState.RenameDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current
            var hasRequestedFocus by remember { mutableStateOf(false) }

            // 🌟 WAPAS AAGAYA: Smart Auto-Select logic
            var textFieldValue by remember {
                mutableStateOf(
                    androidx.compose.ui.text.input.TextFieldValue(
                        text = state.textInput,
                        selection = androidx.compose.ui.text.TextRange(0, state.textInput.length)
                    )
                )
            }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(UnifiedFolderAction.CloseSheet)
                },
                title = { Text("Rename", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = {
                            textFieldValue = it
                            onAction(UnifiedFolderAction.OnTextInputChange(it.text))
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                if (!hasRequestedFocus) {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                    hasRequestedFocus = true
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        // 🌟 WAPAS AAGAYA: Clear 'X' Icon
                        trailingIcon = {
                            if (textFieldValue.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    textFieldValue = androidx.compose.ui.text.input.TextFieldValue("", androidx.compose.ui.text.TextRange.Zero)
                                    onAction(UnifiedFolderAction.OnTextInputChange(""))
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(UnifiedFolderAction.ConfirmRename)
                        },
                        enabled = textFieldValue.text.trim().isNotEmpty()
                    ) { Text("Rename", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(UnifiedFolderAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is UnifiedFolderSheetState.DeleteConfirm -> {
            val itemCount = sheetState.items.size
            AlertDialog(
                onDismissRequest = { onAction(UnifiedFolderAction.CloseSheet) },
                title = { Text(if (itemCount > 1) "Delete $itemCount items?" else "Delete Item?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Text(
                        "Are you sure you want to permanently delete the selected item(s)? This action cannot be undone.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onAction(UnifiedFolderAction.ConfirmDelete) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Delete", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(UnifiedFolderAction.CloseSheet) }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is UnifiedFolderSheetState.DetailsDialog -> {
            val name = when (val item = sheetState.item) {
                is HomeItem.FolderItem -> item.folder.name
                is HomeItem.PdfItem -> item.pdf.name
            }
            val dateRaw = sheetState.item.lastModified
            val sizeStr = if (sheetState.item is HomeItem.PdfItem) {
                val bytes = sheetState.item.pdf.sizeInBytes
                if (bytes >= 1024 * 1024) String.format(Locale.US, "%.1f MB", bytes / (1024f * 1024f))
                else String.format(Locale.US, "%.1f KB", bytes / 1024f)
            } else "Folder Directory"

            val currentLocale = LocalConfiguration.current.locales[0]
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", currentLocale)
            val formattedDate = sdf.format(Date(if (dateRaw < 1000000000000L) dateRaw * 1000 else dateRaw))

            AlertDialog(
                onDismissRequest = { onAction(UnifiedFolderAction.CloseSheet) },
                title = { Text("Item Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Column {
                        DetailRow("Name", name)
                        DetailRow("Size/Type", sizeStr)
                        DetailRow("Modified", formattedDate)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onAction(UnifiedFolderAction.CloseSheet) }) {
                        Text("Close", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // 🌟 2026 PREMIUM FULL-SCREEN PICKER UX
        is UnifiedFolderSheetState.AppPdfPicker -> {
            com.edu.pdf.presentation.common.picker.GlobalPdfPickerSheet(
                onDismiss = { onAction(UnifiedFolderAction.CloseSheet) },
                onPdfsSelected = { selectedIds ->
                    onAction(UnifiedFolderAction.MovePdfsToCurrentFolder(selectedIds))
                }
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\UnifiedFolderScreen.kt
``kotlin
package com.edu.pdf.presentation.folders

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.components.SelectionTopBar
import com.edu.pdf.presentation.home.components.UnifiedGridItem
import com.edu.pdf.presentation.home.components.UnifiedListItem
import androidx.compose.foundation.layout.WindowInsets
@Composable
fun UnifiedFolderScreen(
    folderId: String? = null,
    folderName: String? = null,
    folderType: FolderType? = null,
    onBack: () -> Unit,
    onPdfClick: (String) -> Unit,
    onFolderNavigate: (String, String, FolderType) -> Unit,
    onBreadcrumbNavigate: (Folder?) -> Unit,
    viewModel: UnifiedFolderViewModel = hiltViewModel()
) {
    LaunchedEffect(folderId, folderName, folderType) {
        if (folderId != null && folderName != null && folderType != null) {
            viewModel.onAction(UnifiedFolderAction.InitializeFolder(folderId, folderName, folderType))
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagedPdfs = viewModel.pagedPdfsFlow.collectAsLazyPagingItems()

    val isSelectionMode = uiState.isSelectionMode
    val selectedPdfs = uiState.selectedIds

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is UnifiedFolderEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    is UnifiedFolderEvent.ClearMultiSelection -> viewModel.onAction(UnifiedFolderAction.SetSelectionMode(false))
                }
            }
        }
    }

    if (uiState.folderType == FolderType.SECURE_VAULT) {
        DisposableEffect(lifecycleOwner) {
            val activity = context as? ComponentActivity
            activity?.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
            val observer = LifecycleEventObserver { _, event -> if (event == Lifecycle.Event.ON_PAUSE) onBack() }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                activity?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    val selectedItems = remember(uiState.folders, selectedPdfs, pagedPdfs.itemSnapshotList) {
        if (selectedPdfs.isEmpty()) emptyList()
        else {
            val loadedPdfs = pagedPdfs.itemSnapshotList.items
            selectedPdfs.mapNotNull { id ->
                uiState.folders.find { it.folder.folderId == id } ?: loadedPdfs.find { it.pdf.id == id }
            }
        }
    }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.onAction(UnifiedFolderAction.ImportFile(it.toString())) }
    }
    BackHandler(enabled = isSelectionMode) {
        viewModel.onAction(UnifiedFolderAction.SetSelectionMode(false))
    }
    val onLongPressEnableSelection: (String) -> Unit = { id ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        if (!isSelectionMode) {
            viewModel.onAction(UnifiedFolderAction.SetSelectionMode(true))
            if (!selectedPdfs.contains(id)) viewModel.onAction(UnifiedFolderAction.ToggleSelection(id))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (isSelectionMode) {
                // 🌟 STEP 1: UI Maths yahin calculate karo
                val totalItems = uiState.folders.size + pagedPdfs.itemCount
                val isAllSelected = totalItems > 0 && selectedPdfs.size >= totalItems

                SelectionTopBar(
                    selectedCount = selectedPdfs.size,
                    isAllSelected = isAllSelected, // 🌟 FIX: Naya parameter jo humne pehle banaya tha
                    onClearSelection = { viewModel.onAction(UnifiedFolderAction.SetSelectionMode(false)) },
                    onSelectAllToggle = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        if (isAllSelected) {
                            viewModel.onAction(UnifiedFolderAction.SelectAll(emptyList()))
                        } else {
                            viewModel.onAction(UnifiedFolderAction.SelectAllItems)
                        }
                    }
                )
            } else {
                UnifiedCustomTopBar(
                    title = uiState.folderName,
                    isGridView = uiState.isGridView,
                    canCreateSubFolders = uiState.canCreateSubFolders,
                    // 🌟 PURE MVI & ELITE FIX: 'X' dabane par direct root par jump karo!
                    onBackClick = { onBreadcrumbNavigate(null) },
                    onAddFolderClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId))) },
                    onSortClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.SortPicker)) },
                    onToggleView = { viewModel.onAction(UnifiedFolderAction.ToggleViewMode) },
                    onSelectClick = {
                        // 🌟 1-CLICK MAGIC: Selection Mode ON करो और तुरंत सब सेलेक्ट कर लो!
                        viewModel.onAction(UnifiedFolderAction.SetSelectionMode(true))
                        viewModel.onAction(UnifiedFolderAction.SelectAllItems)
                    }

                )
            }
        },
        bottomBar = {
            if (isSelectionMode) {
                // 🌟 ELITE FIX: Yahan bhi hamara naya Master Bar lag gaya! Ekdum clean.
                com.edu.pdf.presentation.common.SmartSelectionBottomBar(
                    selectedItems = selectedItems,
                    tabIndex = 1, // Folders hamesha Move behavior use karte hain
                    onDelete = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.DeleteConfirm(selectedItems))) },
                    onMove = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(selectedItems))) },
                    onMerge = { Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show() },
                    onShare = {
                        val pdfUris = selectedItems.mapNotNull { it as? HomeItem.PdfItem }.map { it.pdf.id.toUri() }
                        if (pdfUris.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                                type = "application/pdf"
                                putParcelableArrayListExtra(Intent.EXTRA_STREAM, java.util.ArrayList(pdfUris))
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDFs via"))
                        }
                    },
                    onRemoveFromRecent = {},
                    onUnfavorite = {}
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            if (uiState.shouldShowBreadcrumbs) {
                com.edu.pdf.presentation.common.PremiumBreadcrumbs(
                    breadcrumbs = uiState.breadcrumbs,
                    onNavigate = { folder -> onBreadcrumbNavigate(folder) }
                )
            }

            if (uiState.folders.isEmpty() && pagedPdfs.itemCount == 0 && !uiState.isLoading) {
                PremiumEmptyState(
                    canImport = uiState.canImport,
                    canCreateFolder = uiState.canCreateSubFolders,
                    onImportFromDeviceClick = { filePicker.launch("application/pdf") },
                    onImportFromAppClick = { viewModel.onAction(UnifiedFolderAction.OpenAppPdfPicker) },
                    onCreateFolderClick = { viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.CreateFolderDialog(uiState.folderId))) }
                )
            } else {
                if (uiState.isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 110.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 120.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // 🌟 STEP 1: Pehle saare Folders render karo
                        items(items = uiState.folders, key = { it.folder.folderId }) { folder ->
                            UnifiedGridItem(
                                item = folder, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                onAction = { action ->
                                    when (action) {
                                        is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                        else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(folder)))
                                    }
                                },
                                onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                            )
                        }

                        // 🌟 STEP 2: Uske baad Paging 3 wale PDFs render karo
                        items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                            val pdfItem = pagedPdfs[index]
                            if (pdfItem != null) {
                                UnifiedGridItem(
                                    item = pdfItem, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                    onAction = { action ->
                                        when (action) {
                                            is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                            else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(pdfItem)))
                                        }
                                    },
                                    onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
                        // 🌟 STEP 1: Pehle saare Folders render karo
                        items(items = uiState.folders, key = { it.folder.folderId }) { folder ->
                            UnifiedListItem(
                                item = folder, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                onAction = { action ->
                                    when (action) {
                                        is HomeAction.NavigateToVirtualFolder -> onFolderNavigate(action.folder.folderId, action.folder.name, uiState.folderType)
                                        else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(folder)))
                                    }
                                },
                                onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                            )
                        }

                        // 🌟 STEP 2: Uske baad Paging 3 wale PDFs render karo
                        items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                            val pdfItem = pagedPdfs[index]
                            if (pdfItem != null) {
                                UnifiedListItem(
                                    item = pdfItem, isSelectionMode = isSelectionMode, selectedPdfs = selectedPdfs,
                                    onAction = { action ->
                                        when (action) {
                                            is HomeAction.ValidateAndOpenPdf -> onPdfClick(action.pdf.path)
                                            else -> viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.ItemMenu(pdfItem)))
                                        }
                                    },
                                    onToggleSelection = { viewModel.onAction(UnifiedFolderAction.ToggleSelection(it)) }, onLongPress = onLongPressEnableSelection
                                )
                            }
                        }
                    }
                }
            }
        }
        UnifiedFolderOverlays(state = uiState, foldersTree = uiState.foldersTree, onAction = viewModel::onAction)
    }
}

@Composable
fun UnifiedCustomTopBar(title: String, isGridView: Boolean, canCreateSubFolders: Boolean, onBackClick: () -> Unit, onAddFolderClick: () -> Unit, onSortClick: () -> Unit, onToggleView: () -> Unit, onSelectClick: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.Default.Close, "Close") }
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
            if (canCreateSubFolders) IconButton(onClick = onAddFolderClick) { Icon(Icons.Default.CreateNewFolder, "New Folder") }
            Row {
                IconButton(onClick = onSortClick) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
                IconButton(onClick = onToggleView) { Icon(if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView, "Toggle View") }
                IconButton(onClick = onSelectClick) { Icon(Icons.Outlined.CheckBox, "Select") }
            }
        }
    }
}

@Composable
fun PremiumEmptyState(canImport: Boolean, canCreateFolder: Boolean, onImportFromDeviceClick: () -> Unit, onImportFromAppClick: () -> Unit, onCreateFolderClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (canImport) {
                ProEmptyStateCard(title = "Add from this app", icon = Icons.Default.Home, iconTint = MaterialTheme.colorScheme.error, onClick = onImportFromAppClick)
                ProEmptyStateCard(title = "Add from device", icon = Icons.Default.PhoneAndroid, iconTint = MaterialTheme.colorScheme.primary, onClick = onImportFromDeviceClick)
            }
            if (canCreateFolder) {
                ProEmptyStateCard(title = "Create folder", icon = Icons.Default.Folder, iconTint = MaterialTheme.colorScheme.tertiary, onClick = onCreateFolderClick)
            }
        }
    }
}

@Composable
private fun ProEmptyStateCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\UnifiedFolderViewModel.kt
``kotlin
package com.edu.pdf.presentation.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeleteFolderUseCase
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface UnifiedFolderSheetState {
    data object None : UnifiedFolderSheetState
    data object SortPicker : UnifiedFolderSheetState
    data class CreateFolderDialog(val parentId: String?) : UnifiedFolderSheetState
    data class ItemMenu(val item: HomeItem) : UnifiedFolderSheetState
    data class RenameDialog(val item: HomeItem, val currentName: String) : UnifiedFolderSheetState
    data class DetailsDialog(val item: HomeItem) : UnifiedFolderSheetState
    data class DeleteConfirm(val items: List<HomeItem>) : UnifiedFolderSheetState
    data class MovePicker(val items: List<HomeItem>) : UnifiedFolderSheetState
    data object AppPdfPicker : UnifiedFolderSheetState
}

sealed interface UnifiedFolderEvent {
    data class ShowSnackbar(val message: String) : UnifiedFolderEvent
    data object ClearMultiSelection : UnifiedFolderEvent

}

sealed interface UnifiedFolderAction {
    data class InitializeFolder(val id: String, val name: String, val type: FolderType) : UnifiedFolderAction
    data class ToggleSelection(val id: String) : UnifiedFolderAction
    data class SetSelectionMode(val enabled: Boolean) : UnifiedFolderAction
    data class SelectAll(val ids: List<String>) : UnifiedFolderAction
    data class OpenSheet(val state: UnifiedFolderSheetState) : UnifiedFolderAction
    data object CloseSheet : UnifiedFolderAction
    data class OnTextInputChange(val text: String) : UnifiedFolderAction
    data class UpdateSortType(val type: SortType) : UnifiedFolderAction
    data object ToggleViewMode : UnifiedFolderAction
    data object ConfirmCreateFolder : UnifiedFolderAction
    data object ConfirmRename : UnifiedFolderAction
    data object ConfirmDelete : UnifiedFolderAction
    data class ConfirmMove(val targetFolderId: String?) : UnifiedFolderAction
    data class ToggleFavorite(val pdfId: String, val isFav: Boolean) : UnifiedFolderAction
    data class ImportFile(val uriString: String) : UnifiedFolderAction
    data object OpenAppPdfPicker : UnifiedFolderAction
    data class ToggleVaultStatus(val pdf: com.edu.pdf.domain.model.PdfFile) : UnifiedFolderAction
    data class MovePdfsToCurrentFolder(val pdfIds: List<String>) : UnifiedFolderAction

    data object SelectAllItems : UnifiedFolderAction
}

// 🌟 ELITE FIX: UI State ab aur bhi clean ho gaya! Pdfs ko hata diya, ab wo Paging se aayenge
data class UnifiedFolderUiState(
    val isLoading: Boolean = true,
    val isProcessing: Boolean = false,
    val folderType: FolderType = FolderType.PHYSICAL_DEVICE,
    val folderId: String = "",
    val folderName: String = "",
    val folders: ImmutableList<HomeItem.FolderItem> = persistentListOf(),
    val breadcrumbs: ImmutableList<Folder> = persistentListOf(),
    val foldersTree: ImmutableList<Folder> = persistentListOf(), 
    val isSelectionMode: Boolean = false,
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val isGridView: Boolean = false,
    val sortType: SortType = SortType.DATE_DESC,
    val activeSheetState: UnifiedFolderSheetState = UnifiedFolderSheetState.None,
    val textInput: String = "",
    val canCreateSubFolders: Boolean = false,
    val canImport: Boolean = false,
    val canRenameOrDelete: Boolean = false,
    val shouldShowBreadcrumbs: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UnifiedFolderViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val userPreferences: UserPreferences,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val createFolderUseCase: com.edu.pdf.domain.usecase.CreateFolderUseCase // 🌟 NAYA ADD KIYA
) : ViewModel() {

    private val _currentFolderId = MutableStateFlow<String?>(null)
    private val _currentFolderName = MutableStateFlow("")
    private val _currentFolderType = MutableStateFlow(FolderType.PHYSICAL_DEVICE)

    private val _events = Channel<UnifiedFolderEvent>()
    val events = _events.receiveAsFlow()

    private val _sortType = MutableStateFlow(SortType.DATE_DESC)
    private val _internalState = MutableStateFlow(UnifiedFolderUiState())

    private fun initFolderData(id: String, name: String, type: FolderType) {
        val decodedId = android.net.Uri.decode(id)
        val decodedName = android.net.Uri.decode(name)
        val actualId = if (decodedId.isBlank() || decodedId == "root") null else decodedId

        _currentFolderId.value = actualId
        _currentFolderName.value = decodedName
        _currentFolderType.value = type
        _internalState.update { it.copy(folderId = decodedId, folderName = decodedName, folderType = type) }
    }

    val pagedPdfsFlow: Flow<PagingData<HomeItem.PdfItem>> = combine(_currentFolderId, _currentFolderType, _sortType) { id, type, sort ->
        Triple(id, type, sort)
    }.flatMapLatest { (id, type, sort) ->
        if (type == FolderType.PHYSICAL_DEVICE) {
            repository.getPaginatedPdfsInPhysicalFolder(id ?: "", sortType = sort)
        } else {
            repository.getPaginatedManagedPdfs(id, isVault = type == FolderType.SECURE_VAULT, sortType = sort)
        }
    }.map { pagingData -> pagingData.map { HomeItem.PdfItem(it) } }
        .cachedIn(viewModelScope)

    private val foldersFlow = combine(_currentFolderId, _currentFolderType, _sortType) { id, type, sort ->
        Triple(id, type, sort)
    }.flatMapLatest { (id, type, sort) ->
        if (type == FolderType.PHYSICAL_DEVICE) {
            flowOf(persistentListOf())
        } else {
            repository.getManagedFolders(id, isVault = type == FolderType.SECURE_VAULT).map { folders ->
                // 🌟 Smart Sorting: Jo user ne choose kiya hai wahi apply hoga
                val sortedFolders = when (sort) {
                    SortType.NAME_ASC -> folders.sortedBy { it.name.lowercase() }
                    SortType.NAME_DESC -> folders.sortedByDescending { it.name.lowercase() }
                    SortType.DATE_DESC -> folders.sortedByDescending { it.createdAt }
                    SortType.DATE_ASC -> folders.sortedBy { it.createdAt }
                    SortType.SIZE_DESC -> folders.sortedByDescending { it.pdfCount }
                    SortType.SIZE_ASC -> folders.sortedBy { it.pdfCount }
                }
                sortedFolders.map { HomeItem.FolderItem(it) }.toImmutableList()
            }
        }
    }.flowOn(Dispatchers.Default)

    private val breadcrumbsFlow = combine(_currentFolderId, _currentFolderName, _currentFolderType) { id, name, type -> Triple(id, name, type) }
        .flatMapLatest { (id, name, type) ->
            if (type == FolderType.PHYSICAL_DEVICE) {
                flowOf(persistentListOf(Folder(folderId = id ?: "", name = name)))
            } else {
                repository.getAllManagedFolders(isVault = type == FolderType.SECURE_VAULT).map { allFolders ->
                    val breadcrumbList = mutableListOf<Folder>()
                    var curr = allFolders.find { it.folderId == id }
                    while (curr != null) {
                        breadcrumbList.add(0, curr)
                        curr = allFolders.find { it.folderId == curr.parentFolderId }
                    }
                    breadcrumbList.toImmutableList()
                }
            }
        }

    private val prefsAndTypeFlow = combine(userPreferences.isFolderGridViewFlow, _sortType, _currentFolderType) { isGrid, sort, type -> Triple(isGrid, sort, type) }

    // 🌟 ELITE FIX: Database aur preferences ki calculation ko ek alag flow mein rakha
// Ab typing karne par DB wapas run nahi hoga!
    private val heavyDatabaseFlow = combine(
        foldersFlow,
        breadcrumbsFlow,
        repository.getAllManagedFolders(isVault = false),
        prefsAndTypeFlow
    ) { folders, breadcrumbs, tree, prefs ->
        // Bas data ko return kar do
        data class DbState(val folders: ImmutableList<HomeItem.FolderItem>, val breadcrumbs: ImmutableList<Folder>, val tree: List<Folder>, val prefs: Triple<Boolean, SortType, FolderType>)
        DbState(folders, breadcrumbs, tree, prefs)
    }.flowOn(Dispatchers.Default)

    // 🌟 UI STATE: Ab yahan Database aur User Input (text) merge honge bina DB ko re-run kiye
    val uiState: StateFlow<UnifiedFolderUiState> = combine(
        heavyDatabaseFlow,
        _internalState
    ) { dbData, internal ->
        val (isGrid, sort, type) = dbData.prefs
        val isPhysical = type == FolderType.PHYSICAL_DEVICE
        val isVault = type == FolderType.SECURE_VAULT

        internal.copy(
            isLoading = false,
            folders = dbData.folders,
            breadcrumbs = dbData.breadcrumbs,
            foldersTree = dbData.tree.toImmutableList(),
            isGridView = isGrid,
            sortType = sort,
            canCreateSubFolders = !isPhysical && !isVault,
            canImport = !isPhysical,
            canRenameOrDelete = !isPhysical,
            shouldShowBreadcrumbs = !isPhysical
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UnifiedFolderUiState())

    fun onAction(action: UnifiedFolderAction) {
        when (action) {
            is UnifiedFolderAction.InitializeFolder -> initFolderData(action.id, action.name, action.type)
            is UnifiedFolderAction.ToggleSelection -> {
                val currentSelected = _internalState.value.selectedIds
                val newSelection = if (currentSelected.contains(action.id)) currentSelected.remove(action.id) else currentSelected.add(action.id)
                _internalState.update { it.copy(selectedIds = newSelection) }
            }
            is UnifiedFolderAction.SetSelectionMode -> _internalState.update { it.copy(isSelectionMode = action.enabled, selectedIds = if (!action.enabled) persistentSetOf() else it.selectedIds) }
            is UnifiedFolderAction.SelectAll -> _internalState.update { it.copy(selectedIds = action.ids.toPersistentSet()) }
            is UnifiedFolderAction.SelectAllItems -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val folderIds = uiState.value.folders.map { it.folder.folderId }
                    val type = _currentFolderType.value
                    val id = _currentFolderId.value

                    // 🌟 MVI & MEMORY FIX: Direct ID Fetch for Folders
                    val pdfIds = if (type == FolderType.PHYSICAL_DEVICE) {
                        repository.getPhysicalFolderPdfIdsFast(id ?: "")
                    } else {
                        repository.getManagedPdfIdsFast(id, isVault = type == FolderType.SECURE_VAULT)
                    }

                    val allIds = (folderIds + pdfIds).toPersistentSet()
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(selectedIds = allIds) }
                    }
                }
            }
            is UnifiedFolderAction.OpenSheet -> _internalState.update { it.copy(activeSheetState = action.state, textInput = if (action.state is UnifiedFolderSheetState.RenameDialog) action.state.currentName else "") }
            is UnifiedFolderAction.CloseSheet -> _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None, textInput = "") }
            is UnifiedFolderAction.OnTextInputChange -> _internalState.update { it.copy(textInput = action.text) }
            is UnifiedFolderAction.UpdateSortType -> { _sortType.value = action.type; onAction(UnifiedFolderAction.CloseSheet) }
            is UnifiedFolderAction.ToggleViewMode -> viewModelScope.launch { userPreferences.saveFolderGridViewPreference(!userPreferences.isFolderGridViewFlow.first()) }
            is UnifiedFolderAction.ToggleFavorite -> viewModelScope.launch { repository.toggleFavorite(action.pdfId, action.isFav) }

            is UnifiedFolderAction.ConfirmCreateFolder -> {
                val folderName = _internalState.value.textInput.trim()
                if (folderName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true) } // 🌟 Sheet abhi band nahi karenge
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = createFolderUseCase(folderName, _currentFolderId.value, isVault = _currentFolderType.value == FolderType.SECURE_VAULT) // 🌟 REPOSITORY HATA KAR USECASE LAGA DIYA
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            if (result.isSuccess) {
                                // Success hone par sheet band karo aur message dikhao
                                _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None, textInput = "") }
                                _events.send(UnifiedFolderEvent.ShowSnackbar("Folder created"))
                            } else {
                                // Fail hone par error message dikhao (Sheet open rahegi)
                                _events.send(UnifiedFolderEvent.ShowSnackbar(result.exceptionOrNull()?.message ?: "Error creating folder"))
                            }
                        }
                    }
                }
            }
            is UnifiedFolderAction.ConfirmRename -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.RenameDialog ?: return
                val newName = _internalState.value.textInput.trim()
                if (newName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true) }
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = when (val item = state.item) {
                            is HomeItem.FolderItem -> repository.renameManagedFolder(item.folder.folderId, newName)
                            is HomeItem.PdfItem -> Result.success(renamePdfUseCase(item.pdf, newName)).map{} // Assume PDF rename doesn't fail for now
                        }
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            if (result.isSuccess) {
                                _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None, textInput = "") }
                                _events.send(UnifiedFolderEvent.ShowSnackbar("Renamed successfully"))
                            } else {
                                _events.send(UnifiedFolderEvent.ShowSnackbar(result.exceptionOrNull()?.message ?: "Error renaming folder"))
                            }
                        }
                    }
                }
            }
            is UnifiedFolderAction.ConfirmDelete -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.DeleteConfirm ?: return
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val foldersToDelete = state.items.filterIsInstance<HomeItem.FolderItem>()
                    val pdfsToDelete = state.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf }
                    foldersToDelete.forEach { deleteFolderUseCase(it.folder.folderId) }
                    if (pdfsToDelete.isNotEmpty()) deletePdfsUseCase(pdfsToDelete)
                    withContext(Dispatchers.Main) { _events.send(UnifiedFolderEvent.ClearMultiSelection); _internalState.update { it.copy(isProcessing = false) } }
                }
            }
            is UnifiedFolderAction.ConfirmMove -> {
                val state = _internalState.value.activeSheetState as? UnifiedFolderSheetState.MovePicker ?: return
                if (action.targetFolderId == _currentFolderId.value) { _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.None) }; return }
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val pdfIds = state.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf.id }
                    val folderIds = state.items.filterIsInstance<HomeItem.FolderItem>().map { it.folder.folderId }
                    if (pdfIds.isNotEmpty()) repository.movePdfsToVirtualFolder(pdfIds, action.targetFolderId, isVault = _currentFolderType.value == FolderType.SECURE_VAULT)
                    folderIds.forEach { repository.moveFolderToVirtualFolder(it, action.targetFolderId, isVault = _currentFolderType.value == FolderType.SECURE_VAULT) }
                    withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) }; _events.send(UnifiedFolderEvent.ClearMultiSelection) }
                }
            }
            is UnifiedFolderAction.ImportFile -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = repository.importPdfFromUri(action.uriString, _currentFolderId.value, _currentFolderType.value == FolderType.SECURE_VAULT, _currentFolderType.value == FolderType.PHYSICAL_DEVICE)
                    withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) }; _events.send(UnifiedFolderEvent.ShowSnackbar(if (result.isSuccess) "Imported Successfully" else "Import Failed")) }
                }
            }
            is UnifiedFolderAction.OpenAppPdfPicker -> _internalState.update { it.copy(activeSheetState = UnifiedFolderSheetState.AppPdfPicker) }
            is UnifiedFolderAction.MovePdfsToCurrentFolder -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    repository.movePdfsToVirtualFolder(action.pdfIds, _currentFolderId.value, isVault = _currentFolderType.value == FolderType.SECURE_VAULT)
                    withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) }; _events.send(UnifiedFolderEvent.ShowSnackbar("Added successfully!")) }
                }
            }
            is UnifiedFolderAction.ToggleVaultStatus -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = UnifiedFolderSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val newVaultStatus = !action.pdf.isVault
                    repository.movePdfsToVirtualFolder(listOf(action.pdf.id), null, isVault = newVaultStatus)
                    withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) }; _events.send(UnifiedFolderEvent.ShowSnackbar(if (newVaultStatus) "Secured in Vault" else "Restored to Public")) }
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\components\FolderMenuSheet.kt
``kotlin
package com.edu.pdf.presentation.folders.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.Folder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderMenuSheet(
    folder: Folder,
    onDismiss: () -> Unit,
    onRenameClick: () -> Unit,
    onMoveToClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
            // Header (Yellow Icon + Name + Size)
            // Header (Premium Icon + Name + Size)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.edu.pdf.R.drawable.premium_folder1),
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = folder.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text(text = "📄 ${folder.pdfCount}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))

            // Menu Options (Box-free)
            MenuOptionItem(icon = Icons.Outlined.Edit, text = "Rename", onClick = onRenameClick)
            MenuOptionItem(icon = Icons.AutoMirrored.Outlined.DriveFileMove, text = "Move to", onClick = onMoveToClick)
            MenuOptionItem(icon = Icons.Outlined.Info, text = "Details", onClick = onDetailsClick)
            MenuOptionItem(icon = Icons.Outlined.Delete, text = "Delete", onClick = onDeleteClick)
        }
    }
}

@Composable
private fun MenuOptionItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\vault\VaultScreen.kt
``kotlin
package com.edu.pdf.presentation.folders.vault

import android.text.format.Formatter
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.edu.pdf.domain.model.PdfFile

@Composable
fun VaultScreen(
    onBack: () -> Unit,
    onPdfClick: (String) -> Unit,
    viewModel: VaultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // सिर्फ PDF खोलने और मैसेज दिखाने का एकदम साफ लॉजिक
    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    // PDF पर क्लिक करने पर यहाँ से सीधा खुलेगी
                    is VaultEvent.NavigateToViewer -> onPdfClick(event.tempPath)
                    is VaultEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
// 🌟 ELITE SECURITY FIX: जब भी यूज़र Vault से बाहर जाएगा, सारी डिक्रिप्टेड फाइलें तुरंत नष्ट हो जाएंगी!
    DisposableEffect(Unit) {
        onDispose {
            com.edu.pdf.data.security.SecurityUtils.wipeVaultTempStorage(context)
        }
    }
    VaultScreenPure(
        state = uiState,
        onBack = onBack,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreenPure(
    state: VaultUiState,
    onBack: () -> Unit,
    onAction: (VaultAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = { Text("Private Vault", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // व्यू बदलने वाला बटन (Toggle View) बरकरार है
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                        onAction(VaultAction.ToggleViewMode)
                    }) {
                        Icon(
                            imageVector = if (state.isGridView) Icons.AutoMirrored.Rounded.ViewList else Icons.Rounded.GridView,
                            contentDescription = "Toggle View"
                        )
                    }
                    // नया PDF जोड़ने वाला बटन (+ Add) बरकरार है
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                        onAction(VaultAction.OpenPicker)
                    }) {
                        Icon(Icons.Rounded.Add, contentDescription = "Add PDF")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.vaultPdfs.isEmpty()) {
                // 🌟 ELITE FIX: Boring text ki jagah premium EmptyStateView laga diya
                com.edu.pdf.presentation.home.components.EmptyStateView(
                    title = "Vault is Locked & Empty",
                    subtitle = "Click the '+' icon above to securely hide your private PDFs here."
                )
            } else {
                if (state.isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.vaultPdfs, key = { it.id }) { pdf ->
                            VaultPdfGridItem(
                                pdf = pdf,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onAction(VaultAction.OpenPdf(pdf.path))
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.vaultPdfs, key = { it.id }) { pdf ->
                            VaultPdfListItem(
                                pdf = pdf,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onAction(VaultAction.OpenPdf(pdf.path))
                                }
                            )
                        }
                    }
                }
            }

            // लोडिंग स्क्रीन (जब PDF डिक्रिप्ट होकर खुल रही हो)
            AnimatedVisibility(visible = state.decryptionProgress != null, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)).pointerInput(Unit) { detectTapGestures {} },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.clip(RoundedCornerShape(24.dp)).background(MaterialTheme.colorScheme.surface).padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            progress = { state.decryptionProgress ?: 0f },
                            color = MaterialTheme.colorScheme.primary, strokeWidth = 6.dp, modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Unlocking Vault...", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("${((state.decryptionProgress ?: 0f) * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }

    if (state.isPickerOpen) {
        com.edu.pdf.presentation.common.picker.GlobalPdfPickerSheet(
            onDismiss = { onAction(VaultAction.ClosePicker) },
            onPdfsSelected = { selectedIds ->
                onAction(VaultAction.MoveToVault(selectedIds))
                onAction(VaultAction.ClosePicker)
            }
        )
    }
}

@Composable
fun VaultPdfListItem(pdf: PdfFile, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.PictureAsPdf, null, tint = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(pdf.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(Formatter.formatShortFileSize(context, pdf.sizeInBytes), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        // Delete और Unlock बटन यहाँ से हटा दिए गए हैं!
    }
}

@Composable
fun VaultPdfGridItem(pdf: PdfFile, onClick: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(54.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.PictureAsPdf, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(pdf.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(Formatter.formatShortFileSize(context, pdf.sizeInBytes), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        // Delete और Unlock बटन यहाँ से भी हटा दिए गए हैं!
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\folders\vault\VaultViewModel.kt
``kotlin
package com.edu.pdf.presentation.folders.vault

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.data.security.SecurityUtils
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

// 🌟 STRICT MVI: Single State Object
data class VaultUiState(
    val vaultPdfs: ImmutableList<PdfFile> = persistentListOf(),
    val isPickerOpen: Boolean = false,
    val isGridView: Boolean = false,
    val decryptionProgress: Float? = null,
    val isLoading: Boolean = true
)

// 🌟 EVENTS: One-time navigation and Toasts
sealed interface VaultEvent {
    data class NavigateToViewer(val tempPath: String) : VaultEvent
    data class ShowSnackbar(val message: String) : VaultEvent
}

sealed interface VaultAction {
    data object OpenPicker : VaultAction
    data object ClosePicker : VaultAction
    data object ToggleViewMode : VaultAction
    data class OpenPdf(val pdfPath: String) : VaultAction
    data class MoveToVault(val pdfIds: List<String>) : VaultAction
    data class RemoveFromVault(val pdfId: String) : VaultAction
    data class DeletePdf(val pdf: PdfFile) : VaultAction
}

@HiltViewModel
class VaultViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: PdfRepository,
    private val cryptoEngine: VaultCryptoEngine,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _internalState = MutableStateFlow(VaultUiState())

    private val _events = Channel<VaultEvent>()
    val events = _events.receiveAsFlow()

    val uiState: StateFlow<VaultUiState> = combine(
        repository.getManagedPdfs(parentPath = null, isVault = true),
        userPreferences.isFolderGridViewFlow,
        _internalState
    ) { pdfs, isGrid, internal ->
        internal.copy(
            vaultPdfs = pdfs.toImmutableList(),
            isGridView = isGrid,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, VaultUiState())

    fun onAction(action: VaultAction) {
        when (action) {
            is VaultAction.OpenPicker -> _internalState.update { it.copy(isPickerOpen = true) }
            is VaultAction.ClosePicker -> _internalState.update { it.copy(isPickerOpen = false) }
            is VaultAction.ToggleViewMode -> {
                viewModelScope.launch {
                    val currentGridState = userPreferences.isFolderGridViewFlow.first()
                    userPreferences.saveFolderGridViewPreference(!currentGridState)
                }
            }
            is VaultAction.OpenPdf -> decryptAndOpenPdf(action.pdfPath)
            is VaultAction.MoveToVault -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.movePdfsToVirtualFolder(action.pdfIds, null, isVault = true)
                    _events.send(VaultEvent.ShowSnackbar("Added to Vault"))
                }
            }
            is VaultAction.RemoveFromVault -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.movePdfsToVirtualFolder(listOf(action.pdfId), null, isVault = false)
                    _events.send(VaultEvent.ShowSnackbar("Restored from Vault"))
                }
            }
            is VaultAction.DeletePdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deletePdfsUseCase(listOf(action.pdf))
                    _events.send(VaultEvent.ShowSnackbar("Deleted permanently"))
                }
            }
        }
    }

    private fun decryptAndOpenPdf(pdfPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _internalState.update { it.copy(decryptionProgress = 0f) }
            val lockedFile = File(pdfPath)

            val secureTempDir = File(context.cacheDir, "vault_temp_view")
            if (!secureTempDir.exists()) secureTempDir.mkdirs()
            // 🛡️ SECURITY WIPE: Purani viewing file delete karo naya kholne se pehle
            SecurityUtils.wipeVaultTempStorage(context)

            val tempFile = File(secureTempDir, "view_${System.currentTimeMillis()}.pdf")

            try {
                val totalBytes = lockedFile.length().toFloat().coerceAtLeast(1f)
                var copied = 0L

                cryptoEngine.getEncryptedInputStream(lockedFile).use { input ->
                    tempFile.outputStream().use { output ->
                        val buffer = ByteArray(64 * 1024)
                        var bytesRead: Int
                        var lastEmitTime = 0L

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            copied += bytesRead
                            val now = System.currentTimeMillis()
                            if (now - lastEmitTime > 50) {
                                _internalState.update { it.copy(decryptionProgress = copied / totalBytes) }
                                lastEmitTime = now
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    _internalState.update { it.copy(decryptionProgress = null) }
                    _events.send(VaultEvent.NavigateToViewer(tempFile.absolutePath))
                }
            } catch (_: Exception) {
                if (tempFile.exists()) tempFile.delete()
                withContext(Dispatchers.Main) {
                    _internalState.update { it.copy(decryptionProgress = null) }
                    _events.send(VaultEvent.ShowSnackbar("Decryption Failed!"))
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\HomeOverlays.kt
``kotlin
package com.edu.pdf.presentation.home

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.folders.components.FolderMenuSheet
import com.edu.pdf.presentation.home.components.PdfActionBottomSheet
import com.edu.pdf.presentation.home.components.SortBottomSheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeOverlays(
    state: HomeUiState,
    foldersTree: List<com.edu.pdf.domain.model.Folder> = emptyList(), // 🌟 Yahan add karna tha
    onAction: (HomeAction) -> Unit
) {
    val context = LocalContext.current

    when (val activeSheet = state.activeSheetState) {
        is HomeSheetState.None -> { /* App is Idle - UI is completely clean */ }

        is HomeSheetState.SortPicker -> {
            SortBottomSheet(
                currentSort = state.sortType,
                onSortSelected = { type -> onAction(HomeAction.UpdateSortType(type)) },
                onDismiss = { onAction(HomeAction.CloseSheet) }
            )
        }

        is HomeSheetState.CreateFolderDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current
            var hasRequestedFocus by remember { mutableStateOf(false) }

            // 🌟 NAYA: UI ke andar apna local state
            var localFolderName by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(HomeAction.CloseSheet)
                },
                title = { Text("New Folder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = localFolderName,
                        onValueChange = { localFolderName = it },
                        label = { Text("Folder Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                if (!hasRequestedFocus) {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                    hasRequestedFocus = true
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(HomeAction.ConfirmCreateFolder(localFolderName))
                        },
                        enabled = localFolderName.trim().isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Create", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(HomeAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is HomeSheetState.ItemMenu -> {
            when (val item = activeSheet.item) {
                is HomeItem.FolderItem -> {
                    FolderMenuSheet(
                        folder = item.folder,
                        onDismiss = { onAction(HomeAction.CloseSheet) },
                        onRenameClick = { onAction(HomeAction.OpenSheet(HomeSheetState.RenameDialog(item, item.folder.name))) },
                        onMoveToClick = { onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(listOf(item)))) },
                        onDetailsClick = { onAction(HomeAction.OpenSheet(HomeSheetState.DetailsDialog(item))) },
                        onDeleteClick = { onAction(HomeAction.OpenSheet(HomeSheetState.DeleteConfirm(listOf(item)))) }
                    )
                }
                is HomeItem.PdfItem -> {
                    PdfActionBottomSheet(
                        pdf = item.pdf,
                        onDismiss = { onAction(HomeAction.CloseSheet) },
                        onFavoriteToggle = {
                            onAction(HomeAction.ToggleFavorite(item.pdf))
                            Toast.makeText(context, if (item.pdf.isFavorite) "Removed from Favorites" else "Added to Favorites", Toast.LENGTH_SHORT).show()
                            onAction(HomeAction.CloseSheet)
                        },
                        onShare = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, item.pdf.id.toUri())
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDF"))
                            onAction(HomeAction.CloseSheet)
                        },
                        // Deleted onRenameConfirm completely to fix the parameter error
                        onDelete = { onAction(HomeAction.OpenSheet(HomeSheetState.DeleteConfirm(listOf(item)))) },
                        onActionClick = { actionTitle ->
                            when (actionTitle) {
                                "Move to" -> onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(listOf(item))))

                                "Move to Vault", "Remove from Vault" -> {
                                    onAction(HomeAction.ToggleVaultStatus(item.pdf))
                                }

                                "Rename" -> {
                                    val baseName = item.pdf.name.removeSuffix(".pdf").removeSuffix(".PDF")
                                    onAction(HomeAction.OpenSheet(HomeSheetState.RenameDialog(item, baseName)))
                                }

                                else -> {
                                    Toast.makeText(context, "Feature coming soon!", Toast.LENGTH_SHORT).show()
                                    onAction(HomeAction.CloseSheet)
                                }
                            }
                        }
                    )
                }
            }
        }

        is HomeSheetState.RenameDialog -> {
            val focusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current
            var hasRequestedFocus by remember { mutableStateOf(false) }

            // 🌟 NAYA: Rename ke liye local state
            var localRenameText by remember { mutableStateOf(activeSheet.currentName) }

            AlertDialog(
                onDismissRequest = {
                    keyboard?.hide()
                    onAction(HomeAction.CloseSheet)
                },
                title = { Text("Rename", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    OutlinedTextField(
                        value = localRenameText,
                        onValueChange = { localRenameText = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                if (!hasRequestedFocus) {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                    hasRequestedFocus = true
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        trailingIcon = {
                            if (localRenameText.isNotEmpty()) {
                                IconButton(onClick = {
                                    localRenameText = ""
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboard?.hide()
                            onAction(HomeAction.ConfirmRename(activeSheet.item, localRenameText))
                        },
                        enabled = localRenameText.trim().isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Rename", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        keyboard?.hide()
                        onAction(HomeAction.CloseSheet)
                    }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is HomeSheetState.DeleteConfirm -> {
            val itemCount = activeSheet.items.size
            AlertDialog(
                onDismissRequest = { onAction(HomeAction.CloseSheet) },
                title = { Text(if (itemCount > 1) "Delete $itemCount items?" else "Delete Item?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Text(
                        "Are you sure you want to permanently delete the selected item(s)? This action cannot be undone.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onAction(HomeAction.ConfirmDelete(activeSheet.items)) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Delete", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(HomeAction.CloseSheet) }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        is HomeSheetState.MovePicker -> {
            com.edu.pdf.presentation.common.picker.MovePickerSheetRoute(
                folders = foldersTree, // 🌟 बस ये एक लाइन ऐड करनी है!
                onDismiss = { onAction(HomeAction.CloseSheet) },
                onTargetSelected = { targetFolderId ->
                    onAction(HomeAction.ConfirmMove(targetFolderId))
                }
            )
        }

        is HomeSheetState.DetailsDialog -> {
            val name = when (val item = activeSheet.item) {
                is HomeItem.FolderItem -> item.folder.name
                is HomeItem.PdfItem -> item.pdf.name
            }
            val dateRaw = activeSheet.item.lastModified
            val sizeStr = if (activeSheet.item is HomeItem.PdfItem) {
                val bytes = activeSheet.item.pdf.sizeInBytes
                if (bytes >= 1024 * 1024) String.format(Locale.US, "%.1f MB", bytes / (1024f * 1024f))
                else String.format(Locale.US, "%.1f KB", bytes / 1024f)
            } else "Folder Directory"

            val currentLocale = LocalConfiguration.current.locales[0]
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", currentLocale)
            val formattedDate = sdf.format(Date(if (dateRaw < 1000000000000L) dateRaw * 1000 else dateRaw))

            AlertDialog(
                onDismissRequest = { onAction(HomeAction.CloseSheet) },
                title = { Text("Item Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Column {
                        DetailRow("Name", name)
                        DetailRow("Size/Type", sizeStr)
                        DetailRow("Modified", formattedDate)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onAction(HomeAction.CloseSheet) }) {
                        Text("Close", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
        is HomeSheetState.AppPdfPicker -> {
            com.edu.pdf.presentation.common.picker.GlobalPdfPickerSheet(
                onDismiss = { onAction(HomeAction.CloseSheet) },
                onPdfsSelected = { selectedIds ->
                    onAction(HomeAction.MovePdfsToCurrentFolder(selectedIds))
                }
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}

``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\HomeScreen.kt
``kotlin
@file:Suppress("DEPRECATION")
package com.edu.pdf.presentation.home

import android.content.Intent
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.common.PremiumBottomBarItems
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.home.components.HomeContent
import com.edu.pdf.presentation.home.components.HomeTabs
import com.edu.pdf.presentation.home.components.SelectionTopBar
import kotlinx.collections.immutable.PersistentSet
import kotlinx.coroutines.launch

@Composable
fun HomeScreenWrapper(
    viewModel: HomeViewModel,
    navController: NavHostController,
    onPdfClick: (String) -> Unit,
    onFolderClick: (String, String, com.edu.pdf.domain.model.FolderType) -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(Environment.isExternalStorageManager()) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagedPdfs = viewModel.pagedUncategorizedPdfsFlow.collectAsLazyPagingItems()
    val isSelectionMode = uiState.isSelectionMode
    val selectedPdfs = uiState.selectedIds

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        hasPermission = Environment.isExternalStorageManager()
        if (hasPermission) viewModel.onAction(HomeAction.RefreshData)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    // 🌟 MVI STRICT EVENT OBSERVER: Ye background me crash nahi hone dega
    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is HomeEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    is HomeEvent.NavigateToPdfViewer -> onPdfClick(event.path)
                    // 🌟 2. NAYA EVENT LISTEN KIYA
                    is HomeEvent.NavigateToFolder -> onFolderClick(event.folderId, event.folderName, event.type)
                }
            }
        }
    }

    if (!hasPermission) {
        PermissionScreen {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply { data = "package:${context.packageName}".toUri() }
            permissionLauncher.launch(intent)
        }
    } else {
        LaunchedEffect(Unit) {
            viewModel.onAction(HomeAction.Initialize)
        }
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
        } else {
            HomeScreenPure(
                state = uiState,
                isRefreshing = uiState.isRefreshing,
                isSelectionMode = isSelectionMode,
                selectedPdfs = selectedPdfs,
                pagedPdfs = pagedPdfs,
                navController = navController,
                onSearchClick = onSearchClick,
                onSelectionModeChange = { enabled -> viewModel.onAction(HomeAction.SetSelectionMode(enabled)) },
                onToggleSelection = { id -> viewModel.onAction(HomeAction.ToggleSelection(id)) },
                onSelectAll = { ids -> viewModel.onAction(HomeAction.SelectAll(ids)) },
                onAction = viewModel::onAction

            )
            HomeOverlays(state = uiState, foldersTree = uiState.foldersTree, onAction = viewModel::onAction)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreenPure(
    state: HomeUiState,
    isRefreshing: Boolean,
    isSelectionMode: Boolean,
    selectedPdfs: PersistentSet<String>,
    pagedPdfs: androidx.paging.compose.LazyPagingItems<HomeItem.PdfItem>, // 🌟 FIX: Ye line miss ho gayi thi
    navController: NavHostController,
    onSearchClick: () -> Unit,
    onSelectionModeChange: (Boolean) -> Unit,
    onToggleSelection: (String) -> Unit,
    onSelectAll: (List<String>) -> Unit,
    onAction: (HomeAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var currentTab by rememberSaveable { mutableIntStateOf(1) }
    val context = LocalContext.current

    val activity = androidx.activity.compose.LocalActivity.current ?: return
    val windowSizeClass = androidx.compose.material3.windowsizeclass.calculateWindowSizeClass(activity = activity)
    val isTablet = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        currentTab = pagerState.currentPage
    }

    // 🌟 FIX: Paging items aur Folders ko mix karke select karne ke liye
    val currentTabItems = remember(currentTab, state.recentItems, state.currentFolders, state.favoritePdfs, pagedPdfs.itemSnapshotList) {
        when (currentTab) {
            0 -> state.recentItems
            1 -> {
                val pagedList = pagedPdfs.itemSnapshotList.items
                state.currentFolders + pagedList // Folders aur PDFs dono aayenge
            }
            2 -> state.favoritePdfs.map { HomeItem.PdfItem(it) }
            else -> emptyList()
        }
    }

    // 🌟 EXACT FIX: Ab BackHandler sirf Selection Mode me kaam aayega!
    BackHandler(enabled = isSelectionMode) {
        onSelectAll(emptyList())
        onSelectionModeChange(false)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                if (isSelectionMode) {
                    // 🌟 STEP 1: Current Tab me total kitne items hain?
                    val currentTabTotalCount = when (currentTab) {
                        0 -> state.recentItems.size
                        1 -> state.currentFolders.size + pagedPdfs.itemCount
                        2 -> state.favoritePdfs.size
                        else -> 0
                    }

                    // 🌟 STEP 2: Kya Current Tab ke saare items select ho chuke hain?
                    val isAllCurrentTabSelected = if (currentTabTotalCount == 0) false else {
                        if (currentTab == 1) {
                            // Paging Mode: Agar selection size total se zyada/barabar hai, aur jo screen pe dikh rahe hain wo sab selected hain
                            selectedPdfs.size >= currentTabTotalCount && currentTabItems.all { it.id in selectedPdfs }
                        } else {
                            // Memory Mode (Recent/Fav): Kya current tab ke saare ID selected map me hain?
                            val tabIds = currentTabItems.map { it.id }
                            selectedPdfs.containsAll(tabIds) && tabIds.isNotEmpty()
                        }
                    }

                    SelectionTopBar(
                        selectedCount = selectedPdfs.size, // Hamesha total count dikhayega
                        isAllSelected = isAllCurrentTabSelected, // Box RED hoga ya nahi?
                        onClearSelection = {
                            onSelectionModeChange(false)
                        },
                        onSelectAllToggle = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (isAllCurrentTabSelected) {
                                // Agar sab select the, to click karne par sab Deselect kar do
                                onSelectionModeChange(false)
                            } else {
                                // NAYA ACTION: Sirf Current Tab ke items MVI ke zariye select karo
                                onAction(HomeAction.SelectAllInTab(currentTab))
                            }
                        }
                    )
                } else {
                    // 🌟 FIX: Home screen ka title hamesha 'Hi Read' rahega aur Tabs hamesha dikhenge
                    UniversalTopBar(
                        title = "Hi Read",
                        isGridView = state.isGridView,
                        onSelectAllClick = {
                            onSelectionModeChange(true)
                            // 🌟 2026 ELITE FIX: स्क्रीन वाली फाइल्स नहीं, Database से सारी 100,000+ फाइल्स सेलेक्ट होंगी!
                            onAction(HomeAction.SelectAllInTab(currentTab))
                        },
                        onSearchClick = onSearchClick,
                        onSortClick = { onAction(HomeAction.OpenSheet(HomeSheetState.SortPicker)) },
                        onToggleView = { onAction(HomeAction.ToggleViewMode) },
                        onCreateFolderClick = { onAction(HomeAction.OpenSheet(HomeSheetState.CreateFolderDialog())) },
                        scrollBehavior = scrollBehavior
                    )

                    HomeTabs(
                        selectedTabIndex = pagerState.currentPage,
                        onTabSelected = { index -> coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                    )
                }
            }
        },
        bottomBar = {
            if (isSelectionMode || !isTablet) {
                androidx.compose.animation.AnimatedContent(
                    targetState = isSelectionMode,
                    label = "BottomBarSwapAnimation"
                ) { selectionActive ->
                    if (selectionActive) {
                        val selectedIdsSet = remember(selectedPdfs) { selectedPdfs.toSet() }
                        val selectedItemsList = remember(currentTabItems, selectedIdsSet) {
                            if (selectedIdsSet.isEmpty()) emptyList() else currentTabItems.filter { it.id in selectedIdsSet }
                        }

                        // 🌟 ELITE FIX: Hamara naya Master Bar! (No extra NavigationBar wrapper needed)
                        com.edu.pdf.presentation.common.SmartSelectionBottomBar(
                            selectedItems = selectedItemsList,
                            tabIndex = currentTab,
                            onDelete = { onAction(HomeAction.OpenSheet(HomeSheetState.DeleteConfirm(selectedItemsList))) },
                            onMove = { onAction(HomeAction.OpenSheet(HomeSheetState.MovePicker(selectedItemsList))) },
                            onMerge = { Toast.makeText(context, "Merge Engine: Coming Soon!", Toast.LENGTH_SHORT).show() },
                            onShare = {
                                val pdfUris = selectedItemsList.mapNotNull { it as? HomeItem.PdfItem }.map { it.pdf.id.toUri() }
                                if (pdfUris.isNotEmpty()) {
                                    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                                        type = "application/pdf"
                                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, java.util.ArrayList(pdfUris))
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Share PDFs via"))
                                }
                            },
                            onRemoveFromRecent = { onAction(HomeAction.RemoveFromRecent(selectedItemsList)) },
                            onUnfavorite = { onAction(HomeAction.UnfavoritePdfs(selectedItemsList.filterIsInstance<HomeItem.PdfItem>().map { it.pdf })) }
                        )
                    } else {
                        if (!isTablet) {
                            // 🌟 NORMAL APP BAR: Iske andar already height 72dp aur padding set hai
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 0.dp,
                                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                                    PremiumBottomBarItems(navController = navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HomeContent(
                state = state,
                isRefreshing = isRefreshing,
                isSelectionMode = isSelectionMode,
                selectedPdfs = selectedPdfs,
                pagedPdfs = pagedPdfs, // 🌟 FIX: Ye pass karna zaroori hai
                paddingValues = PaddingValues(0.dp),
                pagerState = pagerState,
                onAction = onAction,
                onToggleSelection = onToggleSelection,
                onSelectionModeChange = onSelectionModeChange
            )
        }
    }
}

@Composable
fun PermissionScreen(onRequestPermission: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Storage Permission Required", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("To find and display all PDFs on your device, we need \"All Files Access\".", textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRequestPermission) { Text("Grant Permission") }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\HomeViewModel.kt
``kotlin
package com.edu.pdf.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

sealed interface HomeSheetState {
    data object None : HomeSheetState
    data object SortPicker : HomeSheetState
    data class CreateFolderDialog(val parentId: String? = null) : HomeSheetState
    data class ItemMenu(val item: HomeItem) : HomeSheetState
    data class RenameDialog(val item: HomeItem, val currentName: String) : HomeSheetState
    data class DetailsDialog(val item: HomeItem) : HomeSheetState
    data class MovePicker(val items: List<HomeItem>) : HomeSheetState
    data class DeleteConfirm(val items: List<HomeItem>) : HomeSheetState
    data object AppPdfPicker : HomeSheetState
}

sealed interface HomeEvent {
    data class ShowSnackbar(val message: String) : HomeEvent
    data class NavigateToPdfViewer(val path: String) : HomeEvent
    data class NavigateToFolder(val folderId: String, val folderName: String, val type: com.edu.pdf.domain.model.FolderType) : HomeEvent
}

// 🌟 FIX: textInput wapas aa gaya aur navigation stack hamesha ke liye gayab hai!
data class HomeUiState(
    val isLoading: Boolean = true,
    val recentItems: ImmutableList<HomeItem> = persistentListOf(),
    val currentFolders: ImmutableList<HomeItem.FolderItem> = persistentListOf(),
    val favoritePdfs: ImmutableList<PdfFile> = persistentListOf(),
    val foldersTree: ImmutableList<Folder> = persistentListOf(), // 🌟 NAYA: Folders tree ab state ke andar
    val isRefreshing: Boolean = false, // 🌟 NAYA: Refreshing ab state ke andar
    val isSelectionMode: Boolean = false,
    val selectedIds: PersistentSet<String> = persistentSetOf(),
    val isGridView: Boolean = false,
    val sortType: SortType = SortType.DATE_DESC,
    val activeSheetState: HomeSheetState = HomeSheetState.None,
    val isProcessing: Boolean = false
)

sealed interface HomeAction {
    data object Initialize : HomeAction
    data class ToggleSelection(val id: String) : HomeAction
    data class SetSelectionMode(val enabled: Boolean) : HomeAction
    data class SelectAll(val ids: List<String>) : HomeAction

    data class NavigateToVirtualFolder(val folder: Folder) : HomeAction
    data class OpenSheet(val state: HomeSheetState) : HomeAction
    data object CloseSheet : HomeAction

    data class ConfirmCreateFolder(val folderName: String) : HomeAction
    data class ConfirmRename(val item: HomeItem, val newName: String) : HomeAction
    data class ConfirmDelete(val items: List<HomeItem>) : HomeAction

    data class ConfirmMove(val targetFolderId: String?) : HomeAction
    data class UpdateSortType(val type: SortType) : HomeAction
    data class CreateContextualFolder(val name: String, val parentId: String?) : HomeAction
    data object ToggleViewMode : HomeAction
    data object RefreshData : HomeAction
    data class ValidateAndOpenPdf(val pdf: PdfFile) : HomeAction
    data class ToggleFavorite(val pdf: PdfFile) : HomeAction
    data class ToggleVaultStatus(val pdf: PdfFile) : HomeAction
    data class RemoveFromRecent(val items: List<HomeItem>) : HomeAction
    data class UnfavoritePdfs(val pdfs: List<PdfFile>) : HomeAction
    data object OpenAppPdfPicker : HomeAction
    data class ImportFile(val uriString: String) : HomeAction
    data class MovePdfsToCurrentFolder(val pdfIds: List<String>) : HomeAction

    data class SelectAllInTab(val tabIndex: Int) : HomeAction

}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PdfRepository,
    private val deletePdfsUseCase: DeletePdfsUseCase,
    private val renamePdfUseCase: RenamePdfUseCase,
    private val scanPdfsUseCase: ScanPdfsUseCase,
    private val createFolderUseCase: com.edu.pdf.domain.usecase.CreateFolderUseCase, // 🌟 NAYA ADD KIYA
    private val deleteFolderUseCase: com.edu.pdf.domain.usecase.DeleteFolderUseCase, // 🌟 NAYA ADD KIYA
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.DATE_DESC)
    private val _isRefreshing = MutableStateFlow(false)

    private val _internalState = MutableStateFlow(HomeUiState(isLoading = true, activeSheetState = HomeSheetState.None))
    private var hasInitialized = false


    // 1. Sirf Folders ke liye Flow
    // 1. 🌟 ELITE MVI FIX: Folders ab Sort Type ke hisaab se react karenge
    private val currentFoldersFlow = _sortType.flatMapLatest { sort ->
        repository.getManagedFolders(null, isVault = false).map { folders ->
            val sortedFolders = when (sort) {
                SortType.NAME_ASC -> folders.sortedBy { it.name.lowercase() }
                SortType.NAME_DESC -> folders.sortedByDescending { it.name.lowercase() }
                SortType.DATE_DESC -> folders.sortedByDescending { it.createdAt }
                SortType.DATE_ASC -> folders.sortedBy { it.createdAt }
                SortType.SIZE_DESC -> folders.sortedByDescending { it.pdfCount } // Folder ke liye size = file count
                SortType.SIZE_ASC -> folders.sortedBy { it.pdfCount }
            }
            sortedFolders.map { HomeItem.FolderItem(it) }.toImmutableList()
        }
    }.flowOn(Dispatchers.Default)

    // 2. PDFs ke liye Paging Flow (Strict MVI - UI logic se alag)
    val pagedUncategorizedPdfsFlow = _sortType.flatMapLatest { sort ->
        repository.getAllPdfsPaged(sort).map { pagingData ->
            pagingData.map { HomeItem.PdfItem(it) }
        }
    }.cachedIn(viewModelScope)

    private val favoritePdfsFlow = _sortType.flatMapLatest { sort -> repository.getFavoritePdfs(sort) }

    private val recentItemsFlow = combine(repository.getRecentPdfs(), repository.getRecentFolders()) { recentPdfs, recentFolders ->
        val pdfItems = recentPdfs.map { HomeItem.PdfItem(it) }
        val folderItems = recentFolders.map { HomeItem.FolderItem(it) }
        (pdfItems + folderItems)
            .sortedByDescending { item -> if (item is HomeItem.PdfItem) item.pdf.lastOpenedTime else (item as HomeItem.FolderItem).folder.lastOpenedTime }
            .take(50)
    }

    // 🌟 FIX: Data flows ko Triple mein group kar diya taaki 5-flow limit cross na ho
    // 🌟 FIX: unifiedItemsFlow ki jagah ab currentFoldersFlow aayega
    private val uiDataFlow = combine(recentItemsFlow, currentFoldersFlow, favoritePdfsFlow) { recent, folders, favs ->
        Triple(recent, folders, favs)
    }

    private val prefDataFlow = combine(userPreferences.isGridViewFlow, _sortType, _isRefreshing) { isGrid, sort, refreshing ->
        Triple(isGrid, sort, refreshing)
    }

    // Ab hum sirf 4 chizein combine kar rahe hain: uiData, prefData, tree, internal
    val uiState: StateFlow<HomeUiState> = combine(
        uiDataFlow,
        prefDataFlow,
        repository.getAllManagedFolders(isVault = false),
        _internalState
    ) { uiData, prefData, tree, internal ->
        internal.copy(
            isLoading = false,
            recentItems = uiData.first.toImmutableList(),
            currentFolders = uiData.second,
            favoritePdfs = uiData.third.toImmutableList(),
            foldersTree = tree.toImmutableList(),
            isGridView = prefData.first,
            sortType = prefData.second,
            isRefreshing = prefData.third
        )
    }.distinctUntilChanged().flowOn(Dispatchers.Default).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    private fun scanDeviceForData() { viewModelScope.launch(Dispatchers.IO) { scanPdfsUseCase() } }

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Initialize -> if (!hasInitialized) { scanDeviceForData(); hasInitialized = true }
            is HomeAction.ToggleSelection -> {
                val currentSelected = _internalState.value.selectedIds
                val newSelection = if (currentSelected.contains(action.id)) currentSelected.remove(action.id) else currentSelected.add(action.id)
                _internalState.update { it.copy(selectedIds = newSelection) }
            }
            is HomeAction.SetSelectionMode -> _internalState.update { it.copy(isSelectionMode = action.enabled, selectedIds = if (!action.enabled) persistentSetOf() else it.selectedIds) }
            is HomeAction.SelectAll -> _internalState.update { it.copy(selectedIds = action.ids.toPersistentSet()) }
            // ✅ YAHAN YE NAYA CODE PASTE KARO ✅
            is HomeAction.SelectAllInTab -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val allIds = when (action.tabIndex) {
                        0 -> { // Recent Tab
                            val recentPdfs = repository.getRecentPdfs().first().map { it.id }
                            val recentFolders = repository.getRecentFolders().first().map { it.folderId }
                            recentPdfs + recentFolders
                        }
                        1 -> { // 🌟 MVI & MEMORY FIX: All Files Tab
                            val folderIds = repository.getManagedFolders(null, isVault = false).first().map { it.folderId }
                            val pdfIds = repository.getUncategorizedPdfIdsFast()
                            folderIds + pdfIds
                        }
                        2 -> { // 🌟 MVI & MEMORY FIX: Favorites Tab
                            repository.getFavoritePdfIdsFast()
                        }
                        else -> emptyList()
                    }
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(selectedIds = allIds.toPersistentSet()) }
                    }
                }
            }
            is HomeAction.NavigateToVirtualFolder -> {
                viewModelScope.launch(Dispatchers.IO) { repository.updateFolderLastOpenedTime(action.folder.folderId, System.currentTimeMillis()) }
                viewModelScope.launch { _events.send(HomeEvent.NavigateToFolder(action.folder.folderId, action.folder.name, com.edu.pdf.domain.model.FolderType.VIRTUAL_HUB)) }
            }
            is HomeAction.OpenSheet -> _internalState.update { it.copy(activeSheetState = action.state) }
            is HomeAction.CloseSheet -> _internalState.update { it.copy(activeSheetState = HomeSheetState.None) }
// OnTextInputChange wali puri line delete kar di gayi hai

            is HomeAction.UpdateSortType -> { _sortType.value = action.type; onAction(HomeAction.CloseSheet) }

            is HomeAction.ConfirmCreateFolder -> {
                val folderName = action.folderName.trim() // 🌟 अब डेटा सीधे action से आ रहा है
                if (folderName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                    viewModelScope.launch(Dispatchers.IO) {
                        createFolderUseCase(folderName, null) // 🌟 REPOSITORY HATA KAR USECASE LAGA DIYA
                        withContext(Dispatchers.Main) {
                            _internalState.update { it.copy(isProcessing = false) }
                            _events.send(HomeEvent.ShowSnackbar("Folder created"))
                        }
                    }
                }
            }
            is HomeAction.ConfirmRename -> {
                val newName = action.newName.trim() // 🌟 अब डेटा सीधे action से आ रहा है
                if (newName.isNotBlank()) {
                    _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                    viewModelScope.launch(Dispatchers.IO) {
                        when (val item = action.item) {
                            is HomeItem.FolderItem -> repository.renameManagedFolder(item.folder.folderId, newName)
                            is HomeItem.PdfItem -> renamePdfUseCase(item.pdf, newName)
                        }
                        withContext(Dispatchers.Main) { _internalState.update { it.copy(isProcessing = false) } }
                    }
                }
            }
            is HomeAction.ConfirmDelete -> {
                val itemsToDelete = action.items // 🌟 अब डेटा सीधे action से आ रहा है
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val foldersToDelete = itemsToDelete.filterIsInstance<HomeItem.FolderItem>()
                    val pdfsToDelete = itemsToDelete.filterIsInstance<HomeItem.PdfItem>().map { it.pdf }

                    foldersToDelete.forEach { deleteFolderUseCase(it.folder.folderId) } // 🌟 REPOSITORY HATA KAR USECASE LAGA DIYA
                    if (pdfsToDelete.isNotEmpty()) deletePdfsUseCase(pdfsToDelete)

                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false, isSelectionMode = false, selectedIds = persistentSetOf()) }
                        _events.send(HomeEvent.ShowSnackbar("Items deleted successfully"))
                    }
                }
            }
            is HomeAction.ConfirmMove -> {
                val state = _internalState.value.activeSheetState as? HomeSheetState.MovePicker ?: return
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val pdfIds = state.items.filterIsInstance<HomeItem.PdfItem>().map { it.pdf.id }
                    val folderIds = state.items.filterIsInstance<HomeItem.FolderItem>().map { it.folder.folderId }
                    if (pdfIds.isNotEmpty()) repository.movePdfsToVirtualFolder(pdfIds, action.targetFolderId, isVault = false)
                    folderIds.forEach { repository.moveFolderToVirtualFolder(it, action.targetFolderId, isVault = false) }
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false, isSelectionMode = false, selectedIds = persistentSetOf()) }
                        _events.send(HomeEvent.ShowSnackbar("Moved successfully"))
                    }
                }
            }
            is HomeAction.CreateContextualFolder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.createManagedFolder(action.name, action.parentId)
                    withContext(Dispatchers.Main) { _events.send(HomeEvent.ShowSnackbar("Folder created successfully")) }
                }
            }
            is HomeAction.ToggleViewMode -> viewModelScope.launch { userPreferences.saveGridViewPreference(!userPreferences.isGridViewFlow.first()) }
            is HomeAction.RefreshData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _isRefreshing.value = true; scanPdfsUseCase(); delay(800); _isRefreshing.value = false
                }
            }
            is HomeAction.ValidateAndOpenPdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val exists = File(action.pdf.path).exists() || repository.checkFileExists(action.pdf.id)
                    if (exists) {
                        repository.updateLastOpenedTime(action.pdf.id, System.currentTimeMillis())
                        withContext(Dispatchers.Main) { _events.send(HomeEvent.NavigateToPdfViewer(action.pdf.path)) }
                    } else {
                        withContext(Dispatchers.Main) { _events.send(HomeEvent.ShowSnackbar("File moved or deleted externally.")) }
                        deletePdfsUseCase(listOf(action.pdf))
                    }
                }
            }
            is HomeAction.ToggleFavorite -> viewModelScope.launch { repository.toggleFavorite(action.pdf.id, !action.pdf.isFavorite) }
            is HomeAction.RemoveFromRecent -> viewModelScope.launch(Dispatchers.IO) {
                action.items.forEach { item ->
                    if (item is HomeItem.PdfItem) repository.updateLastOpenedTime(item.pdf.id, 0L) else repository.updateFolderLastOpenedTime((item as HomeItem.FolderItem).folder.folderId, 0L)
                }
                withContext(Dispatchers.Main) { _internalState.update { it.copy(isSelectionMode = false, selectedIds = persistentSetOf()) } }
            }
            is HomeAction.UnfavoritePdfs -> viewModelScope.launch(Dispatchers.IO) {
                action.pdfs.forEach { repository.toggleFavorite(it.id, false) }
                withContext(Dispatchers.Main) { _internalState.update { it.copy(isSelectionMode = false, selectedIds = persistentSetOf()) } }
            }
            is HomeAction.ToggleVaultStatus -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val newVaultStatus = !action.pdf.isVault
                    repository.movePdfsToVirtualFolder(listOf(action.pdf.id), null, isVault = newVaultStatus)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ShowSnackbar(if (newVaultStatus) "Secured in Vault" else "Removed from Vault"))
                    }
                }
            }
            is HomeAction.OpenAppPdfPicker -> _internalState.update { it.copy(activeSheetState = HomeSheetState.AppPdfPicker) }
            is HomeAction.ImportFile -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = repository.importPdfFromUri(action.uriString, null, isVault = false, isPhysicalFolder = false)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ShowSnackbar(if (result.isSuccess) "Imported Successfully" else "Import Failed"))
                    }
                }
            }
            is HomeAction.MovePdfsToCurrentFolder -> {
                _internalState.update { it.copy(isProcessing = true, activeSheetState = HomeSheetState.None) }
                viewModelScope.launch(Dispatchers.IO) {
                    repository.movePdfsToVirtualFolder(action.pdfIds, null, isVault = false)
                    withContext(Dispatchers.Main) {
                        _internalState.update { it.copy(isProcessing = false) }
                        _events.send(HomeEvent.ShowSnackbar("Added successfully!"))
                    }
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\EmptyStateView.kt
``kotlin
package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyStateView(
    title: String,
    subtitle: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 🌟 PREMIUM EMPTY STATE ILLUSTRATION
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.edu.pdf.R.drawable.empty_state_illustration), // Yahan photo ka naam hai
            contentDescription = "Empty State",
            modifier = Modifier
                .fillMaxWidth(0.55f) // 🌟 Photo kitni badi dikhani hai (55% of screen width)
                .aspectRatio(1f) // Square shape maintain rakhega
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onActionClick,
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = actionText, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\HomeContent.kt
``kotlin
package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.itemKey
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.HomeSheetState
import com.edu.pdf.presentation.home.HomeUiState
import kotlinx.collections.immutable.PersistentSet


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeUiState,
    isRefreshing: Boolean,
    isSelectionMode: Boolean,
    selectedPdfs: PersistentSet<String>,
    paddingValues: PaddingValues,
    pagerState: PagerState,
    onAction: (HomeAction) -> Unit,
    onToggleSelection: (String) -> Unit,
    onSelectionModeChange: (Boolean) -> Unit,
    pagedPdfs: androidx.paging.compose.LazyPagingItems<HomeItem.PdfItem>,
) {
    val onLongPressEnableSelection: (String) -> Unit = { id ->
        if (!isSelectionMode) {
            onSelectionModeChange(true)
            if (!selectedPdfs.contains(id)) onToggleSelection(id)
        }
    }
    androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onAction(HomeAction.ImportFile(it.toString())) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            userScrollEnabled = !isSelectionMode
        ) { page ->
            PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = { onAction(HomeAction.RefreshData) }) {

                // 🌟 MVI STRICT FIX: All Files (Page 1) me ab Paging use hoga
                if (page == 1) {
                    if (state.currentFolders.isEmpty() && pagedPdfs.itemCount == 0) {
                        // 🌟 PREMIUM TEXT FOR 'ALL FILES'
                        EmptyStateView(
                            title = "Your Workspace is Empty",
                            subtitle = "Start building your library. Import PDFs or create folders to get organized."
                        )
                    } else {
                        if (state.isGridView) {
                            key(state.sortType) {
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(minSize = 110.dp),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(state.currentFolders, key = { it.id }) { item ->
                                        UnifiedGridItem(item, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection)
                                    }
                                    items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                                        val pdfItem = pagedPdfs[index]
                                        if (pdfItem != null) UnifiedGridItem(pdfItem, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection)
                                    }
                                }
                            }
                        } else {
                            key(state.sortType) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 120.dp)
                                ) {
                                    items(state.currentFolders, key = { it.id }) { item ->
                                        UnifiedListItem(item, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection)
                                    }
                                    items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                                        val pdfItem = pagedPdfs[index]
                                        if (pdfItem != null) UnifiedListItem(pdfItem, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Page 0 (Recent) aur Page 2 (Favorites) ka purana logic
                    val currentList = if (page == 0) state.recentItems else state.favoritePdfs.map { HomeItem.PdfItem(it) }

                    if (currentList.isEmpty()) {
                        // 🌟 PREMIUM SMART TEXT FOR 'RECENT' & 'FAVORITES'
                        if (page == 0) {
                            EmptyStateView(
                                title = "No Recent Activity",
                                subtitle = "Pick up right where you left off. Open any PDF to quickly access it here."
                            )
                        } else {
                            EmptyStateView(
                                title = "No Favorites Yet",
                                subtitle = "Keep your important PDFs handy. Tap the bookmark icon to add them here."
                            )
                        }
                    } else {
                        if (state.isGridView) {
                            key(state.sortType) {
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(minSize = 110.dp),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(currentList, key = { it.id }) { item ->
                                        UnifiedGridItem(item, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection)
                                    }
                                }
                            }
                        } else {
                            key(state.sortType) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 120.dp)
                                ) {
                                    items(currentList, key = { it.id }) { item ->
                                        UnifiedListItem(item, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnifiedGridItem(item: HomeItem, isSelectionMode: Boolean, selectedPdfs: PersistentSet<String>, onAction: (HomeAction) -> Unit, onToggleSelection: (String) -> Unit, onLongPress: (String) -> Unit) {
    val isSelected = selectedPdfs.contains(item.id)
    when (item) {
        is HomeItem.FolderItem -> HomeFolderGridItem(folder = item.folder, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.NavigateToVirtualFolder(item.folder)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
        is HomeItem.PdfItem -> PdfGridItem(pdf = item.pdf, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.ValidateAndOpenPdf(item.pdf)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
    }
}

@Composable
fun UnifiedListItem(item: HomeItem, isSelectionMode: Boolean, selectedPdfs: PersistentSet<String>, onAction: (HomeAction) -> Unit, onToggleSelection: (String) -> Unit, onLongPress: (String) -> Unit) {
    val isSelected = selectedPdfs.contains(item.id)
    when (item) {
        is HomeItem.FolderItem -> HomeFolderListItem(folder = item.folder, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.NavigateToVirtualFolder(item.folder)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
        is HomeItem.PdfItem -> PdfListItem(pdf = item.pdf, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.ValidateAndOpenPdf(item.pdf)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\HomeFolderGridItem.kt
``kotlin
package com.edu.pdf.presentation.home.components

import android.text.format.DateUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.Folder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeFolderGridItem(
    folder: Folder,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onMoreOptionsClick: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val displayDate = remember(folder.createdAt) {
        DateUtils.getRelativeTimeSpanString(
            if (folder.createdAt < 1000000000000L) folder.createdAt * 1000 else folder.createdAt,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

    // 🌟 Design matched EXACTLY with your PdfGridItem
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            // 🌟 PREMIUM 3D FOLDER IMAGE
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.edu.pdf.R.drawable.premium_folder1),
                contentDescription = "Folder",
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = folder.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
            text = displayDate,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 2.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${folder.pdfCount} items",
                fontSize = 11.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .then(if (!isSelectionMode) Modifier.clickable { onMoreOptionsClick() } else Modifier),
                contentAlignment = Alignment.Center
            ) {
                if (isSelectionMode) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\HomeFolderListItem.kt
``kotlin
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
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\HomeTabs.kt
``kotlin
package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edu.pdf.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    // 🌟 Tumhara original premium Data structure with Icons & Strings
    val homeTabsList = listOf(
        Triple(stringResource(R.string.tab_recent), Icons.Default.Schedule, 0),
        Triple(stringResource(R.string.tab_all_files), Icons.Default.Description, 1),
        Triple(stringResource(R.string.tab_favorites), Icons.Default.Favorite, 2)
    )

    // 🌟 2026 MODERN API: PrimaryTabRow (Isme negative width crash apne aap handle hota hai)
    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        containerColor = Color.Transparent,
        divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) },
        indicator = {
            // 🌟 NAYA INDICATOR: No manual math required. matchContentSize = true text ke hisaab se size set karega.
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = true),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        // 🌟 Tumhara Original UI Design bilkul waisa hi hai
        homeTabsList.forEach { (title, icon, index) ->
            val isSelected = selectedTabIndex == index
            val tintColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, modifier = Modifier.size(18.dp), tint = tintColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = tintColor
                        )
                    }
                }
            )
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\MoveFolderListItem.kt
``kotlin
package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.Folder

@Composable
fun MoveFolderListItem(
    folder: Folder,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp), // 🌟 Refined padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🌟 Premium 2026 Squircle Icon
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp)) // Modern smooth curve
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = folder.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold, // Bolder for hierarchy
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = String.format(LocalLocale.current.platformLocale, "📄 %d items", folder.pdfCount),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 🌟 Navigation hint icon
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(22.dp)
        )
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\PdfActionBottomSheet.kt
``kotlin
package com.edu.pdf.presentation.home.components

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.edu.pdf.domain.model.PdfFile
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfActionBottomSheet(
    pdf: PdfFile,
    onDismiss: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onActionClick: (String) -> Unit
) {
    var showDetailsDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = LocalHapticFeedback.current
    rememberCoroutineScope()

    val currentLocale = LocalConfiguration.current.locales.get(0)
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", currentLocale)
    val formattedDate = sdf.format(Date(if (pdf.lastModified < 1000000000000L) pdf.lastModified * 1000 else pdf.lastModified))
    val formattedSize = if (pdf.sizeInBytes >= 1024 * 1024) {
        String.format(Locale.US, "%.1f MB", pdf.sizeInBytes / (1024f * 1024f))
    } else {
        String.format(Locale.US, "%.1f KB", pdf.sizeInBytes / 1024f)
    }

    fun printPdfFile() {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {

                // 🌟 2026 ELITE FIX: Independent Scope जो Bottom Sheet बंद होने पर मरेगा नहीं
                // 🌟 2026 ELITE FIX: SupervisorJob add kiya taaki errors handle ho sakein aur battery bache
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO + kotlinx.coroutines.SupervisorJob()).launch {
                    try {
                        context.contentResolver.openInputStream(pdf.id.toUri())?.use { input ->
                            FileOutputStream(destination?.fileDescriptor).use { output ->
                                val buffer = ByteArray(8 * 1024)
                                var bytesRead: Int
                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                    if (cancellationSignal?.isCanceled == true) {
                                        callback?.onWriteCancelled()
                                        return@launch
                                    }
                                    output.write(buffer, 0, bytesRead)
                                }
                            }
                        }
                        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                    } catch (e: Exception) {
                        callback?.onWriteFailed(e.localizedMessage ?: "Print failed")
                    }
                }
            }

            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback?.onLayoutCancelled()
                    return
                }
                val info = PrintDocumentInfo.Builder(pdf.name)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()
                callback?.onLayoutFinished(info, newAttributes != oldAttributes)
            }
        }
        printManager.print(pdf.name, printAdapter, PrintAttributes.Builder().build())
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🌟 PREMIUM PDF THUMBNAIL PREVIEW
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    PdfThumbnail(
                        pdf = pdf,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pdf.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "$formattedDate • $formattedSize",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onFavoriteToggle()
                }) {
                    Icon(
                        imageVector = if (pdf.isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = if (pdf.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton("Share", Icons.Default.Share) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onShare()
                }
                QuickActionButton("Rename", Icons.Default.FormatColorText) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onActionClick("Rename")
                }
                // 🌟 FIX 1: Details me MVI ki jagah local state trigger karenge
                QuickActionButton("Details", Icons.Default.Info) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showDetailsDialog = true
                }
                // 🌟 FIX 2: Print hone ke baad sheet dismiss kar denge
                QuickActionButton("Print", Icons.Default.Print) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    printPdfFile()
                    onDismiss()
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

            val vaultTitle = if (pdf.isVault) "Remove from Vault" else "Move to Vault"
            val vaultIcon = if (pdf.isVault) Icons.Default.LockOpen else Icons.Default.Lock

            val tools = listOf(
                "Move to" to Icons.AutoMirrored.Filled.DriveFileMove,
                "Merge PDF" to Icons.AutoMirrored.Filled.CallMerge,
                "Split PDF" to Icons.AutoMirrored.Filled.CallSplit,
                "Compress PDF" to Icons.Default.Compress,
                vaultTitle to vaultIcon,
                "Delete" to Icons.Default.Delete
            )

            tools.forEach { (title, icon) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (title == "Delete") onDelete() else onActionClick(title)
                        }
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val color = if (title == "Delete") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (title == "Delete") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        // 🌟 THE MAGIC: Ye dialog sheet ke upar aayega bina sheet ko hide kiye!
        if (showDetailsDialog) {
            AlertDialog(
                onDismissRequest = { showDetailsDialog = false },
                title = { Text("Item Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Column {
                        DetailRow("Name", pdf.name)
                        DetailRow("Size/Type", formattedSize)
                        DetailRow("Modified", formattedDate)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDetailsDialog = false }) {
                        Text("Close", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}


@Composable
fun RowScope.QuickActionButton(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\PdfGridItem.kt
``kotlin
package com.edu.pdf.presentation.home.components

import android.text.format.DateUtils
import android.text.format.Formatter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.PdfFile

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PdfGridItem(
    pdf: PdfFile,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onMoreOptionsClick: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current
    val displaySize = remember(pdf.sizeInBytes) {
        Formatter.formatShortFileSize(context, pdf.sizeInBytes)
    }
    val displayDate = remember(pdf.lastModified) {
        DateUtils.getRelativeTimeSpanString(
            if (pdf.lastModified < 1000000000000L) pdf.lastModified * 1000 else pdf.lastModified,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            PdfThumbnail(pdf = pdf, modifier = Modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = pdf.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
            text = displayDate,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 2.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displaySize,
                fontSize = 11.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .then(if (!isSelectionMode) Modifier.clickable { onMoreOptionsClick() } else Modifier),
                contentAlignment = Alignment.Center
            ) {
                if (isSelectionMode) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\PdfListItem.kt
``kotlin
package com.edu.pdf.presentation.home.components

import android.text.format.DateUtils
import android.text.format.Formatter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pdf.domain.model.PdfFile

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PdfListItem(
    pdf: PdfFile,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onMoreOptionsClick: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current
    val displaySize = remember(pdf.sizeInBytes) {
        Formatter.formatShortFileSize(context, pdf.sizeInBytes)
    }
    val displayDate = remember(pdf.lastModified) {
        DateUtils.getRelativeTimeSpanString(
            if (pdf.lastModified < 1000000000000L) pdf.lastModified * 1000 else pdf.lastModified,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            PdfThumbnail(pdf = pdf, modifier = Modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pdf.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$displayDate  •  $displaySize",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (isSelectionMode) {
            Icon(
                // 🌟 PURE UI CONSISTENCY: Circle hata kar hamesha Square (CheckBox) dikhega
                imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        } else {
            IconButton(onClick = onMoreOptionsClick, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\PdfThumbnail.kt
``kotlin
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
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\SelectionTopBar.kt
``kotlin
package com.edu.pdf.presentation.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTopBar(
    selectedCount: Int,
    isAllSelected: Boolean, // 🌟 UI MATH HATA DIYA: Ab ye seedha HomeScreen se aayega
    onClearSelection: () -> Unit,
    onSelectAllToggle: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "$selectedCount Selected",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onClearSelection) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onSelectAllToggle) {
                Icon(
                    imageVector = if (isAllSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                    contentDescription = null,
                    tint = if (isAllSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            scrolledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\home\components\SortBottomSheet.kt
``kotlin
package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// 🌟 L8 FIX: Naya Import Path
import com.edu.pdf.domain.model.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: SortType,
    onSortSelected: (SortType) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
            Text(
                text = "Sort By",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            val options = listOf(
                SortType.NAME_ASC to "Name (A to Z)",
                SortType.NAME_DESC to "Name (Z to A)",
                SortType.DATE_DESC to "Newest First",
                SortType.DATE_ASC to "Oldest First",
                SortType.SIZE_DESC to "Largest First",
                SortType.SIZE_ASC to "Smallest First"
            )

            options.forEach { (type, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortSelected(type) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isSelected = currentSort == type
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    if (isSelected) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\navigation\Screen.kt
``kotlin
package com.edu.pdf.presentation.navigation

import kotlinx.serialization.Serializable
import com.edu.pdf.domain.model.FolderType

sealed interface Screen {

    @Serializable
    data object Permission : Screen
    @Serializable
    data object Home : Screen

    @Serializable
    data object Folders : Screen

    @Serializable
    data object Tools : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Search : Screen

    // 🌟 THE ELITE FIX: Default values hata diye taaki crash na ho aur strict matching ho
    @Serializable
    data class UnifiedFolder(
        val folderId: String,
        val folderName: String,
        val folderType: FolderType
    ) : Screen

    @Serializable
    data class PdfViewer(val pdfPath: String) : Screen

    // 🌟 GOD MODE: Dedicated Vault Route
    @Serializable
    data object Vault : Screen
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\ocr\OcrComponents.kt
``kotlin
package com.edu.pdf.presentation.ocr

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun LiveTextOverlay(
    state: OcrUiState,
    onStopLiveText: () -> Unit
) {
    if (!state.isLiveTextActive) return

    BackHandler(enabled = true) {
        onStopLiveText()
    }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(100f)
    ) {
        state.capturedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Frozen Screen",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(PointerEventPass.Main).changes.forEach { it.consume() }
                        }
                    }
                }
        )

        SelectionContainer(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {

                // 🌟 RAW ML KIT DEFAULT RENDERING 🌟
                // 🌟 RAW ML KIT DEFAULT RENDERING 🌟
                state.extractedBlocks.forEach { block ->
                    val rect = block.boundingBox ?: return@forEach

                    val leftDp = with(density) { rect.left.toDp() }
                    val topDp = with(density) { rect.top.toDp() }

                    // 🌟 FIX 1: Width calculation add ki
                    val widthDp = with(density) { rect.width().toDp() }
                    val heightDp = with(density) { rect.height().toDp() }

                    // Basic Font Size Calculation
                    val lineCount = block.lineCount.coerceAtLeast(1)
                    val exactFontSize = with(density) { (heightDp / lineCount).toSp() }

                    BasicText(
                        // ML Kit ka default format (\n ke sath)
                        text = block.text,
                        modifier = Modifier
                            .offset(x = leftDp, y = topDp)
                            // 🌟 FIX 2: Strict size lagaya taaki left column right column par OVERLAP na kare!
                            // (Ye false selection ko hamesha ke liye rok dega)
                            .size(width = widthDp, height = heightDp),
                        style = TextStyle(
                            color = Color.Transparent,
                            fontSize = exactFontSize,
                            lineHeight = exactFontSize,
                            // 🌟 FIX 3: Android ke faltu margins hataye taaki text exact dabbe me fit baithe
                            platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)
                        ),
                        // 🌟 FIX 4: Text ko forced agli line me jane se roko. Jo ML Kit ne bola hai wahi rahega!
                        softWrap = false
                    )
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        IconButton(
            onClick = onStopLiveText,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Live Text",
                tint = Color.White
            )
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\ocr\OcrContract.kt
``kotlin
package com.edu.pdf.presentation.ocr

import android.graphics.Bitmap
import com.edu.pdf.domain.ocr.OcrTextBlock

/**
 * 🌟 MVI State: Live Text Overlay kaisa dikhega aur kya yaad rakhega
 */
data class OcrUiState(
    val isLiveTextActive: Boolean = false, // Sheet nahi, ab screen ke upar ka transparent overlay active hoga
    val isLoading: Boolean = false,
    val capturedBitmap: Bitmap? = null, // Screen ka freeze kiya hua snapshot
    val extractedBlocks: List<OcrTextBlock> = emptyList(), // ML Kit se nikle hue words aur unke X,Y coordinates
    val errorMessage: String? = null
)

/**
 * 🌟 MVI Actions: User screen par kya actions le sakta hai
 */
sealed interface OcrAction {
    // Jab user Live Text button dabaye aur screen freeze karni ho
    data class StartLiveText(val bitmap: Bitmap) : OcrAction

    // Jab user 'X' (Close) dabaye aur wapas normal PDF dekhna ho
    data object StopLiveText : OcrAction

    data object ClearError : OcrAction
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\ocr\OcrViewModel.kt
``kotlin
package com.edu.pdf.presentation.ocr

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.ocr.TextRecognitionEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val textRecognitionEngine: TextRecognitionEngine
) : ViewModel() {

    private val _state = MutableStateFlow(OcrUiState())
    val state: StateFlow<OcrUiState> = _state.asStateFlow()

    fun onAction(action: OcrAction) {
        when (action) {
            // Naya action: Jab user button dabaye
            is OcrAction.StartLiveText -> processLiveText(action.bitmap)

            // Naya action: Jab user cancel kare
            OcrAction.StopLiveText -> _state.update {
                it.copy(
                    isLiveTextActive = false,
                    capturedBitmap = null, // Photo memory se hata do
                    extractedBlocks = emptyList(), // Text memory se hata do
                    errorMessage = null
                )
            }

            OcrAction.ClearError -> _state.update {
                it.copy(errorMessage = null)
            }
        }
    }

    private fun processLiveText(bitmap: Bitmap) {
        viewModelScope.launch {
            // 1. Loading shuru karo, Live mode ON karo, aur screenshot (bitmap) memory me save karo
            _state.update {
                it.copy(
                    isLoading = true,
                    isLiveTextActive = true,
                    capturedBitmap = bitmap,
                    errorMessage = null
                )
            }

            // 2. Engine se blocks (Text + Coordinates) nikaalo
            val result = textRecognitionEngine.extractTextFromBitmap(bitmap)

            // 3. Result aane par UI ko batao
            result.fold(
                onSuccess = { blocks ->
                    _state.update {
                        it.copy(isLoading = false, extractedBlocks = blocks)
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Scanning failed")
                    }
                }
            )
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\PdfViewerScreen.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.ViewConfiguration
import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.compose.AndroidFragment
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.pdf.viewer.fragment.PdfViewerFragment
import com.edu.pdf.presentation.pdfviewer.ocr.OcrAction
import com.edu.pdf.presentation.pdfviewer.ocr.OcrSelectionOverlay
// 🌟 IMPORTS ME DHYAN DEIN: Humne naye function ka naam AiChatOverlayScreen rakha tha
import com.edu.pdf.presentation.pdfviewer.ai.AiChatOverlayScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PdfViewerScreen(
    onBack: () -> Unit,
    viewModel: PdfViewerViewModel = hiltViewModel(),
    ocrViewModel: com.edu.pdf.presentation.pdfviewer.ocr.OcrViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? AppCompatActivity ?: return

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ocrState by ocrViewModel.state.collectAsStateWithLifecycle()

    var showAiChat by remember { mutableStateOf(false) }
    var currentCapturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val pdfUri = uiState.pdfUri

    val window = activity.window
    val insetsController = remember(window) { WindowCompat.getInsetsController(window, window.decorView) }
    val touchSlop = remember { ViewConfiguration.get(context).scaledTouchSlop }
    val scope = rememberCoroutineScope()
    var tapJob by remember { mutableStateOf<Job?>(null) }
    val doubleTapTimeout = remember { ViewConfiguration.getDoubleTapTimeout().toLong() }

    LaunchedEffect(uiState.isTopBarVisible) {
        if (uiState.isTopBarVisible) {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
        } else {
            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    BackHandler {
        if (showAiChat) {
            showAiChat = false
            currentCapturedBitmap = null
        } else if (ocrState.isLiveTextActive) {
            ocrViewModel.onAction(OcrAction.StopLiveText)
        } else if (uiState.isSearchActive) {
            viewModel.onAction(PdfViewerAction.ToggleSearch)
        } else {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
            onBack()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            insetsController.show(WindowInsetsCompat.Type.statusBars())
        }
    }

    val darkColorMatrix = remember { ColorMatrix(floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    )) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        // 🌟 MAIN BOX (Yahin par saari layers ek ke upar ek aayengi)
        Box(modifier = Modifier.fillMaxSize()) {

            // ==========================================
            // LAYER 1: BASE (PDF VIEWER & TOP BAR)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AnimatedVisibility(
                    visible = uiState.isTopBarVisible,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .statusBarsPadding()
                            .height(56.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { insetsController.show(WindowInsetsCompat.Type.statusBars()); onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            text = "Pro PDF Viewer",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { viewModel.onAction(PdfViewerAction.ToggleNightMode) }) {
                            Icon(
                                imageVector = if (uiState.isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Night Mode",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // OCR Scanner Button
                        IconButton(
                            onClick = {
                                captureScreenNonBlocking(activity) { bitmap ->
                                    ocrViewModel.onAction(OcrAction.StartLiveText(bitmap))
                                }
                            }
                        ) {
                            Icon(Icons.Default.CenterFocusWeak, contentDescription = "Scan Text", tint = MaterialTheme.colorScheme.onSurface)
                        }

                        IconButton(onClick = { viewModel.onAction(PdfViewerAction.ToggleSearch) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .then(
                            if (uiState.isSearchActive) Modifier
                            else Modifier.pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                                        val downTime = System.currentTimeMillis()
                                        var isTap = true
                                        do {
                                            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                                            if (event.changes.size > 1) {
                                                isTap = false
                                                if (uiState.isTopBarVisible) viewModel.onAction(PdfViewerAction.SetTopBarVisible(false))
                                            }
                                            val pos = event.changes.first().position
                                            if ((pos - down.position).getDistance() > touchSlop) {
                                                isTap = false
                                                if (uiState.isTopBarVisible) viewModel.onAction(PdfViewerAction.SetTopBarVisible(false))
                                            }
                                        } while (event.changes.any { it.pressed })
                                        val upTime = System.currentTimeMillis()
                                        if (isTap && (upTime - downTime) < 200) {
                                            if (tapJob?.isActive == true) {
                                                tapJob?.cancel()
                                            } else {
                                                tapJob = scope.launch {
                                                    delay(doubleTapTimeout)
                                                    viewModel.onAction(PdfViewerAction.ToggleTopBar)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                ) {
                    if (pdfUri != null) {
                        AndroidFragment<PdfViewerFragment>(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { if (uiState.isNightMode) colorFilter = ColorFilter.colorMatrix(darkColorMatrix) },
                            onUpdate = { fragment ->
                                if (fragment.documentUri != pdfUri) {
                                    fragment.documentUri = pdfUri
                                }
                                try {
                                    if (fragment.lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.STARTED)) {
                                        fragment.isTextSearchActive = uiState.isSearchActive
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Failed to load PDF", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } // Column End

            // ==========================================
            // LAYER 2: THE FLOATING AI BUTTON (Naya Jadoo)
            // ==========================================
            AnimatedVisibility(
                visible = !showAiChat && !ocrState.isLiveTextActive, // Jab chat ya OCR khula ho to button chhupa do
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .navigationBarsPadding() // Screen ke niche bar se thoda upar rakhega
            ) {
                FloatingActionButton(
                    onClick = {
                        captureScreenNonBlocking(activity) { bitmap ->
                            currentCapturedBitmap = bitmap
                            showAiChat = true
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Ask AI")
                }
            }

            // ==========================================
            // LAYER 3: OCR OVERLAY
            // ==========================================
            OcrSelectionOverlay(
                state = ocrState,
                onAction = { action -> ocrViewModel.onAction(action) }
            )

            // ==========================================
            // LAYER 4: GEMINI AI CHAT OVERLAY
            // ==========================================
            // Humara naya slide-up screen yahin khulega
            AiChatOverlayScreen(
                isVisible = showAiChat,
                currentPageBitmap = currentCapturedBitmap,
                currentPageNumber = uiState.currentPageNumber, // PdfViewerViewModel mein add karo
                pdfName = uiState.pdfFileName,                 // PdfViewerViewModel mein add karo
                onDismiss = {
                    showAiChat = false
                    currentCapturedBitmap = null
                }
            )

        } // Box End
    }
}

private fun captureScreenNonBlocking(activity: Activity, onCaptured: (Bitmap) -> Unit) {
    val window = activity.window
    val view = window.decorView
    val bitmap = createBitmap(view.width, view.height)
    PixelCopy.request(window, bitmap, { copyResult ->
        if (copyResult == PixelCopy.SUCCESS) {
            onCaptured(bitmap)
        } else {
            bitmap.recycle()
        }
    }, Handler(Looper.getMainLooper()))
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\PdfViewerViewModel.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

data class PdfViewerUiState(
    val pdfUri: Uri? = null,
    val isTopBarVisible: Boolean = true,
    val isNightMode: Boolean = false,
    val isSearchActive: Boolean = false,
    val currentPageNumber: Int = 1,      // 🌟 NAYA
    val pdfFileName: String = ""          // 🌟 NAYA
)

sealed interface PdfViewerAction {
    data class SetTopBarVisible(val visible: Boolean) : PdfViewerAction
    data object ToggleTopBar : PdfViewerAction
    data object ToggleNightMode : PdfViewerAction
    data object ToggleSearch : PdfViewerAction
}

@HiltViewModel
class PdfViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfViewerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val path = savedStateHandle.get<String>("pdfPath")
        val uri = path?.let {
            if (it.startsWith("content://") || it.startsWith("file://")) it.toUri()
            else Uri.fromFile(File(it))
        }
        val fileName = path?.let { File(it).nameWithoutExtension } ?: ""
        _uiState.update { it.copy(pdfUri = uri, pdfFileName = fileName) }
    }

    fun onAction(action: PdfViewerAction) {
        when (action) {
            is PdfViewerAction.SetTopBarVisible -> _uiState.update { it.copy(isTopBarVisible = action.visible) }
            is PdfViewerAction.ToggleTopBar -> _uiState.update { it.copy(isTopBarVisible = !it.isTopBarVisible) }
            is PdfViewerAction.ToggleNightMode -> _uiState.update { it.copy(isNightMode = !it.isNightMode) }
            is PdfViewerAction.ToggleSearch -> _uiState.update { it.copy(isSearchActive = !it.isSearchActive) }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\ai\AiChatBottomSheet.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer.ai

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.pdf.domain.model.ChatMessage
import com.edu.pdf.domain.model.ChatRole

// ════════════════════════════════════════════════════════════
// 🌟 ENTRY POINT — Route Composable
// ════════════════════════════════════════════════════════════

@Composable
fun AiChatOverlayScreen(
    isVisible: Boolean,
    currentPageBitmap: android.graphics.Bitmap?,
    currentPageNumber: Int = 1,
    pdfName: String = "",
    onDismiss: () -> Unit,
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // 🌟 PDF context ViewModel ko do — Bitmap State mein NAHI jayega
    LaunchedEffect(isVisible, currentPageNumber, pdfName) {
        if (isVisible) {
            viewModel.onAction(
                AiChatAction.SetContext(
                    pageNumber = currentPageNumber,
                    pdfName = pdfName,
                    bitmapRef = currentPageBitmap
                )
            )
        }
    }

    // 🌟 Auto-scroll on new message
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            if (event is AiChatEvent.ScrollToBottom && state.messages.isNotEmpty()) {
                listState.animateScrollToItem(state.messages.lastIndex)
            }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(tween(durationMillis = 300)),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 280)
        ) + fadeOut(tween(durationMillis = 200))
    ) {
        // 🌟 IS COLUMN KO DEKHO: Iska modifier ab aisa dikhna chahiye
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                // ❌ Purane statusBarsPadding, navigationBarsPadding aur imePadding ko hata kar ye 1 line likho:
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            // ── 1. TOP BAR (Iske andar ka code bilkul mat chhedna, ye same rahega) ──
            AiChatTopBar(
                pageNumber = state.currentPageNumber,
                pdfName = state.pdfName,
                onDismiss = {
                    keyboardController?.hide()
                    onDismiss()
                }
            )

            // ── 2. CONTENT AREA (Ye bhi bilkul same rahega) ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (state.messages.isEmpty()) {
                    AiEmptyState(
                        prompts = state.suggestedPrompts,
                        pdfName = state.pdfName,
                        onPromptClick = { prompt ->
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.onAction(AiChatAction.OnSmartPromptClick(prompt))
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AiMessageList(
                        messages = state.messages,
                        isThinking = state.isAiThinking,
                        listState = listState,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.errorMessage?.let { error ->
                    AiErrorBanner(
                        message = error,
                        onDismiss = { viewModel.onAction(AiChatAction.DismissError) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
                    )
                }
            }

            // ── 3. INPUT BAR (Ye bhi bilkul same rahega) ──
            AiInputSection(
                input = state.currentInput,
                isThinking = state.isAiThinking,
                onInputChange = {
                    viewModel.onAction(AiChatAction.UpdateInput(it))
                },
                onSendClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                    viewModel.onAction(AiChatAction.SendMessage(state.currentInput))
                },
                onStopClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onAction(AiChatAction.StopStreaming)
                }
            )
        }
    }
}

// ════════════════════════════════════════════════════════════
// TOP BAR
// ════════════════════════════════════════════════════════════

@Composable
private fun AiChatTopBar(
    pageNumber: Int,
    pdfName: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close AI Chat",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                // 🌟 Pulsing AI icon
                AiIconPulse(modifier = Modifier.size(34.dp))

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Read AI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (pdfName.isNotBlank()) {
                        val displayName = if (pdfName.length > 28) {
                            pdfName.take(28) + "…"
                        } else pdfName

                        Text(
                            text = "Page $pageNumber  ·  $displayName",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Powered by Gemini",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                thickness = 0.5.dp
            )
        }
    }
}

// ════════════════════════════════════════════════════════════
// PULSING AI ICON
// ════════════════════════════════════════════════════════════

@Composable
private fun AiIconPulse(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ════════════════════════════════════════════════════════════
// MESSAGE LIST
// ════════════════════════════════════════════════════════════

@Composable
private fun AiMessageList(
    messages: List<ChatMessage>,
    isThinking: Boolean,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(200)) + slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(200)
                )
            ) {
                AiMessageBubble(message = message)
            }
        }

        // 🌟 Typing indicator — sirf tab show karo jab last message stream nahi kar raha
        if (isThinking && messages.lastOrNull()?.isStreaming == false) {
            item(key = "typing_indicator") {
                TypingIndicator()
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
// MESSAGE BUBBLE
// ════════════════════════════════════════════════════════════

@Composable
private fun AiMessageBubble(message: ChatMessage) {
    val isUser = message.role == ChatRole.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        // AI Avatar
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Bubble
        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.82f else 0.88f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isUser) 18.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 18.dp
                        )
                    )
                    .background(
                        if (isUser) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // 🌟 Streaming cursor blink effect
                val displayText = when {
                    message.isStreaming && message.text.isNotEmpty() -> message.text + "▋"
                    message.isStreaming && message.text.isEmpty() -> "▋"
                    else -> message.text
                }

                Text(
                    text = displayText,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // User Avatar
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
// TYPING INDICATOR — 3 Dots
// ════════════════════════════════════════════════════════════

@Composable
private fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing_dots")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // AI Avatar
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Dots Bubble
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 18.dp
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                repeat(3) { dotIndex ->
                    val dotAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.25f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 500,
                                delayMillis = dotIndex * 160
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot_alpha_$dotIndex"
                    )
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = dotAlpha
                                ),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
// EMPTY STATE
// ════════════════════════════════════════════════════════════

@Composable
private fun AiEmptyState(
    prompts: List<String>,
    pdfName: String,
    onPromptClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 140.dp), // bottom space for input bar
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Hero Icon
        Box(
            modifier = Modifier
                .size(84.dp)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Read AI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (pdfName.isNotBlank())
                "Ask me anything about\n\"${pdfName.take(30)}${if (pdfName.length > 30) "…" else ""}\""
            else
                "Ask me anything about this document",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Section header
        Text(
            text = "Suggested Questions",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        // 🌟 Suggestion chips
        prompts.take(4).forEach { prompt ->
            AiSuggestionChip(
                prompt = prompt,
                onClick = { onPromptClick(prompt) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun AiSuggestionChip(
    prompt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = prompt,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ════════════════════════════════════════════════════════════
// INPUT BAR
// ════════════════════════════════════════════════════════════

@Composable
private fun AiInputSection(
    input: String,
    isThinking: Boolean,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 12.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {

            // 🌟 Text Field — pill shape, no visible border
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                placeholder = {
                    Text(
                        text = "Message Read AI…",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(22.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.55f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.45f)
                ),
                shape = RoundedCornerShape(22.dp),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🌟 Send ↔ Stop toggle with animation
            AnimatedContent(
                targetState = isThinking,
                label = "send_stop_toggle",
                transitionSpec = {
                    (scaleIn(tween(180)) + fadeIn(tween(180))) togetherWith
                            (scaleOut(tween(120)) + fadeOut(tween(120)))
                }
            ) { thinking ->
                Box(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            color = when {
                                thinking -> MaterialTheme.colorScheme.error
                                input.isNotBlank() -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            }
                        )
                        .clickable(enabled = thinking || input.isNotBlank()) {
                            if (thinking) onStopClick() else onSendClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (thinking) Icons.Default.Stop
                        else Icons.AutoMirrored.Filled.Send,
                        contentDescription = if (thinking) "Stop" else "Send",
                        tint = if (input.isNotBlank() || thinking) Color.White
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
// ERROR BANNER
// ════════════════════════════════════════════════════════════

@Composable
private fun AiErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss error",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\ai\AiChatContract.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer.ai

import androidx.compose.runtime.Immutable
import com.edu.pdf.domain.model.ChatMessage

// 🌟 WORLD STANDARD: Bitmap KABHI State mein nahi rahega
// Sirf Page Number track karo, Bitmap ViewModel ke andar WeakReference se handle hoga

@Immutable
data class AiChatState(
    val messages: List<ChatMessage> = emptyList(),
    val currentInput: String = "",
    val isAiThinking: Boolean = false,
    val isVisible: Boolean = false,
    val currentPageNumber: Int = 1,    // 🌟 Page number, Bitmap nahi
    val pdfName: String = "",           // 🌟 Context ke liye
    val suggestedPrompts: List<String> = listOf(
        "📝 Summarize this page",
        "🔍 Explain key concepts",
        "❓ What are the main points?",
        "🌐 Translate to Hindi"
    ),
    val errorMessage: String? = null
)

sealed interface AiChatAction {
    data class UpdateInput(val text: String) : AiChatAction
    data class SendMessage(val query: String) : AiChatAction
    data class OnSmartPromptClick(val prompt: String) : AiChatAction
    data class SetContext(
        val pageNumber: Int,
        val pdfName: String,
        val bitmapRef: android.graphics.Bitmap? // 🌟 Direct pass, State mein store nahi
    ) : AiChatAction
    data class SetVisibility(val visible: Boolean) : AiChatAction
    data object StopStreaming : AiChatAction
    data object ClearChat : AiChatAction
    data object DismissError : AiChatAction
}

sealed interface AiChatEvent {
    data class ShowError(val message: String) : AiChatEvent
    data object ScrollToBottom : AiChatEvent
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\ai\AiChatViewModel.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer.ai

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.ChatMessage
import com.edu.pdf.domain.model.ChatRole
import com.edu.pdf.domain.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AiChatState())
    val state = _state.asStateFlow()

    private val _events = Channel<AiChatEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var streamingJob: Job? = null

    // 🌟 WORLD STANDARD: WeakReference — Bitmap GC ke liye available rahega
    // State mein nahi, isliye NO memory leak
    private var currentBitmapRef: WeakReference<Bitmap?> = WeakReference(null)

    fun onAction(action: AiChatAction) {
        when (action) {
            is AiChatAction.UpdateInput ->
                _state.update { it.copy(currentInput = action.text) }

            is AiChatAction.SetContext -> {
                // 🌟 Bitmap WeakReference mein store karo — State mein NAHI
                currentBitmapRef = WeakReference(action.bitmapRef)
                _state.update {
                    it.copy(
                        currentPageNumber = action.pageNumber,
                        pdfName = action.pdfName
                    )
                }
            }

            is AiChatAction.SetVisibility ->
                _state.update { it.copy(isVisible = action.visible) }

            is AiChatAction.ClearChat ->
                _state.update { it.copy(messages = emptyList(), errorMessage = null) }

            is AiChatAction.StopStreaming -> stopStreaming()

            is AiChatAction.SendMessage ->
                handleSendMessage(action.query, currentBitmapRef.get())

            is AiChatAction.OnSmartPromptClick ->
                handleSendMessage(action.prompt, currentBitmapRef.get())

            is AiChatAction.DismissError ->
                _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun handleSendMessage(query: String, bitmap: Bitmap?) {
        if (query.isBlank()) return

        // 🌟 Smart System Context: PDF name aur page number Gemini ko batao
        val contextualQuery = buildString {
            if (_state.value.pdfName.isNotBlank()) {
                append("[PDF: ${_state.value.pdfName}, Page ${_state.value.currentPageNumber}]\n")
            }
            append(query)
        }

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            role = ChatRole.USER,
            text = query  // UI mein sirf user ki query dikhao, context hidden
        )

        _state.update {
            it.copy(
                messages = it.messages + userMsg,
                currentInput = "",
                isAiThinking = true,
                errorMessage = null
            )
        }

        triggerScrollToBottom()

        val aiMsgId = UUID.randomUUID().toString()
        val aiPlaceholder = ChatMessage(
            id = aiMsgId,
            role = ChatRole.MODEL,
            text = "",
            isStreaming = true
        )
        _state.update { it.copy(messages = it.messages + aiPlaceholder) }

        streamingJob?.cancel()
        streamingJob = viewModelScope.launch {
            try {
                aiRepository.chatWithPdfStream(contextualQuery, bitmap).collect { chunk ->
                    if (chunk.isNotEmpty()) {
                        _state.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages.map { msg ->
                                    if (msg.id == aiMsgId) msg.copy(text = msg.text + chunk)
                                    else msg
                                }
                            )
                        }
                        triggerScrollToBottom()
                    }
                }
                finalizeAiMessage(aiMsgId)
            } catch (e: Exception) {
                finalizeAiMessage(aiMsgId)
                val errorText = when {
                    e.message?.contains("API key", ignoreCase = true) == true ->
                        "API Key invalid. Go to Settings → Add Gemini Key."
                    e.message?.contains("network", ignoreCase = true) == true ->
                        "No internet connection."
                    else -> "AI failed: ${e.localizedMessage}"
                }
                _state.update { it.copy(errorMessage = errorText) }
            }
        }
    }

    private fun stopStreaming() {
        streamingJob?.cancel()
        _state.value.messages.lastOrNull { it.isStreaming }?.let {
            finalizeAiMessage(it.id)
        }
    }

    private fun finalizeAiMessage(aiMsgId: String) {
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages.map { msg ->
                    if (msg.id == aiMsgId) msg.copy(isStreaming = false) else msg
                },
                isAiThinking = false
            )
        }
    }

    private fun triggerScrollToBottom() {
        viewModelScope.launch {
            _events.send(AiChatEvent.ScrollToBottom)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 🌟 ViewModel clear hone par WeakReference bhi clear
        currentBitmapRef.clear()
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\ocr\OcrContract.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer.ocr

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.edu.pdf.domain.ocr.OcrTextBlock

// 1. STATE: Jo UI (LiveTextOverlay) को दिखेगा
@Immutable
data class OcrState(
    val isLiveTextActive: Boolean = false,
    val isProcessing: Boolean = false,
    // 🌟 ELITE FIX: Bitmap को यहाँ से हटा दिया गया है ताकि Memory Leak ना हो!
    val extractedTextBlocks: List<OcrTextBlock> = emptyList(),
    val selectedText: String = "", // जो टेक्स्ट यूजर सेलेक्ट करेगा वो यहाँ सेव होगा
    val error: String? = null
)

// 2. ACTION: जो यूजर UI से भेजेगा
sealed interface OcrAction {
    data class StartLiveText(val bitmap: Bitmap) : OcrAction
    data class TextSelected(val text: String) : OcrAction // 🌟 NAYA: Gemini के लिए टेक्स्ट सेलेक्ट करने का एक्शन
    data object StopLiveText : OcrAction
}

// 3. EVENT: ViewModel से UI को वन-टाइम मैसेज (Toast) भेजने के लिए
sealed interface OcrEvent {
    data class ShowToast(val message: String) : OcrEvent
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\ocr\OcrSelectionOverlay.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer.ocr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun OcrSelectionOverlay(
    state: OcrState,
    onAction: (OcrAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!state.isLiveTextActive) return

    val context = LocalContext.current

    // UI Gesture States (Kept in Compose to prevent ViewModel bottlenecking at 60fps)
    var selectedIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragCurrent by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(if (state.isProcessing) Modifier.blur(8.dp) else Modifier)
            .background(if (state.isProcessing) Color(0x33000000) else Color.Transparent)
    ) {
        // 1. Sleek Red Progress Bar
        AnimatedVisibility(
            visible = state.isProcessing,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(500)),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                color = Color.Red,
                trackColor = Color.Transparent
            )
        }

        // 2. Custom Canvas Selection Engine
        if (!state.isProcessing && state.extractedTextBlocks.isNotEmpty()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { tapOffset ->
                                val tappedIndex = state.extractedTextBlocks.indexOfFirst { block ->
                                    block.boundingBox?.let { rect ->
                                        tapOffset.x >= rect.left && tapOffset.x <= rect.right &&
                                                tapOffset.y >= rect.top && tapOffset.y <= rect.bottom
                                    } ?: false
                                }

                                // 🌟 FIX: Assignment Lifted out of If (Clean Kotlin Style)
                                selectedIndices = if (tappedIndex != -1) {
                                    if (selectedIndices.contains(tappedIndex)) {
                                        selectedIndices - tappedIndex
                                    } else {
                                        selectedIndices + tappedIndex
                                    }
                                } else {
                                    emptySet()
                                }
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                dragStart = offset
                                dragCurrent = offset
                                selectedIndices = emptySet()
                            },
                            onDrag = { change, _ ->
                                dragCurrent = change.position
                                val start = dragStart ?: return@detectDragGestures
                                val current = dragCurrent ?: return@detectDragGestures

                                val dragRect = Rect(
                                    left = min(start.x, current.x),
                                    top = min(start.y, current.y),
                                    right = max(start.x, current.x),
                                    bottom = max(start.y, current.y)
                                )

                                val newSelected = mutableSetOf<Int>()
                                state.extractedTextBlocks.forEachIndexed { index, block ->
                                    block.boundingBox?.let { rect ->
                                        val blockRect = Rect(
                                            left = rect.left.toFloat(),
                                            top = rect.top.toFloat(),
                                            right = rect.right.toFloat(),
                                            bottom = rect.bottom.toFloat()
                                        )
                                        if (dragRect.overlaps(blockRect)) {
                                            newSelected.add(index)
                                        }
                                    }
                                }
                                selectedIndices = newSelected
                            },
                            onDragEnd = {
                                dragStart = null
                                dragCurrent = null
                            },
                            onDragCancel = {
                                dragStart = null
                                dragCurrent = null
                            }
                        )
                    }
            ) {
                // Drawing Text Boxes
                state.extractedTextBlocks.forEachIndexed { index, block ->
                    block.boundingBox?.let { rect ->
                        val isSelected = selectedIndices.contains(index)
                        val boxColor = if (isSelected) Color(0x66007AFF) else Color(0x22007AFF)

                        drawRect(
                            color = boxColor,
                            topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
                            size = Size(rect.width().toFloat(), rect.height().toFloat())
                        )
                    }
                }

                // Drawing Drag Selection Square
                dragStart?.let { start ->
                    dragCurrent?.let { current ->
                        val left = min(start.x, current.x)
                        val top = min(start.y, current.y)
                        val width = abs(current.x - start.x)
                        val height = abs(current.y - start.y)

                        drawRect(
                            color = Color(0x44FFFFFF),
                            topLeft = Offset(left, top),
                            size = Size(width, height),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }
        }

        // 3. Floating Action Menu (Copy & Gemini)
        AnimatedVisibility(
            visible = selectedIndices.isNotEmpty(),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xE61C1C1E), shape = MaterialTheme.shapes.extraLarge)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // COPY BUTTON
                TextButton(
                    onClick = {
                        val textToCopy = state.extractedTextBlocks
                            .filterIndexed { index, _ -> selectedIndices.contains(index) }
                            .joinToString(separator = "\n") { it.text }

                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Copied Text", textToCopy)
                        clipboard.setPrimaryClip(clip)

                        Toast.makeText(context, "Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                        selectedIndices = emptySet()
                    }
                ) {
                    Text("Copy", color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(width = 1.dp, height = 24.dp).background(Color.Gray))
                Spacer(modifier = Modifier.width(4.dp))

                // ✨ ASK GEMINI BUTTON
                TextButton(
                    onClick = {
                        val textToAsk = state.extractedTextBlocks
                            .filterIndexed { index, _ -> selectedIndices.contains(index) }
                            .joinToString(separator = "\n") { it.text }

                        // 🌟 FIX: Unused Variable Warning (अब हम textToAsk का इस्तेमाल टोस्ट में कर रहे हैं)
                        Toast.makeText(context, "Asking Gemini: ${textToAsk.take(15)}...", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("✨ Ask Gemini", color = Color(0xFF34C759), fontWeight = FontWeight.Bold)
                }
            }
        }

        // 4. Close Button
        AnimatedVisibility(
            visible = !state.isProcessing && state.extractedTextBlocks.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = { onAction(OcrAction.StopLiveText) },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.8f), shape = MaterialTheme.shapes.small)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close Scanner", tint = Color.Black)
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\pdfviewer\ocr\OcrViewModel.kt
``kotlin
package com.edu.pdf.presentation.pdfviewer.ocr

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.ocr.TextRecognitionEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val textRecognitionEngine: TextRecognitionEngine
) : ViewModel() {

    private val _state = MutableStateFlow(OcrState())
    val state = _state.asStateFlow()

    // 🌟 ELITE FIX: UI को Toast मैसेज भेजने के लिए Channel का इस्तेमाल
    private val _events = Channel<OcrEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: OcrAction) {
        when (action) {
            is OcrAction.StartLiveText -> {
                // UI ओपन करो और लोडिंग दिखाओ
                _state.update {
                    it.copy(
                        isLiveTextActive = true,
                        isProcessing = true,
                        error = null,
                        selectedText = "" // पुराना सिलेक्शन हटा दो
                    )
                }
                extractText(action.bitmap)
            }

            is OcrAction.TextSelected -> {
                // जब यूजर कोई टेक्स्ट कॉपी/सेलेक्ट करे
                _state.update { it.copy(selectedText = action.text) }
            }

            is OcrAction.StopLiveText -> {
                // ओवरले बंद कर दो और सब रीसेट कर दो
                _state.update { OcrState() }
            }
        }
    }

    private fun extractText(bitmap: Bitmap) {
        viewModelScope.launch {
            val result = textRecognitionEngine.extractTextFromBitmap(bitmap)

            result.onSuccess { blocks ->
                _state.update { it.copy(extractedTextBlocks = blocks, isProcessing = false) }
                // 🌟 ELITE MEMORY FIX: काम खत्म होने के बाद Bitmap को मेमोरी से उड़ा दो!
                bitmap.recycle()
            }.onFailure { exception ->
                _state.update { it.copy(error = exception.message, isProcessing = false) }
                // UI को एरर का टोस्ट (Toast) दिखाओ
                _events.send(OcrEvent.ShowToast(exception.message ?: "Scanner Failed"))
                bitmap.recycle()
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\search\SearchScreen.kt
``kotlin
package com.edu.pdf.presentation.search

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.edu.pdf.R
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.presentation.home.components.PdfActionBottomSheet
import com.edu.pdf.presentation.home.components.PdfThumbnail
import com.edu.pdf.presentation.search.components.HighlightedText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onPdfClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query = uiState.query
    val results = uiState.results
    val history = uiState.history

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var selectedPdfForMenu by remember { mutableStateOf<PdfFile?>(null) }

    // 🌟 NAYA LOCAL STATE: Search screen me rename dialog open karne ke liye
    var renameDialogPdf by remember { mutableStateOf<PdfFile?>(null) }

    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // 🌟 MVI FIX: ViewModel के Events को सुनना
    LaunchedEffect(viewModel.events, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is SearchEvent.ShowSnackbar -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        androidx.compose.runtime.withFrameNanos { }
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding() // 🌟 ELITE FIX: Keyboard aate hi list smoothly upar shift ho jayegi
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    TextField(
                        value = query, // 🌟 MVI FIX: सीधा ViewModel का स्टेट इस्तेमाल कर रहे हैं
                        onValueChange = { newValue ->
                            viewModel.onAction(SearchAction.OnQueryChange(newValue)) // 🌟 newValue अब सीधे String है
                        },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        placeholder = { Text("Search your PDFs...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            viewModel.onAction(SearchAction.SaveSearchQuery(query)) // 🌟 MVI FIX
                            keyboardController?.hide()
                        }),
                        trailingIcon = {
                            if (query.isNotEmpty()) { // 🌟 MVI FIX
                                IconButton(onClick = {
                                    viewModel.onAction(SearchAction.ClearSearch)
                                    focusRequester.requestFocus()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    )
                }
            }

            if (query.isBlank()) {
                ZeroStateView(
                    history = history,
                    onHistoryItemClick = { pastQuery -> viewModel.onAction(SearchAction.OnQueryChange(pastQuery)) },
                    onRemoveHistoryItem = { viewModel.onAction(SearchAction.RemoveHistoryItem(it)) },
                    onClearAll = { viewModel.onAction(SearchAction.ClearAllHistory) }
                )
            } else if (results.isEmpty()) {
                EmptyStateView(query = query)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(results, key = { it.id }) { pdf ->
                        SearchItemRow(
                            pdf = pdf,
                            query = query,
                            onClick = {
                                viewModel.onAction(SearchAction.SaveSearchQuery(query))
                                viewModel.onAction(SearchAction.MarkPdfAsOpened(pdf.id))
                                keyboardController?.hide()
                                onPdfClick(pdf.path)
                            },
                            onMoreClick = {
                                scope.launch {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    delay(200)
                                    selectedPdfForMenu = pdf
                                }
                            }
                        )
                    }
                }
            }
        }

        selectedPdfForMenu?.let { pdf ->
            PdfActionBottomSheet(
                pdf = pdf,
                onDismiss = { selectedPdfForMenu = null },
                onFavoriteToggle = {
                    viewModel.onAction(SearchAction.ToggleFavorite(pdf))
                    Toast.makeText(context, if (pdf.isFavorite) "Removed from Favorites" else "Added to Favorites", Toast.LENGTH_SHORT).show()
                    selectedPdfForMenu = null
                },
                onShare = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, pdf.id.toUri())
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
                    selectedPdfForMenu = null
                },
                onDelete = {
                    // 🌟 MVI FIX: सीधा डिलीट मत करो, पहले कन्फर्मेशन पॉप-अप खोलो
                    viewModel.onAction(SearchAction.ShowDeleteConfirmation(pdf))
                    selectedPdfForMenu = null
                },
                onActionClick = { actionTitle ->
                    when (actionTitle) {
                        // 🌟 EXACT FIX: Rename properly wire kar diya!
                        "Rename" -> {
                            renameDialogPdf = pdf
                            selectedPdfForMenu = null
                        }
                        else -> {
                            Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
                            selectedPdfForMenu = null
                        }
                    }
                }
            )
        }

        // Smart Rename Dialog for Search Screen
        val pdfToRename = renameDialogPdf
        if (pdfToRename != null) {
            val focusRequesterDialog = remember { FocusRequester() }
            val baseName = pdfToRename.name.removeSuffix(".pdf").removeSuffix(".PDF")

            // Flag to ensure focus is requested only once
            var hasRequestedFocus by remember { mutableStateOf(false) }

            var renameTextFieldValue by remember {
                mutableStateOf(
                    TextFieldValue(
                        text = baseName,
                        selection = TextRange(0, baseName.length)
                    )
                )
            }

            AlertDialog(
                onDismissRequest = { renameDialogPdf = null },
                title = { Text("Rename File", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = renameTextFieldValue,
                        onValueChange = { renameTextFieldValue = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesterDialog)
                            .onGloballyPositioned {
                                if (!hasRequestedFocus) {
                                    focusRequesterDialog.requestFocus()
                                    keyboardController?.show()
                                    hasRequestedFocus = true
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            if (renameTextFieldValue.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    renameTextFieldValue = TextFieldValue("", TextRange.Zero)
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                                }
                            }
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            if (renameTextFieldValue.text.isNotBlank()) {
                                // 🌟 MVI FIX: सिर्फ Action भेजो और डायलॉग बंद कर दो
                                viewModel.onAction(SearchAction.RenamePdf(pdfToRename, renameTextFieldValue.text))
                                renameDialogPdf = null
                            }
                        }
                    ) { Text("Rename") }
                },
                dismissButton = {
                    TextButton(onClick = { renameDialogPdf = null }) { Text("Cancel") }
                }
            )

        }
        // 🌟 MVI FIX: Delete Confirmation Dialog
        val pdfToDelete = uiState.pdfToDelete
        if (pdfToDelete != null) {
            AlertDialog(
                onDismissRequest = { viewModel.onAction(SearchAction.DismissDeleteConfirmation) },
                title = { Text("Delete File?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = {
                    Text(
                        "Are you sure you want to permanently delete '${pdfToDelete.name}'? This action cannot be undone.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.onAction(SearchAction.ConfirmDeletePdf) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Delete", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onError) }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onAction(SearchAction.DismissDeleteConfirmation) }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun ZeroStateView(
    history: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onRemoveHistoryItem: (String) -> Unit,
    onClearAll: () -> Unit
) {
    var isHistoryExpanded by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 8.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // 1. Premium Search Tips
        item {
            SmartSearchTipsCard()
        }

        // 2. History Header
        if (history.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Searches", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        text = "Clear All",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onClearAll() }
                            .padding(4.dp)
                    )
                }
            }

            // 3. Expandable History List
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow))
                ) {
                    val visibleHistory = if (isHistoryExpanded) history else history.take(3)

                    visibleHistory.forEach { pastQuery ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onHistoryItemClick(pastQuery) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(pastQuery, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                            IconButton(onClick = { onRemoveHistoryItem(pastQuery) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    // 4. Show All Toggle Button
                    if (history.size > 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isHistoryExpanded = !isHistoryExpanded }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isHistoryExpanded) "Show Less" else "Show All",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isHistoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 🌟 FIX: Premium UX Educational Element Rewrite
@Composable
private fun SmartSearchTipsCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Deep Search Enabled", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Typo Tolerance: Automatically adapts to minor spelling variations.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text("• Contextual Scan: Finds files instantly using partial or scattered keywords.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SearchItemRow(
    pdf: PdfFile,
    query: String,
    onClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            PdfThumbnail(pdf = pdf, modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.width(16.dp))
        HighlightedText(
            text = pdf.name,
            query = query,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onMoreClick, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun EmptyStateView(query: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_search))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                renderMode = com.airbnb.lottie.RenderMode.HARDWARE,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No results found for", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("\"$query\"", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Check for typos or try a different word.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\search\SearchViewModel.kt
``kotlin
package com.edu.pdf.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// 🌟 STRICT MVI: State Definition
data class SearchUiState(
    val query: String = "",
    val results: ImmutableList<PdfFile> = persistentListOf(),
    val history: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = false,
    val pdfToDelete: PdfFile? = null
)

// 🌟 STRICT MVI: Actions (Intents from UI)
sealed interface SearchAction {
    data class OnQueryChange(val query: String) : SearchAction
    data object ClearSearch : SearchAction
    data class SaveSearchQuery(val query: String) : SearchAction
    data class RemoveHistoryItem(val query: String) : SearchAction
    data object ClearAllHistory : SearchAction
    data class MarkPdfAsOpened(val pdfId: String) : SearchAction
    data class ToggleFavorite(val pdf: PdfFile) : SearchAction
    // 🌟 MVI FIX: Callbacks हटा दिए गए हैं
    data class RenamePdf(val pdf: PdfFile, val newName: String) : SearchAction
    data class ShowDeleteConfirmation(val pdf: PdfFile) : SearchAction
    data object DismissDeleteConfirmation : SearchAction
    data object ConfirmDeletePdf : SearchAction
}

// 🌟 MVI EVENT: ViewModel से UI को मैसेज (Toast) भेजने के लिए
sealed interface SearchEvent {
    data class ShowSnackbar(val message: String) : SearchEvent
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PdfRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _pdfToDelete = MutableStateFlow<PdfFile?>(null)
    private val _events = kotlinx.coroutines.channels.Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    // 🌟 INSTANT SEARCH: Debounce puri tarah hata diya
    private val searchResultsFlow = _searchQuery
        .map { it.trim() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(persistentListOf())
            } else {
                repository.searchPdfs(query).map { it.toImmutableList() }
            }
        }

    private val searchHistoryFlow = repository.getRecentSearchQueries()
        .map { it.toImmutableList() }

    // 🌟 SINGLE SOURCE OF TRUTH FOR UI
    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        searchResultsFlow,
        searchHistoryFlow,
        _pdfToDelete
    ) { query, results, history, pdfToDelete ->
        SearchUiState(
            query = query,
            results = results,
            history = history,
            isLoading = false,
            pdfToDelete = pdfToDelete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly, // 🌟 Changed to Eagerly for better test reliability
        initialValue = SearchUiState(isLoading = false) // 🌟 Set initial loading to false
    )

    // 🌟 THE REDUCER: UI bas Actions bhejega yahan
    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnQueryChange -> {
                _searchQuery.value = action.query
            }
            is SearchAction.ClearSearch -> {
                _searchQuery.value = ""
            }
            is SearchAction.SaveSearchQuery -> {
                if (action.query.isNotBlank()) {
                    viewModelScope.launch { repository.saveSearchQuery(action.query.trim()) }
                }
            }
            is SearchAction.RemoveHistoryItem -> {
                viewModelScope.launch { repository.deleteSearchQuery(action.query) }
            }
            is SearchAction.ClearAllHistory -> {
                viewModelScope.launch { repository.clearAllSearchHistory() }
            }
            is SearchAction.MarkPdfAsOpened -> {
                viewModelScope.launch { repository.updateLastOpenedTime(action.pdfId, System.currentTimeMillis()) }
            }
            is SearchAction.ToggleFavorite -> {
                viewModelScope.launch { repository.toggleFavorite(action.pdf.id, !action.pdf.isFavorite) }
            }
            is SearchAction.RenamePdf -> {
                // 🌟 MVI FIX: Action लेकर Event वापस भेजना
                viewModelScope.launch(Dispatchers.IO) {
                    val success = repository.renamePdf(action.pdf, action.newName)
                    withContext(Dispatchers.Main) {
                        val msg = if (success) "Renamed successfully" else "Rename failed"
                        _events.send(SearchEvent.ShowSnackbar(msg))
                    }
                }
            }
            is SearchAction.ShowDeleteConfirmation -> {
                _pdfToDelete.value = action.pdf
            }
            is SearchAction.DismissDeleteConfirmation -> {
                _pdfToDelete.value = null
            }
            is SearchAction.ConfirmDeletePdf -> {
                val targetPdf = _pdfToDelete.value
                if (targetPdf != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.deletePdfs(listOf(targetPdf))
                        withContext(Dispatchers.Main) {
                            _pdfToDelete.value = null // पॉप-अप बंद करो
                            _events.send(SearchEvent.ShowSnackbar("File deleted permanently"))
                        }
                    }
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\search\components\HighlightedText.kt
``kotlin
package com.edu.pdf.presentation.search.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun HighlightedText(
    text: String,
    query: String,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    // 🌟 remember logic prevents UI stuttering while typing rapidly
    val annotatedString = remember(text, query, primaryColor) {
        val cleanQuery = query.trim()
        if (cleanQuery.isBlank()) return@remember null

        // 1. Break query into independent tokens
        val tokens = cleanQuery.split(Regex("\\s+")).filter { it.isNotEmpty() }

        // 2. O(1) Memory BooleanArray to map which exact letters should be highlighted
        val matchMap = BooleanArray(text.length)

        // 3. Multi-Token Infix Scanner
        for (token in tokens) {
            var startIndex = 0
            while (startIndex < text.length) {
                val foundIndex = text.indexOf(token, startIndex, ignoreCase = true)
                if (foundIndex == -1) break

                // Mark this specific token's letters as 'true' (highlighted)
                for (i in foundIndex until (foundIndex + token.length)) {
                    matchMap[i] = true
                }
                // Jump ahead to avoid overlapping self-matches
                startIndex = foundIndex + token.length
            }
        }

        // Check if ANY highlights were made
        var hasHighlights = false
        for (isHighlighted in matchMap) {
            if (isHighlighted) {
                hasHighlights = true
                break
            }
        }

        if (!hasHighlights) return@remember null

        // 4. Render the string seamlessly
        buildAnnotatedString {
            var i = 0
            while (i < text.length) {
                if (matchMap[i]) {
                    val start = i
                    while (i < text.length && matchMap[i]) { i++ }
                    withStyle(
                        style = SpanStyle(
                            color = primaryColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    ) {
                        append(text.substring(start, i))
                    }
                } else {
                    val start = i
                    while (i < text.length && !matchMap[i]) { i++ }
                    append(text.substring(start, i))
                }
            }
        }
    }

    Text(
        text = annotatedString ?: buildAnnotatedString { append(text) },
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = onBackgroundColor,
        modifier = modifier
    )
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\settings\SettingsScreen.kt
``kotlin
package com.edu.pdf.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.edu.pdf.presentation.common.UniversalTopBar
import com.edu.pdf.presentation.common.PremiumBottomBar
@OptIn(ExperimentalMaterial3Api::class) // 🌟 BAS YE LINE ADD KARNI HAI
@Composable
fun SettingsScreen(
    isTablet: Boolean,
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.validationMessage) {
        state.validationMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onAction(SettingsAction.ClearMessage)
        }
    }

    Scaffold(
        topBar = { UniversalTopBar(title = "Settings") },
        bottomBar = { if (!isTablet) PremiumBottomBar(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // AI Copilot Section Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("AI Copilot Engine", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Text("Setup your Gemini API Keys for smart PDF features. Keys are encrypted securely.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

            OutlinedTextField(
                value = state.primaryKey,
                onValueChange = { viewModel.onAction(SettingsAction.UpdatePrimaryKey(it)) },
                label = { Text("Primary Gemini API Key") },
                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.fallbackKey1,
                onValueChange = { viewModel.onAction(SettingsAction.UpdateFallback1(it)) },
                label = { Text("Fallback API Key 1 (Optional)") },
                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.fallbackKey2,
                onValueChange = { viewModel.onAction(SettingsAction.UpdateFallback2(it)) },
                label = { Text("Fallback API Key 2 (Optional)") },
                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onAction(SettingsAction.SaveAndVerifyKeys) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !state.isVerifying,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (state.isVerifying) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Verifying...")
                } else {
                    Text("Verify & Save Keys", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\presentation\settings\SettingsViewModel.kt
``kotlin
package com.edu.pdf.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.data.preferences.AiKeyManager
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SettingsUiState(
    val primaryKey: String = "",
    val fallbackKey1: String = "",
    val fallbackKey2: String = "",
    val isVerifying: Boolean = false,
    val validationMessage: String? = null,
    val isSuccess: Boolean = false
)

sealed interface SettingsAction {
    data class UpdatePrimaryKey(val key: String) : SettingsAction
    data class UpdateFallback1(val key: String) : SettingsAction
    data class UpdateFallback2(val key: String) : SettingsAction
    data object SaveAndVerifyKeys : SettingsAction
    data object ClearMessage : SettingsAction
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val keyManager: AiKeyManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    init {
        val savedKeys = keyManager.getKeys()
        _state.update {
            it.copy(
                primaryKey = savedKeys.getOrNull(0) ?: "",
                fallbackKey1 = savedKeys.getOrNull(1) ?: "",
                fallbackKey2 = savedKeys.getOrNull(2) ?: ""
            )
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.UpdatePrimaryKey -> _state.update { it.copy(primaryKey = action.key) }
            is SettingsAction.UpdateFallback1 -> _state.update { it.copy(fallbackKey1 = action.key) }
            is SettingsAction.UpdateFallback2 -> _state.update { it.copy(fallbackKey2 = action.key) }
            is SettingsAction.ClearMessage -> _state.update { it.copy(validationMessage = null) }
            is SettingsAction.SaveAndVerifyKeys -> verifyAndSave()
        }
    }

    private fun verifyAndSave() {
        val currentState = _state.value
        // 🌟 FIX 1: Hamesha trim() use karo taaki copy-paste ke hidden spaces hat jayein
        val primaryKeyTrimmed = currentState.primaryKey.trim()

        if (primaryKeyTrimmed.isBlank()) {
            _state.update { it.copy(validationMessage = "Primary Key is required!") }
            return
        }

        _state.update { it.copy(isVerifying = true, validationMessage = null) }

        viewModelScope.launch {
            // Hum boolean (true/false) ke bajaye String result lenge taaki error padh sakein
            val resultMessage = withContext(Dispatchers.IO) {
                try {
                    val model = GenerativeModel(
                        modelName = "gemini-2.5-flash",
                        apiKey = primaryKeyTrimmed
                    )
                    // "ping" ki jagah "Hello" bhejo, kabhi-kabhi too-short prompts block ho jate hain
                    model.generateContent("Hello")
                    "SUCCESS"
                } catch (e: Exception) {
                    // 🌟 FIX 2: Asli exception ka message pakdo taaki UI par dikha sakein
                    e.localizedMessage ?: "Unknown Network Error"
                }
            }

            if (resultMessage == "SUCCESS") {
                keyManager.saveKeys(
                    primaryKeyTrimmed,
                    currentState.fallbackKey1.trim(),
                    currentState.fallbackKey2.trim()
                )
                _state.update {
                    it.copy(
                        isVerifying = false,
                        isSuccess = true,
                        validationMessage = "AI Keys Verified & Secured! ✅"
                    )
                }
            } else {
                // Yahan tumhe sachai pata chalegi ki aakhir API key kyu nahi le raha tha!
                _state.update {
                    it.copy(
                        isVerifying = false,
                        isSuccess = false,
                        validationMessage = "Failed: $resultMessage ❌"
                    )
                }
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\receiver\FileDetectionReceiver.kt
``kotlin
package com.edu.pdf.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.edu.pdf.worker.PdfDetectionWorker

class FileDetectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d("FileDetectionReceiver", "Received action: $action")
        
        if (action == Intent.ACTION_MEDIA_SCANNER_SCAN_FILE || 
            action == Intent.ACTION_MEDIA_SCANNER_FINISHED) {
            context?.let {
                Log.d("FileDetectionReceiver", "Triggering PDF Scan from Receiver")
                PdfDetectionWorker.enqueueNow(it)
            }
        }
    }
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\ui\theme\Color.kt
``kotlin
package com.edu.pdf.ui.theme

import androidx.compose.ui.graphics.Color

// 🌟 THE MASTER PALETTE (Single Source of Truth)

val BrandPrimary = Color(0xFFE53935) // Premium vibrant red
val SolidError = Color(0xFFD32F2F)   // Solid Danger/Delete Red
val FolderColor = Color(0xFFFFB300)  // 🌟 PRO FIX: Yahan raw color wapas aa gaya

// 🌟 Light Theme Colors
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF5F5F5)

// 🌟 Dark Theme Colors (Flat Seamless Design)
val DarkBackground = Color(0xFF171923)
val DarkSurface = Color(0xFF171923)
val DarkSurfaceVariant = Color(0xFF2A2D3D)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\ui\theme\Theme.kt
``kotlin
@file:Suppress("DEPRECATION")

package com.edu.pdf.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    error = SolidError,           // 🌟 GOD MODE: Default faded red ko hata kar Solid Red laga diya
    errorContainer = SolidError.copy(alpha = 0.1f), // Delete dialogs ke background ke liye
    tertiary = FolderColor,       // 🌟 FOLDERS: App ke saare folders ab ye color lenge
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onPrimary = Color.White,
    onError = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    error = SolidError,           // 🌟 GOD MODE: Dark mode mein bhi ekdum SOLID Red aayega!
    errorContainer = SolidError.copy(alpha = 0.2f),
    tertiary = FolderColor,       // 🌟 FOLDERS: Yahan bhi folders same rahenge
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = Color.White,
    onError = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun PdfTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 🌟 GOD MODE: Dynamic colors completely disabled to enforce strict Brand Identity
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 🌟 2026 PRO FIX: Status bar ko 100% invisible (transparent) kar diya.
            // Ab app seedha notch/camera cutout tak smoothly pahailegi bina kisi 'Patti' ke.
            window.statusBarColor = android.graphics.Color.TRANSPARENT

            // Edge-to-Edge transparent Navigation bar
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            window.isNavigationBarContrastEnforced = false

            // Adjust icon colors (black icons in light mode, white icons in dark mode)
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    // 🌟 THE ELITE FIX: Clean Native Material 3 Theme (Smooth without messy custom ripples)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\ui\theme\Type.kt
``kotlin
package com.edu.pdf.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
``n
### FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\src\main\java\com\edu\pdf\worker\PdfDetectionWorker.kt
``kotlin
package com.edu.pdf.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.notification.PdfNotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.edu.pdf.domain.model.SortType
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class PdfDetectionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PdfRepository,
    private val notificationHelper: PdfNotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("PdfDetectionWorker", "Worker started! Something changed in storage.")
        return try {
            // 1. Scan the device for new PDFs
            repository.scanAndSavePdfs()

            // 2. Find very recent PDFs (Ordered by Date Modified)
            // 🌟 CRITICAL FIX: 'getRecentPdfs' sirf opened files dikhata hai.
            // Hum 'getAllPdfs' use karenge taaki nayi aayi files (Jo khuli nahi hain) wo bhi milein.
            val latestPdfs = repository.getAllPdfs(SortType.DATE_DESC).first().take(10)
            val nowSeconds = System.currentTimeMillis() / 1000
            
            Log.d("PdfDetectionWorker", "Scanning top 10 newest PDFs in system")

            latestPdfs.forEach { pdf ->
                // 🌟 RELAXED CHECK: 5 minute window (300 seconds)
                val ageSeconds = nowSeconds - pdf.lastModified
                Log.d("PdfDetectionWorker", "Analyzing: ${pdf.name}, Age: ${ageSeconds}s")
                
                if (ageSeconds >= 0 && ageSeconds < 300) { 
                     Log.d("PdfDetectionWorker", "NOTIFYING: ${pdf.name}")
                     notificationHelper.showNewPdfNotification(pdf.name, pdf.path)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("PdfDetectionWorker", "Worker Error: ${e.message}")
            Result.retry()
        }
    }

    companion object {
        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .addContentUriTrigger(android.provider.MediaStore.Files.getContentUri("external"), true)
                .build()

            val request = OneTimeWorkRequestBuilder<PdfDetectionWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "PdfDetectionWork",
                ExistingWorkPolicy.KEEP, // 🌟 Changed to KEEP to allow parallel triggers
                request
            )
        }

        fun enqueueNow(context: Context) {
            val request = OneTimeWorkRequestBuilder<PdfDetectionWorker>()
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "PdfDetectionWorkNow", // Different ID to avoid collision
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }
}
``n
## 4. BUILD CONFIGURATIONS
### CONFIG FILE: C:\Users\saud\AndroidStudioProjects\pdf\app\build.gradle.kts
``
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.edu.pdf"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.edu.pdf"
        minSdk = 33
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk { abiFilters.add("arm64-v8a") }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
            // Niche wali lines bhi add kar do taki aage chal kar aur koi conflict na aaye
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/license.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "/META-INF/notice.txt"
            excludes += "/META-INF/ASL2.0"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Nayi PDF aur Fragment dependencies
    implementation(libs.androidx.pdf.viewer.fragment)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.material.icons.extended)
    // Nayi Dependencies yahan paste karein:
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.navigation.compose)
    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.fragment.compose)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.biometric)
    // 🌟 2026 MILITARY GRADE VAULT SECURITY (Google Tink)
    implementation(libs.tink.android)
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.room.paging)
    implementation(libs.androidx.windowsizeclass)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.adaptive.layout)
    implementation(libs.androidx.compose.adaptive.navigation)
    implementation(libs.mlkit.text.recognition.gms)
    implementation(libs.mlkit.text.recognition.devanagari.gms)
    implementation(libs.google.ai.client)
    implementation(libs.google.mlkit.genai)
    implementation(libs.androidx.security.crypto)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
``n
### CONFIG FILE: C:\Users\saud\AndroidStudioProjects\pdf\gradle\libs.versions.toml
``
[versions]
agp = "9.2.0"
coreKtx = "1.18.0"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.10.0"
activityCompose = "1.13.0"
kotlin = "2.3.20"
composeBom = "2026.04.01"
#fragment pdf viewer
pdfVersion = "1.0.0-alpha18"
fragmentKtx = "1.8.9"
appcompat = "1.7.1"
material = "1.13.0"
materialIconsExtended = "1.7.8"
# Inhe [versions] block ke niche add karein
hilt = "2.59.2"
hiltNavigationCompose = "1.3.0"
room = "2.8.4"
ksp = "2.3.6" # Kotlin 2.3.20 ke saath strictly match kiya gaya hai
coroutines = "1.11.0"
datastore = "1.2.1"
navigationCompose = "2.9.8"
coil = "3.4.0"
kotlinxSerialization = "1.11.0"
coreSplashscreen = "1.2.0"
kotlinxImmutable = "0.4.0"
lottieCompose = "6.7.1"
work = "2.11.2"
hiltWork = "1.3.0"
biometric = "1.2.0-alpha05"
tink = "1.21.0"
paging = "3.5.0"
adaptive = "1.2.0"
firebaseCrashlytics = "20.0.6"
googleGmsGoogleServices = "4.4.4"
googleFirebaseCrashlytics = "3.0.7"
mlkit-text-recognition-gms = "19.0.1"
mlkit-devanagari-gms = "16.0.1"
generativeAi = "0.9.0"
mlkitGenAi = "1.0.0-beta2"
securityCrypto = "1.1.0"
mockk = "1.13.10"
coroutinesTest = "1.8.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
#fragment pdf viewer
androidx-pdf-viewer-fragment = { group = "androidx.pdf", name = "pdf-viewer-fragment", version.ref = "pdfVersion" }
androidx-fragment-ktx = { group = "androidx.fragment", name = "fragment-ktx", version.ref = "fragmentKtx" }
androidx-fragment-compose = { group = "androidx.fragment", name = "fragment-compose", version.ref = "fragmentKtx" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
google-material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended", version.ref = "materialIconsExtended" }
# Inhe [libraries] block ke sabse niche add karein
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
androidx-hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }

room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
# [libraries] block mein:
coil-compose = { module = "io.coil-kt.coil3:coil-compose", version.ref = "coil" }
# [libraries] block ke end mein ye add karo:
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
androidx-core-splashscreen = { group = "androidx.core", name = "core-splashscreen", version.ref = "coreSplashscreen" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }
kotlinx-collections-immutable = { group = "org.jetbrains.kotlinx", name = "kotlinx-collections-immutable", version.ref = "kotlinxImmutable" }
lottie-compose = { group = "com.airbnb.android", name = "lottie-compose", version.ref = "lottieCompose" }
work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }
hilt-work = { group = "androidx.hilt", name = "hilt-work", version.ref = "hiltWork" }
hilt-work-compiler = { group = "androidx.hilt", name = "hilt-compiler", version.ref = "hiltWork" }
#biometric
androidx-biometric = { group = "androidx.biometric", name = "biometric", version.ref = "biometric" }
tink-android = { group = "com.google.crypto.tink", name = "tink-android", version.ref = "tink" }
androidx-paging-runtime = { group = "androidx.paging", name = "paging-runtime", version.ref = "paging" }
androidx-paging-compose = { group = "androidx.paging", name = "paging-compose", version.ref = "paging" }
room-paging = { group = "androidx.room", name = "room-paging", version.ref = "room" }
androidx-windowsizeclass = { group = "androidx.compose.material3", name = "material3-window-size-class" }
androidx-compose-adaptive = { group = "androidx.compose.material3.adaptive", name = "adaptive", version.ref = "adaptive" }
androidx-compose-adaptive-layout = { group = "androidx.compose.material3.adaptive", name = "adaptive-layout", version.ref = "adaptive" }
androidx-compose-adaptive-navigation = { group = "androidx.compose.material3.adaptive", name = "adaptive-navigation", version.ref = "adaptive" }
firebase-crashlytics = { group = "com.google.firebase", name = "firebase-crashlytics", version.ref = "firebaseCrashlytics" }
mlkit-text-recognition-gms = { group = "com.google.android.gms", name = "play-services-mlkit-text-recognition", version.ref = "mlkit-text-recognition-gms" }
mlkit-text-recognition-devanagari-gms = { group = "com.google.android.gms", name = "play-services-mlkit-text-recognition-devanagari", version.ref = "mlkit-devanagari-gms" }
google-ai-client = { group = "com.google.ai.client.generativeai", name = "generativeai", version.ref = "generativeAi" }
google-mlkit-genai = { group = "com.google.mlkit", name = "genai-prompt", version.ref = "mlkitGenAi" }
androidx-security-crypto = { group = "androidx.security", name = "security-crypto", version.ref = "securityCrypto" }
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutinesTest" }
[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
google-gms-google-services = { id = "com.google.gms.google-services", version.ref = "googleGmsGoogleServices" }
google-firebase-crashlytics = { id = "com.google.firebase.crashlytics", version.ref = "googleFirebaseCrashlytics" }
``n
### CONFIG FILE: C:\Users\saud\AndroidStudioProjects\pdf\build.gradle.kts
``
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
}
``n
### CONFIG FILE: C:\Users\saud\AndroidStudioProjects\pdf\settings.gradle.kts
``
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "pdf"
include(":app")
``n
