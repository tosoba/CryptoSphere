package com.trm.cryptosphere.core.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.StringResource

@Composable fun StringResource.resolve(): String = getString(LocalContext.current)

fun StringResource.resolve(context: Context): String = getString(context)
