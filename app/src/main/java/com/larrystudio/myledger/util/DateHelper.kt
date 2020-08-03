package com.larrystudio.myledger.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateHelper {

    private const val TAG = "DateHelper"
    private val sdf by lazy { SimpleDateFormat("yyyy-MM-dd") }

    fun areDatesOnTheSameDay(d1: Date, d2: Date): Boolean{
        return sdf.format(d1) == sdf.format(d2)
    }

    fun formatDate(d: Date): String{
        return sdf.format(d)
    }

    fun parseDateString(dateString: String): Date{
        return sdf.parse(dateString)!!
    }

    /**
     * 重新整理成該日凌晨零點零分時的timestamp
     */
    fun timestampOfDate(d: Date): Long{
        return parseDateString(formatDate(d)).time
    }
}