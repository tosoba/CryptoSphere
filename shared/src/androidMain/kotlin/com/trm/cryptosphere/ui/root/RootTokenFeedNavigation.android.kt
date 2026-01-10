package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushToFront
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.ui.root.RootDefaultComponent.ChildConfig

actual fun StackNavigation<ChildConfig>.navigateToTokenFeed(
  tokenId: Int,
  tokenCarouselConfig: TokenCarouselConfig?,
) {
  pushToFront(ChildConfig.TokenNavigation(tokenId, requireNotNull(tokenCarouselConfig)))
}
