import Shared
import SwiftUI

struct NewsFeedItemView: View {
    let item: NewsFeedItem
    let insets: EdgeInsets
    @Binding var tokenCarouselMeasuredHeight: CGFloat

    let onTokenCarouselItemClick: (TokenItem) -> Void
    let onShareClick: () -> Void
    let onOpenClick: () -> Void

    var body: some View {
        AsyncImage(
            url: item.news.imgUrl.flatMap { URL(string: $0) },
            transaction: Transaction(animation: .default)
        ) { phase in
            switch phase {
            case .empty:
                LargeCircularProgressView()
                    .tint(.white)
                    .frame(maxHeight: .infinity)
            case let .success(image):
                image
                    .resizable()
                    .scaledToFill()
            default:
                placeholderImageView
            }
        }
        .containerRelativeFrame([.vertical, .horizontal])
        .background(.black)
        .overlay {
            contentView
        }
    }

    @ViewBuilder
    private var contentView: some View {
        VStack {
            Spacer().frame(height: insets.top)

            Spacer()

            HStack(alignment: .bottom) {
                Spacer().frame(width: insets.leading)
                informationView
                Spacer().frame(width: 16)
                actionsView
                Spacer().frame(width: insets.trailing)
            }
            .background {
                Rectangle()
                    .fill(.thinMaterial)
                    .mask {
                        LinearGradient(
                            colors: [
                                .black.opacity(0),
                                .black.opacity(0.75),
                                .black,
                            ],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    }
            }
        }
    }

    @ViewBuilder
    private var informationView: some View {
        VStack(alignment: .leading) {
            textView(
                text: item.news.title,
                font: Font(\.spacegrotesk_medium, withSizeOf: .title1)
            )

            Spacer().frame(height: 8)

            textView(
                text: item.news.source,
                font: Font(\.manrope_regular, withSizeOf: .subheadline)
            )

            if !item.relatedTokens.isEmpty {
                TokenCarouselViewController(
                    tokens: item.relatedTokens,
                    onItemClick: onTokenCarouselItemClick,
                    measuredHeight: $tokenCarouselMeasuredHeight
                )
                .frame(height: $tokenCarouselMeasuredHeight.wrappedValue)
                .padding(.top, 8)
            }

            Spacer().frame(height: insets.bottom)
        }
        .padding(.vertical)
        .padding(.leading, 8)
    }

    @ViewBuilder
    private var actionsView: some View {
        VStack(alignment: .trailing) {
            Button(action: onShareClick) {
                Image(systemName: "paperplane")
                    .font(.title2)
            }
            .buttonStyle(.bordered)
            .feedShadow()
            .clipShape(.circle)

            Button(action: onOpenClick) {
                Image(systemName: "safari")
                    .font(.largeTitle)
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.large)
            .feedShadow()
            .clipShape(.circle)
            .padding(.top, 16)

            Spacer().frame(height: insets.bottom)
        }
        .foregroundColor(.white)
        .padding(.vertical)
        .padding(.trailing, 8)
    }

    @ViewBuilder
    private var placeholderImageView: some View {
        let screenSize = UIScreen.main.bounds.size
        let size = min(screenSize.width, screenSize.height) / 2

        Image(systemName: "bitcoinsign")
            .resizable()
            .scaledToFill()
            .frame(width: size, height: size)
            .foregroundStyle(.white)
    }

    @ViewBuilder
    private func textView(text: String, font: Font) -> some View {
        Text(text)
            .font(font)
            .lineLimit(5)
            .multilineTextAlignment(.leading)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal)
            .feedShadow()
            .foregroundStyle(.white)
    }
}
