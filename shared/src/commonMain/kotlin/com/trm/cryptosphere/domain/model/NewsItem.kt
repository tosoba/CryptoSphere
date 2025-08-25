package com.trm.cryptosphere.domain.model

import kotlinx.datetime.LocalDateTime

data class NewsItem(
  val id: String,
  val searchKeyWords: List<String>?,
  val feedDate: LocalDateTime,
  val source: String,
  val title: String,
  val sourceLink: String,
  val imgUrl: String?,
  val relatedCoins: List<String>,
  val link: String,
)
