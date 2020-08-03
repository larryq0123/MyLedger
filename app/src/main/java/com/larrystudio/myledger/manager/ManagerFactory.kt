package com.larrystudio.myledger.manager

import android.content.Context
import androidx.room.Room
import com.larrystudio.myledger.LedgerApp
import com.larrystudio.myledger.room.LedgerDB

object ManagerFactory {

    var context: Context? = LedgerApp.appContext

    var db: LedgerDB? = null
    var mLedgerManager: LedgerManager? = null



    fun getLedgerManager(): LedgerManager{
        if(mLedgerManager == null){
            if(db == null){
                db = Room.databaseBuilder(context!!,
                    LedgerDB::class.java, LedgerDB.DB_NAME)
                    .allowMainThreadQueries()
                    .build()
            }

            val categoryDao = db!!.getCategoryDao()
            val recordDao = db!!.getRecordDao()
            mLedgerManager = LedgerManager(categoryDao, recordDao)
        }

        return mLedgerManager!!
    }
}