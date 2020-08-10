package com.larrystudio.myledger.mvp.main.day

import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.room.Record
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator

interface DayLedgerView: BaseMvpView {
    fun markToday(shouldMark: (CalendarDay) -> Boolean)
    fun showLedgers(ledgers: List<Record>)
    fun showBalance(amount: Int)
    fun openLedgerEditView(dateString: String?, ledger: Record?)
}