package com.trm.cryptosphere.ui.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.trm.cryptosphere.core.ui.navigationSuiteLayoutType
import com.trm.cryptosphere.ui.home.page.history.HistoryContent
import com.trm.cryptosphere.ui.home.page.news.feed.NewsFeedContent
import com.trm.cryptosphere.ui.home.page.prices.PricesContent
import com.trm.cryptosphere.ui.home.page.search.SearchContent

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeContent(
  component: HomeComponent,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  val pages by component.pages.subscribeAsState()
  NavigationSuiteScaffold(
    modifier = modifier,
    layoutType = navigationSuiteLayoutType(),
    navigationSuiteItems = {
      HomePageConfig.entries.forEachIndexed { index, config ->
        item(
          selected = pages.selectedIndex == index,
          onClick = { component.selectPage(index) },
          icon = { config.NavigationItemIcon() },
          label = {
            // TODO: proper labels
            Text(config.name.lowercase().capitalize(LocaleList.current))
          },
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
      modifier =
        Modifier.fillMaxSize()
          .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top).asPaddingValues()),
      scrollAnimation = PagesScrollAnimation.Default,
    ) { _, page ->
      when (page) {
        is HomeComponent.Page.NewsFeed -> {
          NewsFeedContent(
            component = page.component,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.fillMaxSize(),
          )
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
