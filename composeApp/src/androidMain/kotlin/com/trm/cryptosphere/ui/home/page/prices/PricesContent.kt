package com.trm.cryptosphere.ui.home.page.prices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.trm.cryptosphere.core.ui.LargeCircularProgressIndicator
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.core.ui.TopSearchBar
import com.trm.cryptosphere.core.ui.cardListItemRoundedCornerShape
import com.trm.cryptosphere.core.ui.clearFocusOnTap
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.shared.MR
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricesContent(component: PricesComponent) {
  val viewModel = component.viewModel
  val tokens = viewModel.tokensPagingState.flow.collectAsLazyPagingItems()

  val scope = rememberCoroutineScope()
  val searchBarState = rememberSearchBarState()
  fun collapseSearchBar() {
    scope.launch { searchBarState.animateToCollapsed() }
  }

  Scaffold(
    modifier = Modifier.clearFocusOnTap(::collapseSearchBar),
    containerColor = MaterialTheme.colorScheme.surface,
    topBar = {
      TopSearchBar(
        searchBarState = searchBarState,
        placeholder = MR.strings.search_tokens.resolve(),
        onQueryChange = viewModel::onQueryChange,
      )
    },
  ) { paddingValues ->
    LazyColumn(
      modifier =
        Modifier.fillMaxSize()
          .padding(top = paddingValues.calculateTopPadding())
          .background(MaterialTheme.colorScheme.surfaceContainer),
      contentPadding =
        PaddingValues(
          top = if (tokens.itemCount == 0) 0.dp else 8.dp,
          bottom =
            if (tokens.itemCount == 0) paddingValues.calculateBottomPadding()
            else paddingValues.calculateBottomPadding() + 8.dp,
          start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
          end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
        ),
    ) {
      if (tokens.loadState.refresh is LoadState.NotLoading && tokens.itemCount == 0) {
        item { PricesEmptyItemContent(modifier = Modifier.fillParentMaxSize().animateItem()) }
      }

      if (tokens.loadState.refresh is LoadState.Loading) {
        item {
          LargeCircularProgressIndicator(modifier = Modifier.fillParentMaxSize().animateItem())
        }
      }

      items(count = tokens.itemCount, key = tokens.itemKey(TokenItem::id)) { index ->
        tokens[index]?.let { token ->
          PriceItemContent(
            token = token,
            shape =
              cardListItemRoundedCornerShape(
                isTopRounded = index == 0,
                isBottomRounded = index == tokens.itemCount - 1,
              ),
            modifier =
              Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp).animateItem(),
            onClick = { component.onTokenClick(token.id, TokenCarouselConfig(null)) },
          )
        }
      }
    }
  }
}

@Composable
private fun PricesEmptyItemContent(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      imageVector = Icons.Default.Search,
      contentDescription = null,
      modifier = Modifier.size(64.dp),
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
      text = MR.strings.no_tokens_found.resolve(),
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(horizontal = 16.dp),
    )
  }
}
