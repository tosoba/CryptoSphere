import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent

    @StateObject @KotlinStateFlow private var newsItems: NewsItemsSnapshotList
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?

    init(component: NewsFeedComponent) {
        self.component = component
        
        _newsItems = .init(component.viewState.newsItemsSnapshotList)
        _loadStates = .init(component.viewState.newsItemsLoadState)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                ProgressView()
            case .notLoading:
                GeometryReader { geometry in
                    ScrollView(.vertical) {
                        LazyVStack(spacing: 0) {
                            ForEach(newsItems.items.indices, id: \.self) { index in
                                NewsFeedItemView(
                                    item: newsItems.itemAt(index: Int32(index)),
                                    safeArea: geometry.safeAreaInsets
                                )
                            }
                        }
                    }
                    .ignoresSafeArea(.container, edges: .all)
                    .scrollIndicators(.hidden)
                    .scrollTargetBehavior(.paging)
                }
            case .error:
                VStack(alignment: .center, spacing: 8) {
                    Text(String(\.error_occurred))
                    Button(
                        action: { component.viewState.retry() },
                        label: { Text(String(\.retry)) }
                    )
                }
            }
        }
        .animation(
            .easeInOut(duration: 0.3),
            value: loadStates?.refresh
        )
    }
}

private struct NewsFeedItemView: View {
    let item: NewsItem?
    let safeArea: EdgeInsets

    var body: some View {
        AsyncImage(url: URL(string: item?.imgUrl ?? "")) { phase in
            switch phase {
            case .empty:
                ProgressView()
            case let .success(image):
                image.resizable().scaledToFill()
            case .failure:
                Image(systemName: "dollarsign").resizable().scaledToFill()
            @unknown default:
                EmptyView()
            }
        }
        .containerRelativeFrame(.vertical)
        .overlay {
            VStack(alignment: .leading) {
                Spacer()

                NewsFeedTextView(text: item?.title ?? "", font: .title)

                Spacer().frame(height: 8).fixedSize()

                if let source = item?.source {
                    NewsFeedTextView(text: source, font: .subheadline)
                }

                Spacer().frame(height: safeArea.bottom).fixedSize()
            }
            .containerRelativeFrame(.horizontal)
            .padding()
            .background {
                LinearGradient(
                    colors: [.clear, .black.opacity(0.7)],
                    startPoint: .center,
                    endPoint: .bottom
                )
            }
        }
    }
}

private struct NewsFeedTextView: View {
    let text: String
    let font: Font

    var body: some View {
        Text(text)
            .font(font)
            .lineLimit(nil)
            .multilineTextAlignment(.leading)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal)
            .shadow(color: .black.opacity(0.3), radius: 8, x: 0, y: 2)
            .shadow(color: .black.opacity(0.3), radius: 2, x: 0, y: 1)
            .foregroundStyle(.white)
    }
}
