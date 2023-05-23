package com.jackmelvin.sothuchi.repository

import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.model.UserMonthBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserMonthBalanceRepository(database: AppDatabase) {
    private val dao = database.userMonthBalanceDao()

    fun findByUserId(userId: Long, year: Int, month: Int): Flow<UserMonthBalance> {
        return dao.findByUserId(userId, year, month).map { balance ->
                if (balance == null) {
                    val newBalance = UserMonthBalance(userId, year, month, 0, 0)
                    insert(newBalance)
                    newBalance
                } else {
                    balance
                }
            }
    }

    suspend fun insert(userMonthBalance: UserMonthBalance) {
        withContext(Dispatchers.IO) {
            dao.insert(userMonthBalance)
        }
    }

    suspend fun update(userMonthBalance: UserMonthBalance) {
        withContext(Dispatchers.IO) {
            dao.update(userMonthBalance)
        }
    }
    suspend fun delete(userMonthBalance: UserMonthBalance) {
        withContext(Dispatchers.IO) {
            dao.delete(userMonthBalance)
        }
    }
}