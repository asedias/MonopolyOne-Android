package com.asedias.monopolyone.di

import com.asedias.monopolyone.data.remote.MonopolyAPI
import com.asedias.monopolyone.data.repository.AuthRepositoryImpl
import com.asedias.monopolyone.data.repository.MainPageRepositoryImpl
import com.asedias.monopolyone.domain.repository.MainPageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MainPageModule {
    @Provides
    @ViewModelScoped
    fun provideMainPageRepo(
        api: MonopolyAPI,
        authRepositoryImpl: AuthRepositoryImpl
    ): MainPageRepository {
        return MainPageRepositoryImpl(api, authRepositoryImpl)
    }
}