package com.trm.cryptosphere.core.ui

import com.trm.cryptosphere.domain.model.TokenCarouselItem
import kotlinx.serialization.Serializable

@Serializable
data class TokenCarouselConfig(
  val parentSharedElementId: String?,
  val items: List<TokenCarouselItem>,
)
