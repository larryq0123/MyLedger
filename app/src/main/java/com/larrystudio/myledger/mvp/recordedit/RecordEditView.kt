package com.larrystudio.myledger.mvp.recordedit

import com.larrystudio.myledger.customview.WheelDatePicker
import com.larrystudio.myledger.mvp.BaseMvpView

interface RecordEditView: BaseMvpView {

    companion object {
        const val DATA_DATE = "date"
        const val DATA_LEDGER = "ledger"
    }

    fun editDayLedger()
    fun editMonthLedger()
    fun changeExpType(type: Int)
    fun setSpinnerItems(items: List<String>)
    fun setSpinnerPosition(position: Int)
    fun getDateString(): String
    fun setDate(dateString: String)
    fun getSelectedCategoryPosition(): Int
    fun getAmount(): String
    fun setAmount(amount: String)
    fun getComment(): String
    fun setComment(comment: String)
    fun showDatePicker(listener: WheelDatePicker.OnDateSelectedListener)
    fun clearEntries()
    fun finishView()
}