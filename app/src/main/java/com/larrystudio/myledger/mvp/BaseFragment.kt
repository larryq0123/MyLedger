package com.larrystudio.myledger.mvp

import android.view.View
import com.larry.larrylibrary.base.VeryBaseFragment
import com.larry.larrylibrary.extension.*
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment: VeryBaseFragment(), BaseMvpView {

    protected val compositeDisposable by lazy { CompositeDisposable() }

    override fun showProgress() {
        activity?.showLoading()
    }

    override fun hideProgress() {
        activity?.hideLoading()
    }

    override fun showSimpleToast(message: String) {
        activity?.showToast(message)
    }

    override fun showSimpleSnack(parent: View, message: String) {
        activity?.showSnack(parent, message)
    }

    override fun showKeyboard() {
        activity?.showSoftInput()
    }

    override fun hideKeyboard() {
        activity?.hideSoftInput()
    }
}