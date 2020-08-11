package com.larrystudio.myledger.mvp.main.day

import com.larrystudio.myledger.mvp.BaseMvpPresenter
import java.util.*

interface DayLedgerPresenter: BaseMvpPresenter {

    fun onLifeResumed()
    fun onDateSelected(date: Date)
    fun onRecordClicked(position: Int)
    fun onAddClicked()

}