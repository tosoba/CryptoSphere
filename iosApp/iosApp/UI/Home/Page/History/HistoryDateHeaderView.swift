import SwiftUI

struct HistoryDateHeaderView: View {
    let date: LocalDate

    var body: some View {
        Text(date.formatted())
            .font(.subheadline)
            .fontWeight(.semibold)
            .foregroundColor(.secondary)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}
