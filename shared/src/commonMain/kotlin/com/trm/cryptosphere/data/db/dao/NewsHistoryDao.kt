package com.trm.cryptosphere.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trm.cryptosphere.data.db.entity.NewsHistoryEntity

@Dao
interface NewsHistoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entity: NewsHistoryEntity)

  @Delete suspend fun delete(entity: NewsHistoryEntity)

  @Query("DELETE FROM news_history") suspend fun deleteAll()

  @Query("SELECT * FROM news_history ORDER BY visited_at DESC")
  fun getAllPagingSource(): PagingSource<Int, NewsHistoryEntity>
}
