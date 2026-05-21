package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteFolderUseCaseTest {

    private lateinit var repository: PdfRepository
    private lateinit var deleteFolderUseCase: DeleteFolderUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        deleteFolderUseCase = DeleteFolderUseCase(repository)
    }

    @Test
    fun `jab folder delete ho, toh repository call honi chahiye`() = runTest {
        // Arrange
        val folderId = "folder_123"
        coEvery { repository.deleteManagedFolder(folderId) } returns Result.success(Unit)

        // Act
        val result = deleteFolderUseCase(folderId)

        // Assert
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { repository.deleteManagedFolder(folderId) }
    }
}