package com.trm.cryptosphere.data.store

import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcCategoryItem
import com.trm.cryptosphere.data.db.dao.CategoryDao
import com.trm.cryptosphere.data.db.entity.CategoryEntity
import com.trm.cryptosphere.data.db.mapper.toCategoryItem
import com.trm.cryptosphere.data.db.mapper.toEntity
import com.trm.cryptosphere.data.store.util.storeBuilder
import com.trm.cryptosphere.data.store.util.usingDispatchers
import com.trm.cryptosphere.domain.model.CategoryItem
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.Validator

class CategoryStore(
  private val api: CoinMarketCapApi,
  private val dao: CategoryDao,
  private val dispatchers: AppCoroutineDispatchers,
) :
  Store<Int, List<CategoryItem>> by storeBuilder(
      fetcher =
        Fetcher.of { page -> api.getCategories(limit = MAX_LIMIT).getDataOrThrow().data },
      sourceOfTruth =
        SourceOfTruth.of<Int, List<CmcCategoryItem>, List<CategoryItem>>(
            reader = { page -> dao.selectAll().map { it.map(CategoryEntity::toCategoryItem) } },
            writer = { page, response -> dao.insert(response.map(CmcCategoryItem::toEntity)) },
            delete = { page -> dao.deleteAll() },
            deleteAll = { dao.deleteAll() },
          )
          .usingDispatchers(
            readDispatcher = dispatchers.databaseRead,
            writeDispatcher = dispatchers.databaseWrite,
          ),
    )
    .validator(Validator.by { result -> result.isNotEmpty() })
    .build() {
  companion object {
    private const val MAX_LIMIT = 5_000
  }
}
