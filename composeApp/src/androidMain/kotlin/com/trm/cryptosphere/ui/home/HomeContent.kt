package com.trm.cryptosphere.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedContent
import com.trm.cryptosphere.ui.home.page.history.HistoryContent
import com.trm.cryptosphere.ui.home.page.prices.PricesContent
import com.trm.cryptosphere.ui.home.page.search.SearchContent

@Composable
fun HomeContent(component: HomeComponent, modifier: Modifier = Modifier) {
  Scaffold(
    modifier = modifier,
    bottomBar = {
      NavigationBar(modifier = Modifier.fillMaxWidth()) {
        val pages by component.pages.subscribeAsState()
        HomePageConfig.entries.forEachIndexed { index, config ->
          NavigationBarItem(
            selected = pages.selectedIndex == index,
            onClick = { component.selectPage(index) },
            icon = { config.NavigationItemIcon() },
            label = {
              // TODO: proper labels
              Text(config.name.lowercase().capitalize(LocaleList.current))
            },
          )
        }
      }
    },
  ) { paddingValues ->
    val pages by component.pages.subscribeAsState()
    ChildPages(
      pages = pages,
      onPageSelected = {},
      pager = { modifier, state, key, pageContent ->
        HorizontalPager(
          modifier = modifier,
          state = state,
          key = key,
          contentPadding = paddingValues,
          userScrollEnabled = false,
          pageContent = pageContent,
        )
      },
      modifier = Modifier.fillMaxSize(),
      scrollAnimation = PagesScrollAnimation.Default,
    ) { _, page ->
      when (page) {
        is HomeComponent.Page.NewsFeed -> {
          NewsFeedContent(component = page.component, modifier = Modifier.fillMaxSize())
        }
        is HomeComponent.Page.Prices -> {
          PricesContent(component = page.component, modifier = Modifier.fillMaxSize())
        }
        is HomeComponent.Page.Search -> {
          SearchContent(component = page.component, modifier = Modifier.fillMaxSize())
        }
        is HomeComponent.Page.History -> {
          HistoryContent(component = page.component, modifier = Modifier.fillMaxSize())
        }
      }
    }
  }
}
