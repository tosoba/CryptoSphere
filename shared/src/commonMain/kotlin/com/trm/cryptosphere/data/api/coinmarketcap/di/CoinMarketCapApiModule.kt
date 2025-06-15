package com.trm.cryptosphere.data.api.coinmarketcap.di

import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi

class CoinMarketCapApiModule {
  val coinMarketCapApi: CoinMarketCapApi by lazy { CoinMarketCapApi() }
}
