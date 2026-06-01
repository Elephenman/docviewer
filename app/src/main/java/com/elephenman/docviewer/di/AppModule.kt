package com.elephenman.docviewer.di

import android.content.Context
import com.elephenman.docviewer.data.repository.BookshelfRepository
import com.elephenman.docviewer.data.repository.DocumentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDocumentRepository(
        @ApplicationContext context: Context
    ): DocumentRepository {
        return DocumentRepository(context)
    }

    @Provides
    @Singleton
    fun provideBookshelfRepository(
        @ApplicationContext context: Context
    ): BookshelfRepository {
        return BookshelfRepository(context)
    }
}
