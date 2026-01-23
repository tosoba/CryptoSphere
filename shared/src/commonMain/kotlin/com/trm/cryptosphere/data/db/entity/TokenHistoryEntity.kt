package com.trm.cryptosphere.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(
  tableName = "token_history",
  foreignKeys =
    [ForeignKey(entity = TokenEntity::class, parentColumns = ["id"], childColumns = ["token_id"])],
  indices = [Index(value = ["token_id"], unique = true)],
)
data class TokenHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  @ColumnInfo(name = "token_id") val tokenId: Int,
  @ColumnInfo(name = "visited_at") val visitedAt: Instant,
)
