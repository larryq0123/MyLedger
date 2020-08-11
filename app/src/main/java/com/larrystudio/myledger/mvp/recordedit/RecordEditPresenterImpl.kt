package com.larrystudio.myledger.mvp.recordedit

import com.larrystudio.myledger.customview.WheelDatePicker
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvp.BaseMvpView
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import com.larrystudio.myledger.util.LogUtil
import java.util.*


class RecordEditPresenterImpl(private val ledgerManager: LedgerManager): RecordEditPresenter{

    private var expType = Category.TYPE_EXP
    private lateinit var categories: List<Category>
    private var record: Record? = null
    private var mvpView: RecordEditView? = null

    override fun onAttach(mvpView: BaseMvpView) {
        this.mvpView = mvpView as RecordEditView
    }

    override fun initLedger(dateString: String?, record: Record?) {
        if(dateString != null){
            mvpView!!.setDate(dateString)
        }


        if(record == null){
            setCategories(expType)
        }else{
            this.record = record
            this.expType = record.category!!.type
            mvpView!!.setDate(DateHelper.formatDate(Date(record.createTimestamp)))
            mvpView!!.setAmount(record.amount.toString())
            mvpView!!.setComment(record.comment)
            mvpView!!.changeExpType(expType)
            setCategories(expType)
            val position = categories.indexOfFirst { it.id == record.categoryID }
            mvpView!!.setSpinnerPosition(position)
        }
    }

    override fun onDateClicked() {
        mvpView!!.showDatePicker(object : WheelDatePicker.OnDateSelectedListener{
            override fun onDateSelected(dateString: String) {
                mvpView!!.setDate(dateString)
            }
        })
    }

    override fun onChangeCategoryClicked() {
        expType = if(expType == Category.TYPE_EXP){
            Category.TYPE_INCOME
        }else{
            Category.TYPE_EXP
        }
        mvpView!!.changeExpType(expType)
        setCategories(expType)
    }

    override fun onSaveClicked() {
        saveRecord(true)
    }

    override fun onOneMoreClicked() {
        saveRecord(false)
    }

    private fun setCategories(expType: Int){
        val categories = ledgerManager.getAllCategories().filter { it.type == expType }

        if(categories.isNullOrEmpty()){
            throw RuntimeException("Category list is null or empty!!!")
        }

        this.categories = categories
        mvpView!!.setSpinnerItems(categories.map { it.name })
    }

    private fun saveRecord(isFinishedAfterAction: Boolean){
        if(mvpView!!.getAmount().isEmpty() || mvpView!!.getAmount() == "0"){
            mvpView!!.showSimpleToast("請填寫金額")
            return
        }

        val dateString = mvpView!!.getDateString()
        val record = Record()
        record.categoryID = categories[mvpView!!.getSelectedCategoryPosition()].id!!
        record.amount = mvpView!!.getAmount().toInt()
        record.comment = mvpView!!.getComment()

        if(this.record == null){
            ledgerManager.insertRecord(record, dateString)
            mvpView!!.showSimpleToast("儲存成功")
        }else{
            record.id = this.record!!.id
            ledgerManager.updateRecord(record, dateString)
            mvpView!!.showSimpleToast("修改成功")
        }

        mvpView!!.clearEntries()
        if(isFinishedAfterAction){
            mvpView!!.finishView()
        }
    }
}