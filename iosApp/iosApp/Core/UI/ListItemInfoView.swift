import SwiftUI

struct ListItemInfoView: View {
    let title: String
    let subtitle: String

    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(Font(resource: \.spacegrotesk_medium, withSizeOf: .headline))
                .foregroundColor(theme.color(\.onSurface))

            Text(subtitle)
                .font(Font(resource: \.spacegrotesk_medium, withSizeOf: .subheadline))
                .foregroundColor(theme.color(\.onSurfaceVariant))
        }
        .lineLimit(1)
    }
}
