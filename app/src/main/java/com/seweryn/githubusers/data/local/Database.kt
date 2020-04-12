package com.seweryn.githubusers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seweryn.githubusers.data.model.Repository
import com.seweryn.githubusers.data.model.User

@Database(entities = [User::class], version = 1)
abstract class Database  : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}