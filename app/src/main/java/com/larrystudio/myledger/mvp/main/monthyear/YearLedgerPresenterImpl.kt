package com.larrystudio.myledger.mvp.main.monthyear

import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import java.util.*

class YearLedgerPresenterImpl(private val ledgerManager: LedgerManager):
    MonthYearLedgerPresenter {

    private var mvpView: MonthYearLedgerView? = null
    private lateinit var selectedDate: Calendar
    private val categories = ArrayList<Category>()
    private val ledgers = ArrayList<Record>()

    override fun onAttach(mvpView: BaseMvpView) {
        this.mvpView = mvpView as MonthYearLedgerView
        selectedDate = Calendar.getInstance()
        mvpView.selectYear(selectedDate.get(Calendar.YEAR))
        mvpView.hideMonthArea()
    }

    override fun onLifeStarted() {
        categories.addAll(ledgerManager.getAllCategories())
        loadLedgersAndShow(selectedDate.time)
    }

    override fun onYearChangeClicked(oldYear: String, isIncrement: Boolean) {
        val newYear = if(isIncrement) oldYear.toInt()+1 else oldYear.toInt()-1
        mvpView!!.selectYear(newYear)
        selectedDate.set(Calendar.YEAR, newYear)
        loadLedgersAndShow(selectedDate.time)
    }

    override fun onYearMonthSelected(year: Int, month: Int) {}

    override fun onCategoryClicked(category: Category) {
        val ledgerByCategory = ledgers.filter { it.categoryID == category.id }
        if(ledgerByCategory.isNullOrEmpty()){
            mvpView!!.showSimpleToast("指定的分類沒有任何紀錄")
        }else {
            mvpView!!.showCategoryDetails(category, ledgerByCategory)
        }
    }

    private fun loadLedgersAndShow(date: Date){
        ledgers.clear()
        ledgers.addAll(ledgerManager.loadRecordsByYear(date))
        setBalance(ledgers)
        mvpView!!.hideCategoryDetails()
        categories.forEach { category ->
            val ledgersByCategory = ledgers.filter { it.categoryID == category.id }
            val count = ledgersByCategory.count()
            val totalAmount = ledgersByCategory.sumBy { it.amount }
            mvpView!!.showCategorySummary(category, count, totalAmount)
        }
    }

    private fun setBalance(ledgers: List<Record>){
        var income = 0
        var exp = 0
        ledgers.forEach {
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