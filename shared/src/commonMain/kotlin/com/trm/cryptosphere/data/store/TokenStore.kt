package com.trm.cryptosphere.data.store

import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokenItem
import com.trm.cryptosphere.data.db.dao.TokenDao
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.mapper.toTokenItem
import com.trm.cryptosphere.data.store.util.storeBuilder
import com.trm.cryptosphere.data.store.util.usingDispatchers
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.Validator

class TokenStore(
  private val api: CoinMarketCapApi,
  private val dao: TokenDao,
  private val dispatchers: AppCoroutineDispatchers,
) :
  Store<Int, List<TokenItem>> by storeBuilder(
      fetcher = Fetcher.of { page -> api.getTokens(limit = 100).getDataOrThrow().data },
      sourceOfTruth =
        SourceOfTruth.of<Int, List<CmcTokenItem>, List<TokenItem>>(
            reader = { page -> dao.selectAllTokens().map { it.map(TokenEntity::toTokenItem) } },
            writer = { page, response -> dao.insertTokensWithTags(response) },
            delete = { page -> dao.deleteAllTokens() },
            deleteAll = { dao.deleteAllTokens() },
          )
          .usingDispatchers(
            readDispatcher = dispatchers.databaseRead,
            writeDispatcher = dispatchers.databaseWrite,
          ),
    )
    .validator(Validator.by { result -> result.isNotEmpty() })
    .build()
