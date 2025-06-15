package com.trm.cryptosphere.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.trm.cryptosphere.data.db.converter.StringListConverter
import com.trm.cryptosphere.data.db.dao.TokenDao
import com.trm.cryptosphere.data.db.entity.TokenEntity

@Database(entities = [TokenEntity::class], version = 1)
@ConstructedBy(CryptoSphereDatabaseConstructor::class)
@TypeConverters(StringListConverter::class)
abstract class CryptoSphereDatabase : RoomDatabase() {
  abstract fun tokenDao(): TokenDao

  companion object {
    const val DATABASE_NAME = "cryptosphere.db"
  }
}

@Suppress("KotlinNoActualForExpect")
internal expect object CryptoSphereDatabaseConstructor :
  RoomDatabaseConstructor<CryptoSphereDatabase> {
  override fun initialize(): CryptoSphereDatabase
}
