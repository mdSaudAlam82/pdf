package com.edu.pdf.presentation.folders.vault

import android.content.Context
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.data.security.VaultCryptoEngine
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VaultViewModelTest {

    private lateinit var context: Context
    private lateinit var repository: PdfRepository
    private lateinit var cryptoEngine: VaultCryptoEngine
    private lateinit var deletePdfsUseCase: DeletePdfsUseCase
    private lateinit var userPreferences: UserPreferences
    
    private lateinit var viewModel: VaultViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        context = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        cryptoEngine = mockk(relaxed = true)
        deletePdfsUseCase = mockk(relaxed = true)
        userPreferences = mockk(relaxed = true)

        // Initial setup: Vault khali dikhao
        every { repository.getManagedPdfs(any(), true) } returns flowOf(emptyList())
        every { userPreferences.isFolderGridViewFlow } returns flowOf(false)

        viewModel = VaultViewModel(
            context = context,
            repository = repository,
            cryptoEngine = cryptoEngine,
            deletePdfsUseCase = deletePdfsUseCase,
            userPreferences = userPreferences
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `jab vault screen khule, toh shuruat mein loading honi chahiye`() = runTest {
        // ViewModel bante hi shuruati state check karo
        val state = viewModel.uiState.value
        assertEquals(true, state.isLoading)
    }

    @Test
    fun `jab user picker open kare, toh state update honi chahiye`() = runTest {
        // Act: User ne PDF select karne ke liye picker khola
        viewModel.onAction(VaultAction.OpenPicker)

        // 🌟 FIX: Wait karo jab tak state mein isPickerOpen true na ho jaye
        val state = viewModel.uiState.first { it.isPickerOpen }
        
        // Assert
        assertEquals(true, state.isPickerOpen)
    }

    @Test
    fun `jab view mode toggle kare, toh preferences call honi chahiye`() = runTest {
        // Arrange: Make sure state is updated
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: User ne Grid/List view badla
        viewModel.onAction(VaultAction.ToggleViewMode)
        
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Hum check karte hain ki kya userPreferences ko toggle karne ki command gayi
        io.mockk.coVerify { userPreferences.saveFolderGridViewPreference(any()) }
    }
}