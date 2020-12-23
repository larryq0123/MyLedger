package com.larrystudio.myledger.mvp.recordedit

import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.larry.larrylibrary.util.GlobalUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.customview.WheelDatePicker
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvp.BaseMVPActivity
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.LogUtil
import kotlinx.android.synthetic.main.activity_record_edit.*

class RecordEditMVPActivity : BaseMVPActivity(), RecordEditView {


    private lateinit var presenter: RecordEditPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_edit)

        val lm = ManagerFactory.getInstance(this).getLedgerManager()
        presenter = RecordEditPresenterImpl(lm)
        presenter.onAttach(this)

        val dateString = intent.getStringExtra(RecordEditView.DATA_DATE)
        val recordJson = intent.getStringExtra(RecordEditView.DATA_LEDGER)
        val record = if(recordJson == null) null
                    else Gson().fromJson(recordJson, Record::class.java)
        presenter.initLedger(dateString, record)
        initListeners()
    }

    private fun initListeners(){
        textDate.setOnClickListener { presenter.onDateClicked() }
        imageChangeType.setOnClickListener { presenter.onChangeCategoryClicked() }
        buttonSave.setOnClickListener { presenter.onSaveClicked() }
        buttonOneMore.setOnClickListener { presenter.onOneMoreClicked() }
    }

    override fun editDayLedger() {}

    override fun editMonthLedger() {}

    override fun setSpinnerItems(items: List<String>) {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, items)
        spinnerCategory.adapter = adapter
    }

    override fun setSpinnerPosition(position: Int) {
        LogUtil.logd(TAG, "setSpinnerPosition(), position = $position")
        spinnerCategory.setSelection(position)
    }

    override fun getDateString(): String {
        return textDate.text.toString()
    }

    override fun getSelectedCategoryPosition(): Int {
        return spinnerCategory.selectedItemPosition
    }

    override fun getAmount(): String {
        return editAmount.text.toString()
    }

    override fun setAmount(amount: String) {
        editAmount.setText(amount)
    }

    override fun getComment(): String {
        return editComment.text.toString()
    }

    override fun setComment(comment: String) {
        editComment.setText(comment)
    }

    override fun showDatePicker(listener: WheelDatePicker.OnDateSelectedListener) {
        val dialog = WheelDatePicker(this, true)
        dialog.listener = listener
        dialog.show()
    }

    override fun setDate(dateString: String) {
        textDate.text = dateString
    }

    override fun changeExpType(type: Int) {
        if(type == Category.TYPE_EXP){
            textExpType.text = "支出"
            textExpType.setTextColor(GlobalUtil.getColor(this, R.color.red))
        }else{
            textExpType.text = "收入"
            textExpType.setTextColor(GlobalUtil.getColor(this, R.color.normalGreen))
        }
    }

    override fun clearEntries() {
        editAmount.setText("")
        editComment.setText("")
    }

    override fun finishView() { finish() }
}