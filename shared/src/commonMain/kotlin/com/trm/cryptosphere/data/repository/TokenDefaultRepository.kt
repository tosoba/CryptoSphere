package com.trm.cryptosphere.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.db.dao.TokenDao
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.entity.junction.TokenWithTagNamesJunction
import com.trm.cryptosphere.data.db.mapper.toTokenItem
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenDefaultRepository(
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
      ?.map(TokenEntity::toTokenItem) ?: emptyList()

  override fun getTokensBySharedTags(id: Int): Flow<PagingData<TokenItem>> =
    Pager(
        config =
          PagingConfig(
            pageSize = TOKEN_DB_PAGE_SIZE,
            prefetchDistance = TOKEN_DB_PREFETCH_DISTANCE,
            initialLoadSize = TOKEN_DB_PAGE_SIZE,
          )
      ) {
        dao.selectTokensBySharedTags(id)
      }
      .flow
      .map { it.map(TokenWithTagNamesJunction::toTokenItem) }

  override fun getTokens(query: String): Flow<PagingData<TokenItem>> =
    Pager(config = PagingConfig(pageSize = TOKEN_DB_PAGE_SIZE)) {
        dao.selectPagedTokens(query.lowercase().trim().takeUnless(String::isEmpty))
      }
      .flow
      .map { it.map(TokenEntity::toTokenItem) }

  override suspend fun getTokensByIds(ids: List<Int>): List<TokenItem> =
    dao.selectByIdsIn(ids).map(TokenEntity::toTokenItem)

  companion object {
    private const val TOKEN_DB_PAGE_SIZE = 100

    // significantly smaller than default value for ViewPager based lists
    private const val TOKEN_DB_PREFETCH_DISTANCE = 5
  }
}
