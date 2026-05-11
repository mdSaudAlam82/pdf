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
import java.util.concurrent.ConcurrentLinkedQueue

// 🌟 NAYA: Ye Pool hamari memory ko bachayega
object PdfBitmapPool {
    private val pool = ConcurrentLinkedQueue<Bitmap>()

    fun getBitmap(width: Int, height: Int): Bitmap {
        // Agar purani khali memory hai (aur size match karta hai) to use saaf karke use karo
        val existing = pool.find { it.width == width && it.height == height }
        if (existing != null) {
            pool.remove(existing)
            existing.eraseColor(Color.WHITE)
            return existing
        }
        // Nahi toh nayi banao
        val newBitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        newBitmap.eraseColor(Color.WHITE)
        return newBitmap
    }

    fun putBitmap(bitmap: Bitmap) {
        // Zyada se zyada 20 Bitmap save rakho, baki aane par purane walo ko delete kar do
        if (pool.size < 20) {
            pool.offer(bitmap)
        } else {
            bitmap.recycle()
        }
    }
}
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

        val thumbFileName = "${pdf.id.hashCode()}.webp"
        val cachedThumbFile = File(cacheFolder, thumbFileName)

        // 🌟 TUMHARA ORIGINAL LOGIC: Agar .webp pehle se hai toh seedha return karo (Fastest!)
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

        // Safely calculate bounds
        val reqWidth = options.size.width
        val targetWidth = if (reqWidth is Dimension.Pixels && reqWidth.px > 0) reqWidth.px else 300
        val boundedWidth = targetWidth.coerceIn(150, 500)

        // 🌟 NAYA LOGIC: Variables ko bahar define kiya taaki finally block mein inko close kar sakein
        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        var page: PdfRenderer.Page? = null

        try {
            // Seedha file open karo, PDF render karo bina kisi cache ke
            pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(pfd)

            if (renderer.pageCount <= 0) return@withContext null

            page = renderer.openPage(0)
            ensureActive()

            // 🌟 FIX 1: Safety check. Agar corrupt PDF hai aur width/height 0 hai toh null return karo, crash mat karo.
            if (page.width <= 0 || page.height <= 0) {
                return@withContext null
            }

            val aspectRatio = page.height.toFloat() / page.width.toFloat()

            // 🌟 FIX 2: coerceAtLeast(1) taaki height kabhi zero (0) na bane.
            val height = (boundedWidth * aspectRatio).toInt().coerceAtLeast(1)

            // ✅ NAYA LOGIC: Pool se memory lo
            val bitmap = PdfBitmapPool.getBitmap(boundedWidth, height)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Image ko .webp me save kar do
            FileOutputStream(cachedThumbFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 85, out)
            }
            // 🌟 JADOO: Delete karne ke bajaye, memory wapas Pool me daal do!
            PdfBitmapPool.putBitmap(bitmap)

            return@withContext SourceFetchResult(
                source = ImageSource(
                    file = cachedThumbFile.toOkioPath(),
                    fileSystem = FileSystem.SYSTEM
                ),
                mimeType = "image/webp",
                dataSource = DataSource.DISK
            )
        } catch (_: SecurityException) {
            // 🌟 FIX 3: runCatching ka use taaki delete fail ho tab bhi app crash na ho
            runCatching { if (cachedThumbFile.exists()) cachedThumbFile.delete() }
            throw SecurityException("PDF_IS_LOCKED")
        } catch (e: Exception) {
            e.printStackTrace()
            // 🌟 FIX 3: runCatching ka use
            runCatching { if (cachedThumbFile.exists()) cachedThumbFile.delete() }
            throw e
        } finally {
            // SABSE ZAROORI FIX: Kaam hote hi turant saari native memory free kar do!
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

// 🌟 NOTE: Tumhara purana 'object PdfRendererCache' maine yahan se poori tarah delete kar diya hai kyunki ab uski zaroorat nahi hai.