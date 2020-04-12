package com.seweryn.githubusers.data

import com.seweryn.githubusers.data.local.Database
import com.seweryn.githubusers.data.model.Repository
import com.seweryn.githubusers.data.model.User
import com.seweryn.githubusers.data.remote.GitHubApi
import com.seweryn.githubusers.utils.network.ConnectionManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class UsersRepositoryImpl(
    private val gitHubApi: GitHubApi,
    private val database: Database,
    private val connectionManager: ConnectionManager
) : UsersRepository {

    override fun getUsers(): Single<List<User>> {
        return if (connectionManager.isConnected())
            fetchUsers(null) else database.usersDao().queryUsers()
    }

    override fun paginateUsers(lastUserId: Int): Single<List<User>> {
        return if (connectionManager.isConnected())
            fetchUsers(lastUserId) else Single.just<List<User>>(listOf())
    }

    private fun fetchUsers(lastUserId: Int?): Single<List<User>> {
        return gitHubApi.getUsers(100)
            .toObservable()
            .flatMapIterable { it }
            .flatMap { user ->
                gitHubApi.getUserRepositories(user.login)
                    .map { user.copy(repositories = it.take(3)) }
                    .toObservable()
            }.toList().doOnSuccess {
                database.usersDao().insertUsers(it)
            }

    }
}