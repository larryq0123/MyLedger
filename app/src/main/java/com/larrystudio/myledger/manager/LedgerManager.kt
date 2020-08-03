package com.larrystudio.myledger.manager

import com.larrystudio.myledger.room.Category
import com.larrystudio.myledger.room.CategoryDao
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.room.RecordDao
import com.larrystudio.myledger.util.DateHelper
import com.larrystudio.myledger.util.LogUtil
import java.util.*

class LedgerManager(private val categoryDao: CategoryDao,
                    private val recordDao: RecordDao) {

    private val TAG = LedgerManager::class.java.simpleName

    fun insertBasicCategories(){
        if(categoryDao.getAll().isEmpty()){
            categoryDao.insert(Category().apply { name = "個人餐費" })
            categoryDao.insert(Category().apply { name = "家庭餐費" })
            categoryDao.insert(Category().apply { name = "零食飲料" })
            categoryDao.insert(Category().apply { name = "交通" })
            categoryDao.insert(Category().apply { name = "日常用品" })
            categoryDao.insert(Category().apply { name = "出遊娛樂" })
            categoryDao.insert(Category().apply { name = "服飾" })
            categoryDao.insert(Category().apply { name = "醫療保健保險" })
            categoryDao.insert(Category().apply { name = "手機網路" })
            categoryDao.insert(Category().apply { name = "水電氣" })
            categoryDao.insert(Category().apply { name = "學習深造" })
            categoryDao.insert(Category().apply { name = "家庭支出" })
            categoryDao.insert(Category().apply { name = "房租" })
            categoryDao.insert(Category().apply { name = "捐款" })
            categoryDao.insert(Category().apply { name = "其他" })

            categoryDao.insert(Category().apply {
                name = "月薪"
                type = Category.TYPE_INCOME
            })
            categoryDao.insert(Category().apply {
                name = "獎金年終"
                type = Category.TYPE_INCOME
            })
            categoryDao.insert(Category().apply {
                name = "外包專案"
                type = Category.TYPE_INCOME
            })
            categoryDao.insert(Category().apply {
                name = "創業收入"
                type = Category.TYPE_INCOME
            })
            categoryDao.insert(Category().apply {
                name = "其他"
                type = Category.TYPE_INCOME
            })
        }
    }

    fun getAllCategories(): List<Category>{
        return categoryDao.getAll()
    }

    fun insertCategory(category: Category): Long{
        return categoryDao.insert(category)
    }

    fun updateCategory(category: Category): Int{
        return categoryDao.update(category)
    }

    fun loadAllRecords(): List<Record>{
        return recordDao.getAll()
    }

    fun loadRecordsByDate(date: Date): List<Record>{
        val timestamp = DateHelper.timestampOfDate(date)
        return recordDao.getRecordByDate(timestamp)
    }

    fun loadRecordsByMonth(date: Date): List<Record>{
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val start = DateHelper.timestampOfDate(calendar.time)
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val end = DateHelper.timestampOfDate(calendar.time)

//        LogUtil.logd(TAG, "beginDate = ${DateHelper.formatDate(calendar.time)}, beginTimestamp = $start")
//        LogUtil.logd(TAG, "endDate = ${DateHelper.formatDate(calendar.time)}, endTimestamp = $end")

        return recordDao.getRecordsByInterval(start, end)
    }

    fun loadRecordsByYear(date: Date): List<Record>{
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val start = DateHelper.timestampOfDate(calendar.time)
        calendar.set(Calendar.MONTH, 11)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        val end = DateHelper.timestampOfDate(calendar.time)

        LogUtil.logd(TAG, "beginDate = ${DateHelper.formatDate(calendar.time)}, beginTimestamp = $start")
        LogUtil.logd(TAG, "endDate = ${DateHelper.formatDate(calendar.time)}, endTimestamp = $end")

        return recordDao.getRecordsByInterval(start, end)
    }

    fun insertRecord(record: Record, dateString: String): Long{
        record.createTimestamp = DateHelper.parseDateString(dateString).time
        return recordDao.insert(record)
    }

    fun updateRecord(record: Record, dateString: String): Int{
        record.createTimestamp = DateHelper.parseDateString(dateString).time
        return recordDao.update(record)
    }

    fun deleteRecord(record: Record): Int{
        return recordDao.delete(record)
    }
}