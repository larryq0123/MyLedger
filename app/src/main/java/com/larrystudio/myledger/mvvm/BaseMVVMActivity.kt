package com.larrystudio.myledger.mvvm

import androidx.lifecycle.Observer
import com.larry.larrylibrary.base.VeryBaseActivity
import com.larry.larrylibrary.extension.hideLoading
import com.larry.larrylibrary.extension.showLoading
import com.larry.larrylibrary.extension.showToast

abstract class BaseMVVMActivity: VeryBaseActivity() {



    protected abstract fun initViewModel()

    protected fun doBasicSubscription(viewModel: BaseViewModel){
        viewModel.ldToast.observe(this, Observer { showToast(it) })
        viewModel.ldLoading.observe(this, Observer { toShow ->
            if(toShow) showLoading()
                else hideLoading()
        })
    }
}