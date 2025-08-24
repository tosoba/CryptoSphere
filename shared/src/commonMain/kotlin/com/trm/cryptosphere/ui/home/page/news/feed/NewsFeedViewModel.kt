package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.LoadableState
import com.trm.cryptosphere.core.base.loadableStateFlowOf
import com.trm.cryptosphere.core.base.restartableStateIn
import com.trm.cryptosphere.domain.usecase.GetNews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted

internal class NewsFeedViewModel(
  dispatchers: AppCoroutineDispatchers,
  private val getNews: GetNews,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  val value =
    loadableStateFlowOf { getNews() }
      .restartableStateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = LoadableState.Loading,
      )

  override fun onDestroy() {
    scope.cancel()
  }
}
