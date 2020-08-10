package com.larrystudio.myledger.mvp.main

import com.larrystudio.myledger.mvp.BaseMvpView


interface MainView: BaseMvpView {

    fun isMenuShown(): Boolean
    fun showMenu(toShow: Boolean)
    fun showDayLedgerView()
    fun showMonthLedgerView()
    fun showYearLedgerView()
}