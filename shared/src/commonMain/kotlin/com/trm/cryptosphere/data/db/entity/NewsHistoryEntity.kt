package com.trm.cryptosphere.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "news_history", indices = [Index(value = ["url"], unique = true)])
data class NewsHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  @ColumnInfo(name = "title") val title: String,
  @ColumnInfo(name = "source") val source: String,
  @ColumnInfo(name = "url") val url: String,
  @ColumnInfo(name = "img_url") val imgUrl: String?,
  @ColumnInfo(name = "visited_at") val visitedAt: LocalDateTime,
)
