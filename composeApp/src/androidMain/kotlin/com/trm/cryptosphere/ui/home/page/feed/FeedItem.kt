package com.trm.cryptosphere.ui.home.page.feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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

@Composable
fun FeedItem(
  item: NewsItem,
  relatedTokens: List<RelatedTokenItem>,
  isCurrent: Boolean,
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

    if (!isCurrent) return@ConstraintLayout

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

    Row(
      modifier =
        Modifier.constrainAs(relatedTokensList) {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          }
          .horizontalScroll(rememberScrollState())
    ) {
      Spacer(modifier = Modifier.width(12.dp))
      relatedTokens.forEach {
        RelatedTokenButton(imageUrl = it.imageUrl.orEmpty())
        Spacer(modifier = Modifier.width(12.dp))
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
private fun RelatedTokenButton(imageUrl: String) {
  OutlinedIconButton(
    // TODO: proper glow effect (like in DayLighter) with color based on image's dominant color
    border =
      BorderStroke(2.dp, Brush.radialGradient(listOf(Color.White, Color.LightGray), radius = 500f)),
    onClick = {},
  ) {
    AsyncImage(
      model = imageUrl, // TODO: loading/error/null URL placeholders
      contentDescription = null,
      contentScale = ContentScale.Fit,
      modifier = Modifier.size(28.dp),
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FeedItemPreview() {
  FeedItem(item = mockNewsItem(), relatedTokens = mockRelatedTokenItems(), isCurrent = true)
}
