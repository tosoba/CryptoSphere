package com.trm.cryptosphere.core.base

import android.content.Intent
import com.trm.cryptosphere.core.PlatformContext

actual fun PlatformContext.shareUrl(url: String) {
  startActivity(
    Intent.createChooser(
      Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, url).setType("text/plain"),
      null,
    )
  )
}
