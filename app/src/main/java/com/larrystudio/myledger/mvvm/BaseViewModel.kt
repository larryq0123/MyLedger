package com.larrystudio.myledger.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel: ViewModel() {
    protected val TAG = javaClass.simpleName
    protected val compositeDisposable by lazy { CompositeDisposable() }
    val ldToast = MutableLiveData<String>()
    val ldLoading = MutableLiveData<Boolean>()

    open fun onCreateLifeCycle(){}
    open fun onStartLifeCycle(){}
    open fun onResumeLifeCycle(){}
    open fun onPauseLifeCycle(){}
    open fun onStopLifeCycle(){}
    open fun onDestroyLifeCycle(){}

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * return true if viewModel consumes this action
     */
    open fun onBackPressed(): Boolean{ return false }

    protected fun showShortToast(message: String){ ldToast.value = message }
    protected fun showLoading(toShow: Boolean){ ldLoading.value = toShow }
}