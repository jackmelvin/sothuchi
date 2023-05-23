package com.jackmelvin.sothuchi.dao

import androidx.room.*
import com.jackmelvin.sothuchi.model.Money
import com.jackmelvin.sothuchi.model.MoneyWithCategory
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MoneyDao {
    @Query("SELECT money.*, category.image_id, category.is_income " +
            "FROM money JOIN category ON money.category_id = category.id " +
            "WHERE money.id = :moneyId")
    fun findById(moneyId: Long): MoneyWithCategory?

    @Query("SELECT money.*, category.image_id, category.is_income " +
            "FROM money JOIN category ON money.category_id = category.id " +
            "WHERE user_id = (:userId) AND date BETWEEN (:startDate) AND (:endDate) ORDER BY date DESC")
    fun findAllInTimeRange(userId: Long, startDate: Calendar, endDate: Calendar): Flow<List<MoneyWithCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(money: Money)

    @Delete
    fun delete(money: Money)

}