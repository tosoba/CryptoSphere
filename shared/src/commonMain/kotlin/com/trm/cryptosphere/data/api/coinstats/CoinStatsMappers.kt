package com.trm.cryptosphere.data.api.coinstats

import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsItem
import com.trm.cryptosphere.domain.model.NewsItem
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
fun CoinStatsNewsItem.toNewsItem(): NewsItem =
  NewsItem(
    id = id,
    searchKeyWords = searchKeyWords,
    feedDate = Instant.fromEpochMilliseconds(feedDate).toLocalDateTime(TimeZone.UTC),
    source = source,
    title = title,
    sourceLink = sourceLink,
    description = source,
    imgUrl = imgUrl,
    relatedCoins = relatedCoins.orEmpty(),
    link = link,
  )
