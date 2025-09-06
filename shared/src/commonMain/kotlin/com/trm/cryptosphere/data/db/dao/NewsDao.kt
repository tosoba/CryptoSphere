package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trm.cryptosphere.data.db.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
  @Insert suspend fun insert(item: NewsEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(items: List<NewsEntity>)

  @Query("SELECT * FROM news") fun selectAll(): Flow<List<NewsEntity>>

  @Query("SELECT * FROM news LIMIT :pageSize OFFSET :page * :pageSize")
  fun selectPage(page: Int, pageSize: Int): Flow<List<NewsEntity>>

  @Query("DELETE FROM news") suspend fun deleteAll()
}
