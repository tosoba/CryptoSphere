package com.trm.cryptosphere.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
  tableName = "token_tag",
  primaryKeys = ["token_id", "tag_name"],
  foreignKeys =
    [
      ForeignKey(
        entity = TokenEntity::class,
        parentColumns = ["id"],
        childColumns = ["token_id"],
        onDelete = ForeignKey.CASCADE,
      ),
      ForeignKey(
        entity = TagEntity::class,
        parentColumns = ["name"],
        childColumns = ["tag_name"],
        onDelete = ForeignKey.CASCADE,
      ),
    ],
  indices =
    [
      Index(value = ["token_id"]),
      Index(value = ["tag_name"]),
      Index(value = ["token_id", "tag_name"]),
    ],
)
data class TokenTagEntity(
  @ColumnInfo(name = "token_id") val tokenId: Int,
  @ColumnInfo(name = "tag_name") val tagName: String,
)
