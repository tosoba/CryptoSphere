import SwiftUI

struct ErrorOccurredCardView: View {
    let isVisible: Bool
    let onRetryClick: () -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass
    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        if isVisible {
            HStack(alignment: .center, spacing: 16) {
                Text(String(\.error_occurred))
                    .foregroundStyle(theme.color(\.onErrorContainer))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.leading, 16)

                Button(action: onRetryClick) {
                    Text(String(\.retry))
                        .foregroundStyle(theme.color(\.onErrorContainer))
                }
                .buttonStyle(.plain)
                .padding(.trailing, 16)
            }
            .padding(.vertical, 16)
            .background(theme.color(\.errorContainer))
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .frame(maxWidth: horizontalSizeClass == .regular ? .infinity : nil)
            .frame(width: horizontalSizeClass == .regular ? UIScreen.main.bounds.width * 0.5 : nil)
            .transition(.opacity.combined(with: .scale))
        }
    }
}
