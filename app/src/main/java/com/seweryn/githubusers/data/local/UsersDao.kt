package com.seweryn.githubusers.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seweryn.githubusers.data.model.User
import io.reactivex.Single

@Dao
interface UsersDao {
    @Query("SELECT * FROM user")
    fun queryUsers(): Single<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(events: List<User>)

}