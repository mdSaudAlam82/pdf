package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeletePdfsUseCaseTest {

    private lateinit var repository: PdfRepository
    private lateinit var deletePdfsUseCase: DeletePdfsUseCase

    @Before
    fun setUp() {
        // Repository ka nakli (mock) version banate hain
        repository = mockk()
        // Use case ko initialize karte hain
        deletePdfsUseCase = DeletePdfsUseCase(repository)
    }

    @Test
    fun `jab list khali ho, toh repository call nahi honi chahiye`() = runTest {
        // Arrange: Ek khali list banate hain
        val emptyList = emptyList<PdfFile>()

        // Act: Delete function ko call karte hain
        deletePdfsUseCase(emptyList)

        // Assert: Check karte hain ki repository ka delete function 0 baar call hua
        coVerify(exactly = 0) { repository.deletePdfs(any()) }
    }

    @Test
    fun `jab list mein PDFs honn, toh repository call honi chahiye`() = runTest {
        // Arrange: 2 nakli PDFs banate hain
        val pdf1 = PdfFile(id = "101", name = "file1.pdf", path = "/a", sizeInBytes = 10, lastModified = 0)
        val pdf2 = PdfFile(id = "102", name = "file2.pdf", path = "/b", sizeInBytes = 20, lastModified = 0)
        val pdfList = listOf(pdf1, pdf2)

        // Batao ki jab delete call ho toh success return kare
        coEvery { repository.deletePdfs(any()) } returns true

        // Act: Delete function call karo
        deletePdfsUseCase(pdfList)

        // Assert: Check karo ki repository ka deletePdfs wahi list lekar 1 baar call hua
        coVerify(exactly = 1) { repository.deletePdfs(pdfList) }
    }
}