import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent

    @StateObject @KotlinStateFlow private var newsFeedItems: [NewsFeedItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

    @State private var scrolledItemId: String?
    @State private var tokenCarouselMeasuredHeight: CGFloat = 90

    init(component: NewsFeedComponent) {
        self.component = component

        _newsFeedItems = .init(component.viewModel.newsPagingState.itemsSnapshotList)
        _loadStates = .init(component.viewModel.newsPagingState.loadStates)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                ProgressView()
                    .scaleEffect(1.5)
                    .containerRelativeFrame([.vertical, .horizontal])
                    .background(.black)
                    .tint(.white)
                    .transition(.opacity)
            case .notLoading:
                GeometryReader { geometry in
                    ScrollView(.vertical) {
                        LazyVStack(spacing: 0) {
                            ForEach(Array(newsFeedItems.enumerated()), id: \.element.news.id) { index, item in
                                NewsFeedItemView(
                                    item: item,
                                    safeArea: geometry.safeAreaInsets,
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
                                    guard case .notLoading = onEnum(of: loadStates?.append) else { return }
                                    if newsFeedItems.count - index < NewsFeedViewModel.companion.PREFETCH_DISTANCE {
                                        component.viewModel.newsPagingState.loadMore()
                                    }
                                }
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
                        let progressViewVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
                        IndeterminateLinearProgressView(isVisible: progressViewVisible)
                    }
                    .overlay(alignment: .top) {
                        let appendErrorVisible = if case .error = onEnum(of: loadStates?.append) { true } else { false }
                        ErrorOccurredCard(
                            isVisible: appendErrorVisible,
                            onRetryClick: { component.viewModel.newsPagingState.retry() }
                        )
                        .padding(.horizontal, 16)
                        .padding(.top, 16)
                    }
                }
            case .error:
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
        .animation(.default, value: loadStates?.refresh)
    }
}
