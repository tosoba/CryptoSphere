package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trm.cryptosphere.data.db.entity.TokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(item: TokenEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(items: List<TokenEntity>)

  @Query("SELECT * FROM TokenEntity") fun selectAll(): Flow<List<TokenEntity>>

  @Query("DELETE FROM TokenEntity") suspend fun deleteAll()
}
