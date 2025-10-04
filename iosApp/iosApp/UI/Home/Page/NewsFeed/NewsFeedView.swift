import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent

    @StateObject @KotlinStateFlow private var newsItems: NewsItemsSnapshotList
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?
    @StateObject @KotlinStateFlow private var relatedTokens: [TokenItem]

    @State private var currentNewsItemIndex: Int? = 0

    init(component: NewsFeedComponent) {
        self.component = component

        _newsItems = .init(component.viewState.newsItemsSnapshotList)
        _loadStates = .init(component.viewState.newsItemsLoadState)
        _relatedTokens = .init(component.relatedTokens)
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
                                    relatedTokens: relatedTokens,
                                    safeArea: geometry.safeAreaInsets
                                )
                                .id(index)
                            }
                        }
                        .scrollTargetLayout()
                    }
                    .ignoresSafeArea(.container, edges: .all)
                    .scrollIndicators(.hidden)
                    .scrollTargetBehavior(.paging)
                    .scrollPosition(id: $currentNewsItemIndex)
                    .onScrollPhaseChange { _, newPhase in
                        if newPhase != .idle { return }
                        guard let index = currentNewsItemIndex else { return }
                        guard let item = newsItems.itemAt(index: Int32(index)) else { return }
                        component.onCurrentItemChanged(item: item)
                    }
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
    let relatedTokens: [TokenItem]
    let safeArea: EdgeInsets
    
    @State var tokenCarouselMeasuredHeight: CGFloat = 1000

    var body: some View {
        AsyncImage(url: URL(string: item?.imgUrl ?? "")) { phase in
            switch phase {
            case .empty:
                ProgressView()
            case let .success(image):
                image.resizable().scaledToFill()
            case .failure:
                let screenSize = UIScreen.main.bounds.size
                let size = min(screenSize.width, screenSize.height) / 2
                Image(systemName: "bitcoinsign").resizable().scaledToFill().frame(width: size, height: size)
            @unknown default:
                EmptyView()
            }
        }
        .containerRelativeFrame(.vertical)
        .overlay {
            HStack(alignment: .bottom) {
                VStack(alignment: .leading) {
                    Spacer()

                    NewsFeedItemTextView(text: item?.title ?? "", font: .title)

                    if let source = item?.source {
                        Spacer().frame(height: 8).fixedSize()
                        
                        NewsFeedItemTextView(text: source, font: .subheadline)
                            .transition(.move(edge: .bottom).combined(with: .opacity))
                    }

                    if !relatedTokens.isEmpty {
                        Spacer().frame(height: 8).fixedSize()
                        
                        TokenCarouselViewController(
                            tokens: relatedTokens,
                            onItemClick: { _ in },
                            measuredHeight: $tokenCarouselMeasuredHeight
                        )
                        .frame(height: $tokenCarouselMeasuredHeight.wrappedValue)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                    }

                    Spacer().frame(height: safeArea.bottom).fixedSize()
                }
                .animation(.easeInOut(duration: 0.3), value: !relatedTokens.isEmpty)
                .animation(.easeInOut(duration: 0.3), value: item?.source != nil)

                Spacer(minLength: 0)

                VStack(alignment: .center) {
                    Button(action: {}) {
                        Image(systemName: "paperplane")
                            .font(.title2)
                    }
                    .buttonStyle(.bordered)
                    .clipShape(.circle)

                    Spacer().frame(height: 24).fixedSize()

                    Button(action: {}) {
                        Image(systemName: "star")
                            .font(.title2)
                    }
                    .buttonStyle(.bordered)
                    .clipShape(.circle)

                    Spacer().frame(height: 24).fixedSize()

                    Button(action: {}) {
                        Image(systemName: "safari")
                            .font(.largeTitle)
                    }
                    .buttonStyle(.borderedProminent)
                    .clipShape(.circle)

                    Spacer().frame(height: safeArea.bottom).fixedSize()
                }
                .foregroundColor(.white)
                .padding(.horizontal)
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

private struct NewsFeedItemTextView: View {
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
