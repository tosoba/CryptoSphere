import Shared
import SwiftUI

struct PricesView: View {
    private let component: PricesComponent
    private let viewModel: PricesViewModel

    @StateObject @KotlinStateFlow private var tokens: [TokenItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?
    @StateObject @KotlinStateFlow private var query: String

    @Environment(\.cryptoSphereTheme) private var theme

    init(component: PricesComponent) {
        self.component = component
        viewModel = component.viewModel

        _tokens = .init(viewModel.tokensPagingState.itemsSnapshotList)
        _loadStates = .init(viewModel.tokensPagingState.loadStates)
        _query = .init(viewModel.query)
    }

    var body: some View {
        GeometryReader { geometry in
            ZStack {
                tokensLoadStatesView(
                    topPadding: geometry.safeAreaInsets.top + SearchBarView.height
                )

                ListTopSearchBarView(
                    placeholder: String(\.search_tokens),
                    query: Binding(
                        get: { query },
                        set: { newQuery in viewModel.onQueryChange(newQuery: newQuery) }
                    ),
                    insets: geometry.safeAreaInsets
                )
            }
            .ignoresSafeArea(.container, edges: .top)
        }
    }

    @ViewBuilder
    private func tokensLoadStatesView(topPadding: CGFloat) -> some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                LargeCircularProgressView()
                    .padding(.top, topPadding)
            case .error:
                errorView
                    .padding(.top, topPadding)
            case .notLoading:
                if tokens.isEmpty {
                    EmptyListView(icon: "magnifyingglass", text: String(\.no_tokens_found))
                        .padding(.top, topPadding)
                } else {
                    tokensScrollView(topPadding: topPadding)
                }
            }
        }
        .animation(.default, value: onEnum(of: loadStates?.refresh))
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(theme.color(\.surfaceContainer))
    }

    @ViewBuilder
    private func tokensScrollView(topPadding: CGFloat) -> some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                Spacer()
                    .frame(height: topPadding)

                ForEach(Array(tokens.enumerated()), id: \.element.id) { index, token in
                    PriceItemView(
                        token: token,
                        onClick: {
                            component.onTokenClick(
                                KotlinInt(value: token.id),
                                TokenCarouselConfig()
                            )
                        }
                    )
                    .onAppear {
                        if loadStates.canLoadMoreItems() && tokens.count - index == PricesViewModel.companion.PAGE_SIZE {
                            viewModel.tokensPagingState.loadMore()
                        }
                    }
                }
            }
            .padding(.horizontal, 16)
        }
        .scrollDismissesKeyboard(.interactively)
        .indeterminateLinearProgressViewOverlay(loadState: loadStates?.append)
    }

    @ViewBuilder
    private var errorView: some View {
        ErrorListView(
            text: String(\.error_occurred),
            onRetryClick: { viewModel.tokensPagingState.retry() }
        )
    }
}
