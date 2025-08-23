package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.trm.cryptosphere.data.db.entity.CategoryEntity

@Dao
interface CategoryDao {
  @Insert suspend fun insert(item: CategoryEntity)
}
