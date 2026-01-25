import SwiftUI

struct EmptyListView: View {
    let icon: String
    let text: String

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: icon)
                .font(.system(size: 60))
                .foregroundColor(.secondary)

            Text(text)
                .font(.title3)
                .foregroundColor(.secondary)
        }
    }
}
