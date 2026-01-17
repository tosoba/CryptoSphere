import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent

    @StateObject @KotlinStateFlow private var feedItems: [NewsFeedItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

    @State private var scrolledItemId: String?
    @State private var tokenCarouselMeasuredHeight: CGFloat = 90

    init(component: NewsFeedComponent) {
        self.component = component

        _feedItems = .init(component.viewModel.newsPagingState.itemsSnapshotList)
        _loadStates = .init(component.viewModel.newsPagingState.loadStates)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                loadingView
            case .notLoading:
                feedView
            case .error:
                errorView
            }
        }
        .animation(.default, value: loadStates?.refresh)
    }

    @ViewBuilder
    private var loadingView: some View {
        ProgressView()
            .scaleEffect(1.5)
            .containerRelativeFrame([.vertical, .horizontal])
            .background(.black)
            .tint(.white)
            .transition(.opacity)
    }

    @ViewBuilder
    private var feedView: some View {
        GeometryReader { geometry in
            ScrollView(.vertical) {
                LazyVStack(spacing: 0) {
                    ForEach(Array(feedItems.enumerated()), id: \.element.news.id) { index, item in
                        itemView(item, at: index, alignedTo: geometry.safeAreaInsets)
                    }
                }
                .scrollTargetLayout()
            }
            .scrollPosition(id: $scrolledItemId)
            .animation(.easeInOut, value: geometry.size)
            .background(.black)
            .ignoresSafeArea(.container, edges: .all)
            .scrollIndicators(.hidden)
            .scrollTargetBehavior(.paging)
            .transition(.opacity)
            .overlay(alignment: .bottom) {
                let progressVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
                IndeterminateLinearProgressView(isVisible: progressVisible)
            }
            .overlay(alignment: .top) {
                let errorVisible = if case .error = onEnum(of: loadStates?.append) { true } else { false }
                ErrorOccurredCard(
                    isVisible: errorVisible,
                    onRetryClick: { component.viewModel.newsPagingState.retry() }
                )
                .padding(.horizontal, 16)
                .padding(.top, 16)
            }
        }
    }

    @ViewBuilder
    private func itemView(_ item: NewsFeedItem, at index: Int, alignedTo insets: EdgeInsets) -> some View {
        NewsFeedItemView(
            item: item,
            insets: insets,
            tokenCarouselMeasuredHeight: $tokenCarouselMeasuredHeight,
            onTokenCarouselItemClick: { id, config in
                component.onTokenCarouselItemClick(KotlinInt(value: id), config)
            }
        )
        .scrollTransition(.animated, axis: .vertical) { content, phase in
            content
                .blur(radius: abs(phase.value) * 15)
                .opacity(1.0 - abs(phase.value))
                .scaleEffect(1.0 - (abs(phase.value) * 0.05))
        }
        .onAppear {
            if loadStates.canLoadMoreItems() && feedItems.count - index == NewsFeedViewModel.companion.PREFETCH_DISTANCE {
                component.viewModel.newsPagingState.loadMore()
            }
        }
    }

    @ViewBuilder
    private var errorView: some View {
        VStack(alignment: .center, spacing: 8) {
            Text(String(\.error_occurred))
            Button(
                action: { component.viewModel.newsPagingState.retry() },
                label: { Text(String(\.retry)) }
            )
        }
        .transition(.opacity)
    }
}
