package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalFeedPager(
  pagerState: PagerState,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(0.dp),
  pageContent: @Composable PagerScope.(page: Int) -> Unit,
) {
  VerticalPager(
    modifier = modifier,
    state = pagerState,
    contentPadding = contentPadding,
    beyondViewportPageCount = 1,
    pageContent = pageContent,
  )
}
