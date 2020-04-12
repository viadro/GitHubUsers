package com.seweryn.githubusers.di

import android.app.Application
import androidx.room.Room
import com.seweryn.githubusers.data.Constants
import com.seweryn.githubusers.data.UsersRepository
import com.seweryn.githubusers.data.UsersRepositoryImpl
import com.seweryn.githubusers.data.local.Database
import com.seweryn.githubusers.data.remote.GitHubApi
import com.seweryn.githubusers.utils.SchedulerProvider
import com.seweryn.githubusers.utils.SchedulerProviderImpl
import com.seweryn.githubusers.utils.network.ConnectionManager
import com.seweryn.githubusers.utils.network.ConnectionManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideSchedulersProvider(): SchedulerProvider = SchedulerProviderImpl()

    @Provides
    @Singleton
    fun provideConnectionManager(): ConnectionManager = ConnectionManagerImpl(application)

    @Provides
    @Singleton
    fun provideUsersRepository(usersApi: GitHubApi, database: Database, connectionManager: ConnectionManager): UsersRepository =
        UsersRepositoryImpl(usersApi, database, connectionManager)

    @Provides
    @Singleton
    fun provideEventsDatabase(): Database = Room.databaseBuilder(
        application,
        Database::class.java, Constants.USERS_DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .build()
}