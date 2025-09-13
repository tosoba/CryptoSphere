package com.trm.cryptosphere.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.trm.cryptosphere.core.base.cancellableRunCatching
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GetNewsUseCase(
  private val newsRepository: NewsRepository,
  private val tokenRepository: TokenRepository,
) {
  operator fun invoke(
    config: PagingConfig =
      PagingConfig(
        pageSize = CoinStatsApi.MAX_LIMIT,
        prefetchDistance = 10,
        initialLoadSize = CoinStatsApi.MAX_LIMIT,
      )
  ): Flow<PagingData<NewsItem>> =
    Pager(config = config, pagingSourceFactory = ::NewsPagingSource).flow

  private inner class NewsPagingSource : PagingSource<Int, NewsItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> =
      cancellableRunCatching {
          coroutineScope {
            val tokensSync = launch {
              if (tokenRepository.getTokensCount() == 0) tokenRepository.performFullTokensSync()
            }

            val page = params.key ?: 0
            val news = async { newsRepository.getNewsPage(page = page, limit = params.loadSize) }

            tokensSync.join()
            val data = news.await()

            LoadResult.Page(
              data = data,
              prevKey = (page - 1).takeIf { it >= 0 },
              nextKey = (page + 1).takeIf { data.isNotEmpty() && it < CoinStatsApi.MAX_PAGE },
            )
          }
        }
        .fold(onSuccess = { it }, onFailure = { LoadResult.Error(it) })

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? = null
  }
}
