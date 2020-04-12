package com.seweryn.githubusers.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationListener(private val layoutManager: LinearLayoutManager, private val action: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if(layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount
            && layoutManager.findFirstVisibleItemPosition() >= 0) {
            action.invoke()
        }
    }
}