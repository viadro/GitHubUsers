package com.seweryn.githubusers.di

import com.seweryn.githubusers.ui.users.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeEventsFragment(): MainActivity
}