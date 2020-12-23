package com.larrystudio.myledger.mvvm.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.larrystudio.myledger.beans.EditRecordEvent
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.mvp.main.day.DayLedgerView
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {




}