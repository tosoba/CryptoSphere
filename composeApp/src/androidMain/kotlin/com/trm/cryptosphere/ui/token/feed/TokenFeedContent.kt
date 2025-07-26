package com.trm.cryptosphere.ui.token.feed

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.rememberTokenCarouselSharedContentState
import com.trm.cryptosphere.domain.model.logoUrl
import kotlinx.coroutines.launch
import mx.platacard.pagerindicator.PagerIndicatorOrientation
import mx.platacard.pagerindicator.PagerWormIndicator

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TokenFeedContent(
  component: TokenFeedComponent,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  Scaffold(modifier = modifier) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      val scope = rememberCoroutineScope()
      val state by component.state.collectAsStateWithLifecycle()
      val pagerState = rememberPagerState(pageCount = state.feedItems::size)

      VerticalFeedPager(pagerState = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        val token = state.feedItems[page]

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
        ) {
          Spacer(modifier = Modifier.height(112.dp))

          AsyncImage(
            modifier = Modifier.size(128.dp),
            model = token.logoUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = token.symbol,
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier =
              Modifier.sharedElement(
                sharedContentState = rememberSharedContentState("token-symbol-${token.symbol}"),
                animatedVisibilityScope = animatedVisibilityScope,
              ),
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(modifier = Modifier.fillMaxWidth()) {
            TokenParameterCard(
              label = "Price",
              value = token.quote.price.toString(),
              modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            TokenParameterCard(
              label = "Market Cap",
              value = token.quote.marketCap.toString(),
              modifier = Modifier.weight(1f),
            )
          }
        }
      }

      PagerWormIndicator(
        pagerState = pagerState,
        activeDotColor = Color.LightGray,
        dotColor = Color.DarkGray,
        dotCount = 5,
        orientation = PagerIndicatorOrientation.Vertical,
        modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp),
      )

      TokenCarousel(
        tokens = component.tokenCarouselConfig.items,
        onItemClick = { item ->
          if (state.mainTokenSymbol == item.symbol) {
            scope.launch { pagerState.animateScrollToPage(0) }
          } else {
            component.reloadFeedForSymbol(item.symbol)
          }
        },
        modifier =
          Modifier.fillMaxWidth()
            .padding(top = 8.dp)
            .sharedElement(
              sharedContentState =
                rememberTokenCarouselSharedContentState(
                  requireNotNull(component.tokenCarouselConfig.parentSharedElementId)
                ),
              animatedVisibilityScope = animatedVisibilityScope,
            ),
      )

      // TODO: navigation toolbar at the bottom
    }
  }
}

@Composable
private fun TokenParameterCard(label: String, value: String, modifier: Modifier = Modifier) {
  OutlinedCard(modifier = modifier) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
      )
      Text(
        text = value,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
      )
    }
  }
}
