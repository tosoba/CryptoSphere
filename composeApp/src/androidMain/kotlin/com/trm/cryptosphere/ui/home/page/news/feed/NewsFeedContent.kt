package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.trm.cryptosphere.core.ui.ErrorOccurredCard
import com.trm.cryptosphere.core.ui.LargeCircularProgressIndicator
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.shared.MR

@Composable
fun NewsFeedContent(component: NewsFeedComponent, onImageUrlChange: (String?) -> Unit) {
  val newsItems = component.viewModel.newsPagingState.flow.collectAsLazyPagingItems()

  val pagerState = rememberPagerState(pageCount = newsItems::itemCount)
  var isRefreshing by remember { mutableStateOf(false) }

  Crossfade(newsItems.loadState.refresh) { loadState ->
    when (loadState) {
      LoadState.Loading -> {
        LargeCircularProgressIndicator(modifier = Modifier.fillMaxSize())
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
          modifier = Modifier.fillMaxSize(),
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
                onLinkClick = component.viewModel::onLinkClick,
                modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainer),
              )
            }
          }

          ErrorOccurredCard(
            visible = newsItems.loadState.append is LoadState.Error,
            onRetryClick = newsItems::retry,
            modifier =
              Modifier.align(Alignment.TopEnd)
                .padding(
                  top =
                    with(LocalDensity.current) {
                      WindowInsets.statusBars.getTop(LocalDensity.current).toDp()
                    } + 16.dp,
                  start = 16.dp,
                  end = 16.dp,
                ),
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
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
        ) {
          Text(text = MR.strings.error_occurred.resolve())
          Button(onClick = newsItems::retry) { Text(MR.strings.retry.resolve()) }
        }
      }
    }
  }
}
