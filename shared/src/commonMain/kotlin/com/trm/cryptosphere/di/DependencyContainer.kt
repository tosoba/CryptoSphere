package com.trm.cryptosphere.di

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.db.CryptoSphereDatabase
import com.trm.cryptosphere.data.db.buildCryptoSphereDatabase
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.home.HomeDefaultComponent
import com.trm.cryptosphere.ui.root.RootComponent
import com.trm.cryptosphere.ui.root.RootDefaultComponent
import com.trm.cryptosphere.ui.token.TokenComponent
import com.trm.cryptosphere.ui.token.TokenDefaultComponent

/**
 * Only data layer dependencies are passed as arguments to DependencyContainer so they can be
 * swapped with fakes (on DI-level) for a larger A-B/integration test if needed (other swappable
 * dependencies will be added as args if needed in the future).
 */
class DependencyContainer(
  private val context: PlatformContext,
  private val coinStatsApi: Lazy<CoinStatsApi> = lazy { CoinStatsApi() },
  private val coinMarketCapApi: Lazy<CoinMarketCapApi> = lazy { CoinMarketCapApi() },
  private val database: Lazy<CryptoSphereDatabase> = lazy { buildCryptoSphereDatabase(context) },
  val createHomeComponent: (ComponentContext) -> HomeComponent = ::HomeDefaultComponent,
  val createTokenComponent: (ComponentContext) -> TokenComponent = ::TokenDefaultComponent,
  val createRootComponent: (ComponentContext) -> RootComponent =
    { componentContext: ComponentContext ->
      RootDefaultComponent(componentContext, createHomeComponent, createTokenComponent)
    },
) {
  /**
   * Default decompose components (some of them at least)/UseCases will be created inside AppModule
   * because they will not be swapped on DI-level.
   */
}
