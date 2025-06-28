package com.trm.cryptosphere.ui.home.page.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.trm.cryptosphere.core.ui.TokensCarousel
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.model.TokenCarouselItem
import kotlin.math.abs
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun FeedContent(component: FeedComponent, modifier: Modifier = Modifier) {
  val tokens = remember(::mockTokenCarouselItems)
  val newsItems = remember { List(3) { mockNewsItem() } }
  val pagerState = rememberPagerState(pageCount = newsItems::size)

  // TODO: change the box background depending on feed item image color palette
  Box(modifier = modifier) {
    // TODO: display no related tokens info text if feed item has no related tokens
    FeedContentPager(pagerState, newsItems)
    TokensCarousel(tokens)
  }
}

@Composable
private fun FeedContentPager(pagerState: PagerState, newsItems: List<NewsItem>) {
  VerticalPager(
    modifier = Modifier.fillMaxSize(),
    state = pagerState,
    beyondViewportPageCount = 1,
    contentPadding = PaddingValues(top = 80.dp, bottom = 32.dp, start = 16.dp, end = 24.dp),
    pageSpacing = 8.dp,
  ) { page ->
    Card {
      val isCurrent = page == pagerState.currentPage
      FeedItem(
        item = newsItems[page],
        isCurrent = isCurrent,
        modifier =
          Modifier.alpha(
            if (!isCurrent) .75f else 1f - abs(pagerState.currentPageOffsetFraction) / 2f
          ),
      )
    }
  }
}

fun mockNewsItem(): NewsItem =
  NewsItem(
    id = "5bc8659a47bad806ac2fc1fc5dbb7387c04b263432722b17fbba325a6335602d",
    searchKeyWords = listOf("dogecoin", "DOGE"),
    feedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    source = "U.Today",
    title = "Dogecoin Account Drops Casual 'Sup' Tweet: What's Behind It?",
    sourceLink = "https://u.today/",
    description = "Dogecoin Account Drops Casual 'Sup' Tweet: What's Behind It?",
    imgUrl = "https://u.today/sites/default/files/styles/736x/public/2025-06/s7426.jpg",
    relatedCoins =
      listOf(
        "dogecoin",
        "0x4206931337dc273a630d328da6441786bfad668f_ethereum",
        "0x1a8e39ae59e5556b56b76fcba98d22c9ae557396_cronos",
      ),
    link =
      "https://u.today/dogecoin-account-drops-casual-sup-tweet-whats-behind-it?utm_medium=referral&utm_source=coinstats",
  )

fun mockTokenCarouselItems(): List<TokenCarouselItem> =
  listOf(
    TokenCarouselItem("bitcoin", "BTC", "https://static.coinstats.app/coins/1650455588819.png"),
    TokenCarouselItem("ethereum", "ETH", "https://static.coinstats.app/coins/1650455629727.png"),
    TokenCarouselItem("tether", "USDT", "https://static.coinstats.app/coins/1650455771843.png"),
    TokenCarouselItem("bitcoin", "BTC", "https://static.coinstats.app/coins/1650455588819.png"),
    TokenCarouselItem("ethereum", "ETH", "https://static.coinstats.app/coins/1650455629727.png"),
    TokenCarouselItem("tether", "USDT", "https://static.coinstats.app/coins/1650455771843.png"),
    TokenCarouselItem("bitcoin", "BTC", "https://static.coinstats.app/coins/1650455588819.png"),
    TokenCarouselItem("ethereum", "ETH", "https://static.coinstats.app/coins/1650455629727.png"),
    TokenCarouselItem("tether", "USDT", "https://static.coinstats.app/coins/1650455771843.png"),
  )
