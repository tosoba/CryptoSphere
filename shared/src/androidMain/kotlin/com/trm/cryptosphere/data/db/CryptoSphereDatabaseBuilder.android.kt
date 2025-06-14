package com.trm.cryptosphere.data.db

import androidx.room.Room
import com.trm.cryptosphere.core.PlatformContext

actual fun buildCryptoSphereDatabase(context: PlatformContext): CryptoSphereDatabase {
  val applicationContext = context.applicationContext
  return Room.databaseBuilder<CryptoSphereDatabase>(
      context = applicationContext,
      name = applicationContext.getDatabasePath(CryptoSphereDatabase.DATABASE_NAME).absolutePath,
    )
    .build()
}
