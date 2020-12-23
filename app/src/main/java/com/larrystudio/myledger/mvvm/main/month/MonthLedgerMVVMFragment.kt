package com.larrystudio.myledger.mvvm.main.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.larry.larrylibrary.util.AnimatorUtil
import com.larry.larrylibrary.util.GlobalUtil
import com.larry.larrylibrary.util.MeasureUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.adapters.LedgerDetailAdapter
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvvm.BaseMVVMFragment
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.LogUtil
import kotlinx.android.synthetic.main.fragment_month_ledger.*
import java.util.ArrayList
import java.util.HashMap

class MonthLedgerMVVMFragment: BaseMVVMFragment() {

    private lateinit var viewModel: MonthLedgerViewModel
    private lateinit var adapterLedger: LedgerDetailAdapter

    private val monthList by lazy { ArrayList<TextView>() }
    private val categoryViewMap by lazy { HashMap<Category, View>() }
    private val monthChooseListener by lazy {
        View.OnClickListener{
            val year = textYear.text.toString().toInt()
            val month = it.tag.toString().toInt()
            viewModel.onYearMonthSelected(year, month)
        }
    }

    private val categoryClickListener by lazy {
        View.OnClickListener{
            val category = it.tag as Category
            viewModel.onCategoryClicked(category)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_month_ledger, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        groupTextMonths()
        viewModel.onCreateLifeCycle()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStartLifeCycle()
    }

    override fun onDestroyView() {
        monthList.clear()
        super.onDestroyView()
    }

    override fun onBackPressed(): Boolean {
        return viewModel.onBackPressed()
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(activity!!, factory).get(MonthLedgerViewModel::class.java)
        doBasicSubscription(viewModel)
        viewModel.ldYearMonth.observe(viewLifecycleOwner, Observer { selectYearMonth(it.first, it.second) })
        viewModel.ldCategoryRecordsBean.observe(viewLifecycleOwner, Observer {
            if(it == null) hideCategoryDetails()
            else showCategoryDetails(it.category, it.records)
        })
        viewModel.ldCategorySummaryBean.observe(viewLifecycleOwner, Observer { summaries ->
            summaries.forEach { showCategorySummary(it.category, it.recordCount, it.totalAmount) }
        })
        viewModel.ldBalanceBean.observe(viewLifecycleOwner, Observer {
            showBalance(it.income, it.expenditure, it.balance)
        })
    }

    private fun groupTextMonths(){
        monthList.add(textMonth1)
        monthList.add(textMonth2)
        monthList.add(textMonth3)
        monthList.add(textMonth4)
        monthList.add(textMonth5)
        monthList.add(textMonth6)
        monthList.add(textMonth7)
        monthList.add(textMonth8)
        monthList.add(textMonth9)
        monthList.add(textMonth10)
        monthList.add(textMonth11)
        monthList.add(textMonth12)
    }

    private fun initListeners(){
        imageArrowLeft.setOnClickListener {
            viewModel.onYearChangeClicked(textYear.text.toString(), false)
        }

        imageArrowRight.setOnClickListener {
            viewModel.onYearChangeClicked(textYear.text.toString(), true)
        }

        monthList.forEachIndexed { index, textView ->
            textView.tag = index+1
            textView.setOnClickListener(monthChooseListener)
        }

        relativeDetails.setOnClickListener { /* just to prevent views from being clicked */ }
    }

    private fun selectYearMonth(year: Int, month: Int) {
        textYear.text = year.toString()
        val textColor = GlobalUtil.getColor(activity, R.color.almostBlack)
        monthList.forEach {
            it.setTextColor(textColor)
        }

        if(month > 0){
            val selectedTextColor = GlobalUtil.getColor(activity, R.color.colorPrimaryDark)
            monthList[month-1].setTextColor(selectedTextColor)
        }
    }

    private fun showBalance(income: Int , expenditure: Int, balance: Int) {
        val rawIncome = getString(R.string.total_income)
        val incomeString = String.format(rawIncome, income)
        val rawExp = getString(R.string.total_expenditure)
        val expString = String.format(rawExp, expenditure)
        val rawBalance = getString(R.string.this_month_balance)
        val balanceString = String.format(rawBalance, balance)

        textIncome.text = incomeString
        textExpenditure.text = expString
        textBalance.text = balanceString
    }

    private fun showCategorySummary(category: Category, recordCount: Int, totalAmount: Int) {
        LogUtil.logd(TAG, "showCategorySummary, $category, $recordCount, $totalAmount")
        if (categoryViewMap[category] == null){
            categoryViewMap[category] = generateCategoryCards(category)
            linearLedger.addView(categoryViewMap[category])
        }

        setCategoryData(categoryViewMap[category]!!, category, recordCount, totalAmount)
    }

    private fun showCategoryDetails(category: Category, ledgers: List<Record>) {
        relativeDetails.visibility = View.VISIBLE
        textTitle.text = category.name
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapterLedger = LedgerDetailAdapter()
        recyclerView.adapter = adapterLedger
        adapterLedger.refreshLedgers(ledgers)

        if(relativeDetails.height == 0){
            relativeDetails.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    relativeDetails.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    AnimatorUtil.slideVertically(relativeDetails,
                        relativeDetails.height.toFloat(), 0f, null)
                }
            })
        }else{
            AnimatorUtil.slideVertically(relativeDetails,
                relativeDetails.height.toFloat(), 0f, null)
        }
    }

    private fun hideCategoryDetails() {
        if(relativeDetails.visibility == View.VISIBLE)
            AnimatorUtil.slideVertically(relativeDetails, 0f,
                relativeDetails.height.toFloat(),
                object: AnimatorUtil.SlideAnimationCallback{
                    override fun onAnimationEnd() { relativeDetails.visibility = View.GONE }
                })
    }

    private fun generateCategoryCards(category: Category): View {
        val margin = MeasureUtil.getDimensionFromRid(activity, R.dimen.small_padding)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(margin, margin, margin, margin)
        val ledgerView = View.inflate(activity, R.layout.view_ledger_item, null)
        ledgerView.layoutParams = lp
        ledgerView.tag = category
        ledgerView.setOnClickListener(categoryClickListener)
        return ledgerView
    }

    private fun setCategoryData(ledgerView: View, category: Category, recordCount: Int, totalAmount: Int){
        val textCategory = ledgerView.findViewById<TextView>(R.id.textCategory)
        textCategory.text = category.name
        val textComment = ledgerView.findViewById<TextView>(R.id.textComment)
        textComment.text = "共有${recordCount}筆記錄"
        val textAmount = ledgerView.findViewById<TextView>(R.id.textAmount)
        textAmount.text = totalAmount.toString()
        if(!category.isExpenditure()){
            textAmount.setTextColor(GlobalUtil.getColor(activity, R.color.colorIncome))
        }
    }
}