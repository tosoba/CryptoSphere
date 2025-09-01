package com.trm.cryptosphere.data.db.converter

import androidx.room.TypeConverter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
class LocalDateTimeConverter {
  @TypeConverter
  fun fromTimestamp(value: Long?): LocalDateTime? =
    value?.let { Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.UTC) }

  @TypeConverter
  fun dateToTimestamp(date: LocalDateTime?): Long? = date?.toInstant(TimeZone.UTC)?.epochSeconds
}
