import Shared
import SwiftUI

struct PricesView: View {
    private let component: PricesComponent

    @StateObject @KotlinStateFlow private var tokens: [TokenItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

    @State private var searchQuery: String = ""

    init(component: PricesComponent) {
        self.component = component

        _tokens = .init(component.viewModel.tokensPagingState.itemsSnapshotList)
        _loadStates = .init(component.viewModel.tokensPagingState.loadStates)
    }

    @ViewBuilder
    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                loadingView
            case .error:
                errorView
            case .notLoading:
                if tokens.isEmpty {
                    emptyView
                } else {
                    tokensList
                }
            }
        }
        .animation(.default, value: onEnum(of: loadStates?.refresh))
        // TODO: FIXME
//        .searchable(text: $searchQuery, prompt: String(\.search_tokens))
//        .onChange(of: searchQuery) { _, newQuery in
//            component.viewModel.onQueryChange(newQuery: newQuery)
//        }
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
            LazyVStack(spacing: 0) {
                ForEach(Array(tokens.enumerated()), id: \.element.id) { index, token in
                    Button(action: {
                        component.onTokenClick(KotlinInt(value: token.id), TokenCarouselConfig())
                    }) {
                        TokenPriceItemView(token: token)
                            .onAppear {
                                guard case .notLoading = onEnum(of: loadStates?.append) else { return }
                                if tokens.count - index < PricesViewModel.companion.PAGE_SIZE {
                                    component.viewModel.tokensPagingState.loadMore()
                                }
                            }
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 2)
                }
            }
        }
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

private struct TokenPriceItemView: View {
    let token: TokenItem

    var body: some View {
        HStack(spacing: 16) {
            Text("\(token.cmcRank)")
                .font(.title2)
                .foregroundColor(.secondary)

            AsyncImage(
                url: URL(string: token.logoUrl),
                content: { image in image.resizable().aspectRatio(contentMode: .fit) },
                placeholder: { ProgressView() }
            )
            .frame(width: 48, height: 48)
            .clipShape(RoundedRectangle(cornerRadius: 8))

            VStack(alignment: .leading) {
                Text(token.symbol)
                    .font(.headline)
                    .fontWeight(.medium)
                    .foregroundColor(.primary)

                Text(token.name)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            .lineLimit(1)

            Spacer()

            VStack(alignment: .trailing) {
                Text("$\(token.quote.price.fullDecimalFormat(significantDecimals: 3, signed: false))")
                    .font(.headline)
                    .fontWeight(.medium)
                    .foregroundColor(.primary)

                let percentChange24h = token.quote.percentChange24h
                let valueChangePositive = percentChange24h >= 0

                Text("\(percentChange24h.fullDecimalFormat(significantDecimals: 2, signed: true))%")
                    .font(.subheadline)
                    .padding(.horizontal, 4)
                    .foregroundColor(valueChangePositive ? .black : .white)
                    .background(valueChangePositive ? Color.green : Color.red)
                    .clipShape(RoundedRectangle(cornerRadius: 4))
            }
        }
        .padding(16)
        .background(Color(.systemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }
}
