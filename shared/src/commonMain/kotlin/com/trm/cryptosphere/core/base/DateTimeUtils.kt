package com.trm.cryptosphere.core.base

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun nowDateTime(timeZone: TimeZone = TimeZone.UTC): LocalDateTime {
  @OptIn(ExperimentalTime::class)
  return Clock.System.now().toLocalDateTime(timeZone)
}
