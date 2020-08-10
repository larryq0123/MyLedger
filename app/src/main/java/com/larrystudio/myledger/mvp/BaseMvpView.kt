package com.larrystudio.myledger.mvp

import android.content.Context
import android.view.View

interface BaseMvpView {

    fun showProgress()
    fun hideProgress()

    fun showSimpleToast(message: String)
    fun showSimpleSnack(parent: View, message: String)

    fun showKeyboard()
    fun hideKeyboard()
}