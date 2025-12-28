package com.trm.cryptosphere.ui.token.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.trm.cryptosphere.core.base.fullDecimalFormat
import com.trm.cryptosphere.core.base.openUrl
import com.trm.cryptosphere.core.base.shortDecimalFormat
import com.trm.cryptosphere.core.ui.NoRippleInteractionSource
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.util.isCompactHeight
import com.trm.cryptosphere.core.util.isExpandedHeight
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.logoUrl
import com.trm.cryptosphere.shared.MR
import kotlin.math.sign

private const val CMC_CURRENCIES_URL_PREFIX = "https://coinmarketcap.com/currencies/"

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TokenFeedContent(
  component: TokenFeedComponent,
  modifier: Modifier = Modifier,
  onImageUrlChange: (String?) -> Unit,
) {
  val tokens = component.viewModel.tokens.collectAsLazyPagingItems()

  val pagerState = rememberPagerState(pageCount = tokens::itemCount)
  fun currentToken(): TokenItem? = tokens[pagerState.currentPage]

  Crossfade(targetState = tokens.loadState.refresh is LoadState.Loading, modifier = modifier) {
    isLoading ->
    if (isLoading) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    } else {
      VerticalFeedPager(
        pagerState = pagerState,
        key = tokens.itemKey(TokenItem::id),
        modifier = Modifier.fillMaxSize(),
      ) { page ->
        tokens[page]?.let { token ->
          val context = LocalContext.current
          TokenFeedPagerItem(
            token = token,
            mainTokenTagNames = tokens.peek(0)?.tagNames.orEmpty().toSet(),
            onSeeMoreClick = { context.openUrl("$CMC_CURRENCIES_URL_PREFIX${token.slug}") },
            modifier = Modifier.fillMaxSize(),
          )
        }
      }

      DisposableEffect(pagerState.currentPage, tokens) {
        val callbacks =
          object : Lifecycle.Callbacks {
            override fun onResume() {
              if (pagerState.currentPage < tokens.itemCount) {
                val currentToken = currentToken()
                component.onCurrentTokenChange(currentToken)
                onImageUrlChange(currentToken?.logoUrl)
              }
            }
          }
        component.lifecycle.subscribe(callbacks)
        onDispose { component.lifecycle.unsubscribe(callbacks) }
      }
    }
  }
}

@Composable
private fun TokenFeedPagerItem(
  token: TokenItem,
  mainTokenTagNames: Set<String>,
  onSeeMoreClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

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
      model = token.logoUrl,
      contentDescription = null,
      contentScale = ContentScale.Fit,
    )

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
            MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Medium).toSpanStyle()
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
      LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(rowCount),
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
      val seeMoreButtonHeight = 40.dp
      val parameters = remember {
        val valueChangeFormat: (Number?) -> String = {
          it?.toDouble()?.fullDecimalFormat()?.let { formatted -> " $formatted% " }.orEmpty()
        }
        listOfNotNull(
          TokenParameter(
            label = MR.strings.price.resolve(context),
            value = token.quote.price,
            valueFormat = { it.toDouble().fullDecimalFormat() },
            valueChange = token.quote.percentChange24h,
            valueChangeFormat = valueChangeFormat,
          ),
          TokenParameter(
            label = MR.strings.volume_24h.resolve(context),
            value = token.quote.volume24h,
            valueChange = token.quote.volumeChange24h,
            valueChangeFormat = valueChangeFormat,
          ),
          TokenParameter(
            label = MR.strings.market_cap.resolve(context),
            value = token.quote.marketCap,
          ),
          TokenParameter(
            label = MR.strings.circulating_supply.resolve(context),
            value = token.circulatingSupply,
          ),
          TokenParameter(
            label = MR.strings.total_supply.resolve(context),
            value = token.totalSupply,
          ),
          token.maxSupply?.let {
            TokenParameter(label = MR.strings.max_supply.resolve(context), value = it)
          },
        )
      }

      TokenParameterCardsColumn(
        parameters =
          parameters.take(
            ((maxHeight - seeMoreButtonHeight).value / calculateTokenParametersCardHeight().value)
              .toInt()
          ),
        onSeeMoreClick = onSeeMoreClick,
        modifier = Modifier.fillMaxWidth(),
      )
    }
  }
}

@Composable
private fun TokenParameterCardsColumn(
  parameters: List<TokenParameter>,
  onSeeMoreClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    parameters.forEachIndexed { index, parameter ->
      TokenParameterCard(
        parameter = parameter,
        shape =
          when {
            parameters.size == 1 -> {
              RoundedCornerShape(16.dp)
            }
            index == 0 -> {
              RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            }
            index == parameters.lastIndex -> {
              RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            }
            else -> {
              RoundedCornerShape(0.dp)
            }
          },
        modifier = Modifier.fillMaxWidth(),
      )

      Spacer(modifier = Modifier.height(2.dp))
    }

    TextButton(onClick = onSeeMoreClick, modifier = Modifier.fillMaxWidth()) {
      Text(MR.strings.see_more.resolve())
    }
  }
}

@Composable
private fun TokenParameterCard(
  parameter: TokenParameter,
  shape: Shape,
  modifier: Modifier = Modifier,
) {
  Card(modifier = modifier, shape = shape) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
      Text(
        text = parameter.label,
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
      )

      val valueStyle = MaterialTheme.typography.titleLarge
      val valueChangeStyle = MaterialTheme.typography.labelLarge
      val valueChange = parameter.valueChange
      val valueChangeFormat = parameter.valueChangeFormat

      val valueChangeText =
        if (valueChange != null && valueChangeFormat != null) valueChangeFormat(valueChange)
        else null
      val valueChangeId = "valueChange"

      val textMeasurer = rememberTextMeasurer()
      val textSize =
        if (valueChangeText != null) {
          remember(valueChangeText, valueChangeStyle) {
            textMeasurer.measure(valueChangeText, valueChangeStyle).size
          }
        } else {
          null
        }

      Text(
        text =
          buildAnnotatedString {
            append(parameter.valueFormat(parameter.value))
            if (valueChangeText != null) {
              append(' ')
              appendInlineContent(valueChangeId)
            }
          },
        inlineContent =
          if (textSize != null) {
            mapOf(
              valueChangeId to
                InlineTextContent(
                  Placeholder(
                    width = with(LocalDensity.current) { (textSize.width.toDp() + 8.dp).toSp() },
                    height = with(LocalDensity.current) { textSize.height.toDp().toSp() },
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                  )
                ) {
                  val valueChangePositive = valueChange?.toDouble()?.sign == 1.0
                  Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                      Modifier.fillMaxSize()
                        .background(
                          color = if (valueChangePositive) Color.Green else Color.Red,
                          shape = RoundedCornerShape(4.dp),
                        ),
                  ) {
                    Text(
                      text = valueChangeText.orEmpty(),
                      style =
                        if (valueChangePositive) valueChangeStyle
                        else valueChangeStyle.copy(color = Color.White),
                      modifier = Modifier.padding(horizontal = 4.dp),
                    )
                  }
                }
            )
          } else {
            emptyMap()
          },
        style = valueStyle,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
      )
    }
  }
}

@Composable
private fun calculateTokenParametersCardHeight(): Dp {
  val labelTextHeight =
    with(LocalDensity.current) { MaterialTheme.typography.labelSmall.lineHeight.toDp() }
  val valueTextHeight =
    with(LocalDensity.current) { MaterialTheme.typography.titleLarge.lineHeight.toDp() }
  val verticalPadding = 16.dp
  val spacerHeight = 2.dp
  return labelTextHeight + valueTextHeight + verticalPadding + spacerHeight
}

private data class TokenParameter(
  val label: String,
  val value: Number,
  val valueFormat: (Number) -> String = { it.toDouble().shortDecimalFormat() },
  val valueChange: Number? = null,
  val valueChangeFormat: ((Number?) -> String)? = null,
)
