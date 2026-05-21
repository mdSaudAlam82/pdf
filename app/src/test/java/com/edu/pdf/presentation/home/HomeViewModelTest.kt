package com.edu.pdf.presentation.home

import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.CreateFolderUseCase
import com.edu.pdf.domain.usecase.DeleteFolderUseCase
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: PdfRepository
    private lateinit var deletePdfsUseCase: DeletePdfsUseCase
    private lateinit var renamePdfUseCase: RenamePdfUseCase
    private lateinit var scanPdfsUseCase: ScanPdfsUseCase
    private lateinit var createFolderUseCase: CreateFolderUseCase
    private lateinit var deleteFolderUseCase: DeleteFolderUseCase
    private lateinit var userPreferences: UserPreferences

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk(relaxed = true)
        deletePdfsUseCase = mockk(relaxed = true)
        renamePdfUseCase = mockk(relaxed = true)
        scanPdfsUseCase = mockk(relaxed = true)
        createFolderUseCase = mockk(relaxed = true)
        deleteFolderUseCase = mockk(relaxed = true)
        userPreferences = mockk(relaxed = true)

        every { userPreferences.isGridViewFlow } returns flowOf(false)
        every { repository.getManagedFolders(any(), any()) } returns flowOf(emptyList())
        every { repository.getAllManagedFolders(any()) } returns flowOf(emptyList())
        every { repository.getFavoritePdfs(any()) } returns flowOf(emptyList())
        every { repository.getRecentPdfs() } returns flowOf(emptyList())
        every { repository.getRecentFolders() } returns flowOf(emptyList())

        viewModel = HomeViewModel(
            repository = repository,
            deletePdfsUseCase = deletePdfsUseCase,
            renamePdfUseCase = renamePdfUseCase,
            scanPdfsUseCase = scanPdfsUseCase,
            createFolderUseCase = createFolderUseCase,
            deleteFolderUseCase = deleteFolderUseCase,
            userPreferences = userPreferences
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun checkInitialUiStateValues() = runTest {
        // 🌟 ELITE FIX: first { } test ko tab tak wait karwayega jab tak background thread apna kaam karke isLoading ko false na kar de
        val state = viewModel.uiState.first { !it.isLoading }

        assertEquals(false, state.isLoading)
        assertTrue(state.selectedIds.isEmpty())
        assertEquals(false, state.isSelectionMode)
    }

    @Test
    fun whenToggleSelectionActionIsFired_selectedIdsShouldUpdate() = runTest {
        // Pehle thoda wait karo ki shuruati data load ho jaye
        viewModel.uiState.first { !it.isLoading }

        val fakePdfId = "pdf_777"

        // Act: User ne PDF select kiya
        viewModel.onAction(HomeAction.ToggleSelection(fakePdfId))

        // 🌟 ELITE FIX: Wait karo jab tak wo fake ID select hokar State mein save na ho jaye
        val updatedState = viewModel.uiState.first { it.selectedIds.contains(fakePdfId) }

        assertTrue(updatedState.selectedIds.contains(fakePdfId))
    }
}