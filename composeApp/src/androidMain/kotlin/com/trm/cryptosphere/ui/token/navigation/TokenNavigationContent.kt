package com.trm.cryptosphere.ui.token.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.trm.cryptosphere.core.base.openUrl
import com.trm.cryptosphere.core.ui.TokenCarousel
import com.trm.cryptosphere.core.ui.localSharedElement
import com.trm.cryptosphere.core.ui.tokenCarouselSharedTransitionKey
import com.trm.cryptosphere.core.util.isCompactHeight
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.core.util.toNavigationSuiteType
import com.trm.cryptosphere.domain.model.shareUrl
import com.trm.cryptosphere.shared.MR
import com.trm.cryptosphere.ui.token.feed.TokenFeedContent

@OptIn(
  ExperimentalDecomposeApi::class,
  ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalMaterial3Api::class,
)
@Composable
fun TokenNavigationContent(
  component: TokenNavigationComponent,
  onImageUrlChange: (String?) -> Unit,
) {
  val context = LocalContext.current
  val adaptiveInfo = currentWindowAdaptiveInfo()
  val navigationSuiteType = adaptiveInfo.toNavigationSuiteType()

  val tokens by component.viewModel.navigationTokens.collectAsStateWithLifecycle(emptyList())
  val currentNavigationTokenId by
    component.viewModel.currentNavigationTokenId.collectAsStateWithLifecycle(null)
  val currentPresentedFeedToken by component.currentPresentedFeedToken.collectAsStateWithLifecycle()
  val forwardNavigationButtonEnabled = tokens.lastOrNull()?.id != currentPresentedFeedToken?.id

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.surfaceContainer,
    bottomBar = {
      if (navigationSuiteType == NavigationSuiteType.NavigationBar) {
        FlexibleBottomAppBar(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            BottomAppBarButtons(
              backText =
                tokens.getOrNull(tokens.lastIndex - 1)?.symbol ?: MR.strings.home.resolve(),
              forwardText = currentPresentedFeedToken?.symbol.orEmpty(),
              forwardEnabled = forwardNavigationButtonEnabled,
              onBackClick = component::onBackClicked,
              onHomeClick = component.navigateHome,
              onForwardClick = component::navigateToTokenFeed,
            )
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

      val carouselState = rememberCarouselState(itemCount = tokens::size)
      LaunchedEffect(tokens.size) { carouselState.scrollToItem(tokens.lastIndex) }

      TokenCarousel(
        tokens = tokens,
        state = carouselState,
        highlightedTokenId = currentNavigationTokenId,
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

      OpenInBrowserFab(
        modifier =
          Modifier.constrainAs(switchTokenButton) {
            bottom.linkTo(parent.bottom, margin = 16.dp)
            end.linkTo(parent.end, margin = 16.dp)
          },
        onClick = { currentPresentedFeedToken?.shareUrl?.let(context::openUrl) },
      )

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
          VerticalToolbarButtons(
            forwardEnabled = forwardNavigationButtonEnabled,
            onBackClick = component::onBackClicked,
            onHomeClick = component.navigateHome,
            onForwardClick = component::navigateToTokenFeed,
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OpenInBrowserFab(modifier: Modifier = Modifier, onClick: () -> Unit) {
  if (currentWindowAdaptiveInfo().isCompactHeight()) {
    FloatingActionButton(modifier = modifier, onClick = onClick) {
      Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null)
    }
  } else {
    MediumFloatingActionButton(modifier = modifier, onClick = onClick) {
      Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null)
    }
  }
}

@Composable
private fun BottomAppBarButtons(
  backText: String,
  forwardText: String,
  forwardEnabled: Boolean,
  onBackClick: () -> Unit,
  onHomeClick: () -> Unit,
  onForwardClick: () -> Unit,
) {
  Button(onClick = onBackClick, colors = ButtonDefaults.filledTonalButtonColors()) {
    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
    Spacer(modifier = Modifier.width(2.dp))
    Text(backText)
  }

  FilledIconButton(onClick = onHomeClick) {
    Icon(imageVector = Icons.Filled.Home, contentDescription = null)
  }

  Button(
    onClick = onForwardClick,
    enabled = forwardEnabled,
    colors = ButtonDefaults.filledTonalButtonColors(),
  ) {
    Text(forwardText)
    Spacer(modifier = Modifier.width(2.dp))
    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
  }
}

@Composable
private fun VerticalToolbarButtons(
  forwardEnabled: Boolean,
  onBackClick: () -> Unit,
  onHomeClick: () -> Unit,
  onForwardClick: () -> Unit,
) {
  FilledTonalIconButton(onClick = onBackClick) {
    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
  }

  FilledIconButton(onClick = onHomeClick) {
    Icon(imageVector = Icons.Filled.Home, contentDescription = null)
  }

  FilledTonalIconButton(onClick = onForwardClick, enabled = forwardEnabled) {
    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
  }
}
