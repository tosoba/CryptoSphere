import SwiftUI

struct ErrorListView: View {
    let text: String
    let onRetryClick: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.circle")
                .font(.system(size: 60))
                .foregroundColor(.secondary)

            Text(text)
                .font(.title3)
                .foregroundColor(.secondary)

            Button(
                action: onRetryClick,
                label: { Text(String(\.retry)) }
            )
        }
        .frame(maxHeight: .infinity)
    }
}
