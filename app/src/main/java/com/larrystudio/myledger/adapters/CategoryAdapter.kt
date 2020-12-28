package com.larrystudio.myledger.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.larry.larrylibrary.util.GlobalUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.room.Category

class CategoryAdapter(private val categories: MutableList<Category>):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), View.OnClickListener {

    var categoryListListener: CategoryListListener? = null

    override fun getItemCount(): Int { return categories.size }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = View.inflate(parent.context, R.layout.item_category_adapter, null)
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT)
        view.setOnClickListener(this)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindData(categories[position], position)
    }

    fun refreshAll(categories: List<Category>){
        this.categories.clear()
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        val position = v!!.tag as Int
        categoryListListener?.onCategoryClicked(categories[position])
    }

    class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val expString = view.context.getString(R.string.expenditure)
        private val incomeString = view.context.getString(R.string.income)
        private val recordCountString = view.context.getString(R.string.category_record_count)

        private val colorExp = GlobalUtil.getColor(view.context, R.color.colorExpenditure)
        private val colorIncome = GlobalUtil.getColor(view.context, R.color.colorIncome)

        private val textName = view.findViewById<TextView>(R.id.textName)
        private val textType = view.findViewById<TextView>(R.id.textType)
        private val textTotalCount = view.findViewById<TextView>(R.id.textTotalCount)

        fun bindData(category: Category, position: Int){
            itemView.tag = position
            textName.text = category.name
            if(category.isExpenditure()){
                textType.text = expString
                textType.setTextColor(colorExp)
            }else{
                textType.text = incomeString
                textType.setTextColor(colorIncome)
            }
            textTotalCount.text = String.format(recordCountString, category.recordCount)
        }
    }

    interface CategoryListListener{ fun onCategoryClicked(category: Category) }
}