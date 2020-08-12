package com.larrystudio.myledger.mvp.main.monthyear

import com.larrystudio.myledger.mvp.BaseMvpPresenter
import com.larrystudio.myledger.room.Category


interface MonthYearLedgerPresenter: BaseMvpPresenter{
    fun onYearChangeClicked(oldYear: String, isIncrement: Boolean)
    fun onYearMonthSelected(year: Int, month: Int)
    fun onCategoryClicked(category: Category)

    fun onLifeStarted()
    fun onBackPressed(): Boolean
}