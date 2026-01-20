package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ListItemImage(url: String?) {
  AsyncImage(
    model = rememberCrossfadeImageRequest(url),
    contentDescription = null,
    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
    contentScale = ContentScale.Crop,
  )
}
