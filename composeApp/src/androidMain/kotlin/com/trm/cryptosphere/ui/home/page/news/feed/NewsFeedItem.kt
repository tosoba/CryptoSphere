package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.trm.cryptosphere.core.ui.SharedTransitionPreview
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.core.ui.localSharedElement
import com.trm.cryptosphere.core.ui.tokenCarouselSharedTransitionKey
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.model.TokenCarouselItem
import com.trm.cryptosphere.domain.model.mockTokenCarouselItems
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsFeedItem(
  item: NewsItem,
  isCurrent: Boolean,
  carouselItems: List<TokenCarouselItem>,
  modifier: Modifier = Modifier,
  onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
) {
  Box(modifier = modifier) {
    AsyncImage(
      model = item.imgUrl, // TODO: loading/error placeholders
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize(),
    )

    AnimatedVisibility(visible = isCurrent, enter = fadeIn(), exit = fadeOut()) {
      ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (
          backgroundGradient, tokenCarousel, title, source, linkButton, shareButton, starButton) =
          createRefs()
        val secondaryButtonsStartBarrier = createStartBarrier(shareButton, starButton)

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

        OutlinedIconButton(
          modifier =
            Modifier.constrainAs(shareButton) {
              bottom.linkTo(starButton.top, margin = 16.dp)
              start.linkTo(linkButton.start)
              end.linkTo(linkButton.end)
            },
          colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = Color.White),
          border = BorderStroke(1.dp, Color.White),
          onClick = {},
        ) {
          Icon(Icons.Outlined.Share, contentDescription = null)
        }

        OutlinedIconButton(
          modifier =
            Modifier.constrainAs(starButton) {
              bottom.linkTo(linkButton.top, margin = 16.dp)
              start.linkTo(linkButton.start)
              end.linkTo(linkButton.end)
            },
          colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = Color.White),
          border = BorderStroke(1.dp, Color.White),
          onClick = {},
        ) {
          // TODO: change icon or animate the whole button depending on starred state
          Icon(Icons.Outlined.StarOutline, contentDescription = null)
        }

        MediumFloatingActionButton(
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
            MaterialTheme.typography.headlineSmall.copy(
              shadow =
                Shadow(color = Color.DarkGray, offset = Offset(x = 4f, y = 4f), blurRadius = 8f)
            ),
          overflow = TextOverflow.Ellipsis,
          modifier =
            Modifier.constrainAs(title) {
              top.linkTo(parent.top, margin = 16.dp)
              bottom.linkTo(source.top, margin = 4.dp)
              start.linkTo(parent.start, margin = 16.dp)
              end.linkTo(
                if (carouselItems.isEmpty()) linkButton.start else secondaryButtonsStartBarrier,
                margin = 16.dp,
              )

              width = Dimension.fillToConstraints
              verticalBias = 1f
            },
        )

        Text(
          text = item.source,
          color = Color.White,
          style =
            MaterialTheme.typography.bodyMedium.copy(
              shadow =
                Shadow(color = Color.DarkGray, offset = Offset(x = 2f, y = 2f), blurRadius = 4f)
            ),
          overflow = TextOverflow.Ellipsis,
          modifier =
            Modifier.constrainAs(source) {
              if (carouselItems.isEmpty()) {
                bottom.linkTo(linkButton.bottom)
              } else {
                bottom.linkTo(linkButton.top, margin = 16.dp)
              }
              start.linkTo(parent.start, margin = 16.dp)
              end.linkTo(
                if (carouselItems.isEmpty()) linkButton.start else secondaryButtonsStartBarrier,
                margin = 16.dp,
              )

              width = Dimension.fillToConstraints
            },
        )

        TokenCarousel(
          tokens = carouselItems,
          onItemClick = { token ->
            onTokenCarouselItemClick(token.symbol, TokenCarouselConfig(item.id, carouselItems))
          },
          modifier =
            Modifier.constrainAs(tokenCarousel) {
                top.linkTo(linkButton.top)
                bottom.linkTo(linkButton.bottom)
                start.linkTo(parent.start)
                end.linkTo(linkButton.start, margin = 16.dp)

                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
              }
              .localSharedElement(key = tokenCarouselSharedTransitionKey(item.id)),
          contentPadding = PaddingValues(start = 16.dp),
          labelStyle =
            MaterialTheme.typography.labelMedium.copy(
              color = Color.White,
              shadow =
                Shadow(color = Color.DarkGray, offset = Offset(x = 2f, y = 2f), blurRadius = 4f),
            ),
        )
      }
    }
  }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "Carousel Test")
@Composable
private fun NewsFeedItemPreview(
  @PreviewParameter(NewsFeedItemPreviewParameterProvider::class) params: NewsFeedItemPreviewParams
) {
  SharedTransitionPreview {
    NewsFeedItem(
      item = mockNewsItem(),
      isCurrent = true,
      carouselItems = params.carouselItems,
      onTokenCarouselItemClick = { _, _ -> },
    )
  }
}

private data class NewsFeedItemPreviewParams(val carouselItems: List<TokenCarouselItem>)

private class NewsFeedItemPreviewParameterProvider :
  PreviewParameterProvider<NewsFeedItemPreviewParams> {
  override val values: Sequence<NewsFeedItemPreviewParams> =
    sequenceOf(
      NewsFeedItemPreviewParams(carouselItems = emptyList()),
      NewsFeedItemPreviewParams(carouselItems = mockTokenCarouselItems()),
    )
}

@OptIn(ExperimentalTime::class)
private fun mockNewsItem(): NewsItem =
  NewsItem(
    id = "1",
    searchKeyWords = listOf("dogecoin", "DOGE"),
    feedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    source = "U.Today",
    title = "Dogecoin Account Drops Casual 'Sup' Tweet: What's Behind It?",
    sourceLink = "https://u.today/",
    imgUrl = "https://u.today/sites/default/files/styles/736x/public/2025-06/s7426.jpg",
    relatedCoins =
      listOf(
        "dogecoin",
        "0x4206931337dc273a630d328da6441786bfad668f_ethereum",
        "0x1a8e39ae59e5556b56b76fcba98d22c9ae557396_cronos",
      ),
    link =
      "https://u.today/dogecoin-account-drops-casual-sup-tweet-whats-behind-it?utm_medium=referral&utm_source=coinstats",
    fetchedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
  )
