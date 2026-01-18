import SwiftUI

struct HistoryDateHeaderView: View {
    let date: LocalDate

    var body: some View {
        Text(formatDate(date))
            .font(.subheadline)
            .fontWeight(.semibold)
            .foregroundColor(.secondary)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .frame(maxWidth: .infinity, alignment: .leading)
    }

    private func formatDate(_ localDate: LocalDate) -> String {
        guard let date = Calendar.current.date(
            from: DateComponents(
                year: Int(localDate.year),
                month: Int(localDate.month.ordinal + 1),
                day: Int(localDate.day)
            )
        ) else {
            return localDate.description()
        }

        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .none
        return formatter.string(from: date)
    }
}
