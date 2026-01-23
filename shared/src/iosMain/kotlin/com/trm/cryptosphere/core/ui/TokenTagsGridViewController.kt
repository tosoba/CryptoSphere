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

fun TokenTagsGridViewController(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  rowCount: Int,
  heightChanged: (Int) -> Unit,
): UIViewController =
  @OptIn(ExperimentalComposeUiApi::class)
  ComposeUIViewController(configure = { opaque = false }) {
    MaterialTheme {
      TokenTagsGrid(
        token = token,
        mainTokenTagNames,
        rowCount = rowCount,
        modifier =
          Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
            heightChanged(coordinates.size.height)
          },
      )
    }
  }
