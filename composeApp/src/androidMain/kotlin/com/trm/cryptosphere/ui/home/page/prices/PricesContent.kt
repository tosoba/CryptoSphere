package com.trm.cryptosphere.ui.home.page.prices

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.trm.cryptosphere.core.base.fullDecimalFormat
import com.trm.cryptosphere.core.base.shortDecimalFormat
import com.trm.cryptosphere.core.ui.LargeCircularProgressIndicator
import com.trm.cryptosphere.core.ui.clearFocusOnTap
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.shared.MR
import kotlin.math.sign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricesContent(component: PricesComponent) {
  val viewModel = component.viewModel
  val tokens = viewModel.tokens.collectAsLazyPagingItems()
  val scope = rememberCoroutineScope()
  val searchBarState = rememberSearchBarState()

  fun collapseSearchBar() {
    scope.launch { searchBarState.animateToCollapsed() }
  }

  Scaffold(
    modifier = Modifier.clearFocusOnTap(::collapseSearchBar),
    topBar = {
      TopSearchBar(searchBarState = searchBarState, onQueryChange = viewModel::onQueryChange)
    },
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier.fillMaxSize().padding(paddingValues),
      contentPadding = PaddingValues(top = if (tokens.itemCount == 0) 0.dp else 4.dp),
    ) {
      if (tokens.loadState.refresh is LoadState.NotLoading && tokens.itemCount == 0) {
        item {
          EmptyPrices(
            text = MR.strings.no_tokens_found.resolve(),
            modifier = Modifier.fillParentMaxSize(),
          )
        }
      }

      if (tokens.loadState.refresh is LoadState.Loading) {
        item { LargeCircularProgressIndicator(modifier = Modifier.fillParentMaxSize()) }
      }

      items(count = tokens.itemCount, key = tokens.itemKey(TokenItem::id)) { index ->
        tokens[index]?.let { token ->
          TokenPriceItem(token = token, modifier = Modifier.fillMaxWidth())
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopSearchBar(searchBarState: SearchBarState, onQueryChange: (String) -> Unit) {
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
        placeholder = {
          AnimatedVisibility(
            visible =
              textFieldState.text.isEmpty() &&
                searchBarState.currentValue == SearchBarValue.Collapsed,
            enter = fadeIn(),
            exit = fadeOut(),
          ) {
            val searchIconId = "search_icon"
            Text(
              modifier = Modifier.fillMaxWidth(),
              text =
                buildAnnotatedString {
                  appendInlineContent(searchIconId)
                  append(" ")
                  append(MR.strings.search_tokens.resolve())
                },
              inlineContent =
                mapOf(
                  searchIconId to
                    InlineTextContent(
                      Placeholder(
                        width = LocalTextStyle.current.lineHeight,
                        height = LocalTextStyle.current.lineHeight,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                      )
                    ) {
                      Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                      )
                    }
                ),
            )
          }
        },
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
    actions = {},
  )
}

@Composable
private fun TokenPriceItem(token: TokenItem, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier.padding(horizontal = 16.dp, vertical = 2.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = "#${token.cmcRank}",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = token.symbol,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
        )
        Text(
          text = token.name,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = token.quote.price.fullDecimalFormat(),
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface,
        )

        val percentChange24h = token.quote.percentChange24h
        val isPositive = percentChange24h.sign >= 0
        val color = if (isPositive) Color.Green else Color.Red

        Text(
          text = "${percentChange24h.shortDecimalFormat()}%",
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
          color = color,
        )
      }
    }
  }
}

@Composable
private fun EmptyPrices(text: String, modifier: Modifier = Modifier) {
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
      text = text,
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(horizontal = 16.dp),
    )
  }
}
