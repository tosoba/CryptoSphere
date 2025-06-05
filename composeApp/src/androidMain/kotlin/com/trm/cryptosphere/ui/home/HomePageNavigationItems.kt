package com.trm.cryptosphere.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun HomePageConfig.NavigationItemIcon() {
  Icon(
    when (this) {
      HomePageConfig.FEED -> Icons.Filled.RssFeed
      HomePageConfig.PRICES -> Icons.Filled.PriceChange
      HomePageConfig.SEARCH -> Icons.Filled.Search
      HomePageConfig.HISTORY -> Icons.Filled.History
    },
    contentDescription = null,
  )
}
