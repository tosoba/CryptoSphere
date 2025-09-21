package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.db.dao.TokenDao
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.mapper.toTokenItem
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository

class TokenNetworkRepository(
  private val dao: TokenDao,
  private val coinMarketCapApi: CoinMarketCapApi,
) : TokenRepository {
  override suspend fun getTokensCount(): Int = dao.selectTokensCount()

  override suspend fun performFullTokensSync() {
    dao.insertTokensWithTags(
      coinMarketCapApi.getTokens(limit = CoinMarketCapApi.MAX_LIMIT).getDataOrThrow().data
    )
  }

  override suspend fun getTokensMatchingSearchTerms(searchTerms: List<String>): List<TokenItem> =
    searchTerms
      .map { it.lowercase().trim() }
      .filter(String::isNotEmpty)
      .distinct()
      .takeUnless(List<String>::isEmpty)
      ?.let { dao.selectTokensByNameOrSymbol(it) }
      ?.map(TokenEntity::toTokenItem)
      ?.distinctBy(TokenItem::symbol) ?: emptyList()

  override suspend fun getTokensBySharedTags(symbol: String): List<TokenItem> =
    dao.selectTokensBySharedTags(symbol).map(TokenEntity::toTokenItem)
}
