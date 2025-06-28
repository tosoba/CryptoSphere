package com.trm.cryptosphere.ui.token

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TokenContent(component: TokenComponent, modifier: Modifier = Modifier) {
  Scaffold(modifier = modifier) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues))
  }
}
