package com.larrystudio.myledger.mvvm.category

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.larry.larrylibrary.extension.showToast
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvvm.BaseMVVMActivity

class CategoryEditActivity : BaseMVVMActivity() {

    companion object{
        private const val CATEGORY_ID = "category_id"

        fun newIntent(context: Context, cid: Long): Intent{
            return Intent(context, CategoryEditActivity::class.java).apply {
                putExtra(CATEGORY_ID, cid)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_edit)

        initView()
        initParam()
        initViewModel()
    }

    private fun initView(){

    }

    private fun initParam(){
        val cid = intent.getLongExtra(CATEGORY_ID, -1)
        showToast("category id = $cid")
    }

    override fun initViewModel() {

    }
}