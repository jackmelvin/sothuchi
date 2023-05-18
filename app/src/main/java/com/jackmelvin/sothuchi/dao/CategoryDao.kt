package com.jackmelvin.sothuchi.dao

import androidx.room.*
import com.jackmelvin.sothuchi.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE is_income = (:isIncome)")
    suspend fun findAllByType(isIncome: Boolean): List<Category>

    @Query("SELECT * FROM category WHERE id = (:categoryId)")
    suspend fun findById(categoryId: Long): Category?

    @Insert
    suspend fun insert(category: Category)

    @Insert
    suspend fun insertAll(vararg category: Category)

    @Delete
    suspend fun delete(category: Category)
}