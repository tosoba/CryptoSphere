package com.trm.cryptosphere.data.db.mapper

import com.trm.cryptosphere.data.db.entity.junction.TokenHistoryWithTokenJunction
import com.trm.cryptosphere.domain.model.TokenHistoryItem

fun TokenHistoryWithTokenJunction.toDomain() =
  TokenHistoryItem(id = history.id, token = token.toTokenItem(), visitedAt = history.visitedAt)
