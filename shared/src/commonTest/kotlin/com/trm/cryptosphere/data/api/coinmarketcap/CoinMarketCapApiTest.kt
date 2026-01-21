package com.trm.cryptosphere.data.api.coinmarketcap

import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class CoinMarketCapApiTest {
  @Test fun getTokens() = runTest { CoinMarketCapApi().getTokens(100) }

  @Test
  fun getTokensInfo() = runTest {
    CoinMarketCapApi().getTokensInfo(id = List(100) { it + 1 }.joinToString(","))
  }
}
