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
import kotlinx.android.synthetic.main.view_user_item.*
import kotlinx.android.synthetic.main.view_user_item.view.*

class UsersRecyclerAdapter : RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>() {

    private var users: List<User> = listOf()

    fun updateUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_user_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position], isItemOnLastPosition(position))
    }

    private fun isItemOnLastPosition(position: Int) = position == itemCount - 1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User, isLastItem: Boolean) {
            itemView.user_name.text = user.login
            Picasso.get().load(user.avatarUrl).error(R.drawable.ic_nerd).into(itemView.user_avatar)
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
}