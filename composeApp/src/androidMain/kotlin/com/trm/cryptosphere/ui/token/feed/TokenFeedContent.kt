package com.trm.cryptosphere.ui.token.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.VerticalFeedPagerContentPadding
import com.trm.cryptosphere.domain.model.mockTokenCarouselItems

@Composable
fun TokenFeedContent(component: TokenFeedComponent, modifier: Modifier = Modifier) {
  Scaffold(modifier = modifier) { paddingValues ->
    val relatedTokens = remember(::mockTokenCarouselItems)
    val pagerState = rememberPagerState(pageCount = component.symbols::size)

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      VerticalFeedPager(
        pagerState = pagerState,
        contentPadding =
          if (relatedTokens.isEmpty()) VerticalFeedPagerContentPadding.Symmetrical
          else VerticalFeedPagerContentPadding.ExtraTop,
      ) { page ->
        Card(modifier = Modifier.fillMaxSize()) { Text(component.symbols[page]) }
      }

      if (relatedTokens.isNotEmpty()) {
        TokenCarousel(
          tokens = relatedTokens,
          onItemAtIndexClick = { index ->
            // TODO: navigate to another token feed page
          },
        )
      }
    }
  }
}
