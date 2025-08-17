package com.trm.cryptosphere.ui.token.feed

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.trm.cryptosphere.core.ui.PagerIndicatorOrientation
import com.trm.cryptosphere.core.ui.PagerWormIndicator
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.VerticalFeedPager
import com.trm.cryptosphere.core.ui.localSharedElement
import com.trm.cryptosphere.core.ui.tokenCarouselSharedTransitionKey
import com.trm.cryptosphere.core.util.toNavigationSuiteType
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.logoUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TokenFeedContent(
  component: TokenFeedComponent,
  modifier: Modifier = Modifier,
  onImageUrlChange: (String?) -> Unit,
) {
  val navigationSuiteType = currentWindowAdaptiveInfo().toNavigationSuiteType()

  Scaffold(
    modifier = modifier,
    bottomBar = {
      if (navigationSuiteType == NavigationSuiteType.NavigationBar) {
        FlexibleBottomAppBar(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = BottomAppBarDefaults.FlexibleFixedHorizontalArrangement,
        ) {
          AppBarRow(
            overflowIndicator = { menuState ->
              IconButton(
                onClick = { if (menuState.isExpanded) menuState.dismiss() else menuState.show() }
              ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
              }
            }
          ) {
            clickableItem(
              onClick = {},
              icon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) },
              label = "Go back",
            )
            clickableItem(
              onClick = {},
              icon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) },
              label = "Go forward",
              enabled = false,
            )
          }
        }
      }
    },
  ) { paddingValues ->
    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      val (pager, pagerIndicator, tokenCarousel, detailsButton, floatingToolbar) = createRefs()

      val scope = rememberCoroutineScope()
      val state by component.state.collectAsStateWithLifecycle()
      val pagerState = rememberPagerState(pageCount = state.feedItems::size)

      LaunchedEffect(pagerState.currentPage, state.feedItems) {
        onImageUrlChange(state.feedItems[pagerState.currentPage].logoUrl)
      }

      VerticalFeedPager(
        pagerState = pagerState,
        modifier =
          Modifier.constrainAs(pager) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start, margin = 32.dp)
            end.linkTo(
              if (navigationSuiteType == NavigationSuiteType.NavigationBar) pagerIndicator.start
              else floatingToolbar.start,
              margin =
                if (navigationSuiteType == NavigationSuiteType.NavigationBar) 16.dp else 32.dp,
            )

            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
          },
      ) { page ->
        Column(modifier = Modifier.fillMaxSize()) {
          TokenFeedPagerItem(
            token = state.feedItems[page],
            modifier = Modifier.fillMaxWidth().weight(1f),
          )

          Spacer(modifier = Modifier.height(128.dp))
        }
      }

      PagerWormIndicator(
        pagerState = pagerState,
        activeDotColor = Color.LightGray,
        dotColor = Color.DarkGray,
        dotCount = 5,
        orientation = PagerIndicatorOrientation.Vertical,
        modifier =
          Modifier.constrainAs(pagerIndicator) {
            top.linkTo(parent.top)
            bottom.linkTo(detailsButton.top)
            end.linkTo(parent.end, margin = 12.dp)
          },
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
          Modifier.constrainAs(tokenCarousel) {
              start.linkTo(parent.start)
              end.linkTo(detailsButton.start, margin = 16.dp)
              top.linkTo(detailsButton.top)
              bottom.linkTo(detailsButton.bottom)

              width = Dimension.fillToConstraints
              height = Dimension.wrapContent
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
          Modifier.constrainAs(detailsButton) {
            bottom.linkTo(parent.bottom, margin = 16.dp)
            end.linkTo(parent.end, margin = 16.dp)
          },
        onClick = {
          component.navigateToTokenDetails(state.feedItems[pagerState.currentPage].symbol)
        },
      ) {
        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
      }

      if (navigationSuiteType == NavigationSuiteType.NavigationRail) {
        VerticalFloatingToolbar(
          expanded = true,
          modifier =
            Modifier.constrainAs(floatingToolbar) {
              top.linkTo(parent.top, margin = 16.dp)
              bottom.linkTo(detailsButton.top, margin = 16.dp)
              end.linkTo(pagerIndicator.start, margin = 12.dp)
            },
        ) {
          IconButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
          }
          IconButton(onClick = {}, enabled = false) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
          }
        }
      }
    }
  }
}

@Composable
private fun TokenFeedPagerItem(token: TokenItem, modifier: Modifier = Modifier) {
  ConstraintLayout(modifier = modifier) {
    val (logo, symbol, price, marketCap) = createRefs()
    AsyncImage(
      model = token.logoUrl,
      contentDescription = null,
      contentScale = ContentScale.Fit,
      modifier =
        Modifier.constrainAs(logo) {
          top.linkTo(parent.top, margin = 16.dp)
          start.linkTo(parent.start)
          end.linkTo(parent.end)

          width = Dimension.value(128.dp)
          height = Dimension.value(128.dp)
        },
    )

    Text(
      text = token.symbol,
      style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
      modifier =
        Modifier.constrainAs(symbol) {
            top.linkTo(logo.bottom, margin = 8.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          }
          .localSharedElement(key = "token-symbol-${token.symbol}"),
    )

    val tokenParameters = remember {
      listOf(
        TokenParameter(label = "Price", value = token.quote.price.toString()),
        TokenParameter(label = "Market Cap", value = token.quote.marketCap.toString()),
      )
    }
    TokenParameterCard(
      parameter = tokenParameters[0],
      shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
      modifier =
        Modifier.constrainAs(price) {
          top.linkTo(symbol.bottom, margin = 16.dp)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          width = Dimension.fillToConstraints
        },
    )
    TokenParameterCard(
      parameter = tokenParameters[1],
      shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
      modifier =
        Modifier.constrainAs(marketCap) {
          top.linkTo(price.bottom, margin = 2.dp)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          width = Dimension.fillToConstraints
        },
    )
  }
}

private data class TokenParameter(val label: String, val value: String)

@Composable
private fun TokenParameterCard(
  parameter: TokenParameter,
  shape: Shape,
  modifier: Modifier = Modifier,
) {
  Card(modifier = modifier, shape = shape) {
    Column(modifier = Modifier.padding(16.dp)) {
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
