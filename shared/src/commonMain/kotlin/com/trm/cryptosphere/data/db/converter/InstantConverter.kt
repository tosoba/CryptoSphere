package com.trm.cryptosphere.data.db.converter

import androidx.room.TypeConverter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class InstantConverter {
  @TypeConverter fun fromTimestamp(value: Long?): Instant? = value?.let(Instant::fromEpochSeconds)

  @TypeConverter fun instantToTimestamp(instant: Instant?): Long? = instant?.epochSeconds
}
