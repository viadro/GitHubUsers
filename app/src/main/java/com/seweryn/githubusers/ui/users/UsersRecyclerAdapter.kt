package com.seweryn.githubusers.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seweryn.githubusers.R
import com.seweryn.githubusers.data.model.User
import com.seweryn.githubusers.ui.extensions.showConditionally
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_user_item.view.*

class UsersRecyclerAdapter : RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>() {

    private var users: List<User> = listOf()
    private var isLoading = false

    fun updateUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    fun seLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_progress_item, parent, false)
                ViewHolder.ProgressViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_user_item, parent, false)
                ViewHolder.UserViewHolder(view)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (isLoading) users.size + 1
        else users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.UserViewHolder -> holder.bind(
                users[position],
                isItemOnLastPosition(position)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == itemCount - 1) 0
        else 1
    }

    private fun isItemOnLastPosition(position: Int) = (position == itemCount - 1)
            || (isLoading && position == itemCount - 2)

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class UserViewHolder(itemView: View) : ViewHolder(itemView) {
            fun bind(user: User, isLastItem: Boolean) {
                itemView.user_name.text = user.login
                Picasso.get().load(user.avatarUrl).error(R.drawable.ic_nerd)
                    .into(itemView.user_avatar)
                itemView.content_divider.showConditionally(!isLastItem)
                itemView.user_repositories_container.removeAllViews()
                user.repositories.forEach {
                    itemView.user_repositories_container.addView(
                        TextView(itemView.context).apply {
                            text = it.name
                            textSize = 15f
                        })
                }
            }
        }

        class ProgressViewHolder(itemView: View) : ViewHolder(itemView)
    }
}