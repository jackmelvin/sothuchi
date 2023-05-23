package com.jackmelvin.sothuchi.repository

import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.helper.Constants
import com.jackmelvin.sothuchi.model.Money
import com.jackmelvin.sothuchi.model.MoneyWithCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.*

class MoneyRepository(database: AppDatabase) {
    private val moneyDao = database.moneyDao()
    private val userMonthBalanceRepository = UserMonthBalanceRepository(database)

    suspend fun findById(moneyId: Long): MoneyWithCategory? {
        return withContext(Dispatchers.IO) {
            moneyDao.findById(moneyId)
        }
    }

    fun findAllInTimeRange(userId: Long, startDate: Calendar, endDate: Calendar): Flow<List<MoneyWithCategory>> {
        return moneyDao.findAllInTimeRange(userId, startDate, endDate)
    }

    suspend fun insert(moneyWithCategory: MoneyWithCategory) {
        withContext(Dispatchers.IO) {
            val money = moneyWithCategory.money
            moneyDao.insert(money)

            // Update user month balance after inserting money
            val userMonthBalance = userMonthBalanceRepository.findByUserId(Constants.USER_ID, money.date.get(Calendar.YEAR), money.date.get(Calendar.MONTH) + 1)
                .first()
            if(moneyWithCategory.isIncome) {
                userMonthBalance.incomeSummary += money.amount
            } else {
                userMonthBalance.paymentSummary += money.amount
            }
            userMonthBalanceRepository.update(userMonthBalance)
        }
    }

    suspend fun delete(money: Money) {
        withContext(Dispatchers.IO) {
            moneyDao.delete(money)
        }
    }
}