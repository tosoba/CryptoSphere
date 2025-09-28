package com.trm.cryptosphere.ui.home.page.prices

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trm.cryptosphere.core.util.resolve
import com.trm.cryptosphere.shared.MR

@Composable
fun PricesContent(component: PricesComponent, modifier: Modifier = Modifier) {
  Box(modifier = modifier, contentAlignment = Alignment.Center) {
    Text(MR.strings.prices.resolve())
  }
}
