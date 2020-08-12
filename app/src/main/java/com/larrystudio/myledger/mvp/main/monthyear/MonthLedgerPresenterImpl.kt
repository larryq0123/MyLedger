package com.larrystudio.myledger.mvp.main.monthyear

import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import java.util.*
import kotlin.collections.ArrayList

class MonthLedgerPresenterImpl(private val ledgerManager: LedgerManager):
    MonthYearLedgerPresenter {

    private var mvpView: MonthYearLedgerView? = null
    private lateinit var selectedDate: Calendar
    private val categories = ArrayList<Category>()
    private val records = ArrayList<Record>()

    override fun onAttach(mvpView: BaseMvpView) {
        this.mvpView = mvpView as MonthYearLedgerView
        selectedDate = Calendar.getInstance()
        mvpView.selectYearMonth(selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH)+1)
    }

    override fun onLifeStarted() {
        categories.addAll(ledgerManager.getAllCategories())
        loadLedgersAndShow(selectedDate.time)
    }

    override fun onYearChangeClicked(oldYear: String, isIncrement: Boolean) {
        val newYear = if(isIncrement) oldYear.toInt()+1 else oldYear.toInt()-1
        if(newYear == selectedDate.get(Calendar.YEAR)){
            mvpView!!.selectYearMonth(newYear, selectedDate.get(Calendar.MONTH)+1)
        }else{
            mvpView!!.selectYearMonth(newYear, -1)
        }
    }

    override fun onYearMonthSelected(year: Int, month: Int) {
        mvpView!!.selectYearMonth(year, month)
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month-1)
        loadLedgersAndShow(selectedDate.time)
    }

    override fun onCategoryClicked(category: Category) {
        val recordByCategory = records.filter { it.categoryID == category.id }
        if(recordByCategory.isNullOrEmpty()){
            mvpView!!.showSimpleToast("指定的分類沒有任何紀錄")
        }else {
            mvpView!!.showCategoryDetails(category, recordByCategory)
        }
    }

    private fun loadLedgersAndShow(date: Date){
        records.clear()
        records.addAll(ledgerManager.loadRecordsByMonth(date))
        setBalance(records)
        mvpView!!.hideCategoryDetails()
        categories.forEach { category ->
            val recordsByCategory = records.filter { it.categoryID == category.id }
            val count = recordsByCategory.count()
            val totalAmount = recordsByCategory.sumBy { it.amount }
            mvpView!!.showCategorySummary(category, count, totalAmount)
        }
    }

    private fun setBalance(records: List<Record>){
        var income = 0
        var exp = 0
        records.forEach {
            if(!it.category!!.isExpenditure()){
                income += it.amount
            }else{
                exp += it.amount
            }
        }
        val balance = income - exp
        mvpView!!.showBalance(income, exp, balance)
    }

    override fun onBackPressed(): Boolean {
        return if(mvpView!!.isCategoryDetailsShown()){
            mvpView!!.hideCategoryDetails()
            true
        }else{
            false
        }
    }
}