import Foundation
import Shared

extension LocalDate {
    func formatted() -> String {
        guard let date = Calendar.current.date(
            from: DateComponents(
                year: Int(year),
                month: Int(month.ordinal + 1),
                day: Int(day)
            )
        ) else {
            return description()
        }

        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .none
        return formatter.string(from: date)
    }
}

extension LocalTime {
    func formatted() -> String {
        guard let date = Calendar.current.date(
            from: DateComponents(
                hour: Int(hour),
                minute: Int(minute),
                second: Int(second)
            )
        ) else {
            return description()
        }

        let formatter = DateFormatter()
        formatter.dateStyle = .none
        formatter.timeStyle = .medium
        return formatter.string(from: date)
    }
}
