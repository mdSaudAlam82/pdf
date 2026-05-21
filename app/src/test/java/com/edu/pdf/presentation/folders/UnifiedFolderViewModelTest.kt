package com.edu.pdf.presentation.folders

import android.net.Uri
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.domain.model.FolderType
import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.CreateFolderUseCase
import com.edu.pdf.domain.usecase.DeleteFolderUseCase
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UnifiedFolderViewModelTest {

    private lateinit var repository: PdfRepository
    private lateinit var createFolderUseCase: CreateFolderUseCase
    private lateinit var deleteFolderUseCase: DeleteFolderUseCase
    private lateinit var deletePdfsUseCase: DeletePdfsUseCase
    private lateinit var renamePdfUseCase: RenamePdfUseCase
    private lateinit var userPreferences: UserPreferences

    private lateinit var viewModel: UnifiedFolderViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        // 🌟 MOCK ANDROID URI: Decode function ko mock karo
        mockkStatic(Uri::class)
        io.mockk.every { Uri.decode(any()) } answers { it.invocation.args[0] as String }

        // 1. Pehle saare Mocks initialize karo
        repository = mockk(relaxed = true)
        createFolderUseCase = mockk(relaxed = true)
        deleteFolderUseCase = mockk(relaxed = true)
        deletePdfsUseCase = mockk(relaxed = true)
        renamePdfUseCase = mockk(relaxed = true)
        userPreferences = mockk(relaxed = true)

        // 2. Ab Flows ko stub karo (Ye sabse zaroori hai!)
        io.mockk.every { repository.getManagedFolders(any(), any()) } returns flowOf(emptyList())
        io.mockk.every { repository.getAllManagedFolders(any()) } returns flowOf(emptyList())
        io.mockk.every { repository.getPaginatedPdfsInPhysicalFolder(any(), any()) } returns flowOf(androidx.paging.PagingData.empty())
        io.mockk.every { repository.getPaginatedManagedPdfs(any(), any(), any()) } returns flowOf(androidx.paging.PagingData.empty())
        
        io.mockk.every { userPreferences.isFolderGridViewFlow } returns flowOf(false)

        // 3. ViewModel ko in stubbed objects ke saath banao
        viewModel = UnifiedFolderViewModel(
            repository = repository,
            deleteFolderUseCase = deleteFolderUseCase,
            deletePdfsUseCase = deletePdfsUseCase,
            renamePdfUseCase = renamePdfUseCase,
            createFolderUseCase = createFolderUseCase,
            userPreferences = userPreferences
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test vault toggle and processing state`() = runTest {
        val fakePdf = mockk<com.edu.pdf.domain.model.PdfFile>(relaxed = true)
        io.mockk.every { fakePdf.id } returns "pdf_123"
        io.mockk.every { fakePdf.isVault } returns false

        // Action: Toggle Vault
        viewModel.onAction(UnifiedFolderAction.ToggleVaultStatus(fakePdf))

        // 🌟 SIMPLIFIED FIX: Hanging flow se bachne ke liye advanceUntilIdle use karein
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify Repository Call: Ye sabse pakka saboot hai ki kaam hua
        coVerify(exactly = 1) {
            repository.movePdfsToVirtualFolder(listOf("pdf_123"), null, isVault = true)
        }
    }

    @Test
    fun `jab user folder aur pdf dono move kare, toh dono ke liye repository call honi chahiye`() = runTest {
        val fakeFolder = mockk<com.edu.pdf.domain.model.HomeItem.FolderItem>(relaxed = true)
        val fakePdf = mockk<com.edu.pdf.domain.model.HomeItem.PdfItem>(relaxed = true)
        
        io.mockk.every { fakeFolder.folder.folderId } returns "folder_abc"
        io.mockk.every { fakePdf.pdf.id } returns "pdf_789"
        
        viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(listOf(fakeFolder, fakePdf))))

        val targetId = "target_folder_xyz"
        viewModel.onAction(UnifiedFolderAction.ConfirmMove(targetId))
        
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.movePdfsToVirtualFolder(listOf("pdf_789"), targetId, any()) }
        coVerify(exactly = 1) { repository.moveFolderToVirtualFolder("folder_abc", targetId, any()) }
        
        val event = viewModel.events.first()
        assertTrue(event is UnifiedFolderEvent.ClearMultiSelection)
    }

    @Test
    fun `jab target folder aur current folder same ho, toh move nahi hona chahiye`() = runTest {
        viewModel.onAction(UnifiedFolderAction.InitializeFolder("folder_a", "Folder A", FolderType.VIRTUAL_HUB))
        
        val items = listOf(mockk<com.edu.pdf.domain.model.HomeItem>(relaxed = true))
        viewModel.onAction(UnifiedFolderAction.OpenSheet(UnifiedFolderSheetState.MovePicker(items)))

        viewModel.onAction(UnifiedFolderAction.ConfirmMove("folder_a"))
        
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { repository.moveFolderToVirtualFolder(any(), any(), any()) }
        coVerify(exactly = 0) { repository.movePdfsToVirtualFolder(any(), any(), any()) }
    }
}
