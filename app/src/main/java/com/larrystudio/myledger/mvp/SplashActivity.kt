package com.larrystudio.myledger.mvp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.larrystudio.myledger.R
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvp.main.MainActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val wait = Single.timer(1500, TimeUnit.MILLISECONDS)
        val init = Single.timer(500, TimeUnit.MILLISECONDS).map {
            val lm = ManagerFactory.getInstance(this).getLedgerManager()
            lm.insertBasicCategories()
            return@map 1L
        }

        Single.zip(wait, init, BiFunction<Long, Long, Long> { p0, p1 -> p0 })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _: Long ->
                goPage(MainActivity::class.java, true)
            }
    }
}