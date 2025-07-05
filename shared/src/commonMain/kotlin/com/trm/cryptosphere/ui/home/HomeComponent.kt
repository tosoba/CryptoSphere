package com.trm.cryptosphere.ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.domain.model.TokenCarouselItem
import com.trm.cryptosphere.ui.home.page.history.HistoryComponent
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedComponent
import com.trm.cryptosphere.ui.home.page.prices.PricesComponent
import com.trm.cryptosphere.ui.home.page.search.SearchComponent

interface HomeComponent {
  val pages: Value<ChildPages<*, Page>>

  fun selectPage(index: Int)

  sealed interface Page {
    class NewsFeed(val component: NewsFeedComponent) : Page

    class Prices(val component: PricesComponent) : Page

    class Search(val component: SearchComponent) : Page

    class History(val component: HistoryComponent) : Page
  }

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (List<TokenCarouselItem>, String) -> Unit,
    ): HomeComponent
  }
}
