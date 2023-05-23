package com.jackmelvin.sothuchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.model.Category
import com.jackmelvin.sothuchi.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryViewModel(application: SoThuChiApplication) : ViewModel() {
    private val categoryRepository = CategoryRepository(application.database)
    private val incomeCategories = categoryRepository.incomeCategories
    private val paymentCategories = categoryRepository.paymentCategories

    fun getCategories(isIncome: Boolean): Flow<List<Category>> = if(isIncome) incomeCategories else paymentCategories

    /**
     * Factory for constructing CategoryViewModel with parameter
     */
    class Factory(val app: SoThuChiApplication) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoryViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}