import SwiftUI

struct HistoryItemView<S: Shape>: View {
    let imageUrl: String?
    let title: String
    let subtitle: String
    let visitedAt: LocalTime
    let shape: S
    let onClick: () -> Void
    let onDelete: () -> Void

    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 16) {
                ListItemImageView(imageUrl: imageUrl)

                ListItemInfoView(title: title, subtitle: subtitle)

                Spacer()

                Text(visitedAt.formatted())
                    .font(Font(resource: \.manrope_regular, withSizeOf: .caption1))
                    .foregroundColor(.secondary)
            }
            .padding()
            .background(theme.color(\.surface))
            .clipShape(shape)
        }
        .buttonStyle(.plain)
        .contextMenu {
            Button(role: .destructive, action: onDelete) {
                Label(String(\.delete), systemImage: "trash")
            }
        }
    }
}
