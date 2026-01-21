import Shared
import SwiftUI

struct HistoryTokensListView: View {
    let items: [HistoryTokenListItem]
    let loadStates: CombinedLoadStates?
    let onItemClick: (TokenItem) -> Void
    let onDelete: (Int64) -> Void
    let loadMore: () -> Void
    let retry: () -> Void

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                LargeCircularProgressView()
            case .error:
                errorView
            case .notLoading:
                if items.isEmpty {
                    emptyView
                } else {
                    tokensList
                }
            }
        }
        .animation(.default, value: onEnum(of: loadStates?.refresh))
    }

    @ViewBuilder
    private var errorView: some View {
        ErrorListView(text: String(\.error_occurred), onRetryClick: retry)
    }

    @ViewBuilder
    private var emptyView: some View {
        EmptyListView(icon: "clock.arrow.circlepath", text: String(\.no_tokens_history))
    }

    @ViewBuilder
    private var tokensList: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                ForEach(Array(items.enumerated()), id: \.element.key) { index, item in
                    Group {
                        switch onEnum(of: item) {
                        case let .dateHeader(header):
                            HistoryDateHeaderView(date: header.date)
                        case let .item(token):
                            HistoryTokenItemView(
                                item: token.data,
                                onClick: { onItemClick(token.data.token) },
                                onDelete: { onDelete(token.data.id) }
                            )
                            .onAppear {
                                if loadStates.canLoadMoreItems() && items.count - index == HistoryViewModel.companion.PAGE_SIZE {
                                    loadMore()
                                }
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
}

private struct HistoryTokenItemView: View {
    let item: TokenHistoryItem
    let onClick: () -> Void
    let onDelete: () -> Void

    var body: some View {
        HistoryItemView(
            imageUrl: item.token.logoUrl,
            title: item.token.name,
            subtitle: item.token.symbol,
            visitedAt: item.visitedAt.time,
            onClick: onClick,
            onDelete: onDelete
        )
    }
}
