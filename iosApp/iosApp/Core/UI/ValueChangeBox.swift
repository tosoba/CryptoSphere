import SwiftUI

extension Text {
    func valueChangeBox(isPositive: Bool) -> some View {
        font(.subheadline)
            .padding(.horizontal, 4)
            .foregroundColor(isPositive ? .primary : .white)
            .background(isPositive ? .green : .red)
            .clipShape(RoundedRectangle(cornerRadius: 4))
    }
}
