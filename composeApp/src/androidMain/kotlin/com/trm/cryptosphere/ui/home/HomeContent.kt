package com.trm.cryptosphere.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.core.util.toNavigationSuiteType
import com.trm.cryptosphere.shared.MR
import com.trm.cryptosphere.ui.home.page.history.HistoryContent
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedContent
import com.trm.cryptosphere.ui.home.page.prices.PricesContent

@Composable
fun HomeContent(component: HomeComponent, onImageUrlChange: (String?) -> Unit) {
  val pages by component.pages.subscribeAsState()

  NavigationSuiteScaffold(
    modifier = Modifier.fillMaxSize(),
    layoutType = currentWindowAdaptiveInfo().toNavigationSuiteType(),
    navigationSuiteItems = {
      HomePageConfig.entries.forEachIndexed { index, config ->
        item(
          selected = pages.selectedIndex == index,
          onClick = { component.selectPage(index) },
          icon = { config.NavigationItemIcon() },
          label = { Text(config.label) },
        )
      }
    },
  ) {
    ChildPages(
      pages = pages,
      onPageSelected = {},
      pager = { modifier, state, key, pageContent ->
        HorizontalPager(
          modifier = modifier,
          state = state,
          key = key,
          userScrollEnabled = false,
          pageContent = pageContent,
        )
      },
      modifier = Modifier.fillMaxSize(),
      scrollAnimation = PagesScrollAnimation.Default,
    ) { _, page ->
      DisposableEffect(Unit) {
        val callbacks =
          object : Lifecycle.Callbacks {
            override fun onResume() {
              if (page !is HomeComponent.Page.NewsFeed) {
                onImageUrlChange(null)
              }
            }
          }
        component.lifecycle.subscribe(callbacks)
        onDispose { component.lifecycle.unsubscribe(callbacks) }
      }
      when (page) {
        is HomeComponent.Page.NewsFeed -> {
          NewsFeedContent(component = page.component, onImageUrlChange = onImageUrlChange)
        }
        is HomeComponent.Page.Prices -> {
          PricesContent(component = page.component)
        }
        is HomeComponent.Page.History -> {
          HistoryContent(component = page.component)
        }
      }
    }
  }
}

private val HomePageConfig.label: String
  @Composable
  get() =
    when (this) {
      HomePageConfig.FEED -> MR.strings.feed
      HomePageConfig.PRICES -> MR.strings.prices
      HomePageConfig.HISTORY -> MR.strings.history
    }.resolve()
