package com.seweryn.githubusers.data.remote

import com.seweryn.githubusers.data.model.Repository
import com.seweryn.githubusers.data.model.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("users")
    fun getUsers(@Query("since") lastUserId: Int?): Single<List<User>>

    @GET("users/{user_login}/repos")
    fun getUserRepositories(@Path("user_login") userLogin: String): Single<List<Repository>>
}