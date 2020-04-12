package com.seweryn.githubusers.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.seweryn.githubusers.viewmodel.BaseViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<T: BaseViewModel> : AppCompatActivity() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: T

    abstract fun viewModel(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        viewModel = viewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

}