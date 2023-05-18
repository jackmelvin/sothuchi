package com.jackmelvin.sothuchi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.converter.DateConverter
import com.jackmelvin.sothuchi.dao.CategoryDao
import com.jackmelvin.sothuchi.dao.MoneyDao
import com.jackmelvin.sothuchi.dao.UserDao
import com.jackmelvin.sothuchi.dao.UserMonthBalanceDao
import com.jackmelvin.sothuchi.model.Category
import com.jackmelvin.sothuchi.model.Money
import com.jackmelvin.sothuchi.model.User
import com.jackmelvin.sothuchi.model.UserMonthBalance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Category::class, Money::class, User::class, UserMonthBalance::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun moneyDao(): MoneyDao
    abstract fun userDao(): UserDao
    abstract fun userMonthBalanceDao(): UserMonthBalanceDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "sothuchi"
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                GlobalScope.launch {
                                    INSTANCE?.categoryDao()?.insertAll(
                                        Category(R.drawable.salary, "Salary", true),
                                        Category(R.drawable.bonus, "Bonus", true),
                                        Category(R.drawable.food, "Food", false),
                                        Category(R.drawable.home, "Rent", false),
                                        Category(R.drawable.train, "Transportation", false)
                                    )
                                    INSTANCE?.userDao()?.insert(User("jackmelvin", 1))
                                }
                            }
                        })
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}