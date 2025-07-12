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
  contentPadding: VerticalFeedPagerContentPadding,
  modifier: Modifier = Modifier,
  pageContent: @Composable PagerScope.(page: Int) -> Unit,
) {
  VerticalPager(
    modifier = modifier,
    state = pagerState,
    beyondViewportPageCount = 1,
    contentPadding = contentPadding.values,
    pageSpacing = 8.dp,
    pageContent = pageContent,
  )
}

enum class VerticalFeedPagerContentPadding(val values: PaddingValues) {
  Symmetrical(PaddingValues(vertical = 32.dp, horizontal = 16.dp)),
  ExtraTop(PaddingValues(top = 80.dp, bottom = 32.dp, start = 16.dp, end = 16.dp)),
}
