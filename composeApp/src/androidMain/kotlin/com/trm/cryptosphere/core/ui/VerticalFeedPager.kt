package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalFeedPager(
  pagerState: PagerState,
  pageContent: @Composable PagerScope.(page: Int) -> Unit,
) {
  VerticalPager(
    modifier = Modifier.fillMaxSize(),
    state = pagerState,
    beyondViewportPageCount = 1,
    contentPadding = PaddingValues(top = 80.dp, bottom = 32.dp, start = 16.dp, end = 24.dp),
    pageSpacing = 8.dp,
    pageContent = pageContent,
  )
}
