package com.larrystudio.myledger.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = Record.TABLE_NAME,
    indices = [Index(value = ["categoryID"], name = "i_category"),
        Index(value = ["timestamp"], name = "i_timestamp")])
class Record {

    companion object{
        const val TABLE_NAME = "record"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "timestamp")
    var createTimestamp: Long = 0

    var categoryID: Long = 0

    var amount: Int = 0

    var comment: String = ""
}