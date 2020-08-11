package com.larrystudio.myledger.manager

import android.content.Context
import androidx.room.Room
import com.larrystudio.myledger.LedgerApp
import com.larrystudio.myledger.room.LedgerDB
import com.larrystudio.myledger.util.LogUtil

class ManagerFactory(private val context: Context) {

    companion object{
        private var instance: ManagerFactory? = null

        fun getInstance(c: Context? = null): ManagerFactory{
            if(instance == null){
                instance = ManagerFactory(c ?: LedgerApp.appContext)
            }

            return instance!!
        }
    }

    private var db: LedgerDB? = null
    private var mLedgerManager: LedgerManager? = null

    fun closeDB(){
        if(db?.isOpen == true){
            db?.close()
            db = null
            mLedgerManager = null
        }
    }

    fun getLedgerManager(): LedgerManager{
        if(mLedgerManager == null){
            if(db == null){
                db = Room.databaseBuilder(context!!, LedgerDB::class.java, LedgerDB.DB_NAME)
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