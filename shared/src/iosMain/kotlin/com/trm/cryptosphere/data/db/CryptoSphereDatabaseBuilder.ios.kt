package com.trm.cryptosphere.data.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.trm.cryptosphere.core.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun buildCryptoSphereDatabase(context: PlatformContext): CryptoSphereDatabase =
  Room.databaseBuilder<CryptoSphereDatabase>(
      name = "${documentDirectory()}${CryptoSphereDatabase.DATABASE_NAME}"
    )
    .setDriver(BundledSQLiteDriver())
    .build()

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String =
  requireNotNull(
    NSFileManager.defaultManager
      .URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
      )
      ?.path
  )
