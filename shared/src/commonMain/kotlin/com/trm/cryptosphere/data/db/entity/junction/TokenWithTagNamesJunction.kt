package com.trm.cryptosphere.data.db.entity.junction

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.trm.cryptosphere.data.db.entity.TokenEntity

data class TokenWithTagNamesJunction(
  @Embedded val token: TokenEntity,
  @ColumnInfo(name = "tag_names") val tagNames: String?,
)
