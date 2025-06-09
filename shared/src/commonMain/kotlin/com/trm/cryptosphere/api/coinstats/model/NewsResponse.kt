package com.trm.cryptosphere.api.coinstats.model

import kotlinx.serialization.Serializable

@Serializable data class NewsResponse(val result: List<NewsResponseItem>)
