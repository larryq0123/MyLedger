package com.larrystudio.myledger.mvp.main.monthyear

import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record

interface MonthYearLedgerView: BaseMvpView {

    fun hideMonthArea()
    fun selectYear(year: Int)
    fun selectYearMonth(year: Int, month: Int)
    fun showBalance(income: Int , expenditure: Int, balance: Int)
    fun showCategorySummary(category: Category, recordCount: Int, totalAmount: Int)
    fun isCategoryDetailsShown(): Boolean
    fun showCategoryDetails(category: Category, ledgers: List<Record>)
    fun hideCategoryDetails()
    fun openLedgerEditView(dateString: String?, ledger: Record?)
}