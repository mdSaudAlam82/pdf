package com.edu.pdf.data.repository

import com.edu.pdf.data.local.dao.PdfDao
import com.edu.pdf.data.local.dao.SearchHistoryDao
import com.edu.pdf.data.local.entity.PdfEntity
import com.edu.pdf.data.source.DeviceStorageDataSource
import com.edu.pdf.data.preferences.UserPreferences
import com.edu.pdf.domain.repository.PdfRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PdfRepositoryTest {

    private lateinit var repository: PdfRepository
    private val pdfDao = mockk<PdfDao>()
    private val searchHistoryDao = mockk<SearchHistoryDao>()
    private val deviceStorage = mockk<DeviceStorageDataSource>()
    private val vaultStorage = mockk<com.edu.pdf.data.source.VaultDataSource>()
    private val userPreferences = mockk<UserPreferences>()

    @Before
    fun setup() {
        repository = PdfRepositoryImpl(
            pdfDao,
            searchHistoryDao,
            deviceStorage,
            vaultStorage,
            userPreferences
        )
    }

    @Test
    fun `getPdfByPath should return domain model when entity exists`() = runTest {
        val path = "/storage/test.pdf"
        val entity = PdfEntity(id = "1", name = "test.pdf", path = path, sizeInBytes = 100L, lastModified = 0L)
        
        coEvery { pdfDao.getPdfByPath(path) } returns entity
        
        val result = repository.getPdfByPath(path)
        
        assertEquals("test.pdf", result?.name)
        assertEquals(path, result?.path)
    }

    @Test
    fun `getPdfByPathFlow should emit domain model when entity exists`() = runTest {
        val path = "/storage/test.pdf"
        val entity = PdfEntity(id = "1", name = "test.pdf", path = path, sizeInBytes = 100L, lastModified = 0L)
        
        coEvery { pdfDao.getPdfByPathFlow(path) } returns flowOf(entity)
        
        val result = repository.getPdfByPathFlow(path).first()
        
        assertEquals("test.pdf", result?.name)
    }
}
