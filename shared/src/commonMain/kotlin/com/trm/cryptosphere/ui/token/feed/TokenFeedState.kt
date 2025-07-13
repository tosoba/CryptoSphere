package com.trm.cryptosphere.ui.token.feed

import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.serialization.Serializable

@Serializable
data class TokenFeedState(val mainTokenSymbol: String, val feedItems: List<TokenItem>)
