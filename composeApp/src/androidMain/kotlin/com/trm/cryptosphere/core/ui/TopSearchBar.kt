package com.trm.cryptosphere.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.shared.MR
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
  searchBarState: SearchBarState,
  placeholder: String,
  onQueryChange: (String) -> Unit,
  actions: @Composable RowScope.() -> Unit = {},
) {
  val scope = rememberCoroutineScope()
  val textFieldState = rememberTextFieldState()

  LaunchedEffect(textFieldState.text) { onQueryChange(textFieldState.text.toString()) }

  AppBarWithSearch(
    state = searchBarState,
    inputField = {
      SearchBarDefaults.InputField(
        searchBarState = searchBarState,
        textFieldState = textFieldState,
        onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
          AnimatedVisibility(
            visible = textFieldState.text.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
          ) {
            IconButton(onClick = { textFieldState.setTextAndPlaceCursorAtEnd("") }) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = MR.strings.clear.resolve(),
              )
            }
          }
        },
      )
    },
    actions = actions,
  )
}
