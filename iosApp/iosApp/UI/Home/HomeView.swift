import Shared
import SwiftUI

struct HomeView: View {
    private let component: HomeComponent

    @StateValue
    private var pages: ChildPages<AnyObject, HomeComponentPage>

    private var selectedPageIndex: Int {
        Int(pages.selectedIndex)
    }

    private var selectedPage: HomeComponentPage {
        page(at: selectedPageIndex)
    }

    private var navigationBarHidden: Bool {
        if case .newsFeed = onEnum(of: selectedPage) {
            return true
        }
        return false
    }

    init(_ component: HomeComponent) {
        self.component = component
        _pages = StateValue(component.pages)
    }

    var body: some View {
        TabView(
            selection: Binding(
                get: { selectedPageIndex },
                set: { index in component.selectPage(index: Int32(index)) }
            )
        ) {
            ForEach(pages.items.indices, id: \.self) { index in
                tab(at: index)
            }
        }
        .navigationTitle(selectedPage.title)
        .toolbarBackground(navigationBarHidden ? .hidden : .automatic, for: .navigationBar)
        .toolbar {
            if case .newsFeed = onEnum(of: selectedPage) {
                ToolbarItem(placement: .principal) {
                    Text("")
                }
            }
        }
    }

    private func tab(at index: Int) -> Tab<Int, PageView, DefaultTabLabel> {
        let page = page(at: index)
        return Tab(page.title, systemImage: page.systemImage, value: index) {
            PageView(page: page, onSeedImageUrlChange: component.onSeedImageUrlChange)
        }
    }

    private func page(at index: Int) -> HomeComponentPage {
        pages.items[index].instance!
    }
}

private extension HomeComponentPage {
    var title: String {
        switch onEnum(of: self) {
        case .newsFeed: String(\.feed)
        case .prices: String(\.prices)
        case .history: String(\.history)
        }
    }

    var systemImage: String {
        switch onEnum(of: self) {
        case .newsFeed: "newspaper"
        case .prices: "dollarsign.circle"
        case .history: "clock"
        }
    }
}

private struct PageView: View {
    let page: HomeComponentPage
    let onSeedImageUrlChange: (String?) -> Void

    var body: some View {
        switch onEnum(of: page) {
        case let .newsFeed(newsFeedPage):
            NewsFeedView(component: newsFeedPage.component)
        case let .prices(pricesPage):
            PricesView(component: pricesPage.component)
                .onAppear { onSeedImageUrlChange(nil) }
        case let .history(historyPage):
            HistoryView(component: historyPage.component)
                .onAppear { onSeedImageUrlChange(nil) }
        }
    }
}
