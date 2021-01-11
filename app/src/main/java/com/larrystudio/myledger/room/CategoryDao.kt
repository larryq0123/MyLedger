package com.larrystudio.myledger.room

import androidx.room.*

@Dao
interface CategoryDao {

    @Query("select * from ${Category.TABLE_NAME} order by type")
    fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category): Long

    @Update
    //成功的話，會回傳資料庫中異動的數量
    fun update(category: Category): Int

    @Query("delete from ${Category.TABLE_NAME}")
    fun deleteAll()
}