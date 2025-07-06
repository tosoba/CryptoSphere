package com.trm.cryptosphere.ui.token.feed

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.VerticalFeedPagerContentPadding
import com.trm.cryptosphere.core.ui.rememberTokenCarouselSharedContentState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TokenFeedContent(
  component: TokenFeedComponent,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  Scaffold(modifier = modifier) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      VerticalFeedPager(
        pagerState = rememberPagerState(pageCount = component.tokenFeedItems::size),
        contentPadding =
          if (component.tokenCarouselItems.isEmpty()) VerticalFeedPagerContentPadding.Symmetrical
          else VerticalFeedPagerContentPadding.ExtraTop,
      ) { page ->
        Card(modifier = Modifier.fillMaxSize()) {
          val item = component.tokenFeedItems[page]
          Text(
            text = item,
            style = MaterialTheme.typography.headlineLarge,
            modifier =
              Modifier.padding(16.dp)
                .sharedElement(
                  rememberSharedContentState("token-symbol-$item"),
                  animatedVisibilityScope,
                ),
          )
        }
      }

      if (component.tokenCarouselItems.isNotEmpty()) {
        TokenCarousel(
          tokens = component.tokenCarouselItems,
          onItemClick = { index ->
            // TODO: navigate to another token feed page
          },
          modifier =
            Modifier.sharedElement(
              sharedContentState = rememberTokenCarouselSharedContentState(),
              animatedVisibilityScope = animatedVisibilityScope,
            ),
        )
      }

      // TODO: navigation toolbar at the bottom
    }
  }
}
