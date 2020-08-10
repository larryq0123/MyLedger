package com.larrystudio.myledger.mvp.main.day

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.larry.larrylibrary.util.GlobalUtil
import com.larry.larrylibrary.util.MeasureUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvp.BaseFragment
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.android.synthetic.main.fragment_day_ledger.*

class DayLedgerFragment: BaseFragment(), DayLedgerView {

    private val drawableToday: Drawable by lazy {
        ContextCompat.getDrawable(activity!!, R.drawable.bg_today)!!
    }

    lateinit var presenter: DayLedgerPresenter

    private val ledgerClickListener by lazy {
        View.OnClickListener{
            val position = linearLedger.indexOfChild(it) - 2
            presenter.onLedgerClicked(position)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_ledger, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = DayLedgerPresenterImpl(
            ManagerFactory.getInstance(activity).getLedgerManager())
        presenter.onAttach(this)
        initListeners()
    }

    private fun initListeners(){
        floatingAdd.setOnClickListener {
            presenter.onAddClicked()
        }

        calendarView.setOnDateChangedListener { calendarView, date, b ->
            val dateString = "${date.year}-${date.month+1}-${date.day}"
            presenter.onDateSelected(DateHelper.parseDateString(dateString))
        }
    }

    override fun markToday(shouldMark: (CalendarDay) -> Boolean) {
        calendarView.addDecorator(object : DayViewDecorator{
            override fun shouldDecorate(c: CalendarDay?): Boolean {
                return c != null && shouldMark(c)
            }
            override fun decorate(facade: DayViewFacade?) {
                facade?.setBackgroundDrawable(drawableToday)
            }
        })
    }

    override fun showLedgers(ledgers: List<Record>){
        if(linearLedger.childCount > 2){
            linearLedger.removeViews(2, linearLedger.childCount-2)
        }

        val margin = MeasureUtil.getDimensionFromRid(activity, R.dimen.small_padding)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(margin, margin, margin, margin)

        ledgers.forEach {
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

    override fun showBalance(amount: Int) {
        textBalance.text = String.format(getString(R.string.today_balance), amount)
//        val textColor = if(amount >= 0) GlobalUtil.getColor(activity, R.color.colorIncome)
//        else GlobalUtil.getColor(activity, R.color.colorExpenditure)
//        textBalance.setTextColor(textColor)
    }

    override fun openLedgerEditView(dateString: String?, ledger: Record?) {

    }
}