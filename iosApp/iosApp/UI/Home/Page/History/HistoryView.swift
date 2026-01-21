import Shared
import SwiftUI

struct HistoryView: View {
    private let component: HistoryComponent
    private let viewModel: HistoryViewModel

    @StateObject @KotlinStateFlow private var newsItems: [HistoryNewsListItem]
    @StateObject @KotlinOptionalStateFlow private var newsLoadStates: CombinedLoadStates?
    @StateObject @KotlinStateFlow private var tokenItems: [HistoryTokenListItem]
    @StateObject @KotlinOptionalStateFlow private var tokenLoadStates: CombinedLoadStates?
    @StateObject @KotlinStateFlow private var query: String

    @State private var selectedPage: HistoryPage = .news
    @State private var showDeleteAllConfirmation = false

    init(component: HistoryComponent) {
        self.component = component
        viewModel = component.viewModel

        _newsItems = .init(viewModel.newsHistoryPagingState.itemsSnapshotList)
        _newsLoadStates = .init(viewModel.newsHistoryPagingState.loadStates)
        _tokenItems = .init(viewModel.tokenHistoryPagingState.itemsSnapshotList)
        _tokenLoadStates = .init(viewModel.tokenHistoryPagingState.loadStates)
        _query = .init(viewModel.query)
    }

    var body: some View {
        VStack(spacing: 12) {
            SearchBar(
                placeholder: String(\.search_history),
                query: Binding(
                    get: { query },
                    set: { newQuery in viewModel.onQueryChange(newQuery: newQuery) }
                )
            )
            .padding(.horizontal)

            Picker("History Page", selection: $selectedPage.animation(.default)) {
                Text(String(\.news)).tag(HistoryPage.news)
                Text(String(\.tokens)).tag(HistoryPage.tokens)
            }
            .pickerStyle(.segmented)
            .padding(.horizontal)

            ZStack {
                switch selectedPage {
                case .news:
                    HistoryNewsListView(
                        items: newsItems,
                        loadStates: newsLoadStates,
                        onItemClick: { item in
                            viewModel.onNewsClick(news: item)
                            PlatformContext.shared.openUrl(url: item.url)
                        },
                        onDelete: { id in
                            viewModel.onDeleteNewsHistoryClick(id: id)
                        },
                        loadMore: { viewModel.newsHistoryPagingState.loadMore() },
                        retry: { viewModel.newsHistoryPagingState.retry() }
                    )
                case .tokens:
                    HistoryTokensListView(
                        items: tokenItems,
                        loadStates: tokenLoadStates,
                        onItemClick: { token in
                            component.onTokenClick(KotlinInt(value: token.id), TokenCarouselConfig())
                        },
                        onDelete: { id in
                            viewModel.onDeleteTokenHistoryClick(id: id)
                        },
                        loadMore: { viewModel.tokenHistoryPagingState.loadMore() },
                        retry: { viewModel.tokenHistoryPagingState.retry() }
                    )
                }
            }
            .animation(.default, value: selectedPage)
        }
    }
}

private struct HistoryNewsListView: View {
    let items: [HistoryNewsListItem]
    let loadStates: CombinedLoadStates?
    let onItemClick: (NewsHistoryItem) -> Void
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
                    newsList
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
        EmptyListView(icon: "clock.arrow.circlepath", text: String(\.no_news_history))
    }

    private var newsList: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                ForEach(Array(items.enumerated()), id: \.element.key) { index, item in
                    Group {
                        switch onEnum(of: item) {
                        case let .dateHeader(header):
                            HistoryDateHeaderView(date: header.date)
                        case let .item(news):
                            HistoryNewsItemView(
                                item: news.data,
                                onClick: { onItemClick(news.data) },
                                onDelete: { onDelete(news.data.id) }
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
        .overlay(alignment: .top) {
            let errorVisible = if case .error = onEnum(of: loadStates?.append) { true } else { false }
            ErrorOccurredCard(isVisible: errorVisible, onRetryClick: retry)
                .padding(.horizontal, 16)
                .padding(.top, 16)
        }
    }
}

private struct HistoryTokensListView: View {
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
        .overlay(alignment: .top) {
            let errorVisible = if case .error = onEnum(of: loadStates?.append) { true } else { false }
            ErrorOccurredCard(isVisible: errorVisible, onRetryClick: retry)
                .padding(.horizontal, 16)
                .padding(.top, 16)
        }
    }
}

private struct HistoryNewsItemView: View {
    let item: NewsHistoryItem
    let onClick: () -> Void
    let onDelete: () -> Void

    var body: some View {
        HistoryItemView(
            imageUrl: item.imgUrl,
            title: item.title,
            subtitle: item.source,
            visitedAt: item.visitedAt.time,
            onClick: onClick,
            onDelete: onDelete
        )
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
