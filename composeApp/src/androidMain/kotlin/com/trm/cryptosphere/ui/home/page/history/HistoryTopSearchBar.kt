package com.trm.cryptosphere.ui.home.page.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import com.trm.cryptosphere.core.ui.TopSearchBar
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.shared.MR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTopSearchBar(
  searchBarState: SearchBarState,
  onQueryChange: (String) -> Unit,
  onDeleteAllClick: () -> Unit,
  isDeleteEnabled: Boolean,
) {
  TopSearchBar(
    searchBarState = searchBarState,
    placeholder = MR.strings.search_history.resolve(),
    onQueryChange = onQueryChange,
    actions = {
      TooltipBox(
        positionProvider =
          TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Companion.Above),
        tooltip = { PlainTooltip { Text(MR.strings.delete_history.resolve()) } },
        state = rememberTooltipState(),
      ) {
        FilledTonalIconButton(onClick = onDeleteAllClick, enabled = isDeleteEnabled) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = MR.strings.delete_history.resolve(),
          )
        }
      }
    },
  )
}
