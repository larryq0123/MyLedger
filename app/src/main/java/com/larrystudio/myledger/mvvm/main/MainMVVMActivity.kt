package com.larrystudio.myledger.mvvm.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.larry.larrylibrary.extension.showToast
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvvm.BaseMVVMActivity
import com.larrystudio.myledger.mvvm.BaseMVVMFragment
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.mvvm.category.CategoryManageActivity
import com.larrystudio.myledger.mvvm.main.day.DayLedgerMVVMFragment
import com.larrystudio.myledger.mvvm.main.month.MonthLedgerMVVMFragment
import com.larrystudio.myledger.mvvm.main.year.YearLedgerMVVMFragment
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_mvvm.*
import java.util.concurrent.TimeUnit

class MainMVVMActivity : BaseMVVMActivity() {

    private lateinit var currentFragment: BaseMVVMFragment
    private val dayFragment by lazy { DayLedgerMVVMFragment() }
    private val monthFragment by lazy { MonthLedgerMVVMFragment() }
    private val yearFragment by lazy { YearLedgerMVVMFragment() }
    private var confirmToFinish = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_mvvm)
        initViewModel()
        initDrawer()
        openDayLedger()
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        val viewModel: MainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        doBasicSubscription(viewModel)
    }

    private fun initDrawer(){
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener when (it.itemId) {
                R.id.action_day_ledger -> {
                    openDayLedger()
                    true
                }
                R.id.action_month_ledger -> {
                    openMonthLedger()
                    true
                }
                R.id.action_year_ledger -> {
                    openYearLedger()
                    true
                }
                R.id.action_category -> {
                    goPage(CategoryManageActivity::class.java, false)
                    false
                }
                else -> false
            }
        }
    }

    private fun openDayLedger(){
        supportActionBar?.title = getString(R.string.daily_ledger)
        currentFragment = dayFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContent, dayFragment)
            .commit()
    }

    private fun openMonthLedger(){
        supportActionBar?.title = getString(R.string.month_ledger)
        currentFragment = monthFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContent, monthFragment)
            .commit()
    }

    private fun openYearLedger(){
        supportActionBar?.title = getString(R.string.year_ledger)
        currentFragment = yearFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContent, yearFragment)
            .commit()
    }

    @SuppressLint("CheckResult")
    override fun onBackPressed() {
        if(!currentFragment.onBackPressed()){
            if(confirmToFinish){
                super.onBackPressed()
            }else{
                confirmToFinish = true
                showToast("再點擊一次返回以退出app")
                Completable.timer(2000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe { confirmToFinish = false }
            }
        }
    }
}