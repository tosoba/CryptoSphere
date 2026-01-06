package com.trm.cryptosphere.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

fun cardListItemRoundedCornerShape(
  isTopRounded: Boolean,
  isBottomRounded: Boolean,
): RoundedCornerShape =
  RoundedCornerShape(
    topStart = if (isTopRounded) 16.dp else 0.dp,
    topEnd = if (isTopRounded) 16.dp else 0.dp,
    bottomStart = if (isBottomRounded) 16.dp else 0.dp,
    bottomEnd = if (isBottomRounded) 16.dp else 0.dp,
  )
