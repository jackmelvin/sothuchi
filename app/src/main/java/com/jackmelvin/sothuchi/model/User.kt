package com.jackmelvin.sothuchi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "name") val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)
