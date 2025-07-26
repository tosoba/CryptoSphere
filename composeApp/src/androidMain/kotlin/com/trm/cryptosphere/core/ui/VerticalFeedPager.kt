package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalFeedPager(
  pagerState: PagerState,
  modifier: Modifier = Modifier,
  pageContent: @Composable PagerScope.(page: Int) -> Unit,
) {
  VerticalPager(
    modifier = modifier,
    state = pagerState,
    beyondViewportPageCount = 1,
    pageContent = pageContent,
  )
}
