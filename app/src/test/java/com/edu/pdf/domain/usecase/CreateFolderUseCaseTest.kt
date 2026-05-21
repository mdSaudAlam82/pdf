package com.edu.pdf.domain.usecase

import com.edu.pdf.domain.repository.PdfRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateFolderUseCaseTest {

    // 1. Apna "Stunt Double" (Mock) aur asli UseCase banayenge
    private lateinit var repository: PdfRepository
    private lateinit var useCase: CreateFolderUseCase

    @Before
    fun setUp() {
        // Repository ka ek nakli roop (mock) tayar kiya
        repository = mockk(relaxed = true)

        // Asli UseCase ko ye nakli repository pakda di
        useCase = CreateFolderUseCase(repository)
    }

    // 🌟 Test Case 1: Agar folder ka naam khali (blank) hai
    @Test
    fun whenFolderNameIsBlank_returnFailure() = runTest {
        // Act: UseCase ko ekdum khali naam dekar chalaya ("   ")
        val result = useCase.invoke(name = "   ", parentId = null)

        // Assert: Hum check kar rahe hain ki kya error (failure) aaya?
        assertTrue(result.isFailure)

        // Check kar rahe hain ki kya exactly wahi error message aaya jo aapne file me likha hai?
        assertEquals("Folder name cannot be empty", result.exceptionOrNull()?.message)

        // Verify: Check karo ki nakli repository ko galti se bhi call toh nahi chali gayi?
        coVerify(exactly = 0) { repository.createManagedFolder(any(), any(), any()) }
    }

    // 🌟 Test Case 2: Agar folder ka naam ekdum sahi hai
    @Test
    fun whenFolderNameIsValid_callRepositoryAndReturnSuccess() = runTest {
        val validName = "My Notes"
        val expectedResult = Result.success("path/to/My Notes")

        // Arrange: Nakli repository ko sikhaya ki jab "My Notes" aaye toh 'expectedResult' dena
        coEvery { repository.createManagedFolder(validName, null, false) } returns expectedResult

        // Act: UseCase ko sahi naam dekar chalaya
        val result = useCase.invoke(name = validName, parentId = null)

        // Assert: Hum check kar rahe hain ki kya Success aaya?
        assertTrue(result.isSuccess)
        assertEquals(expectedResult, result)

        // Verify: Check karo ki nakli repository ko exactly 1 baar wahi naam dekar call kiya gaya tha ya nahi?
        coVerify(exactly = 1) { repository.createManagedFolder(validName, null, false) }
    }
}