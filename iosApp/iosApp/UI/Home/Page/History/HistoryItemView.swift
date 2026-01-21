import SwiftUI

struct HistoryItemView: View {
    let imageUrl: String?
    let title: String
    let subtitle: String
    let visitedAt: LocalTime
    let onClick: () -> Void
    let onDelete: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 16) {
                AsyncImage(url: URL(string: imageUrl ?? "")) { image in
                    image.resizable().aspectRatio(contentMode: .fit)
                } placeholder: {
                    Color(.systemGray4)
                }
                .frame(width: 48, height: 48)
                .clipShape(RoundedRectangle(cornerRadius: 12))

                VStack(alignment: .leading, spacing: 4) {
                    Text(title)
                        .font(.headline)

                    Text(subtitle)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                .lineLimit(1)

                Spacer()

                Text(visitedAt.formatted())
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            .padding(16)
            .background(Color(.systemBackground))
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
        .contextMenu {
            Button(role: .destructive, action: onDelete) {
                Label(String(\.delete), systemImage: "trash")
            }
        }
    }
}
