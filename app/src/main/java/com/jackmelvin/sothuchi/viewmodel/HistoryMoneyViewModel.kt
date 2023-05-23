package com.jackmelvin.sothuchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.helper.Constants
import com.jackmelvin.sothuchi.model.MoneyWithCategory
import com.jackmelvin.sothuchi.model.UserMonthBalance
import com.jackmelvin.sothuchi.repository.MoneyRepository
import com.jackmelvin.sothuchi.repository.UserMonthBalanceRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class HistoryMoneyViewModel(application: SoThuChiApplication) : ViewModel() {
    private val moneyRepository = MoneyRepository(application.database)
    private val userMonthBalanceRepository = UserMonthBalanceRepository(application.database)
    private val USER_ID = Constants.USER_ID
    private val calendar = Calendar.getInstance()
    private var currentYear = calendar.get(Calendar.YEAR)
    private var currentMonth = calendar.get(Calendar.MONTH) + 1

    lateinit var moneyHistory: Flow<List<MoneyWithCategory>>
    lateinit var currentMonthBalance: Flow<UserMonthBalance>
//    lateinit var previousMonthBalance: Flow<UserMonthBalance>

    init {
        refreshData()
    }

    fun setYearMonth(year: Int, month: Int) {
        currentYear = year
        currentMonth = month
        refreshData()
    }

    private fun refreshData() {
        moneyHistory = findMoney()
        currentMonthBalance = findCurrentMonthBalance()
//        previousMonthBalance = findPreviousMonthBalance()
    }

    private fun findMoney() = moneyRepository.findAllInTimeRange(
        USER_ID,
        Calendar.getInstance().apply {set(currentYear, currentMonth - 1, 1, 0, 0, 0) },
        Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth - 1)
            set(get(Calendar.YEAR), get(Calendar.MONTH), getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0)
        }
    )

    private fun findCurrentMonthBalance() = userMonthBalanceRepository.findByUserId(USER_ID, currentYear, currentMonth)
//    private fun findPreviousMonthBalance() = userMonthBalanceRepository.findByUserId(USER_ID, currentYear, currentMonth - 1)


    /**
     * Factory for constructing HistoryMoneyViewModel with parameter
     */
    class Factory(val app: SoThuChiApplication) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryMoneyViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HistoryMoneyViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}