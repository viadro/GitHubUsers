package com.seweryn.githubusers.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seweryn.githubusers.data.UsersRepository
import com.seweryn.githubusers.utils.SchedulerProvider
import com.seweryn.githubusers.viewmodel.UsersViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule  {

    @Provides
    fun provideRatesViewModelFactory(usersRepository: UsersRepository, schedulerProvider: SchedulerProvider) = object :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UsersViewModel(usersRepository, schedulerProvider) as T
        }

    }
}