package com.larrystudio.myledger.beans

import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.Record

data class CategoryRecordsBean(val category: Category, val records: List<Record>)

data class CategorySummaryBean(val category: Category, val recordCount: Int, val totalAmount: Int)

data class BalanceBean(val income: Int , val expenditure: Int, val balance: Int)

data class TempRecord(val date: String, val categoryPosition: Int, val amount: String, val comment: String)