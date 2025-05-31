package com.trm.cryptosphere.ui.home.page.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FeedContent(component: FeedComponent, modifier: Modifier = Modifier) {
  Box(modifier = modifier, contentAlignment = Alignment.Center) { Text("Feed") }
}
