package com.larrystudio.myledger.mvvm.splash

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.mvvm.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class SplashViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {

    val ldNavigate = SingleLiveEvent<Int>()

    override fun onCreateLifeCycle() {
        val wait = Single.timer(1500, TimeUnit.MILLISECONDS)
        val init = Single.timer(500, TimeUnit.MILLISECONDS).map {
            ledgerManager.insertBasicCategories()
            return@map 1L
        }

        Single.zip(wait, init, BiFunction<Long, Long, Long> { p0, p1 -> p0 })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _: Long -> ldNavigate.value = 1 }
    }
}