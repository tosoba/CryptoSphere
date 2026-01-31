import Shared
import SwiftUI

struct TokenFeedView: View {
    private let component: TokenFeedComponent

    @StateObject @KotlinStateFlow private var feedItems: [TokenItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

    @State private var scrolledItemId: Int32?
    private var navigationToken: TokenItem? { feedItems.first }
    private var currentPresentedToken: TokenItem? { feedItems.first(where: { $0.id == scrolledItemId }) }
    private var navigateToTokenFeedToolbarItemVisible: Bool {
        scrolledItemId != nil && navigationToken?.id != scrolledItemId
    }

    @State private var tokenTagsGridMeasuredHeight: CGFloat = 120
    
    @Environment(\.cryptoSphereTheme) private var theme

    init(_ component: TokenFeedComponent) {
        self.component = component

        _feedItems = .init(component.viewModel.tokensPagingState.itemsSnapshotList)
        _loadStates = .init(component.viewModel.tokensPagingState.loadStates)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .notLoading:
                feedView
            default:
                LargeCircularProgressView()
            }
        }
        .animation(.default, value: loadStates?.refresh)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(theme.color(\.surfaceContainer))
    }

    var feedView: some View {
        GeometryReader { geometry in
            ScrollView(.vertical) {
                LazyVStack(spacing: 0) {
                    ForEach(Array(feedItems.enumerated()), id: \.element.id) { index, item in
                        itemView(item, at: index, in: geometry)
                            .id(item.id)
                    }
                }
                .scrollTargetLayout()
            }
            .scrollPosition(id: $scrolledItemId, anchor: .top)
            .ignoresSafeArea(.container, edges: .all)
            .scrollIndicators(.hidden)
            .scrollTargetBehavior(.paging)
            .indeterminateLinearProgressViewOverlay(loadState: loadStates?.append)
        }
        .navigationTitle(navigationToken?.symbol ?? "")
        .toolbar {
            if navigateToTokenFeedToolbarItemVisible {
                ToolbarItem(placement: .topBarTrailing) {
                    navigateToTokenFeedButton
                }
            }
        }
        .animation(.default, value: navigateToTokenFeedToolbarItemVisible)
    }

    @ViewBuilder
    private var navigateToTokenFeedButton: some View {
        Button(
            action: {
                if let item = currentPresentedToken {
                    component.navigateToTokenFeed(item)
                }
            }
        ) {
            if let item = currentPresentedToken {
                HStack(spacing: 4) {
                    Text(item.symbol)
                    Image(systemName: "chevron.forward")
                }
            } else {
                Image(systemName: "chevron.forward")
            }
        }
        .transition(.opacity.combined(with: .scale))
    }

    @ViewBuilder
    private func itemView(_ item: TokenItem, at index: Int, in geometry: GeometryProxy) -> some View {
        TokenFeedItemView(
            token: item,
            mainTokenTagNames: Set(feedItems.first?.tagNames ?? []),
            safeAreaInsets: geometry.safeAreaInsets,
            tokenTagsGridMeasuredHeight: $tokenTagsGridMeasuredHeight
        )
        .containerRelativeFrame([.vertical, .horizontal])
        .onAppear {
            component.onSeedImageUrlChange(item.logoUrl)

            if loadStates.canLoadMoreItems() && feedItems.count - index == TokenFeedViewModel.companion.PREFETCH_DISTANCE {
                component.viewModel.tokensPagingState.loadMore()
            }
        }
    }
}
