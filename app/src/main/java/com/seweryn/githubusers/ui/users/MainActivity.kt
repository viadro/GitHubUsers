package com.seweryn.githubusers.ui.users

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.seweryn.githubusers.R
import com.seweryn.githubusers.ui.BaseActivity
import com.seweryn.githubusers.ui.PaginationListener
import com.seweryn.githubusers.ui.extensions.showConditionally
import com.seweryn.githubusers.viewmodel.UsersViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<UsersViewModel>() {

    private val layoutManager = LinearLayoutManager(this)
    private val adapter = UsersRecyclerAdapter()

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {   }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {   }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.searchPhrase(search_input.text.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        users_list.layoutManager = layoutManager
        users_list.adapter = adapter
        users_list.addOnScrollListener(PaginationListener(layoutManager){
            if(viewModel.loadMoreUsersAllowed.value == true) viewModel.loadMoreUsers() })
        search_input.addTextChangedListener(textWatcher)
        observeUsers()
        observeProgress()
        observeError()
    }

    override fun viewModel() = ViewModelProvider(this, viewModelFactory).get(UsersViewModel::class.java)

    private fun observeUsers() {
        viewModel.users.observe(this, Observer {
            adapter.updateUsers(it)
        })
    }

    private fun observeProgress() {
        viewModel.progress.observe(this, Observer {
            //progress_bar.showConditionally(it)
            adapter.seLoading(it)
        })
    }

    private fun observeError() {
        viewModel.error.observe(this, Observer {
            error_msg.showConditionally(it)
        })
    }
}
