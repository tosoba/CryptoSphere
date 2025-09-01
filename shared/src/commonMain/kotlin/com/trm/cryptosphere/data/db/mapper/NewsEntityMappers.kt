package com.trm.cryptosphere.data.db.mapper

import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsItem
import com.trm.cryptosphere.data.db.entity.NewsEntity
import com.trm.cryptosphere.domain.model.NewsItem
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun CoinStatsNewsItem.toEntity(): NewsEntity =
  NewsEntity(
    id = id,
    searchKeyWords = searchKeyWords,
    feedDate =
      @OptIn(ExperimentalTime::class)
      Instant.fromEpochMilliseconds(feedDate).toLocalDateTime(TimeZone.UTC),
    source = source,
    title = title,
    sourceLink = sourceLink,
    imgUrl = imgUrl,
    relatedCoins = relatedCoins.orEmpty(),
    link = link,
  )

fun NewsEntity.toNewsItem(): NewsItem =
  NewsItem(
    id = id,
    searchKeyWords = searchKeyWords,
    feedDate = feedDate,
    source = source,
    title = title,
    sourceLink = sourceLink,
    imgUrl = imgUrl,
    relatedCoins = relatedCoins,
    link = link,
  )
