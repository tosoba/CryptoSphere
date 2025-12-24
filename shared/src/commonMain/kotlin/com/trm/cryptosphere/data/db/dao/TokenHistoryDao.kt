package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trm.cryptosphere.data.db.entity.TokenHistoryEntity

@Dao
interface TokenHistoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entity: TokenHistoryEntity)

  @Delete suspend fun delete(entity: TokenHistoryEntity)

  @Query("DELETE FROM token_history") suspend fun deleteAll()
}
