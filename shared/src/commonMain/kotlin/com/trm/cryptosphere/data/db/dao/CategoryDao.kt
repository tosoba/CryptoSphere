package com.trm.cryptosphere.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trm.cryptosphere.data.db.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
  @Insert suspend fun insert(item: CategoryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(items: List<CategoryEntity>)

  @Query("SELECT * FROM category") fun selectAll(): Flow<List<CategoryEntity>>

  @Query("DELETE FROM category") suspend fun deleteAll()
}
