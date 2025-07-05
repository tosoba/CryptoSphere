package com.trm.cryptosphere.ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.ChildNavState
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedComponent
import com.trm.cryptosphere.ui.home.page.history.HistoryComponent
import com.trm.cryptosphere.ui.home.page.prices.PricesComponent
import com.trm.cryptosphere.ui.home.page.search.SearchComponent

class HomeDefaultComponent(
  componentContext: ComponentContext,
  private val onTokenClick: (String) -> Unit,
  private val newsFeedComponentFactory: NewsFeedComponent.Factory,
  private val createPricesComponent: (ComponentContext) -> PricesComponent,
  private val createSearchComponent: (ComponentContext) -> SearchComponent,
  private val createHistoryComponent: (ComponentContext) -> HistoryComponent,
) : HomeComponent, ComponentContext by componentContext {
  private val navigation = PagesNavigation<HomePageConfig>()

  override val pages: Value<ChildPages<*, HomeComponent.Page>> =
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
          newsFeedComponentFactory(componentContext = componentContext, onTokenClick = onTokenClick)
        )
      }
      HomePageConfig.PRICES -> {
        HomeComponent.Page.Prices(createPricesComponent(componentContext))
      }
      HomePageConfig.SEARCH -> {
        HomeComponent.Page.Search(createSearchComponent(componentContext))
      }
      HomePageConfig.HISTORY -> {
        HomeComponent.Page.History(createHistoryComponent(componentContext))
      }
    }
}
