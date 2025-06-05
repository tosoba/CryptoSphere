package com.trm.cryptosphere.ui.home.page.feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.model.RelatedTokenItem
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun FeedContent(component: FeedComponent, modifier: Modifier = Modifier) {
  val newsItem = remember(::mockNewsItem)
  val relatedTokens = remember(::mockRelatedTokenItems)
  FeedItem(item = newsItem, relatedTokens = relatedTokens, modifier = modifier)
}

@Composable
private fun FeedItem(
  item: NewsItem,
  relatedTokens: List<RelatedTokenItem>,
  modifier: Modifier = Modifier,
) {
  ConstraintLayout(modifier = modifier) {
    val (
      backgroundImage,
      backgroundGradient,
      title,
      description,
      linkButton,
      starButton,
      relatedTokensList) =
      createRefs()
    val buttonsStartBarrier = createStartBarrier(linkButton, starButton)

    AsyncImage(
      model = item.imgUrl, // TODO: loading/error placeholders
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier =
        Modifier.constrainAs(backgroundImage) {
          centerTo(parent)
          width = Dimension.matchParent
          height = Dimension.matchParent
        },
    )

    Box(
      modifier =
        Modifier.constrainAs(backgroundGradient) {
            bottom.linkTo(parent.bottom)
            top.linkTo(title.top, margin = (-128).dp)
            width = Dimension.matchParent
            height = Dimension.fillToConstraints
          }
          .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black)))
    )

    Column(
      modifier =
        Modifier.constrainAs(relatedTokensList) {
          bottom.linkTo(starButton.top, margin = 8.dp)
          end.linkTo(parent.end, margin = 16.dp)
        }
    ) {
      relatedTokens.forEach {
        RelatedTokenButton(imageUrl = it.imageUrl.orEmpty())
        Spacer(modifier = Modifier.height(16.dp))
      }
    }

    OutlinedIconButton(
      modifier =
        Modifier.constrainAs(starButton) {
          bottom.linkTo(linkButton.top, margin = 24.dp)
          end.linkTo(parent.end, margin = 16.dp)
        },
      colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = Color.White),
      border = BorderStroke(1.dp, Color.White),
      onClick = {},
    ) {
      Icon(
        Icons.Outlined.StarOutline, // TODO: icon depending on starred state
        contentDescription = null,
      )
    }

    FloatingActionButton(
      modifier =
        Modifier.constrainAs(linkButton) {
          bottom.linkTo(parent.bottom, margin = 24.dp)
          end.linkTo(parent.end, margin = 16.dp)
        },
      onClick = {},
    ) {
      Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
    }

    Text(
      text = item.title,
      color = Color.White,
      style =
        MaterialTheme.typography.headlineMedium.copy(
          shadow = Shadow(color = Color.DarkGray, offset = Offset(x = 4f, y = 4f), blurRadius = 8f)
        ),
      modifier =
        Modifier.constrainAs(title) {
            bottom.linkTo(
              if (!item.description.isNullOrBlank()) description.top else parent.bottom,
              margin = if (!item.description.isNullOrBlank()) 8.dp else 16.dp,
            )
            start.linkTo(parent.start)
            end.linkTo(buttonsStartBarrier)
            width = Dimension.fillToConstraints
          }
          .padding(horizontal = 16.dp),
    )

    item.description?.let {
      Text(
        text = it,
        color = Color.White,
        style =
          MaterialTheme.typography.bodyMedium.copy(
            shadow =
              Shadow(color = Color.DarkGray, offset = Offset(x = 2f, y = 2f), blurRadius = 4f)
          ),
        modifier =
          Modifier.constrainAs(description) {
              bottom.linkTo(parent.bottom, margin = 16.dp)
              start.linkTo(parent.start)
              end.linkTo(buttonsStartBarrier)
              width = Dimension.fillToConstraints
            }
            .padding(horizontal = 16.dp),
      )
    }
  }
}

@Composable
private fun RelatedTokenButton(imageUrl: String, modifier: Modifier = Modifier) {
  OutlinedIconButton(modifier = modifier, border = BorderStroke(1.dp, Color.White), onClick = {}) {
    AsyncImage(
      model = imageUrl, // TODO: loading/error/null URL placeholders
      contentDescription = null,
      contentScale = ContentScale.Fit,
      modifier = Modifier.size(24.dp),
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun NewsItemPreview() {
  FeedItem(item = mockNewsItem(), relatedTokens = mockRelatedTokenItems())
}

private fun mockNewsItem(): NewsItem =
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

private fun mockRelatedTokenItems(): List<RelatedTokenItem> =
  listOf(
    RelatedTokenItem("bitcoin", "BTC", "https://static.coinstats.app/coins/1650455588819.png"),
    RelatedTokenItem("ethereum", "ETH", "https://static.coinstats.app/coins/1650455629727.png"),
    RelatedTokenItem("tether", "USDT", "https://static.coinstats.app/coins/1650455771843.png"),
  )
