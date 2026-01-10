package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.router.stack.StackNavigation
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

expect fun StackNavigation<RootDefaultComponent.ChildConfig>.navigateToTokenFeed(
  tokenId: Int,
  tokenCarouselConfig: TokenCarouselConfig?,
)
