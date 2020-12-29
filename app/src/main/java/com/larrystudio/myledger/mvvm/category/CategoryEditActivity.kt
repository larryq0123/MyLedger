package com.larrystudio.myledger.mvvm.category

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.larry.larrylibrary.extension.showToast
import com.larry.larrylibrary.util.GlobalUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.mvvm.BaseMVVMActivity
import com.larrystudio.myledger.mvvm.EventObserver
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.room.Category
import kotlinx.android.synthetic.main.activity_category_edit.*

class CategoryEditActivity : BaseMVVMActivity() {

    companion object{
        private const val CATEGORY_ID = "category_id"

        fun newIntent(context: Context, cid: Long): Intent{
            return Intent(context, CategoryEditActivity::class.java).apply {
                putExtra(CATEGORY_ID, cid)
            }
        }
    }

    private lateinit var viewModel: CategoryEditViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_edit)

        initView()
        initViewModel()
    }

    private fun initView(){
        imageChangeType.setOnClickListener { viewModel.onChangeTypeClicked() }
        buttonCancel.setOnClickListener { finish() }
        buttonSave.setOnClickListener {
            val name = editName.text.toString()
            viewModel.onButtonSaveClicked(name)
        }
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(CategoryEditViewModel::class.java)
        doBasicSubscription(viewModel)
        viewModel.ldCategory.observe(this, Observer { setupCategory(it) })
        viewModel.ldNavigate.observe(this, EventObserver{ success ->
            if(success) setResult(RESULT_OK)
            finish()
        })

        val cid = intent.getLongExtra(CATEGORY_ID, -1L)
        viewModel.prepareCategory(cid)
    }

    private fun setupCategory(category: Category){
        if(category.isExpenditure()){
            textType.text = getString(R.string.expenditure)
            textType.setTextColor(GlobalUtil.getColor(this, R.color.colorExpenditure))
        }else{
            textType.text = getString(R.string.income)
            textType.setTextColor(GlobalUtil.getColor(this, R.color.colorIncome))
        }

        editName.setText(category.name)
    }
}