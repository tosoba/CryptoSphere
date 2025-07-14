package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.VerticalFeedPagerContentPadding
import com.trm.cryptosphere.domain.model.mockNewsItem
import kotlin.math.abs

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.NewsFeedContent(
  component: NewsFeedComponent,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  val newsItems = remember { List(3) { mockNewsItem(it.toString()) } }
  val pagerState = rememberPagerState(pageCount = newsItems::size)

  // TODO: change the background depending on feed item image color palette
  VerticalFeedPager(
    pagerState = pagerState,
    contentPadding = VerticalFeedPagerContentPadding.Symmetrical,
    modifier = modifier,
  ) { page ->
    ElevatedCard {
      val isCurrent = page == pagerState.currentPage
      NewsFeedItem(
        item = newsItems[page],
        isCurrent = isCurrent,
        animatedVisibilityScope = animatedVisibilityScope,
        modifier =
          Modifier.alpha(
            if (!isCurrent) .75f else 1f - abs(pagerState.currentPageOffsetFraction) / 2f
          ),
        onTokenCarouselItemClick = component.onTokenCarouselItemClick,
      )
    }
  }
}
