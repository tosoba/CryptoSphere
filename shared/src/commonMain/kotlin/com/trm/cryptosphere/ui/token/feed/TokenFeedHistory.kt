package com.trm.cryptosphere.ui.token.feed

import kotlinx.serialization.Serializable

@Serializable
data class TokenFeedHistory(val tokenId: Int, val backTokenIds: List<Int> = emptyList())
