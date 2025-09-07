package com.trm.cryptosphere.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.trm.cryptosphere.core.base.cancellableRunCatching
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsItem
import com.trm.cryptosphere.data.api.coinstats.toNewsItem
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NewsNetworkRepository(
  private val coinStatsApi: CoinStatsApi,
  private val tokenRepository: TokenRepository,
) : NewsRepository {
  override fun getNewsFlow(): Flow<PagingData<NewsItem>> =
    Pager(
        config = PagingConfig(pageSize = CoinStatsApi.MAX_LIMIT, prefetchDistance = 10),
        pagingSourceFactory = ::NewsPagingDataSource,
      )
      .flow

  private inner class NewsPagingDataSource : PagingSource<Int, NewsItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> = coroutineScope {
      cancellableRunCatching {
          val tokensSync = launch {
            if (tokenRepository.getTokensCount() == 0) tokenRepository.performFullTokensSync()
          }

          val page = params.key ?: 0
          val news = async {
            coinStatsApi
              .getNews(page = page + CoinStatsApi.PAGE_OFFSET)
              .getDataOrThrow()
              .result
              .filter { item -> !item.relatedCoins.isNullOrEmpty() }
              .map(CoinStatsNewsItem::toNewsItem)
          }

          tokensSync.join()
          val data = news.await()

          LoadResult.Page(
            data = data,
            prevKey = (page - 1).takeIf { it >= 0 },
            nextKey = (page + 1).takeIf { data.isNotEmpty() && it < 100 },
          )
        }
        .fold(onSuccess = { it }, onFailure = { LoadResult.Error(it) })
    }

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? = null
  }
}
