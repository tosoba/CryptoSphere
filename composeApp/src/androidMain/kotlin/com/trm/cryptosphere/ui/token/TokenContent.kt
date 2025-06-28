package com.trm.cryptosphere.ui.token

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TokenContent(component: TokenComponent, modifier: Modifier = Modifier) {
  Scaffold(
    modifier = modifier,
    topBar = {
      @OptIn(ExperimentalMaterial3Api::class) CenterAlignedTopAppBar(title = { Text("Token") })
    },
  ) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues))
  }
}
