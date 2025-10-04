package com.trm.cryptosphere.core.ui

import android.content.Context
import androidx.collection.lruCache
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.allowRgb565
import coil3.size.Size
import coil3.size.SizeResolver
import coil3.toBitmap
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.ktx.themeColors
import com.trm.cryptosphere.core.base.cancellableRunCatching
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DynamicTheme(
  imageUrl: String?,
  colorExtractor: ColorExtractor,
  fallback: Color = MaterialTheme.colorScheme.primary,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  StatusBarContentAppearanceEffect(
    if (useDarkTheme) StatusBarContentAppearance.LIGHT else StatusBarContentAppearance.DARK
  )

  val result by
    produceState<ColorExtractor.Result?>(initialValue = null, imageUrl, colorExtractor) {
      value =
        cancellableRunCatching { imageUrl?.let { colorExtractor.calculatePrimaryColor(it) } }
          .getOrNull()
    }
  DynamicMaterialTheme(
    seedColor = result?.color ?: fallback,
    primary = if (result == null) MaterialTheme.colorScheme.primary else null,
    secondary = if (result == null) MaterialTheme.colorScheme.secondary else null,
    tertiary = if (result == null) MaterialTheme.colorScheme.tertiary else null,
    error = if (result == null) MaterialTheme.colorScheme.error else null,
    isDark = useDarkTheme,
    animate = result.let { it != null && !it.cached },
    content = content,
  )
}

class ColorExtractor(private val imageLoader: ImageLoader, private val context: Context) {
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
              .allowHardware(false)
              .allowRgb565(true)
              .target(
                onSuccess = { result -> cont.resume(result.toBitmap().asImageBitmap()) },
                onError = { cont.resumeWithException(IllegalArgumentException()) },
              )
              .build()
          )
        }
        .themeColors()
        .first()
        .also { cache.put(imageUrl, it) }
        .let { Result(color = it, cached = false) }

  data class Result(val color: Color, val cached: Boolean)

  private companion object {
    val DEFAULT_REQUEST_SIZE = SizeResolver(Size(96, 96))
  }
}
