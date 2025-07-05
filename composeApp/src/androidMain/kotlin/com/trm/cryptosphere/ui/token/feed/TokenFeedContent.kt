package com.trm.cryptosphere.ui.token.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.VerticalFeedPagerContentPadding

@Composable
fun TokenFeedContent(component: TokenFeedComponent, modifier: Modifier = Modifier) {
  Scaffold(modifier = modifier) { paddingValues ->
    val pagerState = rememberPagerState(pageCount = component.tokenFeedItems::size)

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      VerticalFeedPager(
        pagerState = pagerState,
        contentPadding =
          if (component.tokenCarouselItems.isEmpty()) VerticalFeedPagerContentPadding.Symmetrical
          else VerticalFeedPagerContentPadding.ExtraTop,
      ) { page ->
        Card(modifier = Modifier.fillMaxSize()) { Text(component.tokenFeedItems[page]) }
      }

      if (component.tokenCarouselItems.isNotEmpty()) {
        TokenCarousel(
          tokens = component.tokenCarouselItems,
          onItemClick = { index ->
            // TODO: navigate to another token feed page
          },
        )
      }

      // TODO: navigation toolbar at the bottom
    }
  }
}
