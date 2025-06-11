package com.trm.cryptosphere.api.coinstats

import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class CoinStatsApiTest {
  @Test fun getNews() = runTest { CoinStatsApi().getNews() }
}
