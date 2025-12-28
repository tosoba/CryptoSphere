package com.trm.cryptosphere.data.db.mapper

import com.trm.cryptosphere.data.db.entity.junction.TokenHistoryWithTokenJunction
import com.trm.cryptosphere.domain.model.TokenHistoryItem

fun TokenHistoryWithTokenJunction.toDomain() =
  TokenHistoryItem(
    id = history.id,
    tokenId = history.tokenId,
    tokenName = token.name,
    tokenSymbol = token.symbol,
    visitedAt = history.visitedAt,
  )
