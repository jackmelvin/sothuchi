package com.jackmelvin.sothuchi.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class MoneyWithCategory(
    @Embedded val money: Money,
    @ColumnInfo(name = "image_id") val imageId: Int,
    @ColumnInfo(name = "is_income") val isIncome: Boolean
)
