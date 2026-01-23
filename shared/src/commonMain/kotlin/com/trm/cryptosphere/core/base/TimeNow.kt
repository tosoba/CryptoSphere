package com.trm.cryptosphere.core.base

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun nowInstant(): Instant = Clock.System.now()

fun nowDateTime(): LocalDateTime = nowInstant().toLocalDateTime(TimeZone.currentSystemDefault())
