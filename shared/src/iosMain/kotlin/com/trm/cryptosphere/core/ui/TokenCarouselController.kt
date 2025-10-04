@file:Suppress("unused") // Used in Swift

package com.trm.cryptosphere.core.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.cryptosphere.domain.model.TokenItem
import platform.UIKit.UIViewController

@OptIn(ExperimentalComposeUiApi::class)
fun TokenCarouselController(
  tokens: List<TokenItem>,
  onItemClick: (TokenItem) -> Unit,
): UIViewController =
  ComposeUIViewController(configure = { opaque = false }) {
    TokenCarousel(tokens = tokens, onItemClick = onItemClick)
  }
