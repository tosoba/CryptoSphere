import Shared
import SwiftUI

struct TokenFeedParameterCardView: View {
    let parameter: TokenFeedParameter
    let cornerRadius: (top: CGFloat, bottom: CGFloat)
    
    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(String(parameter.label))
                .font(.caption)
                .foregroundColor(theme.color(\.onSurfaceVariant))

            HStack(spacing: 8) {
                Text(parameter.valueFormat(parameter.value))
                    .font(.headline)
                    .foregroundColor(theme.color(\.onSurface))

                if let valueChange = parameter.valueChange as? Double,
                   let valueChangeFormat = parameter.valueChangeFormat
                {
                    let changeText = valueChangeFormat(valueChange)
                    if !changeText.isEmpty {
                        Text(changeText)
                            .valueChangeText(isPositive: valueChange >= 0)
                    }
                }
            }
        }
        .lineLimit(1)
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
        .background(
            UnevenRoundedRectangle(
                topLeadingRadius: cornerRadius.top,
                bottomLeadingRadius: cornerRadius.bottom,
                bottomTrailingRadius: cornerRadius.bottom,
                topTrailingRadius: cornerRadius.top
            )
            .fill(theme.color(\.surface))
        )
    }
}
