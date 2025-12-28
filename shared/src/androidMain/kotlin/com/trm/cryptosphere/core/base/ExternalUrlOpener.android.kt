package com.trm.cryptosphere.core.base

import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.trm.cryptosphere.core.PlatformContext

actual fun PlatformContext.openUrl(url: String) {
  CustomTabsIntent.Builder().build().launchUrl(this, url.toUri())
}
