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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trm.cryptosphere.core.ui.BottomGradientOverlay
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.TopGradientOverlay
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.VerticalFeedPagerContentPadding
import com.trm.cryptosphere.core.ui.rememberTokenCarouselSharedContentState
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TokenFeedContent(
  component: TokenFeedComponent,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  Scaffold(modifier = modifier) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      val scope = rememberCoroutineScope()
      val state by component.state.collectAsStateWithLifecycle()
      val pagerState = rememberPagerState(pageCount = state.feedItems::size)

      VerticalFeedPager(
        pagerState = pagerState,
        contentPadding = VerticalFeedPagerContentPadding.ExtraTop,
        modifier = Modifier.fillMaxSize(),
      ) { page ->
        val item = state.feedItems[page]
        Card(
          modifier = Modifier.fillMaxSize(),
          onClick = { component.navigateToTokenDetails(item) },
        ) {
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
      }

      TokenCarousel(
        tokens = component.tokenCarouselConfig.items,
        onItemClick = { item ->
          if (state.mainTokenSymbol == item.symbol) {
            scope.launch { pagerState.animateScrollToPage(0) }
          } else {
            component.reloadFeedForSymbol(item.symbol)
          }
        },
        modifier =
          Modifier.sharedElement(
            sharedContentState =
              rememberTokenCarouselSharedContentState(
                requireNotNull(component.tokenCarouselConfig.parentSharedElementId)
              ),
            animatedVisibilityScope = animatedVisibilityScope,
          ),
      )

      TopGradientOverlay(modifier = Modifier.align(Alignment.TopCenter))

      BottomGradientOverlay(modifier = Modifier.align(Alignment.BottomCenter))

      // TODO: navigation toolbar at the bottom
    }
  }
}
