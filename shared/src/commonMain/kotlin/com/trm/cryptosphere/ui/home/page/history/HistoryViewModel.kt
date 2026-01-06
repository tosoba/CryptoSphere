package com.trm.cryptosphere.ui.home.page.history

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HistoryViewModel(
  private val newsHistoryRepository: NewsHistoryRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val query = MutableStateFlow("")

  fun onQueryChange(newQuery: String) {
    query.value = newQuery.trim()
  }

  val newsHistory: Flow<PagingData<NewsHistoryListItem>> =
    query
      .debounce(250)
      .flatMapLatest(newsHistoryRepository::getHistory)
      .map { pagingData ->
        pagingData
          .map { NewsHistoryListItem.Item(it) }
          .insertSeparators { before: NewsHistoryListItem.Item?, after: NewsHistoryListItem.Item? ->
            if (after == null) return@insertSeparators null

            val afterDate = after.news.visitedAt.date
            if (before == null) {
              return@insertSeparators NewsHistoryListItem.DateHeader(afterDate)
            }

            val beforeDate = before.news.visitedAt.date
            if (beforeDate != afterDate) {
              NewsHistoryListItem.DateHeader(afterDate)
            } else {
              null
            }
          }
      }
      .cachedIn(scope)

  val tokenHistory: Flow<PagingData<TokenHistoryListItem>> =
    query
      .debounce(250)
      .flatMapLatest(tokenHistoryRepository::getHistory)
      .map { pagingData ->
        pagingData
          .map { TokenHistoryListItem.Item(it) }
          .insertSeparators { before: TokenHistoryListItem.Item?, after: TokenHistoryListItem.Item?
            ->
            if (after == null) return@insertSeparators null

            val afterDate = after.token.visitedAt.date
            if (before == null) {
              return@insertSeparators TokenHistoryListItem.DateHeader(afterDate)
            }

            val beforeDate = before.token.visitedAt.date
            if (beforeDate != afterDate) {
              TokenHistoryListItem.DateHeader(afterDate)
            } else {
              null
            }
          }
      }
      .cachedIn(scope)

  fun deleteAllNewsHistory() {
    scope.launch { newsHistoryRepository.deleteAll() }
  }

  fun deleteAllTokenHistory() {
    scope.launch { tokenHistoryRepository.deleteAllHistory() }
  }

  fun deleteNewsHistory(id: Long) {
    scope.launch { newsHistoryRepository.deleteNewsHistory(id) }
  }

  fun deleteTokenHistory(id: Long) {
    scope.launch { tokenHistoryRepository.deleteTokenHistory(id) }
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
