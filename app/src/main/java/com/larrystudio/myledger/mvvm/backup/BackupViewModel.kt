package com.larrystudio.myledger.mvvm.backup

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.util.LogUtil
import kotlinx.coroutines.*

class BackupViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {

    override fun onStartLifeCycle() {
        viewModelScope.launch {
            LogUtil.logd(TAG, "thread name = ${Thread.currentThread().name}")
            delay(1500)
            LogUtil.logd(TAG, "after delay, thread name = ${Thread.currentThread().name}")
            newSingleThreadContext("test").use { testContext ->
                withContext(testContext){
                    delay(200)
                    LogUtil.logd(TAG, "in newSingleThreadContext, thread name = ${Thread.currentThread().name}")
                }
            }
            LogUtil.logd(TAG, "after newSingleThreadContext, thread name = ${Thread.currentThread().name}")
        }
    }

    fun onBackupClicked(){
        val backupBean = ledgerManager.backUpAll()
        LogUtil.logd(TAG, "backupBean = ${Gson().toJson(backupBean)}")
    }

}