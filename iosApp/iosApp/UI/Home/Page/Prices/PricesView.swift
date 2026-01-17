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
                    loadingView
                        .transition(.opacity)
                case .error:
                    errorView
                        .transition(.opacity)
                case .notLoading:
                    if tokens.isEmpty {
                        emptyView
                            .transition(.opacity)
                    } else {
                        tokensList
                            .transition(.opacity)
                    }
                }
            }
            .animation(.default, value: onEnum(of: loadStates?.refresh))
        }
    }

    @ViewBuilder
    private var loadingView: some View {
        ProgressView()
            .scaleEffect(1.5)
            .containerRelativeFrame([.vertical, .horizontal])
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
                                guard case .notLoading = onEnum(of: loadStates?.append) else { return }
                                if tokens.count - index < PricesViewModel.companion.PAGE_SIZE {
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
        .overlay(alignment: .bottom) {
            let progressVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
            IndeterminateLinearProgressView(isVisible: progressVisible)
        }
        .overlay(alignment: .top) {
            let errorVisible = if case .error = onEnum(of: loadStates?.append) { true } else { false }
            ErrorOccurredCard(
                isVisible: errorVisible,
                onRetryClick: { component.viewModel.tokensPagingState.retry() }
            )
            .padding(.horizontal, 16)
            .padding(.top, 16)
        }
    }

    @ViewBuilder
    private var emptyView: some View {
        VStack(spacing: 16) {
            Image(systemName: "magnifyingglass")
                .font(.system(size: 64))
                .foregroundColor(.secondary)

            Text(String(\.no_tokens_found))
                .font(.title2)
                .foregroundColor(.secondary)
        }
    }

    @ViewBuilder
    private var errorView: some View {
        VStack(alignment: .center, spacing: 8) {
            Text(String(\.error_occurred))

            Button(
                action: { component.viewModel.tokensPagingState.retry() },
                label: { Text(String(\.retry)) }
            )
        }
    }
}
