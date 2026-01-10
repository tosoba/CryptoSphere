import Shared
import SwiftUI

struct NewsFeedItemView: View {
    let item: NewsFeedItem
    let safeArea: EdgeInsets
    @Binding var tokenCarouselMeasuredHeight: CGFloat
    let onTokenCarouselItemClick: (Int32, TokenCarouselConfig) -> Void

    private var newsInformationBody: some View {
        VStack(alignment: .leading) {
            NewsFeedItemTextView(text: item.news.title, font: .title)

            NewsFeedItemTextView(text: item.news.source, font: .subheadline)
                .padding(.top, 8)

            if !item.relatedTokens.isEmpty {
                TokenCarouselViewController(
                    tokens: item.relatedTokens,
                    onItemClick: { token in
                        onTokenCarouselItemClick(token.id, TokenCarouselConfig())
                    },
                    measuredHeight: $tokenCarouselMeasuredHeight
                )
                .frame(height: $tokenCarouselMeasuredHeight.wrappedValue)
                .padding(.top, 8)
            }

            Spacer().frame(height: safeArea.bottom)
        }
        .padding(.vertical)
        .padding(.leading, 8)
    }

    private var newsActionsBody: some View {
        VStack(alignment: .trailing) {
            Button(action: { PlatformContext.shared.shareUrl(url: item.news.link) }) {
                Image(systemName: "paperplane")
                    .font(.title2)
            }
            .buttonStyle(.bordered)
            .feedShadow()
            .clipShape(.circle)

            Button(action: { PlatformContext.shared.openUrl(url: item.news.link) }) {
                Image(systemName: "safari")
                    .font(.largeTitle)
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.large)
            .feedShadow()
            .clipShape(.circle)
            .padding(.top, 24)

            Spacer().frame(height: safeArea.bottom)
        }
        .foregroundColor(.white)
        .padding(.vertical)
        .padding(.trailing, 8)
    }

    var body: some View {
        AsyncImage(
            url: URL(string: item.news.imgUrl ?? ""),
            transaction: Transaction(animation: .default)
        ) { phase in
            switch phase {
            case .empty:
                ProgressView().scaleEffect(1.5).tint(.white)
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
                    newsInformationBody
                    Spacer().frame(width: 16)
                    newsActionsBody
                    Spacer().frame(width: safeArea.trailing)
                }
                .background {
                    Rectangle()
                        .fill(.ultraThinMaterial)
                        .mask {
                            VStack(spacing: 0) {
                                LinearGradient(
                                    colors: [
                                        .black.opacity(0),
                                        .black.opacity(0.75),
                                        .black,
                                    ],
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
