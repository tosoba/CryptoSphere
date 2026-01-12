package com.trm.cryptosphere.data.api.coinmarketcap

import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class CoinMarketCapApiTest {
  @Test fun getTokens() = runTest { CoinMarketCapApi().getTokens(100) }

  @Test
  fun getTokensInfo() = runTest {
    CoinMarketCapApi().getTokensInfo(id = List(100) { it + 1 }.joinToString(","))
  }
}
