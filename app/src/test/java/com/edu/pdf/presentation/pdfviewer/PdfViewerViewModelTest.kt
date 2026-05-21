package com.edu.pdf.presentation.pdfviewer

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PdfViewerViewModelTest {

    @Before
    fun setUp() {
        // 🌟 Mock Android Uri class
        mockkStatic(Uri::class)
    }

    @Test
    fun `jab ViewModel bante hi pdfPath mil jaye, toh URI aur FileName set hona chahiye`() {
        // Arrange: SavedStateHandle mein ek nakli path dalo
        val fakePath = "/storage/emulated/0/Documents/test_file.pdf"
        val savedStateHandle = SavedStateHandle(mapOf("pdfPath" to fakePath))
        
        // Mock Uri behavior specifically for this test
        val mockUri = mockk<Uri>(relaxed = true)
        every { Uri.fromFile(any()) } returns mockUri
        every { mockUri.path } returns fakePath

        // Act: ViewModel ko initialize karo
        val viewModel = PdfViewerViewModel(savedStateHandle)

        // Assert: Check karo ki kya FileName "test_file" hai (extension ke bina)
        val state = viewModel.uiState.value
        assertEquals("test_file", state.pdfFileName)
        assertEquals(fakePath, state.pdfUri?.path)
    }

    @Test
    fun `jab user top bar toggle kare, toh visibility badalni chahiye`() {
        // Arrange
        val viewModel = PdfViewerViewModel(SavedStateHandle())
        val initialVisibility = viewModel.uiState.value.isTopBarVisible

        // Act: Toggle action bhejo
        viewModel.onAction(PdfViewerAction.ToggleTopBar)

        // Assert: Pehle true tha toh ab false hona chahiye
        assertEquals(!initialVisibility, viewModel.uiState.value.isTopBarVisible)
    }

    @Test
    fun `jab user night mode badle, toh state update honi chahiye`() {
        // Arrange
        val viewModel = PdfViewerViewModel(SavedStateHandle())
        val initialMode = viewModel.uiState.value.isNightMode

        // Act
        viewModel.onAction(PdfViewerAction.ToggleNightMode)

        // Assert
        assertEquals(!initialMode, viewModel.uiState.value.isNightMode)
    }
}