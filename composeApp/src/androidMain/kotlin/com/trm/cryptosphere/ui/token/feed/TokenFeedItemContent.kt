package com.trm.cryptosphere.ui.token.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atMost
import coil3.compose.AsyncImage
import com.trm.cryptosphere.core.ui.NoRippleInteractionSource
import com.trm.cryptosphere.core.ui.cardListItemRoundedCornerShape
import com.trm.cryptosphere.core.ui.rememberCrossfadeImageRequest
import com.trm.cryptosphere.core.util.isCompactHeight
import com.trm.cryptosphere.core.util.isExpandedHeight
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.logoUrl

@Composable
fun TokenFeedItemContent(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  modifier: Modifier = Modifier,
) {
  ConstraintLayout(modifier = modifier) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val isCompactHeight = windowAdaptiveInfo.isCompactHeight()
    val (logo, symbol, tags, parameters) = createRefs()
    val halfWidthGuideline = createGuidelineFromStart(.5f)

    AsyncImage(
      modifier =
        Modifier.constrainAs(logo) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(if (isCompactHeight) halfWidthGuideline else parent.end)

            height = Dimension.fillToConstraints.atMost(128.dp)
          }
          .aspectRatio(1f)
          .clip(RoundedCornerShape(16.dp)),
      model = rememberCrossfadeImageRequest(token.logoUrl),
      contentDescription = null,
      contentScale = ContentScale.Fit,
    )

    TokenFeedRankAndSymbol(
      token = token,
      modifier =
        Modifier.constrainAs(symbol) {
          top.linkTo(logo.bottom, margin = 8.dp)
          start.linkTo(parent.start)
          end.linkTo(if (isCompactHeight) halfWidthGuideline else parent.end)
        },
    )

    if (token.tagNames.isNotEmpty()) {
      val rowCount =
        minOf(
          when {
            isCompactHeight -> 2
            windowAdaptiveInfo.isExpandedHeight() -> 5
            else -> 3
          },
          token.tagNames.size,
        )
      TokenFeedTagsGrid(
        token = token,
        mainTokenTagNames = mainTokenTagNames,
        rowCount = rowCount,
        modifier =
          Modifier.constrainAs(tags) {
              top.linkTo(symbol.bottom, margin = 8.dp)
              start.linkTo(parent.start)
              end.linkTo(if (isCompactHeight) halfWidthGuideline else parent.end)
              if (isCompactHeight) bottom.linkTo(parent.bottom)

              width = Dimension.fillToConstraints
              height = Dimension.preferredWrapContent
            }
            .height(32.dp * rowCount + 4.dp * (rowCount - 1)),
      )
    }

    BoxWithConstraints(
      modifier =
        Modifier.constrainAs(parameters) {
          top.linkTo(
            anchor =
              when {
                isCompactHeight -> parent.top
                token.tagNames.isNotEmpty() -> tags.bottom
                else -> symbol.bottom
              },
            margin = if (isCompactHeight) 0.dp else 16.dp,
          )
          start.linkTo(
            anchor = if (isCompactHeight) halfWidthGuideline else parent.start,
            margin = if (isCompactHeight) 32.dp else 0.dp,
          )
          end.linkTo(parent.end)
          bottom.linkTo(parent.bottom)

          width = Dimension.fillToConstraints
          height =
            if (isCompactHeight) Dimension.fillToConstraints else Dimension.preferredWrapContent
          verticalBias = if (isCompactHeight) 0.5f else 0f
        },
      contentAlignment = if (isCompactHeight) Alignment.Center else Alignment.TopCenter,
    ) {
      TokenFeedParameterCardsColumn(
        parameters =
          rememberTokenFeedParameters(token)
            .take((maxHeight.value / tokenFeedParameterCardHeight().value).toInt()),
        modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}

@Composable
private fun TokenFeedRankAndSymbol(token: TokenItem, modifier: Modifier = Modifier) {
  val rankId = "rank"
  val rankStyle = MaterialTheme.typography.titleMedium
  val textMeasurer = rememberTextMeasurer()
  val textSize =
    remember(token.cmcRank, rankStyle) {
      textMeasurer.measure(" #${token.cmcRank} ", rankStyle).size
    }

  Text(
    text =
      buildAnnotatedString {
        appendInlineContent(rankId)
        withStyle(
          MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold).toSpanStyle()
        ) {
          append(' ')
          append(token.symbol)
        }
      },
    inlineContent =
      mapOf(
        rankId to
          InlineTextContent(
            Placeholder(
              width = with(LocalDensity.current) { (textSize.width.toDp() + 8.dp).toSp() },
              height = with(LocalDensity.current) { textSize.height.toDp().toSp() },
              placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline,
            )
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier =
                Modifier.fillMaxSize()
                  .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp),
                  ),
            ) {
              Text(
                text = "#${token.cmcRank}",
                style = rankStyle.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.padding(horizontal = 4.dp),
              )
            }
          }
      ),
    modifier = modifier,
  )
}

@Composable
private fun TokenFeedTagsGrid(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  rowCount: Int,
  modifier: Modifier = Modifier,
) {
  LazyHorizontalStaggeredGrid(
    rows = StaggeredGridCells.Fixed(rowCount),
    modifier = modifier,
    horizontalItemSpacing = 4.dp,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    items(token.tagNames) { name ->
      FilterChip(
        onClick = {},
        label = { Text(name) },
        selected = name in mainTokenTagNames,
        interactionSource = NoRippleInteractionSource(),
      )
    }
  }
}

@Composable
private fun TokenFeedParameterCardsColumn(
  parameters: List<TokenFeedParameter>,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    parameters.forEachIndexed { index, parameter ->
      TokenFeedParameterCard(
        parameter = parameter,
        shape =
          cardListItemRoundedCornerShape(
            isTopRounded = index == 0,
            isBottomRounded = index == parameters.lastIndex,
          ),
        modifier = Modifier.fillMaxWidth(),
      )

      Spacer(modifier = Modifier.height(2.dp))
    }
  }
}

@Composable
private fun tokenFeedParameterCardHeight(): Dp {
  val labelTextHeight =
    with(LocalDensity.current) { MaterialTheme.typography.labelSmall.lineHeight.toDp() }
  val valueTextHeight =
    with(LocalDensity.current) { MaterialTheme.typography.titleLarge.lineHeight.toDp() }
  val verticalPadding = 16.dp
  val spacerHeight = 2.dp
  return labelTextHeight + valueTextHeight + verticalPadding + spacerHeight
}
