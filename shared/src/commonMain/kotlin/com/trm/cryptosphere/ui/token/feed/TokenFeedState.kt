package com.trm.cryptosphere.ui.token.feed

import kotlinx.serialization.Serializable

@Serializable data class TokenFeedState(val mainTokenSymbol: String, val feedItems: List<String>)
