package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.shared.MR

@Composable
fun NewsFeedContent(
  component: NewsFeedComponent,
  modifier: Modifier = Modifier,
  onImageUrlChange: (String?) -> Unit,
) {
  val newsItems = component.viewState.newsFeedItems.collectAsLazyPagingItems()
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
        DisposableEffect(pagerState.currentPage, newsItems) {
          val callbacks =
            object : Lifecycle.Callbacks {
              override fun onResume() {
                onImageUrlChange(newsItems[pagerState.currentPage]?.news?.imgUrl)
              }
            }
          component.lifecycle.subscribe(callbacks)
          onDispose { component.lifecycle.unsubscribe(callbacks) }
        }

        PullToRefreshBox(
          isRefreshing = isRefreshing,
          onRefresh = newsItems::refresh,
          modifier = modifier,
        ) {
          VerticalFeedPager(
            pagerState = pagerState,
            key = newsItems.itemKey { it.news.id },
            modifier = Modifier.fillMaxSize(),
          ) { page ->
            newsItems[page]?.let {
              NewsFeedItemContent(
                item = it,
                isCurrent = page == pagerState.currentPage,
                onRelatedTokenItemClick = component.onTokenCarouselItemClick,
              )
            }
          }

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
          Text(loadState.error.message ?: MR.strings.error_occurred.resolve())
          Button(onClick = newsItems::retry) { Text(MR.strings.retry.resolve()) }
        }
      }
    }
  }
}
