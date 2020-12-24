package com.larrystudio.myledger.mvvm.main.month

import androidx.lifecycle.MutableLiveData
import com.larrystudio.myledger.beans.BalanceBean
import com.larrystudio.myledger.beans.CategoryRecordsBean
import com.larrystudio.myledger.beans.CategorySummaryBean
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.LogUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class MonthLedgerViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {


    val ldYearMonth = MutableLiveData<Pair<Int, Int>>()
    val ldCategoryRecordsBean = MutableLiveData<CategoryRecordsBean?>()
    val ldCategorySummaryBean = MutableLiveData<List<CategorySummaryBean>>()
    val ldBalanceBean = MutableLiveData<BalanceBean>()

    private lateinit var selectedDate: Calendar
    private val categories = ArrayList<Category>()
    private val records = ArrayList<Record>()




    override fun onCreateLifeCycle() {
        selectedDate = Calendar.getInstance()
        ldYearMonth.value = Pair(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH)+1)
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
        val newMonth =
            if(newYear == selectedDate.get(Calendar.YEAR)) selectedDate.get(Calendar.MONTH)+1
            else -1
        ldYearMonth.value = Pair(newYear, newMonth)
    }

    fun onYearMonthSelected(year: Int, month: Int) {
        ldYearMonth.value = Pair(year, month)
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month-1)
        loadLedgersAndShow(selectedDate.time)
    }

    fun onCategoryClicked(category: Category) {
        val recordByCategory = records.filter { it.categoryID == category.id }
        if(recordByCategory.isNullOrEmpty()){
            ldToast.value = "指定的分類沒有任何紀錄"
        }else {
            ldCategoryRecordsBean.value = CategoryRecordsBean(category, recordByCategory)
        }
    }

    private fun loadLedgersAndShow(date: Date){
        records.clear()
        records.addAll(ledgerManager.loadRecordsByMonth(date))

        ldCategoryRecordsBean.value = null
        setBalance(records)
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
                val recordsByCategory = records.filter { it.categoryID == category.id }
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