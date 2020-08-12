package com.larrystudio.myledger.mvp.main.monthyear

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.larry.larrylibrary.util.AnimatorUtil
import com.larry.larrylibrary.util.GlobalUtil
import com.larry.larrylibrary.util.MeasureUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.manager.ManagerFactory
import com.larrystudio.myledger.mvp.BaseFragment
import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.LogUtil
import kotlinx.android.synthetic.main.fragment_month_ledger.*
import java.util.*

class MonthYearLedgerFragment: BaseFragment(), MonthYearLedgerView {

    companion object {
        const val LEDGER_TYPE = "ledger_type"
        const val LEDGER_TIMESTAMP = "timestamp"
        const val TYPE_MONTH = "month"
        const val TYPE_YEAR = "year"

        fun getInstance(type: String): MonthYearLedgerFragment{
            val fragment = MonthYearLedgerFragment()
            val bundle = Bundle()
            bundle.putString(LEDGER_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var ledgerManager: LedgerManager
    private lateinit var adapterLedger: LedgerDetailAdapter

    private val presenter: MonthYearLedgerPresenter by lazy {
        val type = arguments!!.getString(LEDGER_TYPE)

        return@lazy if(type == TYPE_MONTH){
            MonthLedgerPresenterImpl(ledgerManager)
        }else{
            YearLedgerPresenterImpl(ledgerManager)
        }
    }

    private val monthList by lazy { ArrayList<TextView>() }
    private val categoryViewMap by lazy { HashMap<Category, View>() }
    private val monthChooseListener by lazy {
        View.OnClickListener{
            val year = textYear.text.toString().toInt()
            val month = it.tag.toString().toInt()
            presenter.onYearMonthSelected(year, month)
        }
    }

    private val categoryClickListener by lazy {
        View.OnClickListener{
            val category = it.tag as Category
            presenter.onCategoryClicked(category)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_month_ledger, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupTextMonths()
        ledgerManager = ManagerFactory.getInstance(activity).getLedgerManager()
        presenter.onAttach(this)
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        presenter.onLifeStarted()
    }

    private fun groupTextMonths(){
        monthList.clear()
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
            presenter.onYearChangeClicked(textYear.text.toString(), false)
        }

        imageArrowRight.setOnClickListener {
            presenter.onYearChangeClicked(textYear.text.toString(), true)
        }

        monthList.forEachIndexed { index, textView ->
            textView.tag = index+1
            textView.setOnClickListener(monthChooseListener)
        }

        relativeDetails.setOnClickListener { /* just to prevent views from being clicked */ }
    }

    override fun hideMonthArea() {
        linearMonth1.visibility = View.GONE
        linearMonth2.visibility = View.GONE
    }

    override fun selectYear(year: Int) {
        textYear.text = year.toString()
    }

    override fun selectYearMonth(year: Int, month: Int) {
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

    override fun showBalance(income: Int , expenditure: Int, balance: Int) {
        val rawIncome = getString(R.string.total_income)
        val incomeString = String.format(rawIncome, income)
        val rawExp = getString(R.string.total_expenditure)
        val expString = String.format(rawExp, expenditure)
        val rawBalance = getString(R.string.this_year_balance)
        val balanceString = String.format(rawBalance, balance)

        textIncome.text = incomeString
        textExpenditure.text = expString
        textBalance.text = balanceString
    }

    override fun showCategorySummary(category: Category, recordCount: Int, totalAmount: Int) {
        LogUtil.logd(TAG, "showCategorySummary, $category, $recordCount, $totalAmount")
        if (categoryViewMap[category] == null){
            categoryViewMap[category] = generateCategoryCards(category)
            linearLedger.addView(categoryViewMap[category])
        }

        setCategoryData(categoryViewMap[category]!!, category, recordCount, totalAmount)
    }

    override fun isCategoryDetailsShown(): Boolean {
        return relativeDetails.visibility == View.VISIBLE
    }

    override fun showCategoryDetails(category: Category, ledgers: List<Record>) {
        relativeDetails.visibility = View.VISIBLE
        textTitle.text = category.name
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapterLedger = LedgerDetailAdapter(ledgerManager)
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

    override fun hideCategoryDetails() {
        if(relativeDetails.visibility == View.VISIBLE)
            AnimatorUtil.slideVertically(relativeDetails, 0f,
                relativeDetails.height.toFloat(),
                object: AnimatorUtil.SlideAnimationCallback{
                    override fun onAnimationEnd() { relativeDetails.visibility = View.GONE }
                })
    }

    override fun openLedgerEditView(dateString: String?, ledger: Record?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generateCategoryCards(category: Category): View{
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