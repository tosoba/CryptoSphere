import SwiftUI

struct ListItemInfoView: View {
    let title: String
    let subtitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.headline)

            Text(subtitle)
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .lineLimit(1)
    }
}
