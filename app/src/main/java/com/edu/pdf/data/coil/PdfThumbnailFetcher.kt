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

        // 🌟 THE ELITE FIX: UUID का इस्तेमाल करें ताकि दुनिया की कोई भी दो PDF का नाम कभी सेम न हो सके!
        val uniqueId = java.util.UUID.nameUUIDFromBytes(pdf.id.toByteArray()).toString()
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