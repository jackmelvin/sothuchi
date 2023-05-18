package com.jackmelvin.sothuchi.dao

import androidx.room.*
import com.jackmelvin.sothuchi.model.Money
import java.util.*

@Dao
interface MoneyDao {
    @Query("SELECT * FROM money WHERE id = (:moneyId)")
    suspend fun findById(moneyId: Long): Money?

    @Query("SELECT * FROM money WHERE user_id = (:userId) AND date BETWEEN (:startDate) AND (:endDate)")
    suspend fun findAllInTimeRange(userId: Long, startDate: Calendar, endDate: Calendar): List<Money>

    @Insert
    suspend fun insert(money: Money)

    @Delete
    suspend fun delete(money: Money)

}