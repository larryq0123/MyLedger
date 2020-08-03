package com.larry.larrylibrary.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Larry on 2018/2/14.
 */

private val Context.isPrint: Boolean
    get() = true

private var toast: Toast? = null

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT){
    if (toast == null){
        toast = Toast.makeText(this, message, duration)
    }else{
        toast!!.setText(message)
    }

    toast!!.show()
}

fun Context.showSnack(view: View, message: String, duration: Int = Snackbar.LENGTH_INDEFINITE, buttonString: String = "確認", clickListener: View.OnClickListener = View.OnClickListener {  }){
    Snackbar.make(view, message, duration)
            .setAction(buttonString, clickListener)
            .show()
}

