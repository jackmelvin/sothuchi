package com.jackmelvin.sothuchi.dao

import androidx.room.*
import com.jackmelvin.sothuchi.model.UserMonthBalance

@Dao
interface UserMonthBalanceDao {
    @Query("SELECT * FROM user_month_balance WHERE user_id = (:userId) AND year = (:year) AND month = (:month)")
    suspend fun findByUserId(userId: Long, year: Int, month: Int): UserMonthBalance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userMonthBalance: UserMonthBalance)

    @Update
    suspend fun update(userMonthBalance: UserMonthBalance)

    @Delete
    suspend fun delete(userMonthBalance: UserMonthBalance)

}