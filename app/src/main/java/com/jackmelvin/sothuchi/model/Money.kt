package com.jackmelvin.sothuchi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "money", foreignKeys = [
    ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.RESTRICT
    ),
    ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class Money(
    @ColumnInfo(name = "date") val date: Calendar,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "memo") val memo: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)