package com.trm.cryptosphere.core.base

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StateFlowInstance<T>(initialState: T) : InstanceKeeper.Instance {
  private val _state = MutableStateFlow(initialState)
  val state = _state.asStateFlow()

  var value: T
    get() = _state.value
    set(value) {
      _state.value = value
    }

  fun update(function: (T) -> T) {
    _state.update(function)
  }
}
