package com.trm.cryptosphere.ui.home.page.history

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.trm.cryptosphere.core.base.openUrl
import com.trm.cryptosphere.core.ui.LargeCircularProgressIndicator
import com.trm.cryptosphere.core.ui.ListItemImage
import com.trm.cryptosphere.core.ui.ListItemInfoColumn
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.core.ui.TopSearchBar
import com.trm.cryptosphere.core.ui.cardListItemRoundedCornerShape
import com.trm.cryptosphere.core.ui.clearFocusOnTap
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.TokenHistoryItem
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.logoUrl
import com.trm.cryptosphere.shared.MR
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(component: HistoryComponent) {
  val viewModel = component.viewModel
  val newsHistory = viewModel.newsHistoryPagingState.flow.collectAsLazyPagingItems()
  val tokenHistory = viewModel.tokenHistoryPagingState.flow.collectAsLazyPagingItems()

  val context = LocalContext.current
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

  Scaffold(
    modifier = Modifier.clearFocusOnTap(::collapseSearchBar),
    topBar = {
      TopSearchBar(
        searchBarState = searchBarState,
        placeholder = MR.strings.search_history.resolve(),
        onQueryChange = viewModel::onQueryChange,
        actions = {
          TooltipBox(
            positionProvider =
              TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = { PlainTooltip { Text(MR.strings.delete_history.resolve()) } },
            state = rememberTooltipState(),
          ) {
            FilledTonalIconButton(
              onClick = { deleteAllDialogVisible = true },
              enabled =
                when (HistoryPage.fromIndex(pagerState.currentPage)) {
                  HistoryPage.NEWS -> newsHistory
                  HistoryPage.TOKENS -> tokenHistory
                }.itemCount > 0,
            ) {
              Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = MR.strings.delete_history.resolve(),
              )
            }
          }
        },
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
          HistoryNewsPage(
            items = newsHistory,
            onClick = { item ->
              component.viewModel.onNewsClick(item)
              context.openUrl(item.url)
            },
            onDelete = viewModel::onDeleteNewsHistoryClick,
          )
        } else {
          HistoryTokensPage(
            items = tokenHistory,
            onClick = { token -> component.onTokenClick(token.id, TokenCarouselConfig(null)) },
            onDelete = viewModel::onDeleteTokenHistoryClick,
          )
        }
      }
    }
  }
}

@Composable
private fun HistoryNewsPage(
  items: LazyPagingItems<HistoryNewsListItem>,
  onClick: (NewsHistoryItem) -> Unit,
  onDelete: (Long) -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = if (items.itemCount == 0) 0.dp else 4.dp),
  ) {
    if (items.loadState.refresh is LoadState.NotLoading && items.itemCount == 0) {
      item {
        HistoryEmptyItemContent(
          text = MR.strings.no_news_history.resolve(),
          modifier = Modifier.fillParentMaxSize().animateItem(),
        )
      }
    }

    if (items.loadState.refresh is LoadState.Loading) {
      item { LargeCircularProgressIndicator(modifier = Modifier.fillParentMaxSize().animateItem()) }
    }

    items(count = items.itemCount, key = items.itemKey(HistoryNewsListItem::key)) { index ->
      val previousItem = if (index > 0) items[index - 1] else null
      val currentItem = items[index]
      val nextItem = if (index < items.itemCount - 1) items[index + 1] else null

      when (currentItem) {
        is HistoryNewsListItem.Item -> {
          val dismissState = rememberSwipeToDismissBoxState()
          val shape =
            cardListItemRoundedCornerShape(
              isTopRounded = previousItem is HistoryNewsListItem.DateHeader,
              isBottomRounded = nextItem is HistoryNewsListItem.DateHeader || nextItem == null,
            )
          SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { DismissBackground(dismissState = dismissState, shape = shape) },
            content = {
              HistoryNewsItemContent(
                item = currentItem.data,
                shape = shape,
                onClick = { onClick(currentItem.data) },
              )
            },
            onDismiss = { if (it != SwipeToDismissBoxValue.Settled) onDelete(currentItem.data.id) },
            modifier = Modifier.animateItem(),
          )
        }
        is HistoryNewsListItem.DateHeader -> {
          HistoryDateHeaderItemContent(currentItem.date)
        }
        null -> {}
      }
    }
  }
}

@Composable
private fun HistoryTokensPage(
  items: LazyPagingItems<HistoryTokenListItem>,
  onClick: (TokenItem) -> Unit,
  onDelete: (Long) -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = if (items.itemCount == 0) 0.dp else 4.dp),
  ) {
    if (items.loadState.refresh is LoadState.NotLoading && items.itemCount == 0) {
      item {
        HistoryEmptyItemContent(
          text = MR.strings.no_tokens_history.resolve(),
          modifier = Modifier.fillParentMaxSize().animateItem(),
        )
      }
    }

    if (items.loadState.refresh is LoadState.Loading) {
      item { LargeCircularProgressIndicator(modifier = Modifier.fillParentMaxSize().animateItem()) }
    }

    items(count = items.itemCount, key = items.itemKey(HistoryTokenListItem::key)) { index ->
      val previousItem = if (index > 0) items[index - 1] else null
      val currentItem = items[index]
      val nextItem = if (index < items.itemCount - 1) items[index + 1] else null

      when (currentItem) {
        is HistoryTokenListItem.Item -> {
          val dismissState = rememberSwipeToDismissBoxState()
          val shape =
            cardListItemRoundedCornerShape(
              isTopRounded = previousItem is HistoryTokenListItem.DateHeader,
              isBottomRounded = nextItem is HistoryTokenListItem.DateHeader || nextItem == null,
            )
          SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { DismissBackground(dismissState = dismissState, shape = shape) },
            content = {
              HistoryTokenItemContent(
                item = currentItem.data,
                shape = shape,
                onClick = { onClick(currentItem.data.token) },
              )
            },
            onDismiss = { if (it != SwipeToDismissBoxValue.Settled) onDelete(currentItem.data.id) },
            modifier = Modifier.animateItem(),
          )
        }
        is HistoryTokenListItem.DateHeader -> {
          HistoryDateHeaderItemContent(currentItem.date)
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
private fun LazyItemScope.HistoryDateHeaderItemContent(date: LocalDate) {
  Text(
    text = LocalDate.Formats.ISO.format(date),
    style = MaterialTheme.typography.labelLarge,
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).animateItem(),
  )
}

@Composable
private fun HistoryNewsItemContent(
  item: NewsHistoryItem,
  shape: RoundedCornerShape,
  onClick: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    onClick = onClick,
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      ListItemImage(item.imgUrl)

      Spacer(modifier = Modifier.width(16.dp))

      ListItemInfoColumn(topText = item.title, bottomText = item.source)

      Spacer(modifier = Modifier.width(16.dp))

      VisitedAtTimeText(item.visitedAt.time)
    }
  }
}

@Composable
private fun HistoryTokenItemContent(
  item: TokenHistoryItem,
  shape: RoundedCornerShape,
  onClick: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    onClick = onClick,
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      ListItemImage(item.token.logoUrl)

      Spacer(modifier = Modifier.width(16.dp))

      ListItemInfoColumn(topText = item.token.name, bottomText = item.token.symbol)

      Spacer(modifier = Modifier.width(16.dp))

      VisitedAtTimeText(item.visitedAt.time)
    }
  }
}

@Composable
private fun VisitedAtTimeText(time: LocalTime) {
  Text(
    text = LocalTime.Formats.ISO.format(time),
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    style = MaterialTheme.typography.bodySmall,
  )
}

@Composable
private fun HistoryEmptyItemContent(text: String, modifier: Modifier = Modifier) {
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
