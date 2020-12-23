package com.larrystudio.myledger.mvvm.main.day

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.larry.larrylibrary.util.GlobalUtil
import com.larry.larrylibrary.util.MeasureUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvp.recordedit.RecordEditMVPActivity
import com.larrystudio.myledger.mvp.recordedit.RecordEditView
import com.larrystudio.myledger.mvvm.BaseMVVMFragment
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.android.synthetic.main.fragment_day_ledger.*
import java.util.*

class DayLedgerMVVMFragment: BaseMVVMFragment() {

    private lateinit var viewModel: DayLedgerViewModel
    private val drawableToday: Drawable by lazy {
        ContextCompat.getDrawable(activity!!, R.drawable.bg_today)!!
    }

    private val ledgerClickListener by lazy {
        View.OnClickListener{
            val position = linearLedger.indexOfChild(it) - 2
            viewModel.onRecordClicked(position)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_ledger, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        markToday()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResumeLifeCycle()
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(activity!!, factory).get(DayLedgerViewModel::class.java)
        doBasicSubscription(viewModel)
        viewModel.ldNavigate.observe(viewLifecycleOwner, Observer { openRecordEditView(it.date, it.record) })
        viewModel.ldRecords.observe(viewLifecycleOwner, Observer { showRecords(it) })
        viewModel.ldBalance.observe(viewLifecycleOwner, Observer { showBalance(it) })
    }

    private fun markToday() {
        val today = Date()
        calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(c: CalendarDay?): Boolean {
                return c != null && DateHelper.areDatesOnTheSameDay(c.date, today)
            }
            override fun decorate(facade: DayViewFacade?) {
                facade?.setBackgroundDrawable(drawableToday)
            }
        })
    }

    private fun initListeners(){
        floatingAdd.setOnClickListener {
            viewModel.onAddClicked()
        }

        calendarView.setOnDateChangedListener { calendarView, date, b ->
            val dateString = "${date.year}-${date.month+1}-${date.day}"
            viewModel.onDateSelected(DateHelper.parseDateString(dateString))
        }
    }

    fun showRecords(records: List<Record>){
        if(linearLedger.childCount > 2){
            linearLedger.removeViews(2, linearLedger.childCount-2)
        }

        val margin = MeasureUtil.getDimensionFromRid(activity, R.dimen.small_padding)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(margin, margin, margin, margin)

        records.forEach {
            val ledgerView = View.inflate(activity, R.layout.view_ledger_item, null)
            ledgerView.layoutParams = lp
            val textCategory = ledgerView.findViewById<TextView>(R.id.textCategory)
            textCategory.text = it.category!!.name
            val textComment = ledgerView.findViewById<TextView>(R.id.textComment)
            textComment.text = it.comment
            val textAmount = ledgerView.findViewById<TextView>(R.id.textAmount)
            textAmount.text = it.amount.toString()
            if(!it.category!!.isExpenditure()){
                textAmount.setTextColor(GlobalUtil.getColor(activity, R.color.colorIncome))
            }

            linearLedger.addView(ledgerView)
            ledgerView.setOnClickListener(ledgerClickListener)
        }
    }

    fun showBalance(amount: Int) {
        textBalance.text = String.format(getString(R.string.today_balance), amount)
        val textColor = if(amount >= 0)
            GlobalUtil.getColor(activity, R.color.colorIncome)
        else GlobalUtil.getColor(activity, R.color.colorExpenditure)
        textBalance.setTextColor(textColor)
    }

    private fun openRecordEditView(dateString: String?, record: Record?) {
        val intent = Intent(activity, RecordEditMVPActivity::class.java)

        if(dateString != null){
            intent.putExtra(RecordEditView.DATA_DATE, dateString)
        }

        if(record != null){
            intent.putExtra(RecordEditView.DATA_LEDGER, Gson().toJson(record))
        }

        startActivity(intent)
    }
}