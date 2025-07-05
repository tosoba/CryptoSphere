package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.VerticalFeedPagerContentPadding
import com.trm.cryptosphere.domain.model.mockNewsItem
import com.trm.cryptosphere.domain.model.mockTokenCarouselItems
import kotlin.math.abs

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.NewsFeedContent(
  component: NewsFeedComponent,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  val tokenCarouselItems = remember(::mockTokenCarouselItems)
  val newsItems = remember { List(3) { mockNewsItem() } }
  val pagerState = rememberPagerState(pageCount = newsItems::size)

  // TODO: change the box background depending on feed item image color palette
  Box(modifier = modifier) {
    VerticalFeedPager(
      pagerState = pagerState,
      contentPadding = VerticalFeedPagerContentPadding.ExtraTop,
    ) { page ->
      Card {
        val isCurrent = page == pagerState.currentPage
        NewsFeedItem(
          item = newsItems[page],
          isCurrent = isCurrent,
          modifier =
            Modifier.alpha(
              if (!isCurrent) .75f else 1f - abs(pagerState.currentPageOffsetFraction) / 2f
            ),
        )
      }
    }

    if (tokenCarouselItems.isNotEmpty()) {
      TokenCarousel(
        tokens = tokenCarouselItems,
        onItemClick = { token ->
          component.onTokenCarouselItemClick(token.symbol, tokenCarouselItems)
        },
        modifier =
          Modifier.sharedElement(
            rememberSharedContentState("token-carousel"),
            animatedVisibilityScope,
          ),
      )
    }
  }
}
