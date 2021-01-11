package com.larrystudio.myledger.mvvm.recordedit

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.larrystudio.myledger.beans.TempRecord
import com.larrystudio.myledger.customview.WheelDatePicker
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.mvvm.Event
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import java.util.*

class RecordEditViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {

    val ldDate = MutableLiveData<String>()
    val ldCategories = MutableLiveData<List<Category>>()
    val ldSpinnerPosition = MutableLiveData<Int>()
    val ldAmount = MutableLiveData<String>()
    val ldComment = MutableLiveData<String>()
    val ldExpType = MutableLiveData<Int>()

    private var record: Record? = null


    fun initLedger(date: String?, jsonRecord: String?) {
        if(!date.isNullOrEmpty()){
            ldDate.value = date
        }

        val record = jsonRecord?.let { Gson().fromJson(it, Record::class.java) }
        if(record == null){
            ldExpType.value = Category.TYPE_EXP
            setCategories(Category.TYPE_EXP)
        }else{
            this.record = record
            this.ldExpType.value = record.category!!.type
            ldDate.value = DateHelper.formatDate(Date(record.createTimestamp))
            ldAmount.value = record.amount.toString()
            ldComment.value = record.comment
            setCategories(record.category!!.type)
            val position = ldCategories.value!!.indexOfFirst { it.id == record.categoryID }
            ldSpinnerPosition.value = position
        }
    }

    fun onChangeCategoryClicked() {
        var expType = ldExpType.value
        expType = if(expType == Category.TYPE_EXP){
            Category.TYPE_INCOME
        }else{
            Category.TYPE_EXP
        }
        ldExpType.value = expType
        setCategories(expType)
    }

    fun onSaveClicked(tempRecord: TempRecord): Boolean {
        return saveRecord(tempRecord)
    }

    fun onOneMoreClicked(tempRecord: TempRecord): Boolean {
        return saveRecord(tempRecord)
    }

    private fun setCategories(expType: Int){
        val categories = ledgerManager.getAllCategories().filter { it.type == expType }
        if(categories.isNullOrEmpty()){
            throw RuntimeException("Category list is null or empty!!!")
        }

        ldCategories.value = categories
    }

    private fun saveRecord(tempRecord: TempRecord): Boolean{
        if(tempRecord.amount.isEmpty() || tempRecord.amount == "0"){
            showShortToast("請填寫金額")
            return false
        }

        val record = Record()
        record.categoryID = ldCategories.value!![tempRecord.categoryPosition].id!!
        record.amount = tempRecord.amount.toInt()
        record.comment = tempRecord.comment

        if(this.record == null){
            ledgerManager.insertRecord(record, tempRecord.date)
            showShortToast("儲存成功")
        }else{
            record.id = this.record!!.id
            ledgerManager.updateRecord(record, tempRecord.date)
            showShortToast("修改成功")
        }

        return true
    }
}