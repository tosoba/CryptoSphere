package com.trm.cryptosphere.ui.home.page.history

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.trm.cryptosphere.domain.model.NewsHistoryListItem
import com.trm.cryptosphere.domain.model.TokenHistoryListItem

@Composable
fun HistoryContent(component: HistoryComponent, modifier: Modifier = Modifier) {
  var selectedTabIndex by remember { mutableStateOf(0) }
  val tabs = listOf("News", "Tokens")

  Column(modifier = modifier.fillMaxSize()) {
    SecondaryTabRow(selectedTabIndex = selectedTabIndex) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTabIndex == index,
          onClick = { selectedTabIndex = index },
          text = { Text(text = title) },
        )
      }
    }

    Crossfade(targetState = selectedTabIndex) { index ->
      if (index == 0) {
        NewsHistoryList(component.viewModel.newsHistory.collectAsLazyPagingItems())
      } else {
        TokenHistoryList(component.viewModel.tokenHistory.collectAsLazyPagingItems())
      }
    }
  }
}

@Composable
fun NewsHistoryList(items: LazyPagingItems<NewsHistoryListItem>) {
  LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(
      count = items.itemCount,
      key =
        items.itemKey {
          when (it) {
            is NewsHistoryListItem.Item -> it.news.id
            is NewsHistoryListItem.Separator -> it.date
          }
        },
    ) { index ->
      when (val item = items[index]) {
        is NewsHistoryListItem.Item -> NewsHistoryItem(item)
        is NewsHistoryListItem.Separator -> DateHeader(date = item.date)
        null -> {}
      }
    }
  }
}

@Composable
fun TokenHistoryList(items: LazyPagingItems<TokenHistoryListItem>) {
  LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(
      count = items.itemCount,
      key =
        items.itemKey {
          when (it) {
            is TokenHistoryListItem.Item -> it.token.id
            is TokenHistoryListItem.Separator -> it.date
          }
        },
    ) { index ->
      when (val item = items[index]) {
        is TokenHistoryListItem.Item -> TokenHistoryItem(item)
        is TokenHistoryListItem.Separator -> DateHeader(date = item.date)
        null -> {}
      }
    }
  }
}

@Composable
fun DateHeader(date: String) {
  Text(
    text = date,
    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
  )
}

@Composable
fun NewsHistoryItem(item: NewsHistoryListItem.Item) {
  val news = item.news
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
      AsyncImage(
        model = news.imgUrl,
        contentDescription = null,
        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = news.url, // Using URL as title for now as title isn't in entity
          style = MaterialTheme.typography.bodyLarge,
          maxLines = 1,
        )
      }
      Text(text = news.visitedAt.toString(), style = MaterialTheme.typography.bodySmall)
    }
  }
}

@Composable
fun TokenHistoryItem(item: TokenHistoryListItem.Item) {
  val token = item.token
  Card(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
      // Placeholder for Token Image
      Box(
        modifier =
          Modifier.size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
      ) {
        Text(text = token.tokenSymbol.take(1), style = MaterialTheme.typography.titleMedium)
      }

      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = token.tokenName, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
        Text(
          text = token.tokenSymbol,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Text(text = token.visitedAt.toString(), style = MaterialTheme.typography.bodySmall)
    }
  }
}
