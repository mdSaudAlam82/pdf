package com.edu.pdf.presentation.pdfviewer.ai

import com.edu.pdf.domain.repository.AiRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AiChatViewModelTest {

    private lateinit var aiRepository: AiRepository
    private lateinit var viewModel: AiChatViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        aiRepository = mockk(relaxed = true)
        viewModel = AiChatViewModel(aiRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `jab user message bheje, toh message list mein add hona chahiye aur AI thinking state mein jana chahiye`() = runTest {
        // Arrange
        val query = "Hello AI"
        
        // Act: User ne message bheja
        viewModel.onAction(AiChatAction.SendMessage(query))

        // Assert: 
        val state = viewModel.state.value
        // 1. Check karo ki 2 message hain (ek User ka, ek AI ka placeholder)
        assertEquals(2, state.messages.size)
        assertEquals(query, state.messages[0].text)
        // 2. Check karo ki AI thinking mode mein hai
        assertTrue(state.isAiThinking)
    }

    @Test
    fun `jab AI response stream kare, toh message text update hona chahiye`() = runTest {
        // Arrange
        val query = "Summary"
        val aiChunks = listOf("This ", "is ", "a ", "PDF.")
        
        // Repository ko bolo ki ye 4 chunks ek-ek karke bheje
        coEvery { aiRepository.chatWithPdfStream(any(), any()) } returns flowOf(*aiChunks.toTypedArray())

        // Act
        viewModel.onAction(AiChatAction.SendMessage(query))
        
        // Coroutines ko apna kaam khatam karne do
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        val lastMessage = state.messages.last()
        assertEquals("This is a PDF.", lastMessage.text)
        assertEquals(false, state.isAiThinking)
    }

    @Test
    fun `jab API key galat ho, toh error message dikhna chahiye`() = runTest {
        // Arrange: Fake Error tayyar karo
        coEvery { aiRepository.chatWithPdfStream(any(), any()) } throws Exception("invalid API key")

        // Act
        viewModel.onAction(AiChatAction.SendMessage("Hi"))
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state.errorMessage?.contains("API Key invalid") == true)
    }
}