import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent

    @StateObject @KotlinStateFlow private var newsFeedItems: [NewsFeedItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

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
                            ForEach(newsFeedItems.indices, id: \.self) { index in
                                NewsFeedItemView(
                                    item: newsFeedItems[index],
                                    safeArea: geometry.safeAreaInsets,
                                    tokenCarouselMeasuredHeight: $tokenCarouselMeasuredHeight,
                                    onTokenCarouselItemClick: { id, config in
                                        component.onTokenCarouselItemClick(KotlinInt(value: id), config)
                                    }
                                )
                                .onAppear {
                                    switch onEnum(of: loadStates?.append) {
                                    case .notLoading:
                                        if newsFeedItems.count - index < NewsFeedViewModel.companion.PREFETCH_DISTANCE {
                                            component.viewModel.newsPagingState.loadMore()
                                        }
                                    default:
                                        break
                                    }
                                }
                            }
                        }
                        .scrollTargetLayout()
                    }
                    .ignoresSafeArea(.container, edges: .all)
                    .scrollIndicators(.hidden)
                    .scrollTargetBehavior(.paging)
                    .transition(.opacity)
                    .overlay(alignment: .bottom) {
                        let progressViewVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
                        IndeterminateLinearProgressView(isVisible: progressViewVisible)
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
