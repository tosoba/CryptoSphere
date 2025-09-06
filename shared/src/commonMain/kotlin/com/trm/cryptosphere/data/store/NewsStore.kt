package com.trm.cryptosphere.data.store

import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsItem
import com.trm.cryptosphere.data.db.dao.NewsDao
import com.trm.cryptosphere.data.db.entity.NewsEntity
import com.trm.cryptosphere.data.db.mapper.toEntity
import com.trm.cryptosphere.data.db.mapper.toNewsItem
import com.trm.cryptosphere.data.store.util.storeBuilder
import com.trm.cryptosphere.data.store.util.usingDispatchers
import com.trm.cryptosphere.domain.model.NewsItem
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.Validator

@OptIn(ExperimentalTime::class)
class NewsStore(
  private val api: CoinStatsApi,
  private val dao: NewsDao,
  private val dispatchers: AppCoroutineDispatchers,
) :
  Store<Int, List<NewsItem>> by storeBuilder(
      fetcher =
        Fetcher.of { page ->
          api
            .getNews(page = page + CoinStatsApi.PAGE_OFFSET, limit = CoinStatsApi.MAX_LIMIT)
            .getDataOrThrow()
            .result
        },
      sourceOfTruth =
        SourceOfTruth.of<Int, List<CoinStatsNewsItem>, List<NewsItem>>(
            reader = { page ->
              dao.selectPage(page = page, pageSize = CoinStatsApi.MAX_LIMIT).map {
                it.map(NewsEntity::toNewsItem)
              }
            },
            writer = { page, response ->
              val fetchedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
              dao.insert(response.map { item -> item.toEntity(fetchedDate) })
            },
            delete = { page -> dao.deleteAll() },
            deleteAll = { dao.deleteAll() },
          )
          .usingDispatchers(
            readDispatcher = dispatchers.databaseRead,
            writeDispatcher = dispatchers.databaseWrite,
          ),
    )
    .validator(
      Validator.by { result ->
        val yesterday = Clock.System.now().minus(1, DateTimeUnit.DAY, TimeZone.UTC)
        result.any { item -> item.fetchedDate.toInstant(TimeZone.UTC) > yesterday }
      }
    )
    .build()
