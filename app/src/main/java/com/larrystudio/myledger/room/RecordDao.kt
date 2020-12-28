package com.larrystudio.myledger.room

import androidx.room.*

@Dao
interface RecordDao {

    @Query("select * from " + Record.TABLE_NAME)
    fun getAll(): List<Record>

    @Query("select * from " + Record.TABLE_NAME + " where timestamp = :time")
    fun getRecordByDate(time: Long): List<Record>

    @Query("select * from " + Record.TABLE_NAME + " where timestamp >= :start and timestamp <= :end")
    fun getRecordsByInterval(start: Long, end: Long): List<Record>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: Record): Long

    @Update
    //成功的話，會回傳資料庫中異動的數量
    fun update(record: Record): Int

    @Delete
    //成功的話，會回傳資料庫中異動的數量
    fun delete(record: Record): Int

    @Query("SELECT COUNT(categoryID) FROM ${Record.TABLE_NAME} WHERE categoryID = :id")
    fun getRecordCountByCategory(id: Long): Int
}