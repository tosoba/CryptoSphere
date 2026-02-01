import Shared
import SwiftUI

struct PriceItemView<S: Shape>: View {
    let token: TokenItem
    let shape: S
    let onClick: () -> Void

    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 16) {
                Text("\(token.cmcRank)")
                    .font(Font(resource: \.spacegrotesk_regular, withSizeOf: .title2))
                    .foregroundColor(theme.color(\.onSurfaceVariant))

                ListItemImageView(imageUrl: token.logoUrl)

                ListItemInfoView(title: token.name, subtitle: token.symbol)

                Spacer()

                priceInfo
            }
            .padding()
            .background(theme.color(\.surface))
            .clipShape(shape)
        }
        .buttonStyle(.plain)
    }

    private var priceInfo: some View {
        VStack(alignment: .trailing) {
            Text("$\(token.quote.price.fullDecimalFormat(significantDecimals: 3, signed: false))")
                .font(Font(resource: \.spacegrotesk_medium, withSizeOf: .headline))
                .foregroundColor(theme.color(\.onSurface))

            let percentChange24h = token.quote.percentChange24h
            Text(" \(percentChange24h.fullDecimalFormat(significantDecimals: 2, signed: true))% ")
                .valueChangeText(isPositive: percentChange24h >= 0)
        }
    }
}
