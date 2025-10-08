import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent

    @StateObject @KotlinStateFlow private var newsFeedItems: [NewsFeedItem]
    @StateObject @KotlinOptionalStateFlow private var loadStates: CombinedLoadStates?
    
    @State private var tokenCarouselMeasuredHeight: CGFloat = 90

    init(component: NewsFeedComponent) {
        self.component = component

        _newsFeedItems = .init(component.viewState.newsFeedItemsSnapshotList)
        _loadStates = .init(component.viewState.newsItemsLoadStates)
    }

    var body: some View {
        ZStack {
            switch onEnum(of: loadStates?.refresh) {
            case .loading, .none:
                ProgressView()
                    .foregroundStyle(.white)
                    .containerRelativeFrame([.vertical, .horizontal])
                    .background(.black)
            case .notLoading:
                GeometryReader { geometry in
                    ScrollView(.vertical) {
                        LazyVStack(spacing: 0) {
                            ForEach(newsFeedItems, id: \.news.id) { item in
                                NewsFeedItemView(
                                    item: item,
                                    safeArea: geometry.safeAreaInsets,
                                    tokenCarouselMeasuredHeight: $tokenCarouselMeasuredHeight
                                )
                            }
                        }
                        .scrollTargetLayout()
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
    let item: NewsFeedItem
    let safeArea: EdgeInsets

    @Binding var tokenCarouselMeasuredHeight: CGFloat

    var body: some View {
        AsyncImage(url: URL(string: item.news.imgUrl ?? "")) { phase in
            switch phase {
            case .empty:
                ProgressView()
            case let .success(image):
                image.resizable().scaledToFill()
            case .failure:
                NewsFeedItemPlaceholderImageView()
            @unknown default:
                EmptyView()
            }
        }
        .containerRelativeFrame([.vertical, .horizontal])
        .background(.black)
        .overlay {
            VStack {
                Spacer().frame(height: safeArea.top)

                Spacer()

                HStack(alignment: .bottom) {
                    Spacer().frame(width: safeArea.leading)

                    VStack(alignment: .leading) {
                        NewsFeedItemTextView(text: item.news.title, font: .title)

                        NewsFeedItemTextView(text: item.news.source, font: .subheadline)
                            .padding(.top, 8)
                            .transition(.scale.combined(with: .opacity))

                        if !item.relatedTokens.isEmpty {
                            TokenCarouselViewController(
                                tokens: item.relatedTokens,
                                onItemClick: { _ in },
                                measuredHeight: $tokenCarouselMeasuredHeight
                            )
                            .frame(height: $tokenCarouselMeasuredHeight.wrappedValue)
                            .padding(.top, 8)
                        }

                        Spacer().frame(height: safeArea.bottom)
                    }
                    .padding(.vertical)
                    .padding(.leading, 8)

                    Spacer().frame(width: 16)

                    VStack(alignment: .center) {
                        Button(action: {}) {
                            Image(systemName: "paperplane")
                                .font(.title2)
                        }
                        .buttonStyle(.bordered)
                        .feedShadow()
                        .clipShape(.circle)

                        Button(action: {}) {
                            Image(systemName: "star")
                                .font(.title2)
                        }
                        .buttonStyle(.bordered)
                        .feedShadow()
                        .clipShape(.circle)
                        .padding(.top, 24)

                        Button(action: {}) {
                            Image(systemName: "safari")
                                .font(.largeTitle)
                        }
                        .buttonStyle(.borderedProminent)
                        .feedShadow()
                        .clipShape(.circle)
                        .padding(.top, 24)

                        Spacer().frame(height: safeArea.bottom)
                    }
                    .foregroundColor(.white)
                    .padding(.vertical)
                    .padding(.trailing, 8)

                    Spacer().frame(width: safeArea.trailing)
                }
                .background {
                    Rectangle()
                        .fill(.ultraThinMaterial)
                        .mask {
                            VStack(spacing: 0) {
                                LinearGradient(
                                    colors: [.black.opacity(0), .black.opacity(0.9), .black.opacity(0.95), .black],
                                    startPoint: .top,
                                    endPoint: .bottom
                                )
                                Rectangle()
                            }
                        }
                }
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
            .fontWeight(.medium)
            .lineLimit(5)
            .multilineTextAlignment(.leading)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal)
            .feedShadow()
            .foregroundStyle(.white)
    }
}

private struct NewsFeedItemPlaceholderImageView: View {
    var body: some View {
        let screenSize = UIScreen.main.bounds.size
        let size = min(screenSize.width, screenSize.height) / 2
        Image(systemName: "bitcoinsign")
            .resizable()
            .scaledToFill()
            .frame(width: size, height: size)
            .foregroundStyle(.white)
    }
}
