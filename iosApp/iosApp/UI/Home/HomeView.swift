import Shared
import SwiftUI

struct HomeView: View {
    private let component: HomeComponent
    @StateValue
    private var pages: ChildPages<AnyObject, HomeComponentPage>

    private var activePage: HomeComponentPage {
        pages.items[Int(pages.selectedIndex)].instance!
    }

    init(_ component: HomeComponent) {
        self.component = component
        _pages = StateValue(component.pages)
    }

    var body: some View {
        VStack {
            ChildView(page: activePage)
                .frame(maxHeight: .infinity)
            // TODO: use proper tabs like in tv-maniac
            HStack(alignment: .bottom, spacing: 16) {
                Button(action: { component.selectPage(index: 0) }) {
                    Label("Feed", systemImage: "menucard")
                        .labelStyle(VerticalLabelStyle())
                }

                Button(action: { component.selectPage(index: 1) }) {
                    Label("Prices", systemImage: "123.rectangle")
                        .labelStyle(VerticalLabelStyle())
                }

                Button(action: { component.selectPage(index: 2) }) {
                    Label("Search", systemImage: "list.bullet")
                        .labelStyle(VerticalLabelStyle())
                }

                Button(action: { component.selectPage(index: 3) }) {
                    Label("History", systemImage: "list.bullet")
                        .labelStyle(VerticalLabelStyle())
                }
            }
        }
    }
}

private struct ChildView: View {
    let page: HomeComponentPage

    var body: some View {
        switch onEnum(of: page) {
        case let .feed(feedPage): FeedView()
        case let .prices(pricesPage): PricesView()
        case let .search(searchPage): SearchView()
        case let .history(historyPage): HistoryView()
        }
    }
}

private struct VerticalLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        VStack(alignment: .center, spacing: 8) {
            configuration.icon
            configuration.title
        }
    }
}
