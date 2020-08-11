package com.larrystudio.myledger.customview

import android.app.Dialog
import android.content.Context
import android.view.ViewTreeObserver
import com.larry.larrylibrary.util.AnimatorUtil
import com.larry.larrylibrary.util.MeasureUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.util.LogUtil
import kotlinx.android.synthetic.main.layout_wheel_date_picker.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WheelDatePicker(context: Context, private val isCancelable: Boolean): Dialog(
        context, android.R.style.Theme_Translucent_NoTitleBar) {

    private val TAG = javaClass.simpleName
    private val minYear = 2000
    private val maxYear = 2100

    private val mCalendar: Calendar by lazy { GregorianCalendar.getInstance() }
    private val dayMap: MutableMap<Int, List<String>> by lazy { HashMap<Int, List<String>>() }
    private var isReadyToCancel = false
    var listener: OnDateSelectedListener? = null

    init {
        setContentView(R.layout.layout_wheel_date_picker)
        initYearPicker()
        initMonthPicker()
        initDayPicker()
        textCancel.setOnClickListener { dismissDialog() }
        textConfirm.setOnClickListener {
            listener?.onDateSelected(getChosenDate())
            dismissDialog()
        }
        constraintMain.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                constraintMain.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val screenHeight = MeasureUtil.getScreenDimens(context)[1].toFloat()
                AnimatorUtil.animateBackgroundDim(relativeRoot, false)
                AnimatorUtil.slideVertically(constraintMain, screenHeight, 0f,
                    object: AnimatorUtil.SlideAnimationCallback{
                        override fun onAnimationEnd() {}
                    })
            }
        })
    }

    private fun initYearPicker(){
        val years = ArrayList<String>()
        for(y in minYear .. maxYear){
            years.add(y.toString())
        }
        yearPicker.data = years
        yearPicker.selectedItemPosition = mCalendar.get(Calendar.YEAR) - minYear
        yearPicker.setOnItemSelectedListener { picker, data, position ->
            resetDayPickerData()
        }
    }

    private fun initMonthPicker(){
        val months = ArrayList<String>()
        for (m in 1 .. 12){
            months.add(String.format("%02d", m))
        }
        monthPicker.data = months
        monthPicker.selectedItemPosition = mCalendar.get(Calendar.MONTH)
        monthPicker.setOnItemSelectedListener { picker, data, position ->
            resetDayPickerData()
        }
    }

    private fun initDayPicker(){
        resetDayPickerData()
        dayPicker.selectedItemPosition = mCalendar.get(Calendar.DAY_OF_MONTH) - 1
    }

    private fun resetDayPickerData(){
        val year = minYear + yearPicker.currentItemPosition
        val month = monthPicker.currentItemPosition+1
        val maxDay = getDayOfMonth(month, year)
        LogUtil.logd(TAG, "year = $year, month = $month, maxDay = $maxDay")


        dayPicker.data = if(dayMap[maxDay] != null){
            dayMap[maxDay]
        }else{
            val list = ArrayList<String>()
            for(d in 1 .. maxDay){
                list.add(String.format("%02d", d))
            }
            dayMap[maxDay] = list
            list
        }
    }

    private fun getDayOfMonth(month: Int, year: Int) = when(month){
        1,3,5,7,8,10,12 -> 31
        4,6,9,11 -> 30
        else -> {
            when {
                year%400 == 0 -> 29
                year%100 == 0 -> 28
                year%4 == 0 -> 29
                else -> 28
            }
        }
    }

    /**
     * return: 回傳格式yyyy-MM-dd
     */
    fun getChosenDate(): String{
        val date = "${yearPicker.data[yearPicker.currentItemPosition]}-${monthPicker.data[monthPicker.currentItemPosition]}-${dayPicker.data[dayPicker.currentItemPosition]}"
        LogUtil.logd(TAG, "getChosenDate() = $date")
        return date
    }

    fun dismissDialog() {
        isReadyToCancel = true
        listener = null
        dismiss()
    }

    override fun dismiss() {
        if (isReadyToCancel || isCancelable) {
            val screenHeight = MeasureUtil.getScreenDimens(context)[1].toFloat()
            AnimatorUtil.animateBackgroundDim(relativeRoot, true)
            AnimatorUtil.slideVertically(constraintMain, 0f, screenHeight,
                object: AnimatorUtil.SlideAnimationCallback{
                    override fun onAnimationEnd() { super@WheelDatePicker.dismiss() }
                })
        }
    }

    interface OnDateSelectedListener{
        fun onDateSelected(dateString: String)  // format yyyy-MM-dd
    }
}