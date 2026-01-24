@file:Suppress("unused") // Used in Swift

package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.collectLatest
import platform.UIKit.UIViewController

fun TokenTagsGridViewController(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  rowCount: Int,
  heightChanged: (Int) -> Unit,
  scrollStateChanged: (Boolean) -> Unit,
): UIViewController =
  @OptIn(ExperimentalComposeUiApi::class)
  ComposeUIViewController(configure = { opaque = false }) {
    MaterialTheme {
      val gridState = rememberLazyStaggeredGridState()

      LaunchedEffect(gridState) {
        snapshotFlow { gridState.isScrollInProgress }
          .collectLatest { isScrolling -> scrollStateChanged(isScrolling) }
      }

      TokenTagsGrid(
        token = token,
        mainTokenTagNames = mainTokenTagNames,
        rowCount = rowCount,
        gridState = gridState,
        modifier =
          Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
            heightChanged(coordinates.size.height)
          },
      )
    }
  }
