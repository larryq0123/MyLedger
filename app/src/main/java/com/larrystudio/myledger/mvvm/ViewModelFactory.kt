package com.larrystudio.myledger.mvvm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.larrystudio.myledger.LedgerApp
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvvm.main.day.DayLedgerViewModel
import com.larrystudio.myledger.mvvm.main.MainViewModel
import com.larrystudio.myledger.mvvm.main.month.MonthLedgerViewModel
import com.larrystudio.myledger.mvvm.main.year.YearLedgerViewModel
import com.larrystudio.myledger.mvvm.splash.SplashViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory: ViewModelProvider.Factory {

    private fun getContext(): Context = LedgerApp.appContext

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                val ledgerManager = getLedgerManager()
                return SplashViewModel(ledgerManager) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                val ledgerManager = getLedgerManager()
                return MainViewModel(ledgerManager) as T
            }
            modelClass.isAssignableFrom(DayLedgerViewModel::class.java) ->{
                val ledgerManager = getLedgerManager()
                return DayLedgerViewModel(ledgerManager) as T
            }
            modelClass.isAssignableFrom(MonthLedgerViewModel::class.java) ->{
                val ledgerManager = getLedgerManager()
                return MonthLedgerViewModel(ledgerManager) as T
            }
            modelClass.isAssignableFrom(YearLedgerViewModel::class.java) ->{
                val ledgerManager = getLedgerManager()
                return YearLedgerViewModel(ledgerManager) as T
            }
        }

        throw IllegalArgumentException("Unknown ViewModel class.")
    }


    private fun getLedgerManager(): LedgerManager{
        return ManagerFactory.getInstance(getContext()).getLedgerManager()
    }
}