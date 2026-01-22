package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.logoUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenCarousel(
  tokens: List<TokenItem>,
  onItemClick: (TokenItem) -> Unit,
  modifier: Modifier = Modifier,
  state: CarouselState = rememberCarouselState(itemCount = tokens::size),
  highlightedTokenId: Int? = null,
  contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
  itemHeight: Dp = 80.dp,
  labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
) {
  HorizontalMultiBrowseCarousel(
    state = state,
    modifier = modifier,
    preferredItemWidth = itemHeight * 3 / 2,
    itemSpacing = 8.dp,
    contentPadding = contentPadding,
  ) { index ->
    val token = tokens[index]

    Surface(
      modifier =
        Modifier.height(itemHeight)
          .maskClip(MaterialTheme.shapes.medium)
          .maskBorder(
            border =
              BorderStroke(
                width = 1.dp,
                color =
                  MaterialTheme.colorScheme.outlineVariant.copy(
                    alpha = if (token.id == highlightedTokenId) 1f else .5f
                  ),
              ),
            shape = MaterialTheme.shapes.medium,
          ),
      color =
        MaterialTheme.colorScheme.surface.run {
          if (token.id == highlightedTokenId) this else copy(alpha = alpha * .25f)
        },
      enabled = token.id != highlightedTokenId,
      onClick = { onItemClick(token) },
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(
          modifier = Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(4.dp)),
          model = rememberCrossfadeImageRequest(token.logoUrl),
          contentDescription = null,
          contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "#${token.cmcRank} ${token.symbol}",
          maxLines = 1,
          textAlign = TextAlign.Center,
          style = labelStyle,
          modifier = Modifier.fillMaxWidth().basicMarquee(),
        )

        Spacer(modifier = Modifier.height(8.dp))
      }
    }
  }
}

@Composable fun tokenCarouselSharedTransitionKey(id: String) = "token-carousel-$id"
