package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.trm.cryptosphere.core.ui.PagerIndicatorOrientation
import com.trm.cryptosphere.core.ui.PagerWormIndicator
import com.trm.cryptosphere.core.ui.VerticalFeedPager

@Composable
fun NewsFeedContent(
  component: NewsFeedComponent,
  modifier: Modifier = Modifier,
  onImageUrlChange: (String?) -> Unit,
) {
  val newsItems = component.state.collectAsLazyPagingItems()
  val pagerState = rememberPagerState { newsItems.itemCount }

  when (val loadState = newsItems.loadState.refresh) {
    LoadState.Loading -> {
      Box(modifier = modifier, contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
    is LoadState.NotLoading -> {
      LaunchedEffect(pagerState.currentPage, newsItems) {
        onImageUrlChange(newsItems[pagerState.currentPage]?.imgUrl)
      }

      Box(modifier = modifier) {
        VerticalFeedPager(
          pagerState = pagerState,
          key = newsItems.itemKey { it.id },
          modifier = Modifier.fillMaxSize(),
        ) { page ->
          newsItems[page]?.let {
            NewsFeedItem(
              item = it,
              isCurrent = page == pagerState.currentPage,
              onTokenCarouselItemClick = component.onTokenCarouselItemClick,
            )
          }
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
    is LoadState.Error -> {
      Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
      ) {
        Text(loadState.error.message ?: "Error occurred.")
        Button(onClick = newsItems::retry) { Text("Retry") }
      }
    }
  }
}
