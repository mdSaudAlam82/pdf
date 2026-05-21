package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RenamePdfUseCaseTest {

    private lateinit var repository: PdfRepository
    private lateinit var renamePdfUseCase: RenamePdfUseCase

    @Before
    fun setUp() {
        // 1. Repository ka ek "nakli" (mock) version banate hain
        repository = mockk()
        // 2. Use Case ko initialize karte hain nakli repository ke saath
        renamePdfUseCase = RenamePdfUseCase(repository)
    }

    @Test
    fun `jab naya naam khali ho, toh false return hona chahiye`() = runTest {
        // Arrange (Tayyari)
        val fakePdf = PdfFile(id = "1", name = "purana.pdf", path = "/path", sizeInBytes = 100, lastModified = 0)
        val emptyName = ""

        // Act (Kaam karo)
        val result = renamePdfUseCase(fakePdf, emptyName)

        // Assert (Check karo)
        assertEquals(false, result)
        // Check karo ki repository ko kabhi call hi nahi kiya gaya (kyunki naam khali tha)
        coVerify(exactly = 0) { repository.renamePdf(any(), any()) }
    }

    @Test
    fun `jab naya naam valid ho, toh repository call honi chahiye`() = runTest {
        // Arrange
        val fakePdf = PdfFile(id = "1", name = "purana.pdf", path = "/path", sizeInBytes = 100, lastModified = 0)
        val validName = "naya_naam.pdf"
        
        // Batao ki jab repository.renamePdf call ho, toh true return kare
        coEvery { repository.renamePdf(any(), any()) } returns true

        // Act
        val result = renamePdfUseCase(fakePdf, validName)

        // Assert
        assertEquals(true, result)
        // Check karo ki repository ka renamePdf function 1 baar call hua
        coVerify(exactly = 1) { repository.renamePdf(fakePdf, validName) }
    }
}