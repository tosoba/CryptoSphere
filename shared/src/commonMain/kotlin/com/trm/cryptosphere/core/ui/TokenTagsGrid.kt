package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trm.cryptosphere.domain.model.TokenItem

@Composable
fun TokenTagsGrid(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  rowCount: Int,
  modifier: Modifier = Modifier,
  gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
  chipColors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
) {
  LazyHorizontalStaggeredGrid(
    rows = StaggeredGridCells.Fixed(rowCount),
    state = gridState,
    modifier = modifier,
    horizontalItemSpacing = 4.dp,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    items(token.tagNames) { name ->
      FilterChip(
        onClick = {},
        label = { Text(name) },
        selected = name in mainTokenTagNames,
        colors = chipColors,
        interactionSource = NoRippleInteractionSource(),
      )
    }
  }
}
