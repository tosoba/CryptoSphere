package com.trm.cryptosphere.core.ui.theme

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import coil3.Image
import coil3.request.ImageRequest
import coil3.toBitmap

internal actual fun ImageRequest.Builder.prepareForColorExtractor(): ImageRequest.Builder = this

internal actual fun Image.toComposeImageBitmap(): ImageBitmap = toBitmap().asComposeImageBitmap()
