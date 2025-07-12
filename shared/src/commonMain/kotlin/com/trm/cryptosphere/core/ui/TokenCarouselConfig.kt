package com.trm.cryptosphere.core.ui

import com.trm.cryptosphere.domain.model.TokenCarouselItem
import kotlinx.serialization.Serializable

@Serializable data class TokenCarouselConfig(val items: List<TokenCarouselItem>)
