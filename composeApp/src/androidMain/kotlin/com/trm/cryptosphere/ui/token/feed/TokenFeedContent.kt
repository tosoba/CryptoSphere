package com.trm.cryptosphere.ui.token.feed

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import com.trm.cryptosphere.core.ui.rememberTokenCarouselSharedContentState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TokenFeedContent(
  component: TokenFeedComponent,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  Scaffold(modifier = modifier) { paddingValues ->
    VerticalFeedPager(
      pagerState = rememberPagerState(pageCount = component.tokenFeedItems::size),
      modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) { page ->
      Card(modifier = Modifier.fillMaxSize()) {
        val item = component.tokenFeedItems[page]

        TokenCarousel(
          tokens = component.tokenCarouselItems,
          onItemClick = { index ->
            // TODO: navigate to another token feed page
          },
          modifier =
            Modifier.sharedElement(
              // TODO: replace item with parentId in this case NewsItemId passed as argument to
              // TokenFeedComponent
              sharedContentState = rememberTokenCarouselSharedContentState(item),
              animatedVisibilityScope = animatedVisibilityScope,
            ),
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
  }
}
