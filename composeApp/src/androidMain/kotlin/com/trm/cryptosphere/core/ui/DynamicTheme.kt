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
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.size.Size
import coil.size.SizeResolver
import com.materialkolor.DynamicMaterialExpressiveTheme
import com.materialkolor.ktx.themeColors
import com.trm.cryptosphere.core.util.cancellableRunCatching
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DynamicTheme(
  model: Any?,
  colorExtractor: ColorExtractor,
  fallback: Color = MaterialTheme.colorScheme.primary,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val result by
    produceState<ColorExtractor.Result?>(initialValue = null, model, colorExtractor) {
      value =
        cancellableRunCatching { model?.let { colorExtractor.calculatePrimaryColor(it) } }
          .getOrNull()
    }
  DynamicMaterialExpressiveTheme(
    seedColor = result?.color ?: fallback,
    isDark = useDarkTheme,
    animate = result.let { it != null && !it.cached },
    content = content,
  )
}

class ColorExtractor(private val imageLoader: ImageLoader, private val context: Context) {
  private val cache = lruCache<Any, Color>(100)

  suspend fun calculatePrimaryColor(
    model: Any,
    sizeResolver: SizeResolver = DEFAULT_REQUEST_SIZE,
  ): Result =
    cache[model]?.let { Result(color = it, cached = true) }
      ?: suspendCancellableCoroutine { cont ->
          imageLoader.enqueue(
            ImageRequest.Builder(context)
              .data(model)
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
        .also { cache.put(model, it) }
        .let { Result(color = it, cached = false) }

  data class Result(val color: Color, val cached: Boolean)

  private companion object {
    val DEFAULT_REQUEST_SIZE = SizeResolver(Size(96, 96))
  }
}
