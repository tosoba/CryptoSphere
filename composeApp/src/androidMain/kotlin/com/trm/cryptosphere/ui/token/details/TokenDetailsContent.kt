package com.trm.cryptosphere.ui.token.details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TokenDetailsContent(
  component: TokenDetailsComponent,
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  modifier: Modifier = Modifier,
) {
  Scaffold(modifier = modifier) { paddingValues ->
    Box(
      modifier = Modifier.fillMaxSize().padding(paddingValues),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = component.symbol,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(16.dp),
      )
    }
  }
}
