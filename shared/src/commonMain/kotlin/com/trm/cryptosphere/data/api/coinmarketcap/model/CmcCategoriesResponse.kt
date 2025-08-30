package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.Serializable

@Serializable
data class CmcCategoriesResponse(val status: CmcResponseStatus, val data: List<CmcCategoryItem>)
