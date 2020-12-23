package com.larrystudio.myledger.mvvm.main.year

import androidx.lifecycle.MutableLiveData
import com.larrystudio.myledger.beans.BalanceBean
import com.larrystudio.myledger.beans.CategoryRecordsBean
import com.larrystudio.myledger.beans.CategorySummaryBean
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.mvp.main.monthyear.MonthYearLedgerView
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class YearLedgerViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {

    val ldYear = MutableLiveData<Int>()
    val ldCategoryRecordsBean = MutableLiveData<CategoryRecordsBean?>()
    val ldCategorySummaryBean = MutableLiveData<List<CategorySummaryBean>>()
    val ldBalanceBean = MutableLiveData<BalanceBean>()

    private lateinit var selectedDate: Calendar
    private val categories = ArrayList<Category>()
    private val ledgers = ArrayList<Record>()

    override fun onCreateLifeCycle() {
        selectedDate = Calendar.getInstance()
        ldYear.value = selectedDate.get(Calendar.YEAR)
    }

    override fun onStartLifeCycle() {
        categories.addAll(ledgerManager.getAllCategories())
        loadLedgersAndShow(selectedDate.time)
    }

    override fun onBackPressed(): Boolean {
        return if(ldCategoryRecordsBean.value != null){
            ldCategoryRecordsBean.value = null
            true
        }else{
            false
        }
    }

    fun onYearChangeClicked(oldYear: String, isIncrement: Boolean) {
        val newYear = if(isIncrement) oldYear.toInt()+1 else oldYear.toInt()-1
        ldYear.value = newYear
        selectedDate.set(Calendar.YEAR, newYear)
        loadLedgersAndShow(selectedDate.time)
    }

    fun onCategoryClicked(category: Category) {
        val ledgerByCategory = ledgers.filter { it.categoryID == category.id }
        if(ledgerByCategory.isNullOrEmpty()){
            ldToast.value = "指定的分類沒有任何紀錄"
        }else {
            ldCategoryRecordsBean.value = CategoryRecordsBean(category, ledgers)
        }
    }

    private fun loadLedgersAndShow(date: Date){
        ledgers.clear()
        ledgers.addAll(ledgerManager.loadRecordsByYear(date))
        ldCategoryRecordsBean.value = null
        setBalance(ledgers)
        setSummaries()
    }

    private fun setBalance(records: List<Record>){
        Single.fromCallable {
            var income = 0
            var exp = 0
            records.forEach {
                if(it.category!!.isExpenditure()){
                    exp += it.amount
                }else{
                    income += it.amount
                }
            }
            val balance = income - exp
            return@fromCallable BalanceBean(income, exp, balance)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { bean -> ldBalanceBean.value = bean }
            .also { compositeDisposable.add(it) }
    }

    private fun setSummaries(){
        Single.fromCallable {
            return@fromCallable categories.map { category ->
                val recordsByCategory = ledgers.filter { it.categoryID == category.id }
                val count = recordsByCategory.count()
                val totalAmount = recordsByCategory.sumBy { it.amount }
                CategorySummaryBean(category, count, totalAmount)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { summaries -> ldCategorySummaryBean.value = summaries }
            .also { compositeDisposable.add(it) }
    }
}