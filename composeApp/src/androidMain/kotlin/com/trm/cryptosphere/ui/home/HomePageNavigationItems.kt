package com.trm.cryptosphere.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun HomePageConfig.NavigationItemIcon() {
  Icon(
    when (this) {
      // TODO: proper icons
      HomePageConfig.FEED -> Icons.AutoMirrored.Filled.List
      HomePageConfig.PRICES -> Icons.AutoMirrored.Filled.List
      HomePageConfig.SEARCH -> Icons.AutoMirrored.Filled.List
      HomePageConfig.HISTORY -> Icons.AutoMirrored.Filled.List
    },
    contentDescription = null,
  )
}
