package com.larrystudio.myledger

import android.app.Application
import android.content.Context

class LedgerApp: Application() {

    companion object {
        const val APP_NAME = "MyLedger"
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}