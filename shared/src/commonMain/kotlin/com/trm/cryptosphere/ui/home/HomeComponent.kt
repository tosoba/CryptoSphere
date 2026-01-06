package com.trm.cryptosphere.ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.ui.home.page.history.HistoryComponent
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedComponent
import com.trm.cryptosphere.ui.home.page.prices.PricesComponent

interface HomeComponent : ComponentContext {
  val pages: Value<ChildPages<*, Page>>

  fun selectPage(index: Int)

  sealed interface Page {
    class NewsFeed(val component: NewsFeedComponent) : Page

    class Prices(val component: PricesComponent) : Page

    class History(val component: HistoryComponent) : Page
  }

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenClick: (Int, TokenCarouselConfig) -> Unit,
    ): HomeComponent
  }
}
