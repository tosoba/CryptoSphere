package com.trm.cryptosphere.data.db.entity.junction

import androidx.room.Embedded
import androidx.room.Relation
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.entity.TokenHistoryEntity

data class TokenHistoryWithTokenJunction(
  @Embedded val history: TokenHistoryEntity,
  @Relation(parentColumn = "token_id", entityColumn = "id") val token: TokenEntity,
)
