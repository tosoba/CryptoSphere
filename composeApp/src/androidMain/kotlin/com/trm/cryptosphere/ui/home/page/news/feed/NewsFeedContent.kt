package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trm.cryptosphere.core.ui.PagerIndicatorOrientation
import com.trm.cryptosphere.core.ui.PagerWormIndicator
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.domain.model.mockNewsItem

@Composable
fun NewsFeedContent(component: NewsFeedComponent, modifier: Modifier = Modifier) {
  val newsItems = remember { List(3) { mockNewsItem(it.toString()) } }
  val pagerState = rememberPagerState(pageCount = newsItems::size)

  // TODO: change the theme depending on feed item image color palette
  Box(modifier = modifier) {
    VerticalFeedPager(pagerState = pagerState, modifier = Modifier.fillMaxSize()) { page ->
      NewsFeedItem(
        item = newsItems[page],
        isCurrent = page == pagerState.currentPage,
        onTokenCarouselItemClick = component.onTokenCarouselItemClick,
      )
    }

    PagerWormIndicator(
      pagerState = pagerState,
      activeDotColor = Color.White,
      dotColor = Color.LightGray,
      dotCount = 5,
      orientation = PagerIndicatorOrientation.Vertical,
      modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp),
    )
  }
}
