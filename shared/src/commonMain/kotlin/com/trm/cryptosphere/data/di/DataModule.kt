package com.trm.cryptosphere.data.di

import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.data.api.coinmarketcap.di.CoinMarketCapApiModule
import com.trm.cryptosphere.data.api.coinstats.di.CoinStatsApiModule
import com.trm.cryptosphere.data.db.di.DatabaseModule

class DataModule(
  private val coinStatsApiModule: CoinStatsApiModule,
  private val coinMarketCapApiModule: CoinMarketCapApiModule,
  private val databaseModule: DatabaseModule,
) {
  companion object {
    fun create(context: PlatformContext): DataModule =
      DataModule(
        coinStatsApiModule = CoinStatsApiModule(),
        coinMarketCapApiModule = CoinMarketCapApiModule(),
        databaseModule = DatabaseModule(context),
      )
  }
}
