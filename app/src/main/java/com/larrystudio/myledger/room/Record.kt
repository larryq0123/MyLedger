package com.larrystudio.myledger.room

import androidx.room.*

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

    @Ignore
    var category: Category? = null

    var amount: Int = 0

    var comment: String = ""
    override fun toString(): String {
        return "Record(id=$id, createTimestamp=$createTimestamp, categoryID=$categoryID, category=$category, amount=$amount, comment='$comment')"
    }
}