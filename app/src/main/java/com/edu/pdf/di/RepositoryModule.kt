package com.edu.pdf.di

import com.edu.pdf.data.repository.PdfRepositoryImpl
import com.edu.pdf.domain.repository.PdfRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused") // 🌟 2026 Standard: Tells IDE that Hilt handles this, so don't show "unused" warnings
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPdfRepository(
        impl: PdfRepositoryImpl
    ): PdfRepository

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        impl: com.edu.pdf.data.repository.AiRepositoryImpl
    ): com.edu.pdf.domain.repository.AiRepository
}