package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.trm.cryptosphere.data.db.entity.HistoryEntity
import com.trm.cryptosphere.data.db.entity.HistoryItemEntity
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Dao
interface HistoryDao {
  @Insert suspend fun insertHistory(history: HistoryEntity): Long

  @Upsert suspend fun upsertHistoryItem(historyItem: HistoryItemEntity)

  @Transaction
  suspend fun insertNewHistory(tokenId: Int): Long {
    val historyId = insertHistory(HistoryEntity())
    upsertHistoryItem(
      HistoryItemEntity(historyId = historyId, tokenId = tokenId, lastUpdateAt = nowUtc())
    )
    return historyId
  }

  @Query(
    "DELETE FROM history_item WHERE history_id = :historyId AND last_update_at > :lastUpdateAtAfter"
  )
  suspend fun deleteWhere(historyId: Long, lastUpdateAtAfter: LocalDateTime)

  @Query("SELECT * FROM history_item WHERE history_id = :historyId AND token_id = :tokenId")
  suspend fun getHistoryItem(historyId: Long, tokenId: Int): HistoryItemEntity?

  @Transaction
  suspend fun upsertHistoryItem(historyId: Long, previousItemTokenId: Int, newItemTokenId: Int) {
    getHistoryItem(historyId = historyId, tokenId = previousItemTokenId)?.let {
      deleteWhere(historyId = historyId, lastUpdateAtAfter = it.lastUpdateAt)
    }
    upsertHistoryItem(
      HistoryItemEntity(historyId = historyId, tokenId = newItemTokenId, lastUpdateAt = nowUtc())
    )
  }

  private fun nowUtc(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
}
