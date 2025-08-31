package com.trm.cryptosphere.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.trm.cryptosphere.data.db.converter.StringListConverter
import com.trm.cryptosphere.data.db.dao.CategoryDao
import com.trm.cryptosphere.data.db.dao.TokenDao
import com.trm.cryptosphere.data.db.entity.CategoryEntity
import com.trm.cryptosphere.data.db.entity.TagEntity
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.entity.TokenTagEntity

@Database(
  entities = [TokenEntity::class, CategoryEntity::class, TagEntity::class, TokenTagEntity::class],
  version = 1,
)
@ConstructedBy(CryptoSphereDatabaseConstructor::class)
@TypeConverters(StringListConverter::class)
abstract class CryptoSphereDatabase : RoomDatabase() {
  abstract fun tokenDao(): TokenDao

  abstract fun categoryDao(): CategoryDao

  companion object {
    const val DATABASE_NAME = "cryptosphere.db"
  }
}

@Suppress("KotlinNoActualForExpect")
internal expect object CryptoSphereDatabaseConstructor :
  RoomDatabaseConstructor<CryptoSphereDatabase> {
  override fun initialize(): CryptoSphereDatabase
}
