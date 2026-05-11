package com.edu.pdf.data.mapper

import com.edu.pdf.data.local.dao.FolderWithCount
import com.edu.pdf.data.local.entity.FolderEntity
import com.edu.pdf.data.local.entity.PdfEntity
import com.edu.pdf.domain.model.Folder
import com.edu.pdf.domain.model.PdfFile

fun PdfEntity.toDomainModel(): PdfFile {
    return PdfFile(
        id = id,
        name = name,
        path = path,
        sizeInBytes = sizeInBytes,
        lastModified = lastModified,
        isFavorite = isFavorite,
        lastOpenedTime = lastOpenedTime,
        virtualParentId = parentPath, // 🌟 ELITE FIX: Ab parentPath hi tumhara 'virtualParentId' variable banega
        isVault = isVault
    )
}

fun PdfFile.toEntity(): PdfEntity {
    return PdfEntity(
        id = id,
        name = name,
        path = path,
        sizeInBytes = sizeInBytes,
        lastModified = lastModified,
        isFavorite = isFavorite,
        lastOpenedTime = lastOpenedTime,
        parentPath = virtualParentId, // 🌟 UI se aane wala ID asal me Path hai
        isVault = isVault
    )
}

fun FolderEntity.toDomainModel(pdfCount: Int = 0): Folder {
    return Folder(
        folderId = absolutePath, // 🌟 ELITE FIX: UI jise 'folderId' bol raha hai, wo asal me 'absolutePath' hai
        name = name,
        parentFolderId = parentPath,
        isVault = isVault,
        createdAt = createdAt,
        pdfCount = pdfCount,
        lastOpenedTime = lastOpenedTime
    )
}

fun FolderWithCount.toDomainModel(): Folder {
    return Folder(
        folderId = folder.absolutePath,
        name = folder.name,
        parentFolderId = folder.parentPath,
        isVault = folder.isVault,
        createdAt = folder.createdAt,
        pdfCount = pdfCount,
        lastOpenedTime = folder.lastOpenedTime
    )
}