package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokenItem
import com.trm.cryptosphere.data.db.dao.TokenDao
import com.trm.cryptosphere.data.db.mapper.toEntity
import com.trm.cryptosphere.domain.repository.TokenRepository

class TokenNetworkRepository(
  private val dao: TokenDao,
  private val coinMarketCapApi: CoinMarketCapApi,
) : TokenRepository {
  override suspend fun getTokensCount(): Int = dao.selectTokensCount()

  override suspend fun performFullTokensSync() {
    dao.insertTokens(
      coinMarketCapApi
        .getTokens(limit = CoinMarketCapApi.MAX_LIMIT)
        .getDataOrThrow()
        .data
        .map(CmcTokenItem::toEntity)
    )
  }
}
