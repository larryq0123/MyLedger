package com.larrystudio.myledger.mvp.main.monthyear

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.larry.larrylibrary.extension.defaultLP
import com.larry.larrylibrary.util.GlobalUtil
import com.larrystudio.myledger.R
import com.larrystudio.myledger.manager.LedgerManager
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import java.util.*

class LedgerDetailAdapter(private val ledgerManager: LedgerManager):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var ledgerList = ArrayList<Record>()

    override fun getItemCount(): Int {
        return ledgerList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LedgerHolder {
        val view = View.inflate(parent.context, R.layout.item_ledger_detail, null)
        view.layoutParams = defaultLP()
        return LedgerHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as LedgerHolder
        holder.setData(ledgerList[position])
    }

    fun refreshLedgers(list: List<Record>){
        ledgerList.clear()
        ledgerList.addAll(list)
        notifyDataSetChanged()
    }

    class LedgerHolder(view: View): RecyclerView.ViewHolder(view){
        private val textDate: TextView = view.findViewById(R.id.textDate)
        private val textCategory: TextView = view.findViewById(R.id.textCategory)
        private val textComment: TextView = view.findViewById(R.id.textComment)
        private val textAmount: TextView = view.findViewById(R.id.textAmount)

        private val colorIncome = GlobalUtil.getColor(view.context, R.color.colorIncome)
        private val colorExp = GlobalUtil.getColor(view.context, R.color.colorExpenditure)

        fun setData(record: Record){
            textDate.text = DateHelper.formatDate(Date(record.createTimestamp))
            textCategory.text = record.category!!.name
            textComment.text = record.comment
            textAmount.text = record.amount.toString()
            if(!record.category!!.isExpenditure()){
                textAmount.setTextColor(colorIncome)
            }else{
                textAmount.setTextColor(colorExp)
            }
        }
    }
}