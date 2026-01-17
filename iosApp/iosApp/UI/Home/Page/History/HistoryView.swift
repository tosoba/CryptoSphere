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
                    .transition(.opacity)
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
                    .transition(.opacity)
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
                loadingView
                    .transition(.opacity)
            case .error:
                errorView
                    .transition(.opacity)
            case .notLoading:
                if items.isEmpty {
                    emptyView
                        .transition(.opacity)
                } else {
                    newsList
                        .transition(.opacity)
                }
            }
        }
        .animation(.default, value: onEnum(of: loadStates?.refresh))
    }

    @ViewBuilder
    private var loadingView: some View {
        ProgressView()
            .scaleEffect(1.5)
            .containerRelativeFrame([.vertical, .horizontal])
    }

    @ViewBuilder
    private var errorView: some View {
        VStack(alignment: .center, spacing: 8) {
            Text(String(\.error_occurred))
            Button(action: retry) { Text(String(\.retry)) }
        }
    }

    @ViewBuilder
    private var emptyView: some View {
        HistoryEmptyView(icon: "clock.arrow.circlepath", text: String(\.no_tokens_history))
    }

    private var newsList: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                ForEach(
                    Array(items.enumerated()),
                    id: \.element.key
                ) { index, item in
                    Group {
                        switch onEnum(of: item) {
                        case let .dateHeader(header):
                            DateHeaderView(date: header.date)
                        case let .item(news):
                            HistoryNewsItemRow(
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
        .overlay(alignment: .bottom) {
            let progressVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
            IndeterminateLinearProgressView(isVisible: progressVisible)
        }
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
                loadingView
                    .transition(.opacity)
            case .error:
                errorView
                    .transition(.opacity)
            case .notLoading:
                if items.isEmpty {
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

    @ViewBuilder
    private var loadingView: some View {
        ProgressView()
            .scaleEffect(1.5)
            .containerRelativeFrame([.vertical, .horizontal])
    }

    @ViewBuilder
    private var errorView: some View {
        VStack(alignment: .center, spacing: 8) {
            Text(String(\.error_occurred))
            Button(action: retry) { Text(String(\.retry)) }
        }
    }

    @ViewBuilder
    private var emptyView: some View {
        HistoryEmptyView(icon: "clock.arrow.circlepath", text: String(\.no_tokens_history))
    }

    @ViewBuilder
    private var tokensList: some View {
        ScrollView {
            LazyVStack(spacing: 8) {
                ForEach(
                    Array(items.enumerated()),
                    id: \.element.key
                ) { index, item in
                    Group {
                        switch onEnum(of: item) {
                        case let .dateHeader(header):
                            DateHeaderView(date: header.date)
                        case let .item(token):
                            HistoryTokenItemRow(
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
        .overlay(alignment: .bottom) {
            let progressVisible = if case .loading = onEnum(of: loadStates?.append) { true } else { false }
            IndeterminateLinearProgressView(isVisible: progressVisible)
        }
        .overlay(alignment: .top) {
            let errorVisible = if case .error = onEnum(of: loadStates?.append) { true } else { false }
            ErrorOccurredCard(isVisible: errorVisible, onRetryClick: retry)
                .padding(.horizontal, 16)
                .padding(.top, 16)
        }
    }
}

private struct HistoryNewsItemRow: View {
    let item: NewsHistoryItem
    let onClick: () -> Void
    let onDelete: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 16) {
                AsyncImage(url: URL(string: item.imgUrl ?? "")) { image in
                    image.resizable().aspectRatio(contentMode: .fill)
                } placeholder: {
                    Color(.systemGray4)
                }
                .frame(width: 80, height: 80)
                .clipShape(RoundedRectangle(cornerRadius: 8))

                VStack(alignment: .leading, spacing: 4) {
                    Text(item.title)
                        .font(.headline)
                        .lineLimit(2)
                    Text(item.source)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }

                Spacer()
            }
            .padding(16)
            .background(Color(.systemBackground))
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
        .contextMenu {
            Button(role: .destructive, action: onDelete) {
                Label("Delete", systemImage: "trash")
            }
        }
    }
}

private struct HistoryTokenItemRow: View {
    let item: TokenHistoryItem
    let onClick: () -> Void
    let onDelete: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 12) {
                AsyncImage(url: URL(string: item.token.logoUrl)) { image in
                    image.resizable().aspectRatio(contentMode: .fit)
                } placeholder: {
                    Color(.systemGray4)
                }
                .frame(width: 40, height: 40)
                .clipShape(Circle())

                VStack(alignment: .leading) {
                    Text(item.token.name)
                        .font(.headline)

                    Text(item.token.symbol)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                .lineLimit(1)

                Spacer()
            }
            .padding(16)
            .background(Color(.systemBackground))
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
        .contextMenu {
            Button(role: .destructive, action: onDelete) {
                Label("Delete", systemImage: "trash")
            }
        }
    }
}

private struct HistoryEmptyView: View {
    let icon: String
    let text: String

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: icon)
                .font(.system(size: 60))
                .foregroundColor(.secondary)
            Text(text)
                .font(.title3)
                .foregroundColor(.secondary)
        }
        .frame(maxHeight: .infinity)
    }
}

private struct DateHeaderView: View {
    let date: LocalDate

    var body: some View {
        Text(formatDate(date))
            .font(.subheadline)
            .fontWeight(.semibold)
            .foregroundColor(.secondary)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .frame(maxWidth: .infinity, alignment: .leading)
    }

    private func formatDate(_ localDate: LocalDate) -> String {
        guard let date = Calendar.current.date(
            from: DateComponents(
                year: Int(localDate.year),
                month: Int(localDate.month.ordinal + 1),
                day: Int(localDate.day)
            )
        ) else {
            return localDate.description()
        }

        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .none
        return formatter.string(from: date)
    }
}
