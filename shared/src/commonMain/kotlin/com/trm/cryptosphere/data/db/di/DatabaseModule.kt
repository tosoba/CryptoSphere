package com.trm.cryptosphere.data.db.di

import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.data.db.buildCryptoSphereDatabase
import com.trm.cryptosphere.data.db.dao.TokenDao

class DatabaseModule(private val context: PlatformContext) {
  private val database by lazy { buildCryptoSphereDatabase(context) }

  fun tokenDao(): TokenDao = database.tokenDao()
}
