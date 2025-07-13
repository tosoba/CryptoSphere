package com.trm.cryptosphere.data.api.coinstats.model

import kotlinx.serialization.Serializable

@Serializable data class CoinStatsNewsResponse(val result: List<CoinStatsNewsItem>)
