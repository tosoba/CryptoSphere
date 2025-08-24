package com.trm.cryptosphere.core.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
interface RestartableStateFlow<out T> : StateFlow<T> {
  fun restart()
}

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
fun <T> Flow<T>.restartableStateIn(
  scope: CoroutineScope,
  started: SharingStarted,
  initialValue: T,
): RestartableStateFlow<T> {
  val sharingRestartable = started.asRestartable()
  return object :
    RestartableStateFlow<T>, StateFlow<T> by stateIn(scope, sharingRestartable, initialValue) {
    override fun restart() = sharingRestartable.restart()
  }
}
