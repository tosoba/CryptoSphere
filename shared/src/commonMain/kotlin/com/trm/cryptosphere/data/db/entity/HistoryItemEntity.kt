package com.trm.cryptosphere.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.datetime.LocalDateTime

@Entity(
  tableName = "history_item",
  primaryKeys = ["history_id", "token_id"],
  foreignKeys =
    [
      ForeignKey(
        entity = HistoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["history_id"],
        onDelete = ForeignKey.CASCADE,
      ),
      ForeignKey(entity = TokenEntity::class, parentColumns = ["id"], childColumns = ["token_id"]),
    ],
  indices =
    [Index(value = ["history_id"]), Index(value = ["token_id"]), Index(value = ["last_update_at"])],
)
data class HistoryItemEntity(
  @ColumnInfo(name = "history_id") val historyId: Long,
  @ColumnInfo(name = "token_id") val tokenId: Int,
  @ColumnInfo(name = "last_update_at") val lastUpdateAt: LocalDateTime,
)
