package com.larry.larrylibrary.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.larry.larrylibrary.R
import com.larry.larrylibrary.util.BaseLogUtil
import com.larry.larrylibrary.util.MeasureUtil

/**
 * Created by user on 2018/5/9.
 */
abstract class VeryBaseActivity: AppCompatActivity() {

    protected val TAG: String = javaClass.simpleName
    protected var screenWidth: Int = 0
    protected var screenHeight: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseLogUtil.logLifeCycle(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        BaseLogUtil.logLifeCycle(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        BaseLogUtil.logLifeCycle(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        BaseLogUtil.logLifeCycle(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        BaseLogUtil.logLifeCycle(TAG, "onStop")
    }

    override fun onDestroy() {
        BaseLogUtil.logLifeCycle(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    protected fun getScreenDimens() {
        val dimens = MeasureUtil.getScreenDimens(this)
        screenWidth = dimens[0]
        screenHeight = dimens[1]
    }

    protected fun goPage(c: Class<*>, isFinish: Boolean) {
        val intent = Intent(this, c)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        goPage(intent, isFinish)
    }


    protected fun goPage(intent: Intent, isFinish: Boolean) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        if (isFinish)
            finish()
    }


    protected fun goPageForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        transitForNewActivity()
    }

    protected fun transitForOldActivity() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    protected fun transitForNewActivity() {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }
}