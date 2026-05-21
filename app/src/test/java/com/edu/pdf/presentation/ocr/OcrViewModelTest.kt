package com.edu.pdf.presentation.ocr

import android.graphics.Bitmap
import com.edu.pdf.domain.ocr.OcrTextBlock
import com.edu.pdf.domain.ocr.TextRecognitionEngine
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OcrViewModelTest {

    private lateinit var textRecognitionEngine: TextRecognitionEngine
    private lateinit var viewModel: OcrViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Bitmap::class) // 🌟 NAYA: Bitmap mocking ko support karne ke liye
        textRecognitionEngine = mockk(relaxed = true)
        viewModel = OcrViewModel(textRecognitionEngine)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `jab OCR shuru ho, toh loading state true honi chahiye`() = runTest {
        // Arrange
        val fakeBitmap = mockk<Bitmap>(relaxed = true)
        // 🌟 Engine ko thoda wait karwao taaki hum loading state check kar sakein
        coEvery { textRecognitionEngine.extractTextFromBitmap(any()) } coAnswers {
            kotlinx.coroutines.delay(1000)
            Result.success(emptyList())
        }

        // Act: OCR start karo
        viewModel.onAction(OcrAction.StartLiveText(fakeBitmap))
        
        // 🌟 FIX: Sirf utna hi coroutine chalao jitna shuruati state update ke liye chahiye
        testDispatcher.scheduler.runCurrent()

        // Assert: Check karo ki loading shuru hui
        assertEquals(true, viewModel.state.value.isLoading)
        assertEquals(true, viewModel.state.value.isLiveTextActive)
    }

    @Test
    fun `jab OCR safal ho, toh blocks update hone chahiye aur loading band honi chahiye`() = runTest {
        // Arrange
        val fakeBitmap = mockk<Bitmap>(relaxed = true)
        val fakeBlocks = listOf(OcrTextBlock("Test", null, 1))
        
        coEvery { textRecognitionEngine.extractTextFromBitmap(any()) } returns Result.success(fakeBlocks)

        // Act
        viewModel.onAction(OcrAction.StartLiveText(fakeBitmap))
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(fakeBlocks, state.extractedBlocks)
    }

    @Test
    fun `jab OCR fail ho, toh error message aana chahiye`() = runTest {
        // Arrange
        val fakeBitmap = mockk<Bitmap>(relaxed = true)
        coEvery { textRecognitionEngine.extractTextFromBitmap(any()) } returns Result.failure(Exception("Engine Error"))

        // Act
        viewModel.onAction(OcrAction.StartLiveText(fakeBitmap))
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals("Engine Error", state.errorMessage)
    }
}