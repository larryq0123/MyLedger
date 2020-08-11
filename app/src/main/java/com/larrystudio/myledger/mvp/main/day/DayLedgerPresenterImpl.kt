package com.larrystudio.myledger.mvp.main.day

import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*
import kotlin.collections.ArrayList

class DayLedgerPresenterImpl(private val ledgerManager: LedgerManager):
    DayLedgerPresenter{

    private var mvpView: DayLedgerView? = null

    private lateinit var categories: List<Category>
    private val records = ArrayList<Record>()
    private lateinit var selectedDate: Date


    override fun onAttach(mvpView: BaseMvpView) {
        this.mvpView = mvpView as DayLedgerView
        categories = ledgerManager.getAllCategories()
        selectedDate = Date()
        markToday(selectedDate)
        showRecords(selectedDate)
    }

    override fun onLifeResumed() {
        showRecords(selectedDate)
    }

    override fun onDateSelected(date: Date) {
        selectedDate = date
        showRecords(date)
    }

    override fun onRecordClicked(position: Int) {
        mvpView!!.openRecordEditView(null, records[position])
    }

    override fun onAddClicked() {
        val dateString = DateHelper.formatDate(selectedDate)
        mvpView!!.openRecordEditView(dateString, null)
    }

    private fun markToday(date: Date){
        mvpView!!.markToday{ c: CalendarDay ->
            return@markToday DateHelper.areDatesOnTheSameDay(c.date, date)
        }
    }

    private fun showRecords(date: Date){
        records.clear()
        records.addAll(ledgerManager.loadRecordsByDate(date))
        mvpView!!.showRecords(records)
        mvpView!!.showBalance(records.sumBy {
            if(!it.category!!.isExpenditure()){
                it.amount
            }else{
                -it.amount
            }
        })
    }
}