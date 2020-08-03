package com.larrystudio.myledger.manager

import com.larrystudio.myledger.BuildConfig
import com.larrystudio.myledger.room.Record
import com.larrystudio.myledger.util.DateHelper
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.*
import kotlin.random.Random


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, constants = BuildConfig::class)
class LedgerRecordTest {

    private lateinit var ledgerManager: LedgerManager
    private val NUMBER_OF_DAYS = 31

    @Before
    fun setUp() {
        ManagerFactory.context = RuntimeEnvironment.application
        ledgerManager = ManagerFactory.getLedgerManager()
        ledgerManager.insertBasicCategories()
        insertTestRecords()
    }

    @After
    fun tearDown(){
        if(ManagerFactory.db?.isOpen == true){
            ManagerFactory.db?.close()
            ManagerFactory.db = null
            ManagerFactory.mLedgerManager = null
        }
    }

    private fun insertTestRecords(){
        val categories = ledgerManager.getAllCategories()
        val calendar = Calendar.getInstance()

        for(day in 1..NUMBER_OF_DAYS){
            val dateString = DateHelper.formatDate(calendar.time)
            for(i in 1..5){
                ledgerManager.insertRecord(Record().apply {
                    categoryID = categories[Random.nextInt(categories.size)].id!!
                    amount = Random.nextInt(1000)
                }, dateString)
            }
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }
    }

    @Test
    fun loadAllRecords() {
        val records = ledgerManager.loadAllRecords()
        assert(records.size == 155)
    }

    @Test
    fun loadRecordsByDate() {
        val calendar = Calendar.getInstance()

        assert(ledgerManager.loadRecordsByDate(calendar.time).size == 5)

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        assert(ledgerManager.loadRecordsByDate(calendar.time).size == 5)

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        assert(ledgerManager.loadRecordsByDate(calendar.time).size == 5)
    }

    @Test
    fun loadRecordsByMonth() {
        val calendar = Calendar.getInstance()
        val dayOfThisMonth = calendar.get(Calendar.DAY_OF_MONTH)
        assert(ledgerManager.loadRecordsByMonth(calendar.time).size == 5*dayOfThisMonth)

        calendar.add(Calendar.MONTH, -1)
        val dayOfLastMonth = NUMBER_OF_DAYS-dayOfThisMonth
        assert(ledgerManager.loadRecordsByMonth(calendar.time).size == 5*dayOfLastMonth)
        println("dayOfThisMonth = $dayOfThisMonth, dayOfLastMonth = $dayOfLastMonth")
    }

    @Test
    fun insertRecord() {
        val r1 = Record().apply {
            categoryID = 1
            amount = 100
            comment = "record1"
        }
        val r2 = Record().apply {
            categoryID = 2
            amount = 200
            comment = "record2"
        }
        val r3 = Record().apply {
            categoryID = 3
            amount = 300
            comment = "record3"
        }

        ledgerManager.insertRecord(r1, "2020-09-03")
        ledgerManager.insertRecord(r2, "2020-09-02")
        ledgerManager.insertRecord(r3, "2020-09-01")
        val c = Calendar.getInstance().apply { add(Calendar.MONTH, 1) }
        val records = ledgerManager.loadRecordsByMonth(c.time)
        assert(records.size == 3)
        assertNotNull(records.find { it.categoryID == 1L && it.amount == 100 && it.comment == "record1" })
        assertNotNull(records.find { it.categoryID == 2L && it.amount == 200 && it.comment == "record2" })
        assertNotNull(records.find { it.categoryID == 3L && it.amount == 300 && it.comment == "record3" })
    }

    @Test
    fun deleteRecord() {
        val toDelete = ledgerManager.loadRecordsByMonth(Date())
        toDelete.forEach { ledgerManager.deleteRecord(it) }

        val remain = ledgerManager.loadAllRecords()
        assert(remain.size == (5*NUMBER_OF_DAYS - toDelete.size))
    }
}