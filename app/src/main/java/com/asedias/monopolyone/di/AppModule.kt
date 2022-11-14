package com.asedias.monopolyone.di

import android.app.Application
import com.asedias.monopolyone.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMainRepository(app: Application) : MainRepository {
        return MainRepository()
    }
}
