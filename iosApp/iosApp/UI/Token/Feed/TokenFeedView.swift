import Shared
import SwiftUI

struct TokenFeedView: View {
    private let component: TokenFeedComponent

    @StateObject @KotlinStateFlow private var feedItems: [TokenItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

    @State private var scrolledItemId: String?

    init(_ component: TokenFeedComponent) {
        self.component = component

        _feedItems = .init(component.viewModel.tokensPagingState.itemsSnapshotList)
        _loadStates = .init(component.viewModel.tokensPagingState.loadStates)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                loadingView
            case .notLoading:
                feedView
            case .error:
                EmptyView()
            }
        }
        .animation(.default, value: loadStates?.refresh)
    }

    @ViewBuilder
    private var loadingView: some View {
        ProgressView()
            .scaleEffect(1.5)
            .containerRelativeFrame([.vertical, .horizontal])
            .transition(.opacity)
    }

    @ViewBuilder
    private var feedView: some View {
        GeometryReader { geometry in
            ScrollView(.vertical) {
                LazyVStack(spacing: 0) {
                    ForEach(Array(feedItems.enumerated()), id: \.element.id) { index, item in
                        itemView(item, at: index, alignedTo: geometry.safeAreaInsets)
                    }
                }
                .scrollTargetLayout()
            }
            .scrollPosition(id: $scrolledItemId)
            .animation(.easeInOut, value: geometry.size)
            .ignoresSafeArea(.container, edges: .all)
            .scrollIndicators(.hidden)
            .scrollTargetBehavior(.paging)
            .transition(.opacity)
            .overlay(alignment: .bottom) {
                let progressVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
                IndeterminateLinearProgressView(isVisible: progressVisible)
            }
        }
        .navigationTitle(feedItems.first?.name ?? "")
    }

    @ViewBuilder
    private func itemView(_ item: TokenItem, at _: Int, alignedTo _: EdgeInsets) -> some View {
        VStack(alignment: .center) {
            Text(item.name)
            Button("Go to next") {
                component.onCurrentFeedTokenChange(item)
            }
        }
        .containerRelativeFrame([.vertical, .horizontal])
    }
}
