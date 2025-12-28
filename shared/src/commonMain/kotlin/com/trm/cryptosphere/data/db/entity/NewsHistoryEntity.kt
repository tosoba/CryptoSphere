package com.trm.cryptosphere.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "news_history")
data class NewsHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  @ColumnInfo(name = "url") val url: String,
  @ColumnInfo(name = "visited_at") val visitedAt: LocalDateTime,
)
