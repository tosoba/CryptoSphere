package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.trm.cryptosphere.core.ui.PagerIndicatorOrientation
import com.trm.cryptosphere.core.ui.PagerWormIndicator
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.domain.model.NewsItem

@Composable
fun NewsFeedContent(
  component: NewsFeedComponent,
  modifier: Modifier = Modifier,
  onImageUrlChange: (String?) -> Unit,
) {
  val newsItems = component.newsItems.collectAsLazyPagingItems()
  val relatedTokens by component.relatedTokens.collectAsStateWithLifecycle()
  val pagerState = rememberPagerState { newsItems.itemCount }
  var isRefreshing by remember { mutableStateOf(false) }

  Crossfade(newsItems.loadState.refresh) { loadState ->
    when (loadState) {
      LoadState.Loading -> {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
          CircularProgressIndicator()
        }
      }
      is LoadState.NotLoading -> {
        LaunchedEffect(pagerState.currentPage, newsItems) {
          val item = newsItems[pagerState.currentPage]
          onImageUrlChange(item?.imgUrl)
          item?.let(component::onCurrentItemChanged)
        }

        PullToRefreshBox(
          isRefreshing = isRefreshing,
          onRefresh = newsItems::refresh,
          modifier = modifier,
        ) {
          VerticalFeedPager(
            pagerState = pagerState,
            key = newsItems.itemKey(NewsItem::id),
            modifier = Modifier.fillMaxSize(),
          ) { page ->
            newsItems[page]?.let {
              NewsFeedItem(
                item = it,
                isCurrent = page == pagerState.currentPage,
                relatedTokens = relatedTokens,
                onRelatedTokenItemClick = component.onTokenCarouselItemClick,
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

          AnimatedVisibility(
            visible = newsItems.loadState.append is LoadState.Loading,
            modifier = Modifier.align(Alignment.BottomCenter),
          ) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
          }
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
}
