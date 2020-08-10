package com.larrystudio.myledger.mvp.main.day

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.larrystudio.myledger.R
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*
import kotlin.collections.ArrayList

class DayLedgerPresenterImpl(private val ledgerManager: LedgerManager):
    DayLedgerPresenter{

    private var mvpView: DayLedgerView? = null

    private lateinit var categories: List<Category>
    private val ledgers = ArrayList<Record>()
    private lateinit var selectedDate: Date


    override fun onAttach(mvpView: BaseMvpView) {
        this.mvpView = mvpView as DayLedgerView
        categories = ledgerManager.getAllCategories()
        selectedDate = Date()
        markToday(selectedDate)
        showLedgers(selectedDate)
    }

    override fun onDateSelected(date: Date) {
        selectedDate = date
        showLedgers(date)
    }

    override fun onLedgerClicked(position: Int) {
        mvpView!!.openLedgerEditView(null, ledgers[position])
    }

    override fun onAddClicked() {
        val dateString = DateHelper.formatDate(selectedDate)
        mvpView!!.openLedgerEditView(dateString, null)
    }

    private fun markToday(date: Date){
        mvpView!!.markToday{ c: CalendarDay ->
            return@markToday DateHelper.areDatesOnTheSameDay(c.date, date)
        }
    }

    private fun showLedgers(date: Date){
        ledgers.clear()
        ledgers.addAll(ledgerManager.loadRecordsByDate(date))
        mvpView!!.showLedgers(ledgers)
        mvpView!!.showBalance(ledgers.sumBy {
            if(!it.category!!.isExpenditure()){
                it.amount
            }else{
                -it.amount
            }
        })
    }
}