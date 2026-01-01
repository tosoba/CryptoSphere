package com.trm.cryptosphere.ui.home.page.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.trm.cryptosphere.core.ui.clearFocusOnTap
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.TokenHistoryItem
import com.trm.cryptosphere.domain.model.logoUrl
import com.trm.cryptosphere.shared.MR
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(component: HistoryComponent) {
  val scope = rememberCoroutineScope()

  val tabs = listOf(MR.strings.news.resolve(), MR.strings.tokens.resolve())
  val pagerState = rememberPagerState(pageCount = tabs::size)

  val searchBarState = rememberSearchBarState()
  fun collapseSearchBar() {
    scope.launch { searchBarState.animateToCollapsed() }
  }

  Scaffold(
    modifier = Modifier.clearFocusOnTap(::collapseSearchBar),
    topBar = { TopSearchBar(searchBarState) },
  ) {
    Column(modifier = Modifier.fillMaxSize().padding(it)) {
      SecondaryTabRow(selectedTabIndex = pagerState.currentPage) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = pagerState.currentPage == index,
            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
            text = { Text(text = title) },
          )
        }
      }

      HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier =
          Modifier.fillMaxWidth()
            .weight(1f)
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
      ) { index ->
        val viewModel = component.viewModel
        if (index == 0) NewsHistoryList(viewModel.newsHistory.collectAsLazyPagingItems())
        else TokenHistoryList(viewModel.tokenHistory.collectAsLazyPagingItems())
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopSearchBar(searchBarState: SearchBarState) {
  val scope = rememberCoroutineScope()
  val textFieldState = rememberTextFieldState()

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
                  append(MR.strings.search_history.resolve())
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
    actions = {
      TooltipBox(
        positionProvider =
          TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = { PlainTooltip { Text(MR.strings.delete_history.resolve()) } },
        state = rememberTooltipState(),
      ) {
        FilledTonalIconButton(onClick = {}) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = MR.strings.delete_history.resolve(),
          )
        }
      }
    },
  )
}

@Composable
private fun NewsHistoryList(items: LazyPagingItems<NewsHistoryListItem>) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = if (items.itemCount == 0) 0.dp else 4.dp),
  ) {
    items(
      count = items.itemCount,
      key =
        items.itemKey {
          when (it) {
            is NewsHistoryListItem.Item -> it.news.id
            is NewsHistoryListItem.DateHeader -> it.date.toEpochDays()
          }
        },
    ) { index ->
      val previousItem = if (index > 0) items[index - 1] else null
      val currentItem = items[index]
      val nextItem = if (index < items.itemCount - 1) items[index + 1] else null

      when (currentItem) {
        is NewsHistoryListItem.Item -> {
          NewsHistoryItem(
            item = currentItem.news,
            isTopRounded = previousItem is NewsHistoryListItem.DateHeader,
            isBottomRounded = nextItem is NewsHistoryListItem.DateHeader || nextItem == null,
          )
        }
        is NewsHistoryListItem.DateHeader -> {
          DateHeader(currentItem.date)
        }
        null -> {}
      }
    }
  }
}

@Composable
private fun TokenHistoryList(items: LazyPagingItems<TokenHistoryListItem>) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = if (items.itemCount == 0) 0.dp else 4.dp),
  ) {
    items(
      count = items.itemCount,
      key =
        items.itemKey {
          when (it) {
            is TokenHistoryListItem.Item -> it.token.id
            is TokenHistoryListItem.DateHeader -> it.date.toEpochDays()
          }
        },
    ) { index ->
      val previousItem = if (index > 0) items[index - 1] else null
      val currentItem = items[index]
      val nextItem = if (index < items.itemCount - 1) items[index + 1] else null

      when (currentItem) {
        is TokenHistoryListItem.Item -> {
          TokenHistoryItem(
            item = currentItem.token,
            isTopRounded = previousItem is TokenHistoryListItem.DateHeader,
            isBottomRounded = nextItem is TokenHistoryListItem.DateHeader || nextItem == null,
          )
        }
        is TokenHistoryListItem.DateHeader -> {
          DateHeader(currentItem.date)
        }
        null -> {}
      }
    }
  }
}

@Composable
private fun DateHeader(date: LocalDate) {
  Text(
    text = LocalDate.Formats.ISO.format(date),
    style = MaterialTheme.typography.labelLarge,
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
  )
}

@Composable
private fun NewsHistoryItem(
  item: NewsHistoryItem,
  isTopRounded: Boolean,
  isBottomRounded: Boolean,
) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
    shape = itemShape(isTopRounded = isTopRounded, isBottomRounded = isBottomRounded),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      AsyncImage(
        model = item.imgUrl,
        contentDescription = null,
        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.title,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
        )

        Text(
          text = item.source,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Text(
        text = LocalTime.Formats.ISO.format(item.visitedAt.time),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall,
      )
    }
  }
}

@Composable
private fun TokenHistoryItem(
  item: TokenHistoryItem,
  isTopRounded: Boolean,
  isBottomRounded: Boolean,
) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
    shape = itemShape(isTopRounded = isTopRounded, isBottomRounded = isBottomRounded),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      AsyncImage(
        model = item.token.logoUrl,
        contentDescription = null,
        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Fit,
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.token.symbol,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
        )

        Text(
          text = item.token.name,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Text(
        text = LocalTime.Formats.ISO.format(item.visitedAt.time),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall,
      )
    }
  }
}

private fun itemShape(isTopRounded: Boolean, isBottomRounded: Boolean): RoundedCornerShape =
  when {
    isTopRounded && isBottomRounded -> {
      RoundedCornerShape(16.dp)
    }
    isTopRounded -> {
      RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    }
    isBottomRounded -> {
      RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }
    else -> {
      RoundedCornerShape(0.dp)
    }
  }
