package com.jackmelvin.sothuchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.model.MoneyWithCategory
import com.jackmelvin.sothuchi.repository.MoneyRepository
import kotlinx.coroutines.launch

class InputViewModel(application: SoThuChiApplication) : ViewModel() {
    private val moneyRepository: MoneyRepository = MoneyRepository(application.database)

    fun insertMoney(moneyWithCategory: MoneyWithCategory) {
        viewModelScope.launch {
            moneyRepository.insert(moneyWithCategory)
        }
    }

    /**
     * Factory for constructing InputViewModel with parameter
     */
    class Factory(val app: SoThuChiApplication) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InputViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InputViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}