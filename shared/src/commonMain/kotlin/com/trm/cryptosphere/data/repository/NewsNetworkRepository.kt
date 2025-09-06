package com.trm.cryptosphere.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.trm.cryptosphere.core.base.cancellableRunCatching
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.store.NewsStore
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.mobilenativefoundation.store.store5.impl.extensions.get

class NewsNetworkRepository(
  private val store: NewsStore,
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
          val news = async { store.get(page) }

          tokensSync.join()
          LoadResult.Page(
            data = news.await(),
            prevKey = (page - 1).takeIf { it >= 0 },
            nextKey = page + 1,
          )
        }
        .fold(onSuccess = { it }, onFailure = { LoadResult.Error(it) })
    }

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? = null
  }
}
