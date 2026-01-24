import Shared
import SwiftUI

struct TokenFeedParameterCardView: View {
    let parameter: TokenFeedParameter
    let cornerRadius: (top: CGFloat, bottom: CGFloat)

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(String(parameter.label))
                .font(.caption)
                .foregroundStyle(.secondary)

            HStack(spacing: 8) {
                Text(parameter.valueFormat(parameter.value))
                    .font(.headline)

                if let valueChange = parameter.valueChange as? Double,
                   let valueChangeFormat = parameter.valueChangeFormat
                {
                    let changeText = valueChangeFormat(valueChange)
                    if !changeText.isEmpty {
                        Text(changeText)
                            .valueChangeBox(isPositive: valueChange >= 0)
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
            .fill(Color(uiColor: .systemBackground))
        )
    }
}
