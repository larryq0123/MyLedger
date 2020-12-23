package com.larrystudio.myledger.mvp

import android.view.View
import com.larry.larrylibrary.base.VeryBaseActivity
import com.larry.larrylibrary.extension.*

abstract class BaseMVPActivity: VeryBaseActivity(), BaseMvpView {

    override fun showProgress() { showLoading() }

    override fun hideProgress() { hideLoading() }

    override fun showSimpleToast(message: String) { showToast(message) }

    override fun showSimpleSnack(parent: View, message: String) {
        showSnack(parent, message)
    }

    override fun showKeyboard() { showSoftInput() }

    override fun hideKeyboard() { hideSoftInput() }
}