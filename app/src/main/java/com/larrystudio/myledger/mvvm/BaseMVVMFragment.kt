package com.larrystudio.myledger.mvvm

import androidx.lifecycle.Observer
import com.larry.larrylibrary.base.VeryBaseFragment
import com.larry.larrylibrary.extension.hideLoading
import com.larry.larrylibrary.extension.showLoading
import com.larry.larrylibrary.extension.showToast

abstract class BaseMVVMFragment: VeryBaseFragment() {

    protected abstract fun initViewModel()

    protected fun doBasicSubscription(viewModel: BaseViewModel){
        viewModel.ldToast.observe(viewLifecycleOwner, Observer { activity?.showToast(it) })
        viewModel.ldLoading.observe(viewLifecycleOwner, Observer { toShow ->
            if(toShow) activity?.showLoading()
                else activity?.hideLoading()
        })
    }

    open fun onBackPressed(): Boolean = false
}