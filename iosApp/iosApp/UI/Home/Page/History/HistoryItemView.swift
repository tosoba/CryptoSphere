import SwiftUI

struct HistoryItemView: View {
    let imageUrl: String?
    let title: String
    let subtitle: String
    let visitedAt: LocalTime

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
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            .padding()
            .background(theme.color(\.surface))
            .clipShape(RoundedRectangle(cornerRadius: 16))
        }
        .buttonStyle(.plain)
        .contextMenu {
            Button(role: .destructive, action: onDelete) {
                Label(String(\.delete), systemImage: "trash")
            }
        }
    }
}
