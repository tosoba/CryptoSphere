package com.trm.cryptosphere.ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.ChildNavState
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.ui.home.page.feed.FeedDefaultComponent
import com.trm.cryptosphere.ui.home.page.history.HistoryDefaultComponent
import com.trm.cryptosphere.ui.home.page.prices.PricesDefaultComponent
import com.trm.cryptosphere.ui.home.page.search.SearchDefaultComponent
import kotlinx.serialization.Serializable

class HomeDefaultComponent(componentContext: ComponentContext) :
  HomeComponent, ComponentContext by componentContext {
  private val navigation = PagesNavigation<PageConfig>()

  override val pages: Value<ChildPages<*, HomeComponent.Page>> =
    childPages(
      source = navigation,
      serializer = PageConfig.serializer(),
      initialPages = { Pages(items = PageConfig.entries, selectedIndex = 0) },
      pageStatus = { index, pages ->
        if (index == pages.selectedIndex) ChildNavState.Status.RESUMED
        else ChildNavState.Status.CREATED
      },
      childFactory = ::createPage,
    )

  private fun createPage(
    config: PageConfig,
    componentContext: ComponentContext,
  ): HomeComponent.Page =
    when (config) {
      PageConfig.FEED -> HomeComponent.Page.Feed(FeedDefaultComponent(componentContext))
      PageConfig.PRICES -> HomeComponent.Page.Prices(PricesDefaultComponent(componentContext))
      PageConfig.SEARCH -> HomeComponent.Page.Search(SearchDefaultComponent(componentContext))
      PageConfig.HISTORY -> HomeComponent.Page.History(HistoryDefaultComponent(componentContext))
    }

  @Serializable
  private enum class PageConfig {
    FEED,
    PRICES,
    SEARCH,
    HISTORY,
  }
}
