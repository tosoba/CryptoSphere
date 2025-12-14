import Shared
import SwiftUI

struct HomeView: View {
    private let component: HomeComponent

    @StateValue
    private var pages: ChildPages<AnyObject, HomeComponentPage>

    init(_ component: HomeComponent) {
        self.component = component
        _pages = StateValue(component.pages)
    }

    var body: some View {
        TabView {
            ForEach(pages.items.indices, id: \.self) { index in
                page(at: index).tab
            }
        }
    }

    private func page(at index: Int) -> HomeComponentPage {
        pages.items[index].instance!
    }
}

private extension HomeComponentPage {
    private var title: String {
        switch onEnum(of: self) {
        case .newsFeed: String(\.feed)
        case .prices: String(\.prices)
        case .history: String(\.history)
        }
    }

    private var systemImage: String {
        switch onEnum(of: self) {
        case .newsFeed: "list.bullet.below.rectangle"
        case .prices: "dollarsign.circle"
        case .history: "archivebox"
        }
    }

    var tab: Tab<Never, PageView, DefaultTabLabel> {
        Tab(title, systemImage: systemImage) {
            PageView(page: self)
        }
    }
}

private struct PageView: View {
    let page: HomeComponentPage

    var body: some View {
        switch onEnum(of: page) {
        case let .newsFeed(newsFeedPage): NewsFeedView(component: newsFeedPage.component)
        case let .prices(pricesPage): PricesView(component: pricesPage.component)
        case let .history(historyPage): HistoryView(component: historyPage.component)
        }
    }
}
