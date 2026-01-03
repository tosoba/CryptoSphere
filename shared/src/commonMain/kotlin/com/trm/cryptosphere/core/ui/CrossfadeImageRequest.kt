package com.trm.cryptosphere.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun rememberCrossfadeImageRequest(data: Any?): ImageRequest {
  val context = LocalPlatformContext.current
  return remember(context, data) { ImageRequest.Builder(context).data(data).crossfade(250).build() }
}
