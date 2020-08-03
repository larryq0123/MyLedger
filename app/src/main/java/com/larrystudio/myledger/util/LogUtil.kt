package com.larrystudio.myledger.util

import com.larry.larrylibrary.util.BaseLogUtil
import com.larrystudio.myledger.BuildConfig


object LogUtil {

    private val isPrintLog: Boolean
        get() {
            return BuildConfig.IS_PRINT_LOG
        }

    @JvmStatic
    fun logd(TAG: String, message: String, e: Exception) {
        logd(TAG, "$message, exception = $e")
    }

    @JvmStatic
    fun logd(TAG: String, message: String) {
        if(isPrintLog){
            BaseLogUtil.logd(TAG, message)
        }
    }

    @JvmStatic
    fun loge(TAG: String, message: String, e: Exception) {
        loge(TAG, "$message, exception = $e")
    }

    @JvmStatic
    fun loge(TAG: String, message: String) {
        if(isPrintLog){
            BaseLogUtil.loge(TAG, message)
        }
    }
}