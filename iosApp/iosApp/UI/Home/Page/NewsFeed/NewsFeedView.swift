import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent
    @StateObject @KotlinStateFlow private var newsItems: NewsItemsSnapshotList
    @StateObject @KotlinOptionalStateFlow private var loadState: CombinedLoadStates?

    init(component: NewsFeedComponent) {
        self.component = component
        _newsItems = .init(component.viewState.newsItemsSnapshotList)
        _loadState = .init(component.viewState.newsItemsLoadState)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadState?.refresh) {
            case .loading, .none:
                ProgressView()
            case .notLoading:
                ScrollView(.vertical) {
                    LazyVStack(spacing: 0) {
                        ForEach(newsItems.items.indices, id: \.self) { index in
                            let item = newsItems.itemAt(index: Int32(index))
                            Text(item?.title ?? "")
                        }
                    }
                }
                .scrollIndicators(.hidden)
                .scrollTargetBehavior(.paging)
            case .error:
                VStack(alignment: .center, spacing: 10) {
                    Text("Error occurred")
                    Button(action: { component.viewState.retry() }, label: { Text("Retry") })
                }
            }
        }
        .animation(.easeInOut(duration: 0.3), value: loadState?.refresh)
    }
}
