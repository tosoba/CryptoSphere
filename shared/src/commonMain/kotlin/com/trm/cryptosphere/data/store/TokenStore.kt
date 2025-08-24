package com.trm.cryptosphere.data.store

import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokenItem
import com.trm.cryptosphere.data.db.dao.TokenDao
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.mapper.toEntity
import com.trm.cryptosphere.data.db.mapper.toTokenItem
import com.trm.cryptosphere.data.store.util.storeBuilder
import com.trm.cryptosphere.data.store.util.usingDispatchers
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store

class TokenStore(
  private val api: CoinMarketCapApi,
  private val dao: TokenDao,
  private val dispatchers: AppCoroutineDispatchers,
) :
  Store<Int, List<TokenItem>> by storeBuilder(
      fetcher = Fetcher.of { page: Int -> api.getTokens(limit = 100).getDataOrThrow().data },
      sourceOfTruth =
        SourceOfTruth.of<Int, List<CmcTokenItem>, List<TokenItem>>(
            reader = { page -> dao.selectAll().map { it.map(TokenEntity::toTokenItem) } },
            writer = { page, response -> dao.insert(response.map(CmcTokenItem::toEntity)) },
            delete = { page -> dao.deleteAll() },
            deleteAll = { dao.deleteAll() },
          )
          .usingDispatchers(
            readDispatcher = dispatchers.databaseRead,
            writeDispatcher = dispatchers.databaseWrite,
          ),
    )
    .build()
