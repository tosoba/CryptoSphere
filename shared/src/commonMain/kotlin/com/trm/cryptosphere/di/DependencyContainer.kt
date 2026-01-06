package com.trm.cryptosphere.di

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.cache.disk.createDiskCacheStorage
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.db.CryptoSphereDatabase
import com.trm.cryptosphere.data.db.buildCryptoSphereDatabase
import com.trm.cryptosphere.data.repository.NewsDefaultRepository
import com.trm.cryptosphere.data.repository.NewsHistoryDefaultRepository
import com.trm.cryptosphere.data.repository.TokenDefaultRepository
import com.trm.cryptosphere.data.repository.TokenHistoryDefaultRepository
import com.trm.cryptosphere.domain.manager.BackgroundJobsManager
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import com.trm.cryptosphere.domain.repository.NewsRepository
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
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
import com.trm.cryptosphere.ui.root.RootComponent
import com.trm.cryptosphere.ui.root.RootDefaultComponent
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import com.trm.cryptosphere.ui.token.feed.TokenFeedDefaultComponent
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationComponent
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationDefaultComponent
import io.ktor.client.plugins.cache.storage.CacheStorage

class DependencyContainer(
  private val context: PlatformContext,
  private val backgroundJobsManager: BackgroundJobsManager,
  private val appCoroutineDispatchers: AppCoroutineDispatchers = AppCoroutineDispatchers.default(),
  private val diskCacheStorage: CacheStorage = context.createDiskCacheStorage(),
  private val coinStatsApi: Lazy<CoinStatsApi> = lazy { CoinStatsApi(diskCacheStorage) },
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
  private val tokenHistoryRepository: Lazy<TokenHistoryRepository> = lazy {
    TokenHistoryDefaultRepository(database.value.tokenHistoryDao())
  },
  private val newsHistoryRepository: Lazy<NewsHistoryRepository> = lazy {
    NewsHistoryDefaultRepository(database.value.newsHistoryDao())
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
        newsHistoryRepository = newsHistoryRepository.value,
        dispatchers = appCoroutineDispatchers,
      )
    },
  private val pricesComponentFactory: PricesComponent.Factory =
    PricesComponent.Factory { componentContext, onTokenClick ->
      PricesDefaultComponent(
        componentContext = componentContext,
        onTokenClick = onTokenClick,
        tokenRepository = tokenRepository.value,
        dispatchers = appCoroutineDispatchers,
      )
    },
  private val historyComponentFactory: HistoryComponent.Factory =
    HistoryComponent.Factory { componentContext ->
      HistoryDefaultComponent(
        componentContext = componentContext,
        newsHistoryRepository = newsHistoryRepository.value,
        tokenHistoryRepository = tokenHistoryRepository.value,
        dispatchers = appCoroutineDispatchers,
      )
    },
  val homeComponentFactory: HomeComponent.Factory =
    HomeComponent.Factory { componentContext, onTokenClick ->
      HomeDefaultComponent(
        componentContext = componentContext,
        onTokenClick = onTokenClick,
        newsFeedComponentFactory = newsFeedComponentFactory,
        pricesComponentFactory = pricesComponentFactory,
        historyComponentFactory = historyComponentFactory,
      )
    },
  val tokenFeedComponentFactory: TokenFeedComponent.Factory =
    TokenFeedComponent.Factory { componentContext, tokenId, onCurrentFeedTokenChange ->
      TokenFeedDefaultComponent(
        componentContext = componentContext,
        tokenId = tokenId,
        tokenRepository = tokenRepository.value,
        dispatchers = appCoroutineDispatchers,
        onCurrentFeedTokenChange = onCurrentFeedTokenChange,
      )
    },
  val tokenNavigationComponentFactory: TokenNavigationComponent.Factory =
    TokenNavigationComponent.Factory { componentContext, tokenId, tokenCarouselConfig, navigateHome
      ->
      TokenNavigationDefaultComponent(
        componentContext = componentContext,
        tokenId = tokenId,
        tokenCarouselConfig = tokenCarouselConfig,
        tokenFeedComponentFactory = tokenFeedComponentFactory,
        tokenRepository = tokenRepository.value,
        tokenHistoryRepository = tokenHistoryRepository.value,
        dispatchers = appCoroutineDispatchers,
        navigateHome = navigateHome,
      )
    },
  val createRootComponent: (ComponentContext) -> RootComponent = { componentContext ->
    RootDefaultComponent(
      componentContext = componentContext,
      homeComponentFactory = homeComponentFactory,
      tokenNavigationComponentFactory = tokenNavigationComponentFactory,
      tokenHistoryRepository = tokenHistoryRepository.value,
      dispatchers = appCoroutineDispatchers,
    )
  },
)
