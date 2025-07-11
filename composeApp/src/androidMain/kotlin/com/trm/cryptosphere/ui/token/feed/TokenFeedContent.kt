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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trm.cryptosphere.core.ui.BottomGradientOverlay
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.TopGradientOverlay
import com.trm.cryptosphere.core.ui.VerticalFeedPager
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
        modifier = Modifier.fillMaxSize(),
      ) { page ->
        Card(modifier = Modifier.fillMaxSize()) {
          val item = component.tokenFeedItems[page]

          TokenCarousel(
            tokens = component.tokenCarouselConfig.items,
            onItemClick = { index ->
              // TODO: navigate to another token feed page
            },
            modifier =
              component.tokenCarouselConfig.parentSharedElementId
                ?.takeIf { page == 0 }
                ?.let {
                  Modifier.sharedElement(
                    sharedContentState = rememberTokenCarouselSharedContentState(it),
                    animatedVisibilityScope = animatedVisibilityScope,
                  )
                } ?: Modifier,
          )

          Text(
            text = item,
            style = MaterialTheme.typography.headlineLarge,
            modifier =
              Modifier.padding(16.dp)
                .sharedElement(
                  sharedContentState = rememberSharedContentState("token-symbol-$item"),
                  animatedVisibilityScope = animatedVisibilityScope,
                ),
          )
        }
        // TODO: navigation toolbar at the bottom
      }

      TopGradientOverlay(modifier = Modifier.align(Alignment.TopCenter))

      BottomGradientOverlay(modifier = Modifier.align(Alignment.BottomCenter))
    }
  }
}
