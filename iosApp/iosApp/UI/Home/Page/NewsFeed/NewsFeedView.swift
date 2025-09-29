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
                GeometryReader { geometry in
                    ScrollView(.vertical) {
                        LazyVStack(spacing: 0) {
                            ForEach(newsItems.items.indices, id: \.self) { index in
                                NewsFeedItemView(item: newsItems.itemAt(index: Int32(index)), safeArea: geometry.safeAreaInsets)
                            }
                        }
                    }
                }
                .ignoresSafeArea(.container, edges: .all)
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

struct NewsFeedItemView: View {
    let item: NewsItem?
    let safeArea: EdgeInsets

    var body: some View {
        AsyncImage(url: URL(string: item?.imgUrl ?? "")) { phase in
            switch phase {
            case .empty:
                ProgressView()
            case let .success(image):
                image
                    .resizable()
                    .scaledToFill()
            case .failure:
                Image(systemName: "dollarsign")
                    .resizable()
                    .scaledToFill()
            @unknown default:
                EmptyView()
            }
        }
        .frame(maxWidth: .infinity)
        .containerRelativeFrame(.vertical)
        .overlay(alignment: .bottom) {
            VStack(alignment: .leading, spacing: 10) {
                Text(item?.title ?? "")
                Spacer(minLength: safeArea.bottom)
            }
        }
    }
}
