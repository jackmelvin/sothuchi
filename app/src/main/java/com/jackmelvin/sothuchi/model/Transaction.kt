package com.jackmelvin.sothuchi.model

import java.util.Date

data class Transaction(
    val date: Date,
    val amount: Int,
    val category: Category?,
    val memo: String
)
