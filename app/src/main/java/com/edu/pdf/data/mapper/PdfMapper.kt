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
        virtualParentId = virtualParentId,
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
        virtualParentId = virtualParentId,
        isVault = isVault
    )
}

fun FolderEntity.toDomainModel(pdfCount: Int = 0): Folder {
    return Folder(
        folderId = folderId,
        name = name,
        parentFolderId = parentFolderId,
        isVault = isVault,
        createdAt = createdAt,
        pdfCount = pdfCount,
        lastOpenedTime = lastOpenedTime // 🌟 NAYA: Database se UI me data bhej rahe hain
    )
}

fun FolderWithCount.toDomainModel(): Folder {
    return Folder(
        folderId = folder.folderId,
        name = folder.name,
        parentFolderId = folder.parentFolderId,
        isVault = folder.isVault,
        createdAt = folder.createdAt,
        pdfCount = pdfCount, // 🌟 Real count from Database!
        lastOpenedTime = folder.lastOpenedTime
    )
}