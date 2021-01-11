package com.larrystudio.myledger.manager

import androidx.room.Ignore
import com.larrystudio.myledger.room.Category


data class BackupBean(val message: String, val categories: List<BackupCategory>)

data class BackupCategory(val name: String, val type: Int, val records: List<BackupRecord>)

data class BackupRecord(val createTimestamp: Long, val amount: Int, val comment: String)