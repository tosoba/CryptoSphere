package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.trm.cryptosphere.data.db.entity.TokenEntity

@Dao
interface TokenDao {
  @Insert suspend fun insert(item: TokenEntity)
}
