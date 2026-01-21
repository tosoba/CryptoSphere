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
