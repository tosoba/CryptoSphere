import SwiftUI

struct SearchBarView: View {
    let placeholder: String
    @Binding var query: String

    @Environment(\.cryptoSphereTheme) private var theme

    static let height: CGFloat = 44

    var body: some View {
        HStack {
            Image(systemName: "magnifyingglass")
                .foregroundColor(theme.color(\.onSurfaceVariant))

            TextField(placeholder, text: $query)
                .autocorrectionDisabled()

            Button(action: { query = "" }) {
                Image(systemName: "xmark.circle.fill")
                    .foregroundColor(theme.color(\.onSurfaceVariant))
            }
            .opacity(query.isEmpty ? 0 : 1)
            .disabled(query.isEmpty)
        }
        .padding(8)
        .background(theme.color(\.surface))
        .cornerRadius(8)
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(theme.color(\.onSurfaceVariant), lineWidth: 0.5)
        )
    }
}

struct ListTopSearchBarView: View {
    let placeholder: String
    @Binding var query: String
    let insets: EdgeInsets

    var body: some View {
        VStack {
            Spacer()
                .frame(height: insets.top)

            SearchBarView(placeholder: placeholder, query: $query)
                .padding(.horizontal)

            Spacer()
        }
    }
}
