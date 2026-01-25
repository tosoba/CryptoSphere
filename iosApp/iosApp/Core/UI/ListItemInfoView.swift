import SwiftUI

struct ListItemInfoView: View {
    let title: String
    let subtitle: String

    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.headline)
                .foregroundColor(theme.color(\.onSurface))

            Text(subtitle)
                .font(.subheadline)
                .foregroundColor(theme.color(\.onSurfaceVariant))
        }
        .lineLimit(1)
    }
}
