package com.trm.cryptosphere.ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.ChildNavState
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.ui.home.page.history.HistoryComponent
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedComponent
import com.trm.cryptosphere.ui.home.page.prices.PricesComponent

class HomeDefaultComponent(
  componentContext: ComponentContext,
  private val onTokenClick: (Int, TokenCarouselConfig) -> Unit,
  private val newsFeedComponentFactory: NewsFeedComponent.Factory,
  private val pricesComponentFactory: PricesComponent.Factory,
  private val historyComponentFactory: HistoryComponent.Factory,
  override val onSeedImageUrlChange: (String?) -> Unit,
) : HomeComponent, ComponentContext by componentContext {
  private val navigation = PagesNavigation<HomePageConfig>()

  override val pages: Value<ChildPages<HomePageConfig, HomeComponent.Page>> =
    childPages(
      source = navigation,
      serializer = HomePageConfig.serializer(),
      initialPages = { Pages(items = HomePageConfig.entries, selectedIndex = 0) },
      pageStatus = { index, pages ->
        if (index == pages.selectedIndex) ChildNavState.Status.RESUMED
        else ChildNavState.Status.CREATED
      },
      childFactory = ::createPage,
    )

  override fun selectPage(index: Int) {
    navigation.select(index)
  }

  private fun createPage(
    config: HomePageConfig,
    componentContext: ComponentContext,
  ): HomeComponent.Page =
    when (config) {
      HomePageConfig.FEED -> {
        HomeComponent.Page.NewsFeed(
          newsFeedComponentFactory(
            componentContext = componentContext,
            onTokenCarouselItemClick = onTokenClick,
            onSeedImageUrlChange = onSeedImageUrlChange,
          )
        )
      }
      HomePageConfig.PRICES -> {
        HomeComponent.Page.Prices(
          pricesComponentFactory(componentContext = componentContext, onTokenClick = onTokenClick)
        )
      }
      HomePageConfig.HISTORY -> {
        HomeComponent.Page.History(
          historyComponentFactory(componentContext = componentContext, onTokenClick = onTokenClick)
        )
      }
    }
}
