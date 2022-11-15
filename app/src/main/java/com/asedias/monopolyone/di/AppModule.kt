package com.asedias.monopolyone.di

import android.app.Application
import com.asedias.monopolyone.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object AppModule {
    @Provides
    @ViewModelScoped
    fun provideMainRepository(app: Application) : MainRepository {
        return MainRepository()
    }
}
