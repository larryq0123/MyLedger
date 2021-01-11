package com.larrystudio.myledger.mvvm.backup

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.larrystudio.myledger.manager.BackupBean
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class BackupViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {

    fun onBackupClicked(){
        val backupBean = ledgerManager.backUpAll()
        LogUtil.logd(TAG, "backupBean = ${Gson().toJson(backupBean)}")
    }

    fun onRestoreClicked(json: String){
        val bean = Gson().fromJson(json, BackupBean::class.java)
        ledgerManager.restore(bean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { showShortToast("還原完成") }
    }
}