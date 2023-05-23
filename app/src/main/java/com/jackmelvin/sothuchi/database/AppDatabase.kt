package com.jackmelvin.sothuchi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.dao.CategoryDao
import com.jackmelvin.sothuchi.dao.MoneyDao
import com.jackmelvin.sothuchi.dao.UserDao
import com.jackmelvin.sothuchi.dao.UserMonthBalanceDao
import com.jackmelvin.sothuchi.helper.Constants
import com.jackmelvin.sothuchi.model.Category
import com.jackmelvin.sothuchi.model.Money
import com.jackmelvin.sothuchi.model.User
import com.jackmelvin.sothuchi.model.UserMonthBalance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Category::class, Money::class, User::class, UserMonthBalance::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun moneyDao(): MoneyDao
    abstract fun userDao(): UserDao
    abstract fun userMonthBalanceDao(): UserMonthBalanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "sothuchi")
                    .addCallback(callback)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.categoryDao()?.insert(
                        Category(R.drawable.salary, "Salary", true),
                        Category(R.drawable.bonus, "Bonus", true),
                        Category(R.drawable.food, "Food", false),
                        Category(R.drawable.home, "Rent", false),
                        Category(R.drawable.train, "Transportation", false)
                    )
                    INSTANCE?.userDao()?.insert(User("jackmelvin").apply {id = Constants.USER_ID})
                }
            }
        }
    }
}