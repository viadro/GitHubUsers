package com.seweryn.githubusers.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.seweryn.githubusers.data.local.RepositoryTypeConverters

@Entity
@TypeConverters(RepositoryTypeConverters::class)
data class User(
    @PrimaryKey val id: Int,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    var repositories: List<Repository>
)