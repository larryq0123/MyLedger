package com.larrystudio.myledger.mvvm.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.larry.larrylibrary.extension.showToast
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvvm.BaseMVVMActivity
import com.larrystudio.myledger.mvvm.BaseMVVMFragment
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.mvvm.main.day.DayLedgerMVVMFragment
import com.larrystudio.myledger.mvvm.main.month.MonthLedgerMVVMFragment
import com.larrystudio.myledger.mvvm.main.year.YearLedgerMVVMFragment
import kotlinx.android.synthetic.main.activity_main_mvvm.*

class MainMVVMActivity : BaseMVVMActivity() {

    private lateinit var currentFragment: BaseMVVMFragment
    private val dayFragment by lazy { DayLedgerMVVMFragment() }
    private val monthFragment by lazy { MonthLedgerMVVMFragment() }
    private val yearFragment by lazy { YearLedgerMVVMFragment() }

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
            when (it.itemId) {
                R.id.action_day_ledger -> {
                    openDayLedger()
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_month_ledger -> {
                    openMonthLedger()
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_year_ledger -> {
                    openYearLedger()
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_category -> {
                    showToast("open Category")
                    return@setNavigationItemSelectedListener true
                }
            }

            return@setNavigationItemSelectedListener false
        }
    }

    private fun openDayLedger(){
        supportActionBar?.title = "每日記帳"
        currentFragment = dayFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContent, dayFragment)
            .commit()
    }

    private fun openMonthLedger(){
        supportActionBar?.title = "每月統計"
        currentFragment = monthFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContent, monthFragment)
            .commit()
    }

    private fun openYearLedger(){
        supportActionBar?.title = "年度總結"
        currentFragment = yearFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContent, yearFragment)
            .commit()
    }

    override fun onBackPressed() {
        if(!currentFragment.onBackPressed()){
            super.onBackPressed()
        }
    }
}