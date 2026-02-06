import Shared
import SwiftUI

struct HistoryTokensListView: View {
    let items: [HistoryTokenListItem]
    let loadStates: CombinedLoadStates?
    let topPadding: CGFloat
    let onItemClick: (TokenItem) -> Void
    let onDelete: (Int64) -> Void
    let loadMore: () -> Void
    let retry: () -> Void

    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                LargeCircularProgressView()
                    .padding(.top, topPadding)
            case .error:
                errorView
                    .padding(.top, topPadding)
            case .notLoading:
                if items.isEmpty {
                    emptyView
                        .padding(.top, topPadding)
                } else {
                    tokensList(topPadding: topPadding)
                }
            }
        }
        .animation(.default, value: onEnum(of: loadStates?.refresh))
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private var errorView: some View {
        ErrorListView(text: String(\.error_occurred), onRetryClick: retry)
    }

    private var emptyView: some View {
        EmptyListView(icon: "clock.arrow.circlepath", text: String(\.no_tokens_history))
    }

    private func tokensList(topPadding: CGFloat) -> some View {
        ScrollView {
            LazyVStack(spacing: 2) {
                Spacer()
                    .frame(height: topPadding - 8)

                ForEach(Array(items.enumerated()), id: \.element.key) { index, item in
                    Group {
                        switch onEnum(of: item) {
                        case let .dateHeader(header):
                            HistoryDateHeaderView(date: header.date)
                        case let .item(token):
                            let firstOnDate = if case .dateHeader = onEnum(of: index > 0 ? items[index - 1] : nil) { true } else { false }
                            let lastOnDate = if case .dateHeader = onEnum(of: index < items.count - 1 ? items[index + 1] : nil) { true } else { false }

                            HistoryTokenItemView(
                                item: token.data,
                                shape: historyListItemShape(
                                    for: index,
                                    outOf: items.count,
                                    firstOnDate: firstOnDate,
                                    lastOnDate: lastOnDate
                                ),
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
        .background(theme.color(\.surfaceContainer))
        .indeterminateLinearProgressViewOverlay(loadState: loadStates?.append)
    }
}

private struct HistoryTokenItemView<S: Shape>: View {
    let item: TokenHistoryItem
    let shape: S
    let onClick: () -> Void
    let onDelete: () -> Void

    var body: some View {
        HistoryItemView(
            imageUrl: item.token.logoUrl,
            title: item.token.name,
            subtitle: item.token.symbol,
            visitedAt: item.visitedAt.time,
            shape: shape,
            onClick: onClick,
            onDelete: onDelete
        )
    }
}
