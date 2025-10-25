package com.trm.cryptosphere.di

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.db.CryptoSphereDatabase
import com.trm.cryptosphere.data.db.buildCryptoSphereDatabase
import com.trm.cryptosphere.data.repository.NewsDefaultRepository
import com.trm.cryptosphere.data.repository.TokenDefaultRepository
import com.trm.cryptosphere.domain.manager.BackgroundJobsManager
import com.trm.cryptosphere.domain.repository.NewsRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import com.trm.cryptosphere.domain.usecase.EnqueuePeriodicTokensSyncUseCase
import com.trm.cryptosphere.domain.usecase.GetNewsFeedUseCase
import com.trm.cryptosphere.domain.usecase.PerformInitialTokensSyncUseCase
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

class DependencyContainer(
  private val context: PlatformContext,
  private val backgroundJobsManager: BackgroundJobsManager,
  private val appCoroutineDispatchers: AppCoroutineDispatchers = AppCoroutineDispatchers.default(),
  private val coinStatsApi: Lazy<CoinStatsApi> = lazy { CoinStatsApi() },
  private val coinMarketCapApi: Lazy<CoinMarketCapApi> = lazy { CoinMarketCapApi() },
  private val database: Lazy<CryptoSphereDatabase> = lazy { buildCryptoSphereDatabase(context) },
  val tokenRepository: Lazy<TokenRepository> = lazy {
    TokenDefaultRepository(
      dao = database.value.tokenDao(),
      coinMarketCapApi = coinMarketCapApi.value,
    )
  },
  private val newsRepository: Lazy<NewsRepository> = lazy {
    NewsDefaultRepository(coinStatsApi.value)
  },
  val enqueuePeriodicTokensSyncUseCase: Lazy<EnqueuePeriodicTokensSyncUseCase> = lazy {
    EnqueuePeriodicTokensSyncUseCase(tokenRepository.value, backgroundJobsManager)
  },
  private val performInitialTokensSyncUseCase: Lazy<PerformInitialTokensSyncUseCase> = lazy {
    PerformInitialTokensSyncUseCase(tokenRepository.value, backgroundJobsManager)
  },
  private val getNewsFeedUseCase: Lazy<GetNewsFeedUseCase> = lazy {
    GetNewsFeedUseCase(
      performInitialTokensSyncUseCase = performInitialTokensSyncUseCase.value,
      newsRepository = newsRepository.value,
      tokenRepository = tokenRepository.value,
    )
  },
  private val newsFeedComponentFactory: NewsFeedComponent.Factory =
    NewsFeedComponent.Factory { componentContext, onTokenCarouselItemClick ->
      NewsFeedDefaultComponent(
        componentContext = componentContext,
        onTokenCarouselItemClick = onTokenCarouselItemClick,
        getNewsFeedUseCase = getNewsFeedUseCase.value,
        dispatchers = appCoroutineDispatchers,
      )
    },
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
    TokenFeedComponent.Factory {
      componentContext,
      mainTokenId,
      tokenCarouselConfig,
      navigateToTokenFeed,
      navigateToTokenDetails ->
      TokenFeedDefaultComponent(
        componentContext = componentContext,
        mainTokenId = mainTokenId,
        tokenCarouselConfig = tokenCarouselConfig,
        navigateToTokenFeed = navigateToTokenFeed,
        navigateToTokenDetails = navigateToTokenDetails,
        tokenRepository = tokenRepository.value,
        dispatchers = appCoroutineDispatchers,
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
)
