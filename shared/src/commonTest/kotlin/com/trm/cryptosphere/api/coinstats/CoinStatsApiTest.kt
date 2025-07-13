package com.trm.cryptosphere.api.coinstats

import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class CoinStatsApiTest {
  @Test fun getNews() = runTest { CoinStatsApi().getNews() }
}
