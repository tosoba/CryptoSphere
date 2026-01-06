package com.trm.cryptosphere.ui.home.page.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.trm.cryptosphere.core.ui.LargeCircularProgressIndicator
import com.trm.cryptosphere.core.ui.cardListItemRoundedCornerShape
import com.trm.cryptosphere.core.ui.clearFocusOnTap
import com.trm.cryptosphere.core.ui.rememberCrossfadeImageRequest
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
  val viewModel = component.viewModel

  val scope = rememberCoroutineScope()

  val pagerState = rememberPagerState(pageCount = HistoryPage.entries::size)
  var deleteAllDialogVisible by rememberSaveable { mutableStateOf(false) }

  val searchBarState = rememberSearchBarState()
  fun collapseSearchBar() {
    scope.launch { searchBarState.animateToCollapsed() }
  }

  if (deleteAllDialogVisible) {
    AlertDialog(
      onDismissRequest = { deleteAllDialogVisible = false },
      confirmButton = {
        TextButton(
          onClick = {
            deleteAllDialogVisible = false
            viewModel.onDeleteHistoryClick(page = HistoryPage.fromIndex(pagerState.currentPage))
          }
        ) {
          Text(MR.strings.confirm.resolve())
        }
      },
      dismissButton = {
        TextButton(onClick = { deleteAllDialogVisible = false }) {
          Text(MR.strings.cancel.resolve())
        }
      },
      title = { Text(MR.strings.delete_history.resolve()) },
      text = { Text(MR.strings.delete_history_message.resolve()) },
    )
  }

  val newsHistory = viewModel.newsHistory.collectAsLazyPagingItems()
  val tokenHistory = viewModel.tokenHistory.collectAsLazyPagingItems()

  Scaffold(
    modifier = Modifier.clearFocusOnTap(::collapseSearchBar),
    topBar = {
      TopSearchBar(
        searchBarState = searchBarState,
        deleteEnabled =
          when (HistoryPage.fromIndex(pagerState.currentPage)) {
            HistoryPage.NEWS -> newsHistory
            HistoryPage.TOKENS -> tokenHistory
          }.itemCount > 0,
        onDeleteClick = { deleteAllDialogVisible = true },
        onQueryChange = viewModel::onQueryChange,
      )
    },
  ) {
    Column(modifier = Modifier.fillMaxSize().padding(it)) {
      SecondaryTabRow(selectedTabIndex = pagerState.currentPage) {
        HistoryPage.entries.forEach { page ->
          Tab(
            selected = pagerState.currentPage == page.index,
            onClick = { scope.launch { pagerState.animateScrollToPage(page.index) } },
            text = { Text(text = page.labelRes.resolve()) },
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
        if (index == 0) {
          NewsHistoryList(items = newsHistory, onDelete = viewModel::onDeleteNewsHistoryClick)
        } else {
          TokenHistoryList(items = tokenHistory, onDelete = viewModel::onDeleteTokenHistoryClick)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopSearchBar(
  searchBarState: SearchBarState,
  deleteEnabled: Boolean,
  onDeleteClick: () -> Unit,
  onQueryChange: (String) -> Unit,
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
        FilledTonalIconButton(onClick = onDeleteClick, enabled = deleteEnabled) {
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
private fun NewsHistoryList(items: LazyPagingItems<NewsHistoryListItem>, onDelete: (Long) -> Unit) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = if (items.itemCount == 0) 0.dp else 4.dp),
  ) {
    if (items.loadState.refresh is LoadState.NotLoading && items.itemCount == 0) {
      item {
        EmptyHistory(
          text = MR.strings.no_news_history.resolve(),
          modifier = Modifier.fillParentMaxSize().animateItem(),
        )
      }
    }

    if (items.loadState.refresh is LoadState.Loading) {
      item { LargeCircularProgressIndicator(modifier = Modifier.fillParentMaxSize().animateItem()) }
    }

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
          val dismissState = rememberSwipeToDismissBoxState()
          val shape =
            cardListItemRoundedCornerShape(
              isTopRounded = previousItem is NewsHistoryListItem.DateHeader,
              isBottomRounded = nextItem is NewsHistoryListItem.DateHeader || nextItem == null,
            )
          SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { DismissBackground(dismissState = dismissState, shape = shape) },
            content = { NewsHistoryItem(item = currentItem.news, shape = shape) },
            onDismiss = { if (it != SwipeToDismissBoxValue.Settled) onDelete(currentItem.news.id) },
            modifier = Modifier.animateItem(),
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
private fun TokenHistoryList(
  items: LazyPagingItems<TokenHistoryListItem>,
  onDelete: (Long) -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = if (items.itemCount == 0) 0.dp else 4.dp),
  ) {
    if (items.loadState.refresh is LoadState.NotLoading && items.itemCount == 0) {
      item {
        EmptyHistory(
          text = MR.strings.no_tokens_history.resolve(),
          modifier = Modifier.fillParentMaxSize(),
        )
      }
    }

    if (items.loadState.refresh is LoadState.Loading) {
      item { LargeCircularProgressIndicator(modifier = Modifier.fillParentMaxSize().animateItem()) }
    }

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
          val dismissState = rememberSwipeToDismissBoxState()
          val shape =
            cardListItemRoundedCornerShape(
              isTopRounded = previousItem is TokenHistoryListItem.DateHeader,
              isBottomRounded = nextItem is TokenHistoryListItem.DateHeader || nextItem == null,
            )
          SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { DismissBackground(dismissState = dismissState, shape = shape) },
            content = { TokenHistoryItem(item = currentItem.token, shape = shape) },
            onDismiss = {
              if (it != SwipeToDismissBoxValue.Settled) onDelete(currentItem.token.id)
            },
            modifier = Modifier.animateItem(),
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
private fun DismissBackground(dismissState: SwipeToDismissBoxState, shape: RoundedCornerShape) {
  Box(
    Modifier.fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 2.dp)
      .clip(shape)
      .background(
        if (
          dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart ||
            dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd
        ) {
          MaterialTheme.colorScheme.errorContainer
        } else {
          Color.Transparent
        }
      )
      .padding(horizontal = 16.dp),
    contentAlignment =
      if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart
      else Alignment.CenterEnd,
  ) {
    Icon(
      imageVector = Icons.Default.Delete,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onErrorContainer,
    )
  }
}

@Composable
private fun LazyItemScope.DateHeader(date: LocalDate) {
  Text(
    text = LocalDate.Formats.ISO.format(date),
    style = MaterialTheme.typography.labelLarge,
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).animateItem(),
  )
}

@Composable
private fun NewsHistoryItem(item: NewsHistoryItem, shape: RoundedCornerShape) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      AsyncImage(
        model = rememberCrossfadeImageRequest(item.imgUrl),
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
private fun TokenHistoryItem(item: TokenHistoryItem, shape: RoundedCornerShape) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      AsyncImage(
        model = rememberCrossfadeImageRequest(item.token.logoUrl),
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

@Composable
private fun EmptyHistory(text: String, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      imageVector = Icons.Default.History,
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
