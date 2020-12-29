package com.larrystudio.myledger.mvvm.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.larrystudio.myledger.R
import com.larrystudio.myledger.adapters.CategoryAdapter
import com.larrystudio.myledger.mvvm.BaseMVVMActivity
import com.larrystudio.myledger.mvvm.ViewModelFactory
import com.larrystudio.myledger.room.Category
import kotlinx.android.synthetic.main.activity_category_manage.*
import kotlinx.android.synthetic.main.activity_category_manage.toolbar

class CategoryManageActivity : BaseMVVMActivity() {

    private val EDIT_CATEGORY_REQUEST = 1
    private lateinit var viewModel: CategoryManageViewModel
    private var categoryAdapter: CategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_manage)

        initView()
        initViewModel()
    }

    private fun initView(){
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.category_manage)
        floatingAdd.setOnClickListener { openCategoryEditPage(-1L) }
    }

    override fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(CategoryManageViewModel::class.java)
        doBasicSubscription(viewModel)
        viewModel.ldCategories.observe(this, Observer { updateCategoryList(it) })

        viewModel.refreshCategories()
    }

    private fun updateCategoryList(categories: List<Category>){
        if(categoryAdapter == null){
            recyclerCategory.layoutManager = LinearLayoutManager(this)
            categoryAdapter = CategoryAdapter(categories.toMutableList())
            categoryAdapter!!.categoryListListener = object: CategoryAdapter.CategoryListListener{
                override fun onCategoryClicked(category: Category) { openCategoryEditPage(category.id!!) }
            }
            recyclerCategory.adapter = categoryAdapter
            recyclerCategory.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        }else{
            categoryAdapter?.refreshAll(categories)
        }
    }

    private fun openCategoryEditPage(cid: Long){
        val intent = CategoryEditActivity.newIntent(this@CategoryManageActivity, cid)
        startActivityForResult(intent, EDIT_CATEGORY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == EDIT_CATEGORY_REQUEST && resultCode == RESULT_OK){
            viewModel.refreshCategories()
        }
    }
}