package com.trm.cryptosphere.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.shared.MR

@Composable
fun ErrorOccurredCard(visible: Boolean, onRetryClick: () -> Unit, modifier: Modifier = Modifier) {
  AnimatedVisibility(visible = visible, modifier = modifier) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
      modifier =
        Modifier.fillMaxWidth(
          if (
            currentWindowAdaptiveInfo()
              .windowSizeClass
              .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
          ) {
            .5f
          } else {
            1f
          }
        ),
    ) {
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = MR.strings.error_occurred.resolve(),
          modifier = Modifier.weight(1f).padding(start = 16.dp),
          color = MaterialTheme.colorScheme.onErrorContainer,
        )

        TextButton(onClick = onRetryClick) {
          Text(
            text = MR.strings.retry.resolve(),
            color = MaterialTheme.colorScheme.onErrorContainer,
          )
        }
      }
    }
  }
}
