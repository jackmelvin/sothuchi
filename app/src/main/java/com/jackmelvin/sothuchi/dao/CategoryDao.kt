package com.jackmelvin.sothuchi.dao

import androidx.room.*
import com.jackmelvin.sothuchi.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE is_income = TRUE ORDER BY id ASC")
    fun getAllIncomeCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE is_income = FALSE ORDER BY id ASC")
    fun getAllPaymentCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = (:categoryId)")
    fun findById(categoryId: Long): Category?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg category: Category)

    @Delete
    fun delete(category: Category)
}