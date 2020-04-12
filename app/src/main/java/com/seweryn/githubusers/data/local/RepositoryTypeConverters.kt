package com.seweryn.githubusers.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seweryn.githubusers.data.model.Repository

class RepositoryTypeConverters {
    @TypeConverter
    fun jsonToRepositories(json: String): List<Repository> {
        val gson = Gson()
        val type = object: TypeToken<List<Repository>>(){}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun repositoriesToJson(repositories: List<Repository>): String {
        val gson = Gson()
        val type = object: TypeToken<List<Repository>>(){}.type
        return gson.toJson(repositories, type)
    }

}