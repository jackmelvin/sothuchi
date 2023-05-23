package com.jackmelvin.sothuchi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @ColumnInfo(name = "image_id") val imageId:Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_income") val isIncome: Boolean
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}