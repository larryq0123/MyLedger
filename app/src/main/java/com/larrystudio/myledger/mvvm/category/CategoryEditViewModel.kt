package com.larrystudio.myledger.mvvm.category

import androidx.lifecycle.MutableLiveData
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.mvvm.BaseViewModel
import com.larrystudio.myledger.mvvm.Event
import com.larrystudio.myledger.room.Category

class CategoryEditViewModel(private val ledgerManager: LedgerManager): BaseViewModel() {

    val ldCategory = MutableLiveData<Category>()
    val ldNavigate = MutableLiveData<Event<Boolean>>()


    fun prepareCategory(cid: Long){
        ldCategory.value = ledgerManager.getCategoryByID(cid)
    }

    fun onChangeTypeClicked(){
        val category = ldCategory.value!!
        category.type = if(category.isExpenditure()) Category.TYPE_INCOME else Category.TYPE_EXP
        ldCategory.value = category
    }

    fun onButtonSaveClicked(name: String){
        if(name.isEmpty()){
            ldToast.value = Event("分類名稱不可為空白")
        }else{
            val category = ldCategory.value!!.also { it.name = name }
            saveCategory(category)
        }
    }

    private fun saveCategory(category: Category){
        if(category.id == null){
            ledgerManager.insertCategory(category)
        }else{
            ledgerManager.updateCategory(category)
        }

        //儲存成功，跳轉頁面
        ldNavigate.value = Event(true)
    }
}