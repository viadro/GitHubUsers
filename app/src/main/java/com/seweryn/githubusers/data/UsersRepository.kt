package com.seweryn.githubusers.data

import com.seweryn.githubusers.data.model.User
import io.reactivex.Single

interface UsersRepository {
    fun getUsers(): Single<List<User>>

    fun paginateUsers(lastUserId: Int): Single<List<User>>
}