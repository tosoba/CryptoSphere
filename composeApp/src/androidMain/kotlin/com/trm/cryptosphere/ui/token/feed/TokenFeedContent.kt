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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.trm.cryptosphere.core.ui.BottomGradientOverlay
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.TopGradientOverlay
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.VerticalFeedPagerContentPadding
import com.trm.cryptosphere.core.ui.rememberTokenCarouselSharedContentState
import com.trm.cryptosphere.domain.model.logoUrl
import kotlinx.coroutines.launch

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

      VerticalFeedPager(
        pagerState = pagerState,
        contentPadding = VerticalFeedPagerContentPadding.ExtraTop,
        modifier = Modifier.fillMaxSize(),
      ) { page ->
        val token = state.feedItems[page]

        ElevatedCard(
          modifier = Modifier.fillMaxSize(),
          onClick = { component.navigateToTokenDetails(token.symbol) },
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
              modifier = Modifier.size(128.dp),
              model = token.logoUrl,
              contentDescription = null,
              contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "#${token.cmcRank} ${token.symbol}",
              style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
              modifier =
                Modifier.padding(horizontal = 16.dp)
                  .sharedElement(
                    sharedContentState = rememberSharedContentState("token-symbol-${token.symbol}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                  ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
              OutlinedCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Text(
                    text = "Price",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
                  )
                  Text(
                    text = "${token.quote.price}",
                    style =
                      MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
                  )
                }
              }

              Spacer(modifier = Modifier.width(8.dp))

              OutlinedCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Text(
                    text = "Market Cap",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
                  )
                  Text(
                    text = "${token.quote.marketCap}",
                    style =
                      MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
                  )
                }
              }
            }
          }
        }
      }

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
          Modifier.sharedElement(
            sharedContentState =
              rememberTokenCarouselSharedContentState(
                requireNotNull(component.tokenCarouselConfig.parentSharedElementId)
              ),
            animatedVisibilityScope = animatedVisibilityScope,
          ),
      )

      TopGradientOverlay(modifier = Modifier.align(Alignment.TopCenter))

      BottomGradientOverlay(modifier = Modifier.align(Alignment.BottomCenter))

      // TODO: navigation toolbar at the bottom
    }
  }
}
