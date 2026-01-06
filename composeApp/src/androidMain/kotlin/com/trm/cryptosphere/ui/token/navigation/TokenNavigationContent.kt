package com.trm.cryptosphere.ui.token.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.materialPredictiveBackAnimatable
import com.trm.cryptosphere.core.base.shareUrl
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.localSharedElement
import com.trm.cryptosphere.core.ui.tokenCarouselSharedTransitionKey
import com.trm.cryptosphere.core.util.isCompactHeight
import com.trm.cryptosphere.core.util.toNavigationSuiteType
import com.trm.cryptosphere.domain.model.shareUrl
import com.trm.cryptosphere.ui.token.feed.TokenFeedContent

@OptIn(
  ExperimentalDecomposeApi::class,
  ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalMaterial3Api::class,
)
@Composable
fun TokenNavigationContent(
  component: TokenNavigationComponent,
  modifier: Modifier = Modifier,
  onImageUrlChange: (String?) -> Unit,
) {
  val adaptiveInfo = currentWindowAdaptiveInfo()
  val navigationSuiteType = adaptiveInfo.toNavigationSuiteType()

  Scaffold(
    modifier = modifier,
    bottomBar = {
      if (navigationSuiteType == NavigationSuiteType.NavigationBar) {
        FlexibleBottomAppBar(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            NavigationBarButtons(component)
          }
        }
      }
    },
  ) { paddingValues ->
    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      val (stack, tokenCarousel, switchTokenButton, floatingToolbar) = createRefs()

      ChildStack(
        stack = component.stack,
        modifier =
          Modifier.constrainAs(stack) {
            top.linkTo(parent.top, margin = 16.dp)
            bottom.linkTo(tokenCarousel.top, margin = 16.dp)
            start.linkTo(parent.start, margin = 32.dp)
            end.linkTo(
              if (navigationSuiteType == NavigationSuiteType.NavigationBar) parent.end
              else floatingToolbar.start,
              margin = 32.dp,
            )

            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
          },
        animation =
          stackAnimation(
            animator = fade() + scale(),
            predictiveBackParams = {
              PredictiveBackParams(
                backHandler = component.backHandler,
                onBack = component::onBackClicked,
                animatable = ::materialPredictiveBackAnimatable,
              )
            },
          ),
      ) { child ->
        TokenFeedContent(
          component = child.instance.component,
          modifier = Modifier.fillMaxSize(),
          onImageUrlChange = onImageUrlChange,
        )
      }

      val tokens by component.viewModel.tokens.collectAsStateWithLifecycle(emptyList())
      val currentTokenId by component.viewModel.currentTokenId.collectAsStateWithLifecycle(null)

      val carouselState = rememberCarouselState(itemCount = tokens::size)
      LaunchedEffect(tokens.size) { carouselState.scrollToItem(tokens.lastIndex) }

      TokenCarousel(
        tokens = tokens,
        state = carouselState,
        highlightedTokenId = currentTokenId,
        onItemClick = component::popToToken,
        itemHeight = if (adaptiveInfo.isCompactHeight()) 56.dp else 80.dp,
        modifier =
          Modifier.constrainAs(tokenCarousel) {
              start.linkTo(parent.start)
              end.linkTo(switchTokenButton.start, margin = 16.dp)
              top.linkTo(switchTokenButton.top)
              bottom.linkTo(switchTokenButton.bottom)

              width = Dimension.fillToConstraints
              height = Dimension.fillToConstraints
            }
            .run {
              component.tokenCarouselConfig.parentSharedElementId?.let {
                localSharedElement(key = tokenCarouselSharedTransitionKey(it))
              } ?: this
            },
        contentPadding = PaddingValues(start = 16.dp),
      )

      if (adaptiveInfo.isCompactHeight()) {
        FloatingActionButton(
          modifier =
            Modifier.constrainAs(switchTokenButton) {
              bottom.linkTo(parent.bottom, margin = 16.dp)
              end.linkTo(parent.end, margin = 16.dp)
            },
          onClick = component::navigateToTokenFeed,
        ) {
          Icon(imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
        }
      } else {
        MediumFloatingActionButton(
          modifier =
            Modifier.constrainAs(switchTokenButton) {
              bottom.linkTo(parent.bottom, margin = 16.dp)
              end.linkTo(parent.end, margin = 16.dp)
            },
          onClick = component::navigateToTokenFeed,
        ) {
          Icon(imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
        }
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
          NavigationBarButtons(component)
        }
      }
    }
  }
}

@Composable
private fun NavigationBarButtons(component: TokenNavigationComponent) {
  val context = LocalContext.current
  IconButton(onClick = component::onBackClicked) {
    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
  }
  FilledTonalIconButton(onClick = component.navigateHome) {
    Icon(imageVector = Icons.Filled.Home, contentDescription = null)
  }
  IconButton(onClick = { component.currentFeedToken?.shareUrl?.let(context::shareUrl) }) {
    Icon(imageVector = Icons.Filled.Share, contentDescription = null)
  }
}
