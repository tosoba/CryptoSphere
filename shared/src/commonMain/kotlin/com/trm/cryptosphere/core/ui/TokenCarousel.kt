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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
  contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
  labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
) {
  HorizontalUncontainedCarousel(
    state = rememberCarouselState(itemCount = tokens::size),
    modifier = modifier,
    itemWidth = 120.dp,
    itemSpacing = 8.dp,
    contentPadding = contentPadding,
  ) { index ->
    // TODO: if there is a need to put any border/shadow/glow effect on the card just put it
    // behind the card with the same maskClip and make the card itself smaller
    val token = tokens[index]

    Surface(
      modifier =
        Modifier.width(120.dp)
          .aspectRatio(1.5f)
          .maskClip(MaterialTheme.shapes.medium)
          .maskBorder(
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
            shape = MaterialTheme.shapes.medium,
          ),
      color = MaterialTheme.colorScheme.surfaceContainer.run { copy(alpha = alpha * .25f) },
      onClick = { onItemClick(token) },
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.height(8.dp))

        // TODO: use dominant color of image as card background
        AsyncImage(
          modifier = Modifier.weight(1f).aspectRatio(1f),
          model = token.logoUrl,
          contentDescription = null,
          contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = token.symbol,
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
