package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.trm.cryptosphere.domain.model.TokenCarouselItem

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TokensCarousel(tokens: List<TokenCarouselItem>, onItemClick: (TokenCarouselItem) -> Unit) {
  HorizontalMultiBrowseCarousel(
    state = rememberCarouselState(itemCount = tokens::size),
    modifier =
      Modifier.fillMaxWidth().background(color = Color.Transparent).padding(vertical = 8.dp),
    preferredItemWidth = 112.dp,
    itemSpacing = 8.dp,
    contentPadding = PaddingValues(horizontal = 16.dp),
  ) { index ->
    // TODO: if there is a need to put any border/shadow/glow effect on the card just put it
    // behind the card with the same maskClip and make the card itself smaller
    val token = tokens[index]

    Card(
      modifier = Modifier.width(112.dp).aspectRatio(2f).maskClip(MaterialTheme.shapes.medium),
      colors =
        CardDefaults.cardColors().run {
          copy(
            containerColor = containerColor.copy(alpha = containerColor.alpha * .95f),
            disabledContainerColor =
              disabledContainerColor.copy(alpha = disabledContainerColor.alpha * .95f),
          )
        },
      onClick = { onItemClick(token) },
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.height(4.dp))

        // TODO: use dominant color of image as card background
        AsyncImage(
          modifier = Modifier.weight(1f).aspectRatio(1f),
          model = token.imageUrl,
          contentDescription = null,
          contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = token.symbol,
          maxLines = 1,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.labelMedium,
          modifier = Modifier.fillMaxWidth().basicMarquee(),
        )
      }
    }
  }
}
