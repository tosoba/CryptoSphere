import SwiftUI

struct HistoryDateHeaderView: View {
    let date: LocalDate

    var body: some View {
        Text(date.formatted())
            .font(.subheadline)
            .fontWeight(.semibold)
            .foregroundColor(.secondary)
            .padding(8)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}
