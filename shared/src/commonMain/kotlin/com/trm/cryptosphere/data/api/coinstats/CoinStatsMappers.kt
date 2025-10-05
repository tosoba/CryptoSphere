package com.trm.cryptosphere.data.api.coinstats

import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsItem
import com.trm.cryptosphere.domain.model.NewsItem
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun CoinStatsNewsItem.toNewsItem(): NewsItem =
  NewsItem(
    id = id,
    searchKeyWords = searchKeyWords,
    feedDate = Instant.fromEpochMilliseconds(feedDate).toLocalDateTime(TimeZone.UTC),
    source = source,
    title = title,
    sourceLink = sourceLink,
    imgUrl = imgUrl,
    relatedCoins = relatedCoins.orEmpty(),
    link = link,
    fetchedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
  )
