@file:Suppress("unused") // Used in Swift

package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.cryptosphere.domain.model.TokenItem
import platform.UIKit.UIViewController

fun TokenCarouselViewController(
  tokens: List<TokenItem>,
  onItemClick: (TokenItem) -> Unit,
  heightChanged: (Int) -> Unit,
): UIViewController =
  @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
  ComposeUIViewController(configure = { opaque = false }) {
    MaterialTheme {
      TokenCarousel(
        tokens = tokens,
        onItemClick = onItemClick,
        modifier =
          Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
            heightChanged(coordinates.size.height)
          },
        labelStyle =
          MaterialTheme.typography.labelMedium.copy(
            color = Color.White,
            shadow =
              Shadow(color = Color.DarkGray, offset = Offset(x = 4f, y = 4f), blurRadius = 8f),
          ),
      )
    }
  }
