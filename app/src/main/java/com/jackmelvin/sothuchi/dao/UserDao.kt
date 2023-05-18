package com.jackmelvin.sothuchi.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jackmelvin.sothuchi.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = (:userId)")
    suspend fun findById(userId: Long): User?

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)
}