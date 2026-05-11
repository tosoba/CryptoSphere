package com.trm.cryptosphere.ui.token.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.trm.cryptosphere.core.ui.LargeCircularProgressIndicator
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.logoUrl

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TokenFeedContent(component: TokenFeedComponent, modifier: Modifier = Modifier) {
  val tokens = component.viewModel.tokensPagingState.flow.collectAsLazyPagingItems()

  val pagerState = rememberPagerState(pageCount = tokens::itemCount)
  fun currentToken(): TokenItem? = tokens[pagerState.currentPage]

  Crossfade(targetState = tokens.loadState.refresh is LoadState.Loading, modifier = modifier) {
    isLoading ->
    if (isLoading) {
      LargeCircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
      VerticalFeedPager(
        pagerState = pagerState,
        key = tokens.itemKey(TokenItem::id),
        modifier = Modifier.fillMaxSize(),
      ) { page ->
        tokens[page]?.let { token ->
          TokenFeedItemContent(
            token = token,
            mainTokenTagNames = tokens.peek(0)?.tagNames.orEmpty().toSet(),
            modifier = Modifier.fillMaxSize(),
          )
        }
      }

      DisposableEffect(pagerState.currentPage, tokens) {
        val callbacks =
          object : Lifecycle.Callbacks {
            override fun onResume() {
              if (pagerState.currentPage < tokens.itemCount) {
                val currentToken = currentToken()
                component.onCurrentPresentedFeedTokenChange(currentToken)
                component.onSeedImageUrlChange(currentToken?.logoUrl)
              }
            }
          }
        component.lifecycle.subscribe(callbacks)
        onDispose { component.lifecycle.unsubscribe(callbacks) }
      }
    }
  }
}
