package com.trm.cryptosphere.ui.home.page.history

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.PagingItemsState
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HistoryViewModel(
  private val newsHistoryRepository: NewsHistoryRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val query = MutableStateFlow("")

  val newsHistoryPagingState: PagingItemsState<NewsHistoryListItem> =
    PagingItemsState(scope) {
      query.debounce(250).flatMapLatest(newsHistoryRepository::getHistory).map { pagingData ->
        pagingData
          .map(NewsHistoryListItem::Item)
          .insertDateSeparators(
            date = { (data) -> data.visitedAt.date },
            separatorItem = NewsHistoryListItem::DateHeader,
          )
      }
    }

  val tokenHistoryPagingState: PagingItemsState<TokenHistoryListItem> =
    PagingItemsState(scope) {
      query.debounce(250).flatMapLatest(tokenHistoryRepository::getHistory).map { pagingData ->
        pagingData
          .map(TokenHistoryListItem::Item)
          .insertDateSeparators(
            date = { (data) -> data.visitedAt.date },
            separatorItem = TokenHistoryListItem::DateHeader,
          )
      }
    }

  private fun <R : Any, T : R, S : R> PagingData<T>.insertDateSeparators(
    date: (T) -> LocalDate,
    separatorItem: (LocalDate) -> S,
  ): PagingData<R> = insertSeparators { before: T?, after: T? ->
    if (after == null) return@insertSeparators null

    val afterDate = date(after)
    if (before == null) {
      return@insertSeparators separatorItem(afterDate)
    }

    val beforeDate = date(before)
    if (beforeDate != afterDate) {
      separatorItem(afterDate)
    } else {
      null
    }
  }

  fun onQueryChange(newQuery: String) {
    query.value = newQuery.trim()
  }

  fun onDeleteHistoryClick(page: HistoryPage) {
    when (page) {
      HistoryPage.NEWS -> deleteAllNewsHistory()
      HistoryPage.TOKENS -> deleteAllTokenHistory()
    }
  }

  fun onNewsClick(news: NewsHistoryItem) {
    scope.launch { newsHistoryRepository.updateNewsInHistory(news) }
  }

  private fun deleteAllNewsHistory() {
    scope.launch { newsHistoryRepository.deleteAll() }
  }

  private fun deleteAllTokenHistory() {
    scope.launch { tokenHistoryRepository.deleteAllHistory() }
  }

  fun onDeleteNewsHistoryClick(id: Long) {
    scope.launch { newsHistoryRepository.deleteNewsHistory(id) }
  }

  fun onDeleteTokenHistoryClick(id: Long) {
    scope.launch { tokenHistoryRepository.deleteTokenHistory(id) }
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
