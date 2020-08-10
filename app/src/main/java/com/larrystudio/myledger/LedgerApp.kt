package com.larrystudio.myledger

import android.app.Application
import android.content.Context

class LedgerApp: Application() {

    companion object {
        const val APP_NAME = "MyLedger"
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}