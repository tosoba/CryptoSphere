package com.trm.cryptosphere.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "news")
data class NewsEntity(
  @PrimaryKey val id: String,
  val searchKeyWords: List<String>?,
  val feedDate: LocalDateTime,
  val source: String,
  val title: String,
  val sourceLink: String,
  val imgUrl: String?,
  val relatedCoins: List<String>,
  val link: String,
)
