package com.trm.cryptosphere.core.ui

import com.trm.cryptosphere.domain.model.TokenCarouselItem
import kotlinx.serialization.Serializable

@Serializable
actual class TokenCarouselConfig(
  val parentSharedElementId: String?,
  val items: List<TokenCarouselItem>,
)
