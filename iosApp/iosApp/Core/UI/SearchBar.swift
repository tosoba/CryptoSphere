import SwiftUI

struct SearchBar: View {
    let placeholder: String
    @Binding var query: String

    var body: some View {
        HStack {
            Image(systemName: "magnifyingglass")
                .foregroundColor(.secondary)

            TextField(placeholder, text: $query)
                .autocorrectionDisabled()

            Button(action: { query = "" }) {
                Image(systemName: "xmark.circle.fill")
                    .foregroundColor(.secondary)
            }
            .opacity(query.isEmpty ? 0 : 1)
            .disabled(query.isEmpty)
        }
        .padding(8)
        .background(Color(.systemGray6))
        .cornerRadius(8)
    }
}
