import Shared
import SwiftUI

struct TokenFeedParameterCardView<S: Shape>: View {
    let parameter: TokenFeedParameter
    let shape: S

    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(String(parameter.label))
                .font(Font(resource: \.manrope_medium, withSizeOf: .caption1))
                .foregroundColor(theme.color(\.onSurfaceVariant))

            HStack(spacing: 8) {
                Text(parameter.valueFormat(parameter.value))
                    .font(Font(resource: \.spacegrotesk_medium, withSizeOf: .headline))
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
        .background(shape.fill(theme.color(\.surface)))
    }
}
