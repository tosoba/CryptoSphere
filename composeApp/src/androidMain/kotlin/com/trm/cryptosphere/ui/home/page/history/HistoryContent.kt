package com.trm.cryptosphere.ui.home.page.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.TokenHistoryItem
import com.trm.cryptosphere.shared.MR
import kotlinx.coroutines.launch

@Composable
fun HistoryContent(component: HistoryComponent, modifier: Modifier = Modifier) {
  val tabs = listOf(MR.strings.news.resolve(), MR.strings.tokens.resolve())
  val pagerState = rememberPagerState(pageCount = tabs::size)
  val scope = rememberCoroutineScope()

  Column(modifier = modifier.fillMaxSize()) {
    SecondaryTabRow(selectedTabIndex = pagerState.currentPage) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = pagerState.currentPage == index,
          onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
          text = { Text(text = title) },
        )
      }
    }

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
      val viewModel = component.viewModel
      if (index == 0) NewsHistoryList(viewModel.newsHistory.collectAsLazyPagingItems())
      else TokenHistoryList(viewModel.tokenHistory.collectAsLazyPagingItems())
    }
  }
}

@Composable
private fun NewsHistoryList(items: LazyPagingItems<NewsHistoryListItem>) {
  LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(
      count = items.itemCount,
      key =
        items.itemKey {
          when (it) {
            is NewsHistoryListItem.Item -> it.news.id
            is NewsHistoryListItem.DateHeader -> it.date
          }
        },
    ) { index ->
      when (val item = items[index]) {
        is NewsHistoryListItem.Item -> NewsHistoryItem(item.news)
        is NewsHistoryListItem.DateHeader -> DateHeader(date = item.date)
        null -> {}
      }
    }
  }
}

@Composable
private fun TokenHistoryList(items: LazyPagingItems<TokenHistoryListItem>) {
  LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(
      count = items.itemCount,
      key =
        items.itemKey {
          when (it) {
            is TokenHistoryListItem.Item -> it.token.id
            is TokenHistoryListItem.DateHeader -> it.date
          }
        },
    ) { index ->
      when (val item = items[index]) {
        is TokenHistoryListItem.Item -> TokenHistoryItem(item.token)
        is TokenHistoryListItem.DateHeader -> DateHeader(date = item.date)
        null -> {}
      }
    }
  }
}

@Composable
private fun DateHeader(date: String) {
  Text(
    text = date,
    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
  )
}

@Composable
private fun NewsHistoryItem(item: NewsHistoryItem) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
      AsyncImage(
        model = item.imgUrl,
        contentDescription = null,
        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(text = item.url, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
      }

      Text(text = item.visitedAt.toString(), style = MaterialTheme.typography.bodySmall)
    }
  }
}

@Composable
private fun TokenHistoryItem(item: TokenHistoryItem) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Box(
      modifier =
        Modifier.size(48.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(MaterialTheme.colorScheme.primaryContainer),
      contentAlignment = Alignment.Center,
    ) {
      Text(text = item.tokenSymbol.take(1), style = MaterialTheme.typography.titleMedium)
    }

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(text = item.tokenName, style = MaterialTheme.typography.bodyLarge, maxLines = 1)

      Text(
        text = item.tokenSymbol,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Text(text = item.visitedAt.toString(), style = MaterialTheme.typography.bodySmall)
  }
}
