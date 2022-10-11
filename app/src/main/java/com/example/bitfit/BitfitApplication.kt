package com.example.bitfit

import android.app.Application

class BitfitApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}