package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.ListItemInfoColumn(topText: String, bottomText: String) {
  Column(modifier = Modifier.weight(1f)) {
    Text(
      text = topText,
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurface,
      maxLines = 1,
      modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
    )

    Text(
      text = bottomText,
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      maxLines = 1,
      modifier = Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE),
    )
  }
}
