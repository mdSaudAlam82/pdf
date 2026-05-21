package com.edu.pdf.data.mapper

import com.edu.pdf.data.local.entity.PdfEntity
import com.edu.pdf.domain.model.PdfFile
import org.junit.Assert.assertEquals
import org.junit.Test

class PdfMapperTest {

    @Test
    fun `PdfEntity ko PdfFile mein badalte waqt saara data sahi rehna chahiye`() {
        // Arrange: Ek nakli Database Entity banao
        val entity = PdfEntity(
            id = "777",
            name = "test.pdf",
            path = "/storage/test.pdf",
            sizeInBytes = 1024,
            lastModified = 123456789,
            isFavorite = true,
            parentPath = "folder1"
        )

        // Act: Use Mapper to convert
        val domainModel = entity.toDomainModel()

        // Assert: Check karo ki kya data wahi hai jo humne bheja tha
        assertEquals(entity.id, domainModel.id)
        assertEquals(entity.name, domainModel.name)
        assertEquals(entity.path, domainModel.path)
        assertEquals(entity.isFavorite, domainModel.isFavorite)
        assertEquals(entity.parentPath, domainModel.virtualParentId)
    }

    @Test
    fun `PdfFile ko PdfEntity mein badalte waqt data sahi rehna chahiye`() {
        // Arrange: Ek nakli Domain Model banao
        val domainModel = PdfFile(
            id = "888",
            name = "domain.pdf",
            path = "/path/domain.pdf",
            sizeInBytes = 2048,
            lastModified = 987654321,
            isFavorite = false,
            virtualParentId = "root"
        )

        // Act: Convert back to Entity
        val entity = domainModel.toEntity()

        // Assert: Verify
        assertEquals(domainModel.id, entity.id)
        assertEquals(domainModel.virtualParentId, entity.parentPath)
        assertEquals(domainModel.isFavorite, entity.isFavorite)
    }
}