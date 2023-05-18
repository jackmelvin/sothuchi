package com.jackmelvin.sothuchi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "user_month_balance", primaryKeys = ["user_id", "year", "month"], foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.RESTRICT
    )
])
data class UserMonthBalance (
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "income_summary") var incomeSummary: Int,
    @ColumnInfo(name = "paid_summary") var paidSummary: Int,
)