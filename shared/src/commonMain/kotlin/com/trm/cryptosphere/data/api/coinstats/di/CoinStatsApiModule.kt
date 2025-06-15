package com.trm.cryptosphere.data.api.coinstats.di

import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi

class CoinStatsApiModule {
  val coinStatsApi: CoinStatsApi by lazy { CoinStatsApi() }
}
