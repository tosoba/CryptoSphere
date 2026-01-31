package com.trm.cryptosphere.core.ui.theme

import androidx.collection.lruCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import coil3.Image
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.size.Size
import coil3.size.SizeResolver
import com.materialkolor.ktx.themeColors
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration

class ColorExtractor(private val imageLoader: ImageLoader, private val context: PlatformContext) {
  private val cache = lruCache<Any, Color>(100)

  suspend fun calculatePrimaryColor(
    imageUrl: String,
    sizeResolver: SizeResolver = DEFAULT_REQUEST_SIZE,
  ): Result =
    cache[imageUrl]?.let { Result(color = it, cached = true) }
      ?: suspendCancellableCoroutine { cont ->
          imageLoader.enqueue(
            ImageRequest.Builder(context)
              .data(imageUrl)
              .size(sizeResolver)
              .prepareForColorExtractor()
              .target(
                onSuccess = { result -> cont.resume(result.toComposeImageBitmap()) },
                onError = { cont.resumeWithException(IllegalArgumentException()) },
              )
              .build()
          )
        }
        .themeColors()
        .first()
        .also { cache.put(imageUrl, it) }
        .let { Result(it, false) }

  data class Result(val color: Color, val cached: Boolean)

  companion object {
    private val DEFAULT_REQUEST_SIZE = SizeResolver(Size(96, 96))
  }
}

internal expect val ColorExtractor.Companion.calculationDebounceDuration: Duration

internal expect fun ImageRequest.Builder.prepareForColorExtractor(): ImageRequest.Builder

internal expect fun Image.toComposeImageBitmap(): ImageBitmap

interface ColorExtractorResultProvider {
  val colorExtractorResult: StateFlow<ColorExtractor.Result?>
}
