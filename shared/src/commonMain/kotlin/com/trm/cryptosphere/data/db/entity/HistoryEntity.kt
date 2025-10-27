package com.trm.cryptosphere.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0)
