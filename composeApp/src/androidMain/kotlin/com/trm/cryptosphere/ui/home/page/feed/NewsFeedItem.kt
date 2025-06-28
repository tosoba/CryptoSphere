package com.trm.cryptosphere.ui.home.page.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.trm.cryptosphere.domain.model.NewsItem

@Composable
fun NewsFeedItem(item: NewsItem, isCurrent: Boolean, modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    AsyncImage(
      model = item.imgUrl, // TODO: loading/error placeholders
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize(),
    )

    AnimatedVisibility(visible = isCurrent, enter = fadeIn(), exit = fadeOut()) {
      ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (backgroundGradient, title, description, linkButton, starButton) = createRefs()
        val buttonsStartBarrier = createStartBarrier(linkButton, starButton)

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
            Modifier.constrainAs(starButton) {
              // TODO: different constrains for compact height
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
            MaterialTheme.typography.headlineSmall.copy(
              shadow =
                Shadow(color = Color.DarkGray, offset = Offset(x = 4f, y = 4f), blurRadius = 8f)
            ),
          overflow = TextOverflow.Ellipsis,
          modifier =
            Modifier.constrainAs(title) {
              top.linkTo(parent.top, margin = 16.dp)
              bottom.linkTo(
                if (item.description.isNullOrBlank()) parent.bottom else description.top
              )
              verticalBias = 1f
              start.linkTo(parent.start, margin = 16.dp)
              end.linkTo(buttonsStartBarrier, margin = 16.dp)
              height = Dimension.preferredWrapContent
              width = Dimension.fillToConstraints
            },
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
            overflow = TextOverflow.Ellipsis,
            modifier =
              Modifier.constrainAs(description) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(buttonsStartBarrier, 16.dp)
                width = Dimension.fillToConstraints
              },
          )
        }
      }
    }
  }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun NewsFeedItemPreview() {
  NewsFeedItem(item = mockNewsItem(), isCurrent = true)
}
