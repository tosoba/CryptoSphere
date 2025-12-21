package com.trm.cryptosphere.ui.token.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.trm.cryptosphere.core.base.LoadableState
import com.trm.cryptosphere.core.base.fullDecimalFormat
import com.trm.cryptosphere.core.base.openUrl
import com.trm.cryptosphere.core.base.shortDecimalFormat
import com.trm.cryptosphere.core.ui.NoRippleInteractionSource
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.localSharedElement
import com.trm.cryptosphere.core.ui.tokenCarouselSharedTransitionKey
import com.trm.cryptosphere.core.util.isCompactHeight
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.core.util.toNavigationSuiteType
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.logoUrl
import com.trm.cryptosphere.shared.MR

private const val CMC_CURRENCIES_URL_PREFIX = "https://coinmarketcap.com/currencies/"

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TokenFeedContent(
  component: TokenFeedComponent,
  modifier: Modifier = Modifier,
  onImageUrlChange: (String?) -> Unit,
) {
  val context = LocalContext.current
  val navigationSuiteType = currentWindowAdaptiveInfo().toNavigationSuiteType()

  Scaffold(
    modifier = modifier,
    bottomBar = {
      if (navigationSuiteType == NavigationSuiteType.NavigationBar) {
        FlexibleBottomAppBar(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            IconButton(onClick = component.navigateBack) {
              Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            FilledTonalIconButton(onClick = component.navigateHome) {
              Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            }

            IconButton({}, enabled = false) {
              Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
          }
        }
      }
    },
  ) { paddingValues ->
    ConstraintLayout(
      modifier =
        Modifier.fillMaxSize()
          .padding(
            PaddingValues(
              start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
              end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
              bottom = paddingValues.calculateBottomPadding(),
            )
          )
    ) {
      val (pager, tokenCarousel, switchTokenButton, floatingToolbar) = createRefs()
      val historyIdState by component.viewState.historyId.collectAsStateWithLifecycle()
      val tokens = component.viewState.tokens.collectAsLazyPagingItems()
      val isLoading =
        tokens.loadState.refresh is LoadState.Loading || historyIdState is LoadableState.Loading
      val pagerState = rememberPagerState { tokens.itemCount }

      fun currentToken(): TokenItem? = tokens[pagerState.currentPage]

      Crossfade(
        targetState = isLoading,
        modifier =
          Modifier.constrainAs(pager) {
            top.linkTo(parent.top)
            bottom.linkTo(tokenCarousel.top)
            start.linkTo(parent.start, margin = 32.dp)
            end.linkTo(
              if (navigationSuiteType == NavigationSuiteType.NavigationBar) parent.end
              else floatingToolbar.start,
              margin = 32.dp,
            )

            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
          },
      ) { isLoading ->
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
              TokenFeedPagerItem(
                token = token,
                onSeeMoreClick = { context.openUrl("$CMC_CURRENCIES_URL_PREFIX${token.slug}") },
                modifier = Modifier.fillMaxSize(),
              )
            }
          }

          LaunchedEffect(pagerState.currentPage, tokens) {
            if (pagerState.currentPage < tokens.itemCount) {
              onImageUrlChange(currentToken()?.logoUrl)
            }
          }
        }
      }

      TokenCarousel(
        tokens = component.tokenCarouselConfig.items,
        highlightedTokenId = component.viewState.mode.tokenId,
        onItemClick = { item -> component.navigateToTokenFeed(item.id) },
        modifier =
          Modifier.constrainAs(tokenCarousel) {
              start.linkTo(parent.start)
              end.linkTo(switchTokenButton.start, margin = 16.dp)
              top.linkTo(switchTokenButton.top)
              bottom.linkTo(switchTokenButton.bottom)

              width = Dimension.fillToConstraints
              height = Dimension.fillToConstraints
            }
            .localSharedElement(
              key =
                tokenCarouselSharedTransitionKey(
                  requireNotNull(component.tokenCarouselConfig.parentSharedElementId)
                )
            ),
        contentPadding = PaddingValues(start = 16.dp),
      )

      MediumFloatingActionButton(
        modifier =
          Modifier.constrainAs(switchTokenButton) {
              bottom.linkTo(parent.bottom, margin = 16.dp)
              end.linkTo(parent.end, margin = 16.dp)
            }
            .alpha(if (pagerState.currentPage == 0) .38f else 1f),
        containerColor =
          if (pagerState.currentPage == 0) MaterialTheme.colorScheme.surfaceVariant
          else MaterialTheme.colorScheme.primaryContainer,
        elevation =
          if (pagerState.currentPage == 0) FloatingActionButtonDefaults.bottomAppBarFabElevation()
          else FloatingActionButtonDefaults.elevation(),
        interactionSource = if (pagerState.currentPage == 0) NoRippleInteractionSource() else null,
        onClick = {
          if (pagerState.currentPage != 0) currentToken()?.id?.let(component::navigateToTokenFeed)
        },
      ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
      }

      if (navigationSuiteType == NavigationSuiteType.NavigationRail) {
        VerticalFloatingToolbar(
          expanded = true,
          modifier =
            Modifier.constrainAs(floatingToolbar) {
              top.linkTo(parent.top, margin = 16.dp)
              bottom.linkTo(switchTokenButton.top, margin = 16.dp)
              end.linkTo(parent.end, margin = 16.dp)
            },
        ) {
          IconButton(onClick = component.navigateBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
          }
          FilledTonalIconButton(onClick = component.navigateHome) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
          }
          IconButton(onClick = {}, enabled = false) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
          }
        }
      }
    }
  }
}

@Composable
private fun TokenFeedPagerItem(
  token: TokenItem,
  onSeeMoreClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  ConstraintLayout(modifier = modifier) {
    val isCompactHeight = currentWindowAdaptiveInfo().isCompactHeight()
    val (logoWithSymbol, tokenParametersColumn) = createRefs()

    Column(
      modifier =
        Modifier.constrainAs(logoWithSymbol) {
          top.linkTo(parent.top, margin = if (isCompactHeight) 0.dp else 16.dp)
          start.linkTo(parent.start)
          if (!isCompactHeight) end.linkTo(parent.end)
          if (isCompactHeight) bottom.linkTo(parent.bottom)
        },
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      AsyncImage(
        modifier = Modifier.weight(1f, fill = false).heightIn(max = 128.dp).aspectRatio(1f),
        model = token.logoUrl,
        contentDescription = null,
        contentScale = ContentScale.Fit,
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = token.symbol,
        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
      )

      if (token.tagNames.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier =
            Modifier.fillMaxWidth(if (isCompactHeight) .5f else 1f)
              .horizontalScroll(rememberScrollState())
        ) {
          token.tagNames.forEachIndexed { index, name ->
            SuggestionChip(
              onClick = {},
              label = { Text(name) },
              interactionSource = NoRippleInteractionSource(),
            )

            if (index != token.tagNames.lastIndex) {
              Spacer(modifier = Modifier.width(4.dp))
            }
          }
        }
      }
    }

    BoxWithConstraints(
      modifier =
        Modifier.constrainAs(tokenParametersColumn) {
          top.linkTo(
            anchor = if (isCompactHeight) parent.top else logoWithSymbol.bottom,
            margin = if (isCompactHeight) 0.dp else 16.dp,
          )
          start.linkTo(
            anchor = if (isCompactHeight) logoWithSymbol.end else parent.start,
            margin = if (isCompactHeight) 32.dp else 0.dp,
          )
          end.linkTo(parent.end)
          bottom.linkTo(parent.bottom)

          width = Dimension.fillToConstraints
          height = Dimension.fillToConstraints
        }
    ) {
      val parameters = remember {
        buildList {
          add(
            TokenParameter(
              label = MR.strings.price.resolve(context),
              value = token.quote.price.fullDecimalFormat(),
            )
          )
          add(
            TokenParameter(
              label = MR.strings.market_cap.resolve(context),
              value = token.quote.marketCap.shortDecimalFormat(),
            )
          )
          add(
            TokenParameter(
              label = MR.strings.volume_24h.resolve(context),
              value = token.quote.volume24h.shortDecimalFormat(),
            )
          )
          add(
            TokenParameter(
              label = MR.strings.circulating_supply.resolve(context),
              value = token.circulatingSupply.shortDecimalFormat(),
            )
          )
          add(
            TokenParameter(
              label = MR.strings.total_supply.resolve(context),
              value = token.totalSupply.shortDecimalFormat(),
            )
          )
          add(
            TokenParameter(
              label = MR.strings.max_supply.resolve(context),
              value = token.maxSupply?.shortDecimalFormat() ?: "--",
            )
          )
        }
      }
      val seeMoreButtonHeight = 40.dp
      TokenParameterCardsColumn(
        parameters =
          parameters.take(
            ((maxHeight - seeMoreButtonHeight).value /
                calculateTokenParametersCardColumnHeight(parameters.size).value)
              .toInt()
          ),
        onSeeMoreClick = onSeeMoreClick,
        modifier =
          if (isCompactHeight) Modifier.fillMaxWidth().align(Alignment.Center)
          else Modifier.fillMaxSize(),
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

      if (index != parameters.lastIndex) {
        Spacer(modifier = Modifier.height(2.dp))
      }
    }

    TextButton(onClick = onSeeMoreClick, modifier = Modifier.fillMaxWidth()) {
      Text(MR.strings.see_more.resolve())
    }
  }
}

@Composable
private fun calculateTokenParametersCardColumnHeight(parametersCount: Int): Dp {
  val parameterLabelTextHeight =
    with(LocalDensity.current) { MaterialTheme.typography.labelSmall.fontSize.value.sp.toDp() }
  val parameterValueTextHeight =
    with(LocalDensity.current) { MaterialTheme.typography.headlineSmall.fontSize.value.sp.toDp() }
  val totalVerticalPadding = 16.dp
  val totalSpacersHeight = (parametersCount - 1) * 2.dp
  return parameterLabelTextHeight +
    parameterValueTextHeight +
    totalVerticalPadding +
    totalSpacersHeight
}

private data class TokenParameter(val label: String, val value: String)

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

      Text(
        text = parameter.value,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
      )
    }
  }
}
