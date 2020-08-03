package com.larrystudio.myledger.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Category::class, Record::class], version = 1)
abstract class LedgerDB: RoomDatabase() {


    companion object{
        const val DB_NAME = "my_ledger_db"
    }

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getRecordDao(): RecordDao
}