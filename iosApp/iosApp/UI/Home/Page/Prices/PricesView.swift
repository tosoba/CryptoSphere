import Shared
import SwiftUI

struct PricesView: View {
    private let component: PricesComponent

    @StateObject @KotlinStateFlow private var tokens: [TokenItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?
    @StateObject @KotlinStateFlow private var query: String

    init(component: PricesComponent) {
        self.component = component

        _tokens = .init(component.viewModel.tokensPagingState.itemsSnapshotList)
        _loadStates = .init(component.viewModel.tokensPagingState.loadStates)
        _query = .init(component.viewModel.query)
    }

    @ViewBuilder
    var body: some View {
        VStack {
            SearchBar(
                placeholder: String(\.search_tokens),
                query: Binding(
                    get: { query },
                    set: { newQuery in component.viewModel.onQueryChange(newQuery: newQuery) }
                )
            )
            .padding(.horizontal)

            ZStack {
                switch onEnum(of: loadStates?.refresh) {
                case .loading, .none:
                    LargeCircularProgressView()
                case .error:
                    errorView
                case .notLoading:
                    if tokens.isEmpty {
                        EmptyListView(icon: "magnifyingglass", text: String(\.no_tokens_found))
                    } else {
                        tokensList
                    }
                }
            }
            .animation(.default, value: onEnum(of: loadStates?.refresh))
        }
    }

    @ViewBuilder
    private var tokensList: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                ForEach(Array(tokens.enumerated()), id: \.element.id) { index, token in
                    Button(
                        action: {
                            component.onTokenClick(KotlinInt(value: token.id), TokenCarouselConfig())
                        }
                    ) {
                        PriceItemView(token: token)
                            .onAppear {
                                if loadStates.canLoadMoreItems() && tokens.count - index == PricesViewModel.companion.PAGE_SIZE {
                                    component.viewModel.tokensPagingState.loadMore()
                                }
                            }
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 8)
        }
        .scrollDismissesKeyboard(.interactively)
        .background(Color(.systemGroupedBackground))
        .indeterminateLinearProgressViewOverlay(loadState: loadStates?.append)
    }

    @ViewBuilder
    private var errorView: some View {
        ErrorListView(
            text: String(\.error_occurred),
            onRetryClick: { component.viewModel.tokensPagingState.retry() }
        )
    }
}
