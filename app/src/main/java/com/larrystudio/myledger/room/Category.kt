package com.larrystudio.myledger.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Category.TABLE_NAME)
class Category() {

    companion object{
        const val TABLE_NAME = "category"
        const val TYPE_EXP = 1
        const val TYPE_INCOME = 2
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    var name: String = ""

    var type: Int = TYPE_EXP

    @Transient
    var recordCount: Int = 0

    fun isExpenditure() = type == TYPE_EXP

    override fun toString(): String {
        return "Category(id=$id, name='$name', type=$type)"
    }
}