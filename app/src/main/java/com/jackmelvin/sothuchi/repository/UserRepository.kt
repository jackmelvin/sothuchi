package com.jackmelvin.sothuchi.repository

import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(database: AppDatabase) {
    private val dao = database.userDao()

    suspend fun findById(userId: Long): User? {
        return withContext(Dispatchers.IO) {
            dao.findById(userId)
        }
    }

    suspend fun insert(user: User) {
        withContext(Dispatchers.IO) {
            dao.insert(user)
        }
    }

    suspend fun delete(user: User) {
        withContext(Dispatchers.IO) {
            dao.delete(user)
        }
    }
}