package com.jackmelvin.sothuchi.dao

import androidx.room.*
import com.jackmelvin.sothuchi.model.UserMonthBalance
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMonthBalanceDao {
    @Query("SELECT * FROM user_month_balance WHERE user_id = (:userId) AND year = (:year) AND month = (:month)")
    fun findByUserId(userId: Long, year: Int, month: Int): Flow<UserMonthBalance?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userMonthBalance: UserMonthBalance)

    @Update
    fun update(userMonthBalance: UserMonthBalance)

    @Delete
    fun delete(userMonthBalance: UserMonthBalance)

}