package com.seweryn.githubusers.di

import com.seweryn.githubusers.data.Constants
import com.seweryn.githubusers.data.remote.GitHubApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    @Named("github")
    fun provideEventsApiClient(okHttpClient: OkHttpClient): Retrofit {
        return buildRetrofitClient(
            okHttpClient,
            Constants.GITHUB_BASE_URL
        )
    }

    @Provides
    @Singleton
    fun provideEventsApiInterface(@Named("github") retrofit: Retrofit): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }

    private fun buildRetrofitClient(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}