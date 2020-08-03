package com.larry.larrylibrary.extension

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.larry.larrylibrary.util.MeasureUtil

/**
 * Created by user on 2018/5/4.
 */

fun Fragment.getScreenDimens() = MeasureUtil.getScreenDimens(activity)

fun Fragment.showSnack(view: View, message: String, duration: Int = Snackbar.LENGTH_INDEFINITE, buttonString: String = "確認", clickListener: View.OnClickListener = View.OnClickListener {  }) {
    Snackbar.make(view, message, duration)
            .setAction(buttonString, clickListener)
            .show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(activity, message, duration)
            .show()
}