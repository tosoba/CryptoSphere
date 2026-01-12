package com.trm.cryptosphere.data.api.coinstats

import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class CoinStatsApiTest {
  @Test fun getNews() = runTest { CoinStatsApi(null).getNews() }
}
