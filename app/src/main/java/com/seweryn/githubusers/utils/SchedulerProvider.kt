package com.seweryn.githubusers.utils

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ioScheduler(): Scheduler

    fun uiScheduler(): Scheduler
}