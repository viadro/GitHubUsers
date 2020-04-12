package com.seweryn.githubusers.viewmodel

import androidx.lifecycle.ViewModel
import com.seweryn.githubusers.utils.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(private val schedulersProvider: SchedulerProvider) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    fun onDestroy() {
        subscriptions.clear()
    }

    protected fun clearSubscriptions() = subscriptions.clear()

    protected fun <T> load(
        command: Single<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        subscriptions.add(
            command.subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe(
                    { result -> onSuccess.invoke(result) },
                    { error -> onError.invoke(error) })
        )
    }
}