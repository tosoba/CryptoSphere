package com.trm.cryptosphere.core.ui

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.trm.cryptosphere.domain.model.RelatedTokenItem

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RelatedTokensCarousel(relatedTokens: List<RelatedTokenItem>) {
  val carouselState = rememberCarouselState(itemCount = relatedTokens::size)
  HorizontalUncontainedCarousel(
    state = carouselState,
    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
    itemWidth = 128.dp,
    itemSpacing = 8.dp,
    contentPadding = PaddingValues(horizontal = 16.dp),
    flingBehavior = CarouselDefaults.singleAdvanceFlingBehavior(carouselState),
  ) { index ->
    // TODO: if there is a need to put any border/shadow/glow effect on the card just put it
    // behind the card with the same maskClip and make the card itself smaller
    Card(
      modifier = Modifier.width(128.dp).aspectRatio(2f).maskClip(MaterialTheme.shapes.extraLarge),
      onClick = {},
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.height(4.dp))

        val token = relatedTokens[index]
        // TODO: use dominant color of image as card background
        AsyncImage(
          modifier = Modifier.weight(1f).aspectRatio(1f),
          model = token.imageUrl,
          contentDescription = null,
          contentScale = ContentScale.Fit,
        )
        Text(
          text = token.symbol,
          maxLines = 1,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.labelLarge,
          modifier = Modifier.fillMaxWidth().basicMarquee(),
        )
      }
    }
  }
}
