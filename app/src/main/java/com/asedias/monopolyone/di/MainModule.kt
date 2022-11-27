package com.asedias.monopolyone.di

import android.app.Application
import com.asedias.monopolyone.BuildConfig
import com.asedias.monopolyone.MonopolyApp
import com.asedias.monopolyone.data.MonopolyAPI
import com.asedias.monopolyone.data.repository.AuthRepositoryImpl
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        return Retrofit.Builder().baseUrl(MonopolyAPI.BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }

    @Provides
    @Singleton
    fun provideMonopolyAPI(retrofit: Retrofit): MonopolyAPI {
        return retrofit.create(MonopolyAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepo(api: MonopolyAPI, app: Application): AuthRepositoryImpl {
        return AuthRepositoryImpl(api, app as MonopolyApp)
    }
}