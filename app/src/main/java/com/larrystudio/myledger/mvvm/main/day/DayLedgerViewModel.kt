package com.larrystudio.myledger.mvvm.main.day

import androidx.lifecycle.MutableLiveData
import com.larrystudio.myledger.beans.EditRecordEvent
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import java.util.*

class DayLedgerViewModel(private val ledgerManager: LedgerManager): BaseViewModel()  {


    private lateinit var categories: List<Category>
    private var selectedDate: Date = Date()

    val ldNavigate = MutableLiveData<EditRecordEvent>()
    val ldRecords = MutableLiveData<List<Record>>()
    val ldBalance = MutableLiveData<Int>()


    override fun onCreateLifeCycle() {
        super.onCreateLifeCycle()
        categories = ledgerManager.getAllCategories()
    }

    override fun onResumeLifeCycle() {
        super.onResumeLifeCycle()
        showRecords(selectedDate)
    }

    fun onDateSelected(date: Date) {
        selectedDate = date
        showRecords(date)
    }

    fun onRecordClicked(position: Int) {
        ldNavigate.value = EditRecordEvent(null, ldRecords.value!![position])
    }

    fun onAddClicked() {
        val date = DateHelper.formatDate(selectedDate)
        ldNavigate.value = EditRecordEvent(date, null)
    }

    private fun showRecords(date: Date){
        val records = ledgerManager.loadRecordsByDate(date)
        ldRecords.value = records

        val balance = records.sumBy {
            if(!it.category!!.isExpenditure()){
                it.amount
            }else{
                -it.amount
            }
        }
        ldBalance.value = balance
    }



}