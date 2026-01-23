package com.trm.cryptosphere.data.db.mapper

import com.trm.cryptosphere.data.db.entity.NewsHistoryEntity
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun NewsHistoryEntity.toDomain() =
  NewsHistoryItem(
    id = id,
    title = title,
    source = source,
    url = url,
    imgUrl = imgUrl,
    visitedAt = visitedAt.toLocalDateTime(TimeZone.currentSystemDefault()),
  )
