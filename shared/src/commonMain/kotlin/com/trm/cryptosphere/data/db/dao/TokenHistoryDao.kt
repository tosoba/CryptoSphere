package com.trm.cryptosphere.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trm.cryptosphere.data.db.entity.TokenHistoryEntity
import com.trm.cryptosphere.data.db.entity.junction.TokenHistoryWithTokenJunction

@Dao
interface TokenHistoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entity: TokenHistoryEntity)

  @Delete suspend fun delete(entity: TokenHistoryEntity)

  @Query("DELETE FROM token_history WHERE id = :id") suspend fun deleteById(id: Long)

  @Query("DELETE FROM token_history") suspend fun deleteAll()

  @Query(
    """
        SELECT * FROM token_history 
        INNER JOIN token ON token_history.token_id = token.id
        WHERE LOWER(token.name) LIKE '%' || LOWER(:query) || '%' OR LOWER(token.symbol) LIKE '%' || LOWER(:query) || '%' OR LOWER(token.slug) LIKE '%' || LOWER(:query) || '%'
        ORDER BY visited_at DESC
    """
  )
  fun selectAll(query: String): PagingSource<Int, TokenHistoryWithTokenJunction>
}
