package com.trm.cryptosphere.ui.home.page.history

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.TokenHistoryItem
import com.trm.cryptosphere.domain.model.logoUrl
import com.trm.cryptosphere.shared.MR
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun HistoryContent(component: HistoryComponent, modifier: Modifier = Modifier) {
  val tabs = listOf(MR.strings.news.resolve(), MR.strings.tokens.resolve())
  val pagerState = rememberPagerState(pageCount = tabs::size)
  val scope = rememberCoroutineScope()

  Scaffold(modifier = modifier) {
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
      when (val item = items[index]) {
        is NewsHistoryListItem.Item -> NewsHistoryItem(item.news)
        is NewsHistoryListItem.DateHeader -> DateHeader(item.date)
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
      when (val item = items[index]) {
        is TokenHistoryListItem.Item -> TokenHistoryItem(item.token)
        is TokenHistoryListItem.DateHeader -> DateHeader(item.date)
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
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
  )
}

@Composable
private fun NewsHistoryItem(item: NewsHistoryItem) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
    shape = RoundedCornerShape(16.dp),
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
private fun TokenHistoryItem(item: TokenHistoryItem) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
    shape = RoundedCornerShape(16.dp),
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
