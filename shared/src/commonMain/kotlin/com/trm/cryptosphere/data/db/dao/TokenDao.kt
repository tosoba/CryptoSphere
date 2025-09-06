package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokenItem
import com.trm.cryptosphere.data.db.entity.TagEntity
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.entity.TokenTagEntity
import com.trm.cryptosphere.data.db.mapper.toEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertToken(item: TokenEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTokens(items: List<TokenEntity>)

  @Query("SELECT * FROM token") fun selectAllTokens(): Flow<List<TokenEntity>>

  @Query("DELETE FROM token") suspend fun deleteAllTokens()

  @Query("SELECT COUNT(*) FROM token") suspend fun selectTokensCount(): Int

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertTags(tags: List<TagEntity>): List<Long>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertTokenTags(tokenTags: List<TokenTagEntity>): List<Long>

  @Delete suspend fun deleteTokenTags(tokenTags: List<TokenTagEntity>)

  @Query("DELETE FROM token_tag WHERE token_id = :tokenId")
  suspend fun deleteTokenTagsByTokenId(tokenId: Int)

  @Transaction
  suspend fun insertTokenWithTags(cmcToken: CmcTokenItem) {
    insertToken(cmcToken.toEntity())
    insertTags(cmcToken.tags.map { TagEntity(name = it) })
    insertTokenTags(cmcToken.tags.map { TokenTagEntity(tokenId = cmcToken.id, tagName = it) })
  }

  @Transaction
  suspend fun insertTokensWithTags(cmcTokens: List<CmcTokenItem>) {
    insertTags(cmcTokens.flatMap(CmcTokenItem::tags).map { TagEntity(name = it) })
    insertTokens(cmcTokens.map(CmcTokenItem::toEntity))
    insertTokenTags(
      cmcTokens.flatMap { cmcToken ->
        cmcToken.tags.map { TokenTagEntity(tokenId = cmcToken.id, tagName = it) }
      }
    )
  }
}
