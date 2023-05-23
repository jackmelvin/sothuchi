package com.jackmelvin.sothuchi.dao

import androidx.room.*
import com.jackmelvin.sothuchi.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = (:userId)")
    fun findById(userId: Long): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}