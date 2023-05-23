package com.jackmelvin.sothuchi

import android.app.Application
import com.jackmelvin.sothuchi.database.AppDatabase

class SoThuChiApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}