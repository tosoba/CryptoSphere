package com.trm.cryptosphere.domain.model

import kotlinx.datetime.LocalDateTime

data class NewsHistoryItem(
  val id: Long,
  val title: String,
  val source: String,
  val url: String,
  val imgUrl: String?,
  val visitedAt: LocalDateTime,
)
