package com.larrystudio.myledger.mvvm.recordedit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.larry.larrylibrary.util.GlobalUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.beans.TempRecord
import com.larrystudio.myledger.customview.WheelDatePicker
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvp.recordedit.RecordEditPresenter
import com.larrystudio.myledger.mvp.recordedit.RecordEditPresenterImpl
import com.larrystudio.myledger.mvp.recordedit.RecordEditView
import com.larrystudio.myledger.mvvm.BaseMVVMActivity
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.LogUtil
import kotlinx.android.synthetic.main.activity_record_edit.*

class RecordEditMVVMActivity : BaseMVVMActivity() {

    private lateinit var viewModel: RecordEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_edit_mvvm)

        initViewModel()
        initListeners()
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(RecordEditViewModel::class.java)
        doBasicSubscription(viewModel)

        val dateString = intent.getStringExtra(RecordEditView.DATA_DATE)
        val jsonRecord = intent.getStringExtra(RecordEditView.DATA_LEDGER)
        viewModel.initLedger(dateString, jsonRecord)
        viewModel.ldDate.observe(this, Observer { textDate.text = it })
        viewModel.ldCategories.observe(this, Observer { categories ->
            setSpinnerItems(categories.map { it.name })
        })
        viewModel.ldSpinnerPosition.observe(this, Observer { spinnerCategory.setSelection(it) })
        viewModel.ldAmount.observe(this, Observer { editAmount.setText(it) })
        viewModel.ldComment.observe(this, Observer { editComment.setText(it) })
        viewModel.ldExpType.observe(this, Observer { changeExpType(it) })
    }

    private fun initListeners(){
        textDate.setOnClickListener { showDatePicker() }
        imageChangeType.setOnClickListener { viewModel.onChangeCategoryClicked() }
        buttonSave.setOnClickListener {
            if(viewModel.onSaveClicked(collectTempRecord())){
                clearEntries()
                finish()
            }
        }
        buttonOneMore.setOnClickListener {
            if(viewModel.onOneMoreClicked(collectTempRecord())){
                clearEntries()
            }
        }
    }

    private fun setSpinnerItems(items: List<String>) {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, items)
        spinnerCategory.adapter = adapter
    }

    private fun collectTempRecord(): TempRecord{
        val date = textDate.text.toString()
        val position = spinnerCategory.selectedItemPosition
        val amount = editAmount.text.toString()
        val comment = editComment.text.toString()
        return TempRecord(date, position, amount, comment)
    }

    private fun showDatePicker() {
        val dialog = WheelDatePicker(this, true)
        dialog.listener = object : WheelDatePicker.OnDateSelectedListener{
            override fun onDateSelected(dateString: String) { viewModel.ldDate.value = dateString }
        }
        dialog.show()
    }

    private fun changeExpType(type: Int) {
        if(type == Category.TYPE_EXP){
            textExpType.text = "支出"
            textExpType.setTextColor(GlobalUtil.getColor(this, R.color.red))
        }else{
            textExpType.text = "收入"
            textExpType.setTextColor(GlobalUtil.getColor(this, R.color.normalGreen))
        }
    }

    fun clearEntries() {
        editAmount.setText("")
        editComment.setText("")
    }
}