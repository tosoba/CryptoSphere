package com.trm.cryptosphere.data.db.entity.junction

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.trm.cryptosphere.data.db.entity.TokenEntity

data class TokenWithTagNamesJunction(
  @Embedded val token: TokenEntity,
  @ColumnInfo(name = "all_tag_names") val allTagNames: String?,
  @ColumnInfo(name = "shared_tag_names") val sharedTagNames: String?,
)
