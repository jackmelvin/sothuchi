package com.jackmelvin.sothuchi.repository

import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CategoryRepository(database: AppDatabase) {
    private val dao = database.categoryDao()
    val incomeCategories: Flow<List<Category>> = dao.getAllIncomeCategories()
    val paymentCategories: Flow<List<Category>> = dao.getAllPaymentCategories()

    suspend fun findById(categoryId: Long): Category? {
        return withContext(Dispatchers.IO) {
            dao.findById(categoryId)
        }
    }

    suspend fun insert(vararg category: Category) {
        withContext(Dispatchers.IO) {
            dao.insert(*category)
        }
    }

    suspend fun delete(category: Category) {
        withContext(Dispatchers.IO) {
            dao.delete(category)
        }
    }
}