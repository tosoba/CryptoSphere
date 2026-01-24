@file:Suppress("unused") // Used in Swift

package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.collectLatest
import platform.UIKit.UIColor
import platform.UIKit.UIViewController

fun TokenTagsGridViewController(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  rowCount: Int,
  selectedChipContainerColor: UIColor,
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
        selectedChipContainerColor = selectedChipContainerColor,
        gridState = gridState,
        modifier =
          Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
            heightChanged(coordinates.size.height)
          },
      )
    }
  }

@Composable
private fun TokenTagsGrid(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  rowCount: Int,
  selectedChipContainerColor: UIColor,
  modifier: Modifier = Modifier,
  gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
) {
  TokenTagsGrid(
    token = token,
    mainTokenTagNames = mainTokenTagNames,
    rowCount = rowCount,
    modifier = modifier,
    gridState = gridState,
    chipColors =
      FilterChipDefaults.filterChipColors(
        selectedContainerColor = selectedChipContainerColor.toComposeColor()
      ),
  )
}
