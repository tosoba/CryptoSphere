package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopGradientOverlay(modifier: Modifier = Modifier) {
  Box(
    modifier =
      modifier
        .fillMaxWidth()
        .height(8.dp)
        .background(
          brush =
            Brush.Companion.verticalGradient(
              colors = listOf(MaterialTheme.colorScheme.background, Color.Companion.Transparent)
            )
        )
  )
}

@Composable
fun BottomGradientOverlay(modifier: Modifier = Modifier) {
  Box(
    modifier =
      modifier
        .fillMaxWidth()
        .height(8.dp)
        .background(
          brush =
            Brush.Companion.verticalGradient(
              colors = listOf(Color.Companion.Transparent, MaterialTheme.colorScheme.background)
            )
        )
  )
}
