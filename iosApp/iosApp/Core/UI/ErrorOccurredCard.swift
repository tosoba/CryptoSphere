import SwiftUI

struct ErrorOccurredCard: View {
    let isVisible: Bool
    let onRetryClick: () -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        if isVisible {
            HStack(alignment: .center, spacing: 16) {
                Text(String(\.error_occurred))
                    .foregroundStyle(.onErrorContainer)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.leading, 16)

                Button(action: onRetryClick) {
                    Text(String(\.retry))
                        .foregroundStyle(.onErrorContainer)
                }
                .buttonStyle(.plain)
                .padding(.trailing, 16)
            }
            .padding(.vertical, 12)
            .background(Color.errorContainer)
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .frame(
                maxWidth: horizontalSizeClass == .regular ? .infinity : nil
            )
            .frame(
                width: horizontalSizeClass == .regular ?
                    UIScreen.main.bounds.width * 0.5 : nil
            )
            .transition(.opacity.combined(with: .scale))
        }
    }
}

private extension Color {
    static let errorContainer = Color(red: 0.95, green: 0.84, blue: 0.84)
    static let onErrorContainer = Color(red: 0.41, green: 0.00, blue: 0.00)
}

private extension ShapeStyle where Self == Color {
    static var errorContainer: Color { .errorContainer }
    static var onErrorContainer: Color { .onErrorContainer }
}
