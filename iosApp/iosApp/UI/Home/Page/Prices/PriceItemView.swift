import Shared
import SwiftUI

struct PriceItemView: View {
    let token: TokenItem

    var body: some View {
        HStack(spacing: 16) {
            Text("\(token.cmcRank)")
                .font(.title2)
                .foregroundColor(.primary)

            AsyncImage(
                url: URL(string: token.logoUrl),
                content: { image in image.resizable().aspectRatio(contentMode: .fit) },
                placeholder: { ProgressView() }
            )
            .frame(width: 48, height: 48)
            .clipShape(RoundedRectangle(cornerRadius: 8))

            tokenInfo

            Spacer()

            priceInfo
        }
        .padding(16)
        .background(Color(.systemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    private var tokenInfo: some View {
        VStack(alignment: .leading) {
            Text(token.symbol)
                .font(.headline)
                .fontWeight(.medium)
                .foregroundColor(.primary)

            Text(token.name)
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .lineLimit(1)
    }

    private var priceInfo: some View {
        VStack(alignment: .trailing) {
            Text("$\(token.quote.price.fullDecimalFormat(significantDecimals: 3, signed: false))")
                .font(.headline)
                .fontWeight(.medium)
                .foregroundColor(.primary)

            let percentChange24h = token.quote.percentChange24h
            let valueChangePositive = percentChange24h >= 0

            Text("\(percentChange24h.fullDecimalFormat(significantDecimals: 2, signed: true))%")
                .font(.subheadline)
                .padding(.horizontal, 4)
                .foregroundColor(valueChangePositive ? .primary : .white)
                .background(valueChangePositive ? Color.green : Color.red)
                .clipShape(RoundedRectangle(cornerRadius: 4))
        }
    }
}
