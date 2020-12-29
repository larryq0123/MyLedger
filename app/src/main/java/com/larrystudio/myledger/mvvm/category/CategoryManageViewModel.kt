package com.larrystudio.myledger.mvvm.category

import androidx.lifecycle.MutableLiveData
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.mvvm.Event
import com.larrystudio.myledger.room.Category
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CategoryManageViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {

    val ldCategories = MutableLiveData<List<Category>>()


    fun refreshCategories(){
        Single.fromCallable {
            return@fromCallable ledgerManager.getAllCategories().onEach {
                it.recordCount = ledgerManager.getRecordCountByCategory(it)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: SingleObserver<List<Category>>{
                override fun onSubscribe(d: Disposable) { compositeDisposable.add(d) }
                override fun onSuccess(categories: List<Category>) { ldCategories.value = categories }
                override fun onError(e: Throwable) {
                    ldToast.value = Event("載入類別資訊時發生錯誤")
                }
            })
    }


}