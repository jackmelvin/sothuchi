package com.jackmelvin.sothuchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.helper.Constants
import com.jackmelvin.sothuchi.model.UserMonthBalance
import com.jackmelvin.sothuchi.repository.UserMonthBalanceRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class MainBalanceViewModel(application: SoThuChiApplication) : ViewModel() {
    private val repository = UserMonthBalanceRepository(application.database)
    private val USER_ID = Constants.USER_ID
    private val calendar = Calendar.getInstance()
    val currentMonthBalance: Flow<UserMonthBalance> = repository.findByUserId(USER_ID, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)

    /**
     * Factory for constructing MainBalanceViewModel with parameter
     */
    class Factory(val app: SoThuChiApplication) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainBalanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainBalanceViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}