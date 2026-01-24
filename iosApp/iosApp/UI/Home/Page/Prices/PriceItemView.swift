import Shared
import SwiftUI

struct PriceItemView: View {
    let token: TokenItem
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 16) {
                Text("\(token.cmcRank)")
                    .font(.title2)
                    .foregroundColor(.primary)

                ListItemImageView(imageUrl: token.logoUrl)

                ListItemInfoView(title: token.name, subtitle: token.symbol)

                Spacer()

                priceInfo
            }
            .padding()
            .background(Color(.systemBackground))
            .clipShape(RoundedRectangle(cornerRadius: 16))
        }
        .buttonStyle(.plain)
    }

    private var priceInfo: some View {
        VStack(alignment: .trailing) {
            Text("$\(token.quote.price.fullDecimalFormat(significantDecimals: 3, signed: false))")
                .font(.headline)
                .fontWeight(.medium)
                .foregroundColor(.primary)

            let percentChange24h = token.quote.percentChange24h
            Text(" \(percentChange24h.fullDecimalFormat(significantDecimals: 2, signed: true))% ")
                .valueChangeText(isPositive: percentChange24h >= 0)
        }
    }
}
