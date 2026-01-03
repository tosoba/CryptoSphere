package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReadMore
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.SubcomposeAsyncImage
import com.trm.cryptosphere.core.base.openUrl
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.core.ui.localSharedElement
import com.trm.cryptosphere.core.ui.rememberCrossfadeImageRequest
import com.trm.cryptosphere.core.ui.tokenCarouselSharedTransitionKey
import com.trm.cryptosphere.core.util.isCompactHeight
import com.trm.cryptosphere.domain.model.NewsFeedItem
import com.trm.cryptosphere.domain.model.NewsItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedItemContent(
  item: NewsFeedItem,
  isCurrent: Boolean,
  modifier: Modifier = Modifier,
  onRelatedTokenItemClick: (Int, TokenCarouselConfig) -> Unit,
  onLinkClick: (NewsItem) -> Unit,
) {
  val adaptiveInfo = currentWindowAdaptiveInfo()
  val (news, relatedTokens) = item

  Box(modifier = modifier) {
    SubcomposeAsyncImage(
      model = rememberCrossfadeImageRequest(news.imgUrl),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize(),
      loading = {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
          CircularProgressIndicator(modifier = Modifier.size(64.dp), strokeWidth = 8.dp)
        }
      },
      error = {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
          Icon(
            imageVector = Icons.Outlined.ImageNotSupported,
            contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      },
    )

    AnimatedVisibility(visible = isCurrent, enter = fadeIn(), exit = fadeOut()) {
      ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (
          topBackgroundGradient,
          bottomBackgroundGradient,
          tokenCarousel,
          title,
          source,
          linkButton,
          shareButton) =
          createRefs()
        val topStatusBarHeight =
          with(LocalDensity.current) { WindowInsets.statusBars.getTop(LocalDensity.current).toDp() }
        val secondaryButtonsStartBarrier = createStartBarrier(shareButton)

        Box(
          modifier =
            Modifier.constrainAs(topBackgroundGradient) {
                top.linkTo(parent.top)

                width = Dimension.matchParent
                height = Dimension.value(topStatusBarHeight)
              }
              .background(
                Brush.verticalGradient(
                  0f to MaterialTheme.colorScheme.surfaceContainer,
                  1f to Color.Transparent,
                )
              )
        )

        Box(
          modifier =
            Modifier.constrainAs(bottomBackgroundGradient) {
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
              bottom.linkTo(linkButton.top, margin = 16.dp)
              end.linkTo(linkButton.end)
            },
          colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = Color.White),
          border = BorderStroke(1.dp, Color.White),
          onClick = {},
        ) {
          Icon(Icons.Outlined.Share, contentDescription = null)
        }

        val context = LocalContext.current
        if (adaptiveInfo.isCompactHeight()) {
          FloatingActionButton(
            modifier =
              Modifier.constrainAs(linkButton) {
                bottom.linkTo(parent.bottom, margin = 24.dp)
                end.linkTo(parent.end, margin = 16.dp)
              },
            onClick = {
              onLinkClick(news)
              context.openUrl(news.link)
            },
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ReadMore,
              contentDescription = null,
              modifier = Modifier.size(32.dp),
            )
          }
        } else {
          MediumFloatingActionButton(
            modifier =
              Modifier.constrainAs(linkButton) {
                bottom.linkTo(parent.bottom, margin = 24.dp)
                end.linkTo(parent.end, margin = 16.dp)
              },
            onClick = {
              onLinkClick(news)
              context.openUrl(news.link)
            },
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ReadMore,
              contentDescription = null,
              modifier = Modifier.size(32.dp),
            )
          }
        }

        Text(
          text = news.title,
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
                if (relatedTokens.isEmpty()) linkButton.start else secondaryButtonsStartBarrier,
                margin = 16.dp,
              )

              width = Dimension.fillToConstraints
              verticalBias = 1f
            },
        )

        Text(
          text = news.source,
          color = Color.White,
          style =
            MaterialTheme.typography.bodyMedium.copy(
              shadow =
                Shadow(color = Color.DarkGray, offset = Offset(x = 2f, y = 2f), blurRadius = 4f)
            ),
          overflow = TextOverflow.Ellipsis,
          modifier =
            Modifier.constrainAs(source) {
              if (relatedTokens.isEmpty()) {
                bottom.linkTo(linkButton.bottom)
              } else {
                bottom.linkTo(linkButton.top, margin = 16.dp)
              }
              start.linkTo(parent.start, margin = 16.dp)
              end.linkTo(
                if (relatedTokens.isEmpty()) linkButton.start else secondaryButtonsStartBarrier,
                margin = 16.dp,
              )

              width = Dimension.fillToConstraints
            },
        )

        TokenCarousel(
          tokens = relatedTokens,
          onItemClick = { token ->
            onRelatedTokenItemClick(token.id, TokenCarouselConfig(news.id))
          },
          itemHeight = if (adaptiveInfo.isCompactHeight()) 56.dp else 80.dp,
          modifier =
            Modifier.constrainAs(tokenCarousel) {
                top.linkTo(linkButton.top)
                bottom.linkTo(linkButton.bottom)
                start.linkTo(parent.start)
                end.linkTo(linkButton.start, margin = 16.dp)

                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
              }
              .localSharedElement(key = tokenCarouselSharedTransitionKey(news.id)),
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
