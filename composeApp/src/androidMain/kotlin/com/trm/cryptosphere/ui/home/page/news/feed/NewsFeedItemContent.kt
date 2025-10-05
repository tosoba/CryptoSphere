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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.core.ui.localSharedElement
import com.trm.cryptosphere.core.ui.tokenCarouselSharedTransitionKey
import com.trm.cryptosphere.domain.model.NewsFeedItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsFeedItemContent(
  item: NewsFeedItem,
  isCurrent: Boolean,
  modifier: Modifier = Modifier,
  onRelatedTokenItemClick: (Int, TokenCarouselConfig) -> Unit,
) {
  val (news, relatedTokens) = item

  Box(modifier = modifier) {
    AsyncImage(
      model = news.imgUrl, // TODO: loading/error placeholders
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
            onRelatedTokenItemClick(token.id, TokenCarouselConfig(news.id, relatedTokens))
          },
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
