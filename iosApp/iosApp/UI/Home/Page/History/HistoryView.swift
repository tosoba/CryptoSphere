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
    private var deleteAllDisabled: Bool {
        (newsItems.isEmpty && selectedPage == .news)
            || (tokenItems.isEmpty && selectedPage == .tokens)
    }

    @Environment(\.cryptoSphereTheme) private var theme

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
        GeometryReader { geometry in
            ZStack {
                historyLoadStatesView(topPadding: geometry.safeAreaInsets.top + SearchBarView.height * 2)

                topSearchBarView(insets: geometry.safeAreaInsets)
            }
            .ignoresSafeArea(.container, edges: .top)
        }
        .alert(
            String(\.delete_history),
            isPresented: $showDeleteAllConfirmation
        ) {
            Button(String(\.cancel), role: .cancel) {
                showDeleteAllConfirmation = false
            }

            Button(String(\.confirm), role: .destructive) {
                viewModel.onDeleteHistoryClick(page: selectedPage)
            }
        } message: {
            Text(String(\.delete_history_message))
        }
    }

    @ViewBuilder
    private func topSearchBarView(insets: EdgeInsets) -> some View {
        VStack {
            Spacer()
                .frame(height: insets.top)

            HStack(spacing: 8) {
                SearchBarView(
                    placeholder: String(\.search_history),
                    query: Binding(
                        get: { query },
                        set: { newQuery in viewModel.onQueryChange(newQuery: newQuery) }
                    )
                )

                Button(action: { showDeleteAllConfirmation = true }) {
                    Image(systemName: "trash")
                        .foregroundColor(deleteAllDisabled ? .gray : .red)
                }
                .disabled(deleteAllDisabled)
                .buttonStyle(.bordered)
                .clipShape(.circle)
            }
            .padding(.horizontal)

            historyPagePickerView

            Spacer()
        }
    }

    private var historyPagePickerView: some View {
        Picker("History Page", selection: $selectedPage.animation(.default)) {
            Text(String(\.news))
                .tag(HistoryPage.news)

            Text(String(\.tokens))
                .tag(HistoryPage.tokens)
        }
        .pickerStyle(.segmented)
        .padding(.horizontal)
    }

    private func historyLoadStatesView(topPadding: CGFloat) -> some View {
        ZStack {
            switch selectedPage {
            case .news:
                HistoryNewsListView(
                    items: newsItems,
                    loadStates: newsLoadStates,
                    topPadding: topPadding,
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
                    topPadding: topPadding,
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
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(theme.color(\.surfaceContainer))
    }
}
