package com.larrystudio.myledger.manager

import com.larrystudio.myledger.BuildConfig
import com.larrystudio.myledger.room.Category
import org.junit.*

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, constants = BuildConfig::class)
class LedgerCategoryTest {

    private lateinit var ledgerManager: LedgerManager

    @Before
    fun setUp() {
        ManagerFactory.context = RuntimeEnvironment.application
        ledgerManager = ManagerFactory.getLedgerManager()
        ledgerManager.insertBasicCategories()
    }

    @After
    fun tearDown(){
        if(ManagerFactory.db?.isOpen == true){
            ManagerFactory.db?.close()
            ManagerFactory.db = null
            ManagerFactory.mLedgerManager = null
        }
    }

    @Test
    fun getAllCategories() {
        val categoryList = ledgerManager.getAllCategories()
        val nameList = categoryList.map { it.name }

        assertTrue(nameList.containsAll(listOf("家庭餐費", "日常用品", "手機網路", "房租", "獎金年終", "創業收入")))
    }

    @Test
    fun insertCategory() {
        val c1 = Category().apply { name = "c1" }
        val c2 = Category().apply { name = "c2" }
        val c3 = Category().apply {
            name = "c3"
            type = Category.TYPE_INCOME
        }

        ledgerManager.insertCategory(c1)
        ledgerManager.insertCategory(c2)
        ledgerManager.insertCategory(c3)

        val categories = ledgerManager.getAllCategories()
        assertNotNull(categories.find { it.name == "c1" })
        assertNotNull(categories.find { it.name == "c2" })
        assertNotNull(categories.find { it.name == "c3" && it.type == Category.TYPE_INCOME })
    }

    @Test
    fun updateCategory() {
        val before = ledgerManager.getAllCategories()
        val c1 = before[0]
        val name1 = c1.name
        val c2 = before[1]
        val name2 = c2.name

        ledgerManager.updateCategory(c1.apply { name = "update1" })
        ledgerManager.updateCategory(c2.apply { name = "update2" })
        val after = ledgerManager.getAllCategories()
        assertNull(after.find { it.name == name1 })
        assertNull(after.find { it.name == name2 })
        assertNotNull(after.find { it.name == "update1"})
        assertNotNull(after.find { it.name == "update2"})
    }
}