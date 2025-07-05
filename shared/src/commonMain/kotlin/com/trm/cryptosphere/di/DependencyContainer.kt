package com.trm.cryptosphere.di

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.db.CryptoSphereDatabase
import com.trm.cryptosphere.data.db.buildCryptoSphereDatabase
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.home.HomeDefaultComponent
import com.trm.cryptosphere.ui.home.page.history.HistoryComponent
import com.trm.cryptosphere.ui.home.page.history.HistoryDefaultComponent
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedComponent
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedDefaultComponent
import com.trm.cryptosphere.ui.home.page.prices.PricesComponent
import com.trm.cryptosphere.ui.home.page.prices.PricesDefaultComponent
import com.trm.cryptosphere.ui.home.page.search.SearchComponent
import com.trm.cryptosphere.ui.home.page.search.SearchDefaultComponent
import com.trm.cryptosphere.ui.root.RootComponent
import com.trm.cryptosphere.ui.root.RootDefaultComponent
import com.trm.cryptosphere.ui.token.details.TokenDetailsComponent
import com.trm.cryptosphere.ui.token.details.TokenDetailsDefaultComponent
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import com.trm.cryptosphere.ui.token.feed.TokenFeedDefaultComponent

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
  private val newsFeedComponentFactory: NewsFeedComponent.Factory =
    NewsFeedComponent.Factory(::NewsFeedDefaultComponent),
  private val createPricesComponent: (ComponentContext) -> PricesComponent =
    ::PricesDefaultComponent,
  private val createSearchComponent: (ComponentContext) -> SearchComponent =
    ::SearchDefaultComponent,
  private val createHistoryComponent: (ComponentContext) -> HistoryComponent =
    ::HistoryDefaultComponent,
  val homeComponentFactory: HomeComponent.Factory =
    HomeComponent.Factory { componentContext, onTokenCarouselItemClick ->
      HomeDefaultComponent(
        componentContext = componentContext,
        onTokenCarouselItemClick = onTokenCarouselItemClick,
        newsFeedComponentFactory = newsFeedComponentFactory,
        createPricesComponent = createPricesComponent,
        createSearchComponent = createSearchComponent,
        createHistoryComponent = createHistoryComponent,
      )
    },
  val tokenDetailsComponentFactory: TokenDetailsComponent.Factory =
    TokenDetailsComponent.Factory(::TokenDetailsDefaultComponent),
  val tokenFeedComponentFactory: TokenFeedComponent.Factory =
    TokenFeedComponent.Factory { componentContext, mainTokenSymbol, tokenCarouselItems ->
      TokenFeedDefaultComponent(
        componentContext = componentContext,
        mainTokenSymbol = mainTokenSymbol,
        tokenCarouselItems = tokenCarouselItems,
      )
    },
  val createRootComponent: (ComponentContext) -> RootComponent = { componentContext ->
    RootDefaultComponent(
      componentContext = componentContext,
      homeComponentFactory = homeComponentFactory,
      tokenFeedComponentFactory = tokenFeedComponentFactory,
      tokenDetailsComponentFactory = tokenDetailsComponentFactory,
    )
  },
) {
  /**
   * Default decompose components (some of them at least)/UseCases will be created inside AppModule
   * because they will not be swapped on DI-level.
   */
}
