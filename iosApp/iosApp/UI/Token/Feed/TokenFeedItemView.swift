import Shared
import SwiftUI

struct TokenFeedItemView: View {
    let token: TokenItem
    let mainTokenTagNames: Set<String>
    let safeAreaInsets: EdgeInsets
    @Binding var tokenTagsGridMeasuredHeight: CGFloat

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass
    @Environment(\.verticalSizeClass) private var verticalSizeClass

    var body: some View {
        Group {
            if verticalSizeClass == .compact {
                compactLayout
            } else {
                regularLayout
            }
        }
        .padding(safeAreaInsets)
        .padding()
    }

    @ViewBuilder
    private var compactLayout: some View {
        HStack(spacing: 16) {
            VStack(alignment: .center, spacing: 16) {
                tokenLogo

                symbolWithRank

                if !token.tagNames.isEmpty {
                    TokenTagsGridViewController(
                        token: token,
                        mainTokenTagNames: mainTokenTagNames,
                        rowCount: 3,
                        measuredHeight: $tokenTagsGridMeasuredHeight
                    )
                    .frame(height: $tokenTagsGridMeasuredHeight.wrappedValue)
                }
            }
            .frame(maxWidth: .infinity)

            tokenFeedParameters
                .frame(maxWidth: .infinity)
        }
    }

    @ViewBuilder
    private var regularLayout: some View {
        VStack(spacing: 16) {
            tokenLogo

            symbolWithRank

            if !token.tagNames.isEmpty {
                TokenTagsGridViewController(
                    token: token,
                    mainTokenTagNames: mainTokenTagNames,
                    rowCount: Int32(
                        min(
                            token.tagNames.count,
                            verticalSizeClass == .regular && horizontalSizeClass == .regular ? 5 : 3
                        )
                    ),
                    measuredHeight: $tokenTagsGridMeasuredHeight
                )
                .frame(height: $tokenTagsGridMeasuredHeight.wrappedValue)
            }

            tokenFeedParameters
        }
    }

    @ViewBuilder
    private var tokenLogo: some View {
        AsyncImage(
            url: URL(string: token.logoUrl),
            transaction: Transaction(animation: .default)
        ) { phase in
            switch phase {
            case .empty:
                ProgressView()
            case let .success(image):
                image
                    .resizable()
                    .scaledToFill()
            default:
                Image(systemName: "bitcoinsign")
                    .resizable()
                    .scaledToFit()
            }
        }
        .frame(maxWidth: 108, maxHeight: 108)
        .aspectRatio(1, contentMode: .fit)
        .clipShape(RoundedRectangle(cornerRadius: 16))
    }

    @ViewBuilder
    private var symbolWithRank: some View {
        HStack(alignment: .firstTextBaseline, spacing: 8) {
            Text(" #\(token.cmcRank) ")
                .font(.subheadline)
                .fontWeight(.medium)
                .padding(.horizontal, 2)
                .background(Color(.systemBackground))
                .clipShape(RoundedRectangle(cornerRadius: 4))

            Text(token.symbol)
                .font(.largeTitle)
                .fontWeight(.medium)
        }
    }

    @ViewBuilder
    private var tokenFeedParameters: some View {
        GeometryReader { geometry in
            let totalHeight = geometry.size.height
            let cardHeight = calculateTokenParameterCardHeight()
            let parameters = Array(
                TokenFeedParameterKt.tokenFeedParameters(token: token)
                    .prefix(Int(totalHeight / cardHeight))
            )
            TokenFeedParameterCardsColumnView(parameters: parameters)
        }
    }

    private func calculateTokenParameterCardHeight() -> CGFloat {
        let verticalPadding: CGFloat = 8 * 2
        let vStackSpacing: CGFloat = 4

        let valueTextHeight = "A".size(
            withAttributes: [.font: UIFont.preferredFont(forTextStyle: .headline)]
        ).height
        let valueChangeTextHeight = "A".size(
            withAttributes: [.font: UIFont.preferredFont(forTextStyle: .subheadline)]
        ).height
        let hStackHeight = max(valueChangeTextHeight, valueTextHeight)

        return valueTextHeight + vStackSpacing + hStackHeight + verticalPadding
    }
}
