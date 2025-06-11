package com.trm.cryptosphere.api.coinmarketcap

import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class CoinMarketCapApiTest {
  @Test fun getCryptocurrencies() = runTest { CoinMarketCapApi().getCryptocurrencies(100) }
}
