package com.trm.cryptosphere.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokenItem
import com.trm.cryptosphere.data.db.entity.TagEntity
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.entity.TokenTagEntity
import com.trm.cryptosphere.data.db.entity.junction.TokenWithTagNamesJunction
import com.trm.cryptosphere.data.db.mapper.toEntity

@Dao
interface TokenDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTokens(items: List<TokenEntity>)

  @Query(
    """
    SELECT * FROM token 
    WHERE (name IN (:searchTerms) OR symbol IN (:searchTerms)) 
    AND usd_quote_market_cap > 0 
    AND circulating_supply > 0
    AND id IN (
      SELECT id FROM token AS t1
      WHERE cmc_rank = (SELECT MIN(cmc_rank) FROM token AS t2 WHERE t2.name = t1.name)
    )
    AND id IN (
      SELECT id FROM token AS t1
      WHERE cmc_rank = (SELECT MIN(cmc_rank) FROM token AS t2 WHERE t2.symbol = t1.symbol)
    )
    AND NOT EXISTS (
      SELECT 1 FROM token AS t2
      WHERE LOWER(t2.name) = LOWER(token.symbol) 
      AND t2.cmc_rank < token.cmc_rank
    )
    ORDER BY cmc_rank
    """
  )
  suspend fun selectTokensByNameOrSymbol(searchTerms: List<String>): List<TokenEntity>

  @Query("DELETE FROM token") suspend fun deleteAllTokens()

  @Query("SELECT COUNT(*) FROM token") suspend fun selectTokensCount(): Int

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertTags(tags: List<TagEntity>): List<Long>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertTokenTags(tokenTags: List<TokenTagEntity>): List<Long>

  @Transaction
  suspend fun insertTokensWithTags(cmcTokens: List<CmcTokenItem>) {
    deleteAllTokens()
    insertTags(cmcTokens.flatMap(CmcTokenItem::tags).map { TagEntity(name = it) })
    insertTokens(cmcTokens.map(CmcTokenItem::toEntity))
    insertTokenTags(
      cmcTokens.flatMap { cmcToken ->
        cmcToken.tags.map { TokenTagEntity(tokenId = cmcToken.id, tagName = it) }
      }
    )
  }

  @Query(
    """
    SELECT T2.*, GROUP_CONCAT(TT2.tag_name) as tag_names
    FROM token AS T1
    JOIN token_tag AS TT1 ON T1.id = TT1.token_id
    JOIN token_tag AS TT2 ON TT1.tag_name = TT2.tag_name
    JOIN token AS T2 ON TT2.token_id = T2.id
    WHERE T1.id = :id
    AND T2.usd_quote_market_cap > 0 
    AND T2.circulating_supply > 0
    AND T2.id IN (
      SELECT id FROM token AS t1
      WHERE cmc_rank = (SELECT MIN(cmc_rank) FROM token AS t2 WHERE t2.name = t1.name)
    )
    AND T2.id IN (
      SELECT id FROM token AS t1
      WHERE cmc_rank = (SELECT MIN(cmc_rank) FROM token AS t2 WHERE t2.symbol = t1.symbol)
    )
    AND NOT EXISTS (
      SELECT 1 FROM token AS t2
      WHERE LOWER(t2.name) = LOWER(T2.symbol) 
      AND t2.cmc_rank < T2.cmc_rank
    )
    GROUP BY T2.id
    ORDER BY CASE WHEN T2.id = :id THEN 0 ELSE 1 END, COUNT(TT2.tag_name) DESC, T2.cmc_rank
    """
  )
  fun selectTokensBySharedTags(id: Int): PagingSource<Int, TokenWithTagNamesJunction>
}
