package com.edu.pdf.di

import com.edu.pdf.domain.repository.PdfRepository
import com.edu.pdf.domain.usecase.CreateFolderUseCase // 🌟 NAYA IMPORT
import com.edu.pdf.domain.usecase.DeleteFolderUseCase
import com.edu.pdf.domain.usecase.DeletePdfsUseCase
import com.edu.pdf.domain.usecase.RenamePdfUseCase
import com.edu.pdf.domain.usecase.ScanPdfsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideScanPdfsUseCase(repository: PdfRepository): ScanPdfsUseCase {
        return ScanPdfsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeletePdfsUseCase(repository: PdfRepository): DeletePdfsUseCase {
        return DeletePdfsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRenamePdfUseCase(repository: PdfRepository): RenamePdfUseCase {
        return RenamePdfUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateFolderUseCase(repository: PdfRepository): CreateFolderUseCase {
        return CreateFolderUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideDeleteFolderUseCase(repository: PdfRepository): DeleteFolderUseCase {
        return DeleteFolderUseCase(repository)
    }
}