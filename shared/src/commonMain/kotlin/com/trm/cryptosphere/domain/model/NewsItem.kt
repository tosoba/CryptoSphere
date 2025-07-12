package com.trm.cryptosphere.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class NewsItem(
  val id: String,
  val searchKeyWords: List<String>?,
  val feedDate: LocalDateTime, // TODO: map from Long in JSON
  val source: String,
  val title: String,
  val sourceLink: String,
  val description: String?,
  val imgUrl: String?,
  val relatedCoins: List<String>,
  val link: String,
)

fun mockNewsItem(id: String): NewsItem =
  NewsItem(
    id = id,
    searchKeyWords = listOf("dogecoin", "DOGE"),
    feedDate = @OptIn(ExperimentalTime::class) Clock.System.now().toLocalDateTime(TimeZone.UTC),
    source = "U.Today",
    title = "Dogecoin Account Drops Casual 'Sup' Tweet: What's Behind It?",
    sourceLink = "https://u.today/",
    description = "Dogecoin Account Drops Casual 'Sup' Tweet: What's Behind It?",
    imgUrl = "https://u.today/sites/default/files/styles/736x/public/2025-06/s7426.jpg",
    relatedCoins =
      listOf(
        "dogecoin",
        "0x4206931337dc273a630d328da6441786bfad668f_ethereum",
        "0x1a8e39ae59e5556b56b76fcba98d22c9ae557396_cronos",
      ),
    link =
      "https://u.today/dogecoin-account-drops-casual-sup-tweet-whats-behind-it?utm_medium=referral&utm_source=coinstats",
  )
