package com.trm.cryptosphere.domain.model

import kotlinx.datetime.LocalDateTime

data class TokenHistoryItem(val id: Long, val token: TokenItem, val visitedAt: LocalDateTime)
