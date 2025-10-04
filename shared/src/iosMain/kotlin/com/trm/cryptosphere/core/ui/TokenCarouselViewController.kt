@file:Suppress("unused") // Used in Swift

package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.cryptosphere.domain.model.TokenItem
import platform.UIKit.UIViewController

@OptIn(ExperimentalComposeUiApi::class)
fun TokenCarouselViewController(
  tokens: List<TokenItem>,
  onItemClick: (TokenItem) -> Unit,
  heightChanged: (Int) -> Unit,
): UIViewController =
  ComposeUIViewController(configure = { opaque = false }) {
    MaterialTheme {
      TokenCarousel(
        tokens = tokens,
        onItemClick = onItemClick,
        modifier =
          Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
            heightChanged(coordinates.size.height)
          },
      )
    }
  }
