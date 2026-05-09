package com.edu.pdf.data.coil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import coil3.ImageLoader
import coil3.asImage
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
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

// 🌟 ELITE GUARD: Max 3 rendering threads to strictly avoid Native OOMs!
private val renderDispatcher: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(3)

class PdfThumbnailFetcher(
    private val context: Context,
    private val pdf: PdfFile,
    private val options: Options
) : Fetcher {

    override suspend fun fetch(): FetchResult? = withContext(renderDispatcher) {
        // 🌟 MILITARY GUARD: Never cache thumbnails for Vault files
        if (pdf.isVault) return@withContext null

        val file = File(pdf.path)
        if (!file.exists() || file.length() == 0L) return@withContext null

        val cacheFolder = File(context.cacheDir, "smart_pdf_thumbnails")
        if (!cacheFolder.exists()) cacheFolder.mkdirs()

        val thumbFileName = "${pdf.id.hashCode()}.webp"
        val cachedThumbFile = File(cacheFolder, thumbFileName)

        // ==========================================
        // 🌟 THE ELITE FIX: Let Coil handle the Decoding!
        // ==========================================
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

        // ==========================================
        // 🌟 NATIVE PDF RENDERER (Lazy Persistent)
        // ==========================================

        // Cancel immediately if the user scrolled past this item quickly
        ensureActive()

        // Safely calculate bounds
        val reqWidth = options.size.width
        val targetWidth = if (reqWidth is Dimension.Pixels && reqWidth.px > 0) reqWidth.px else 300
        val boundedWidth = targetWidth.coerceIn(150, 500)

        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null

        try {
            pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(pfd)

            if (renderer.pageCount <= 0) return@withContext null

            renderer.openPage(0).use { page ->

                // Double check cancellation before heavy native C++ processing
                ensureActive()

                // Maintain exact aspect ratio to prevent distorted thumbnails
                val aspectRatio = page.height.toFloat() / page.width.toFloat()
                val height = (boundedWidth * aspectRatio).toInt()

                val bitmap = createBitmap(boundedWidth, height, Bitmap.Config.ARGB_8888)
                bitmap.eraseColor(Color.WHITE) // UX: Fixes black backgrounds on transparent PDFs

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Save to our persistent cache
                FileOutputStream(cachedThumbFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 85, out)
                }

                // Return immediately. 'DataSource.DISK' prevents Coil from duplicating the cache file.
                return@withContext ImageFetchResult(
                    image = bitmap.asImage(),
                    isSampled = false,
                    dataSource = DataSource.DISK
                )
            }
        } catch (_: SecurityException) {
            // 🌟 NAYA LOGIC: Agar SecurityException aayi, matlab 100% PDF me password laga hai!
            if (cachedThumbFile.exists()) cachedThumbFile.delete()
            // Hum ek special exception throw karenge taaki UI ko pata chale ye "Locked" hai
            throw SecurityException("PDF_IS_LOCKED")
        } catch (e: Exception) {
            e.printStackTrace()
            if (cachedThumbFile.exists()) cachedThumbFile.delete()
            throw e // Normal error (Corrupt file etc.)
        } finally {
            renderer?.close()
            pfd?.close()
        }
    }

    class Factory(private val context: Context) : Fetcher.Factory<PdfFile> {
        override fun create(data: PdfFile, options: Options, imageLoader: ImageLoader): Fetcher {
            return PdfThumbnailFetcher(context, data, options)
        }
    }
}