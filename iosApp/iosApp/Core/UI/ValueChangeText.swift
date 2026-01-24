import SwiftUI

extension Text {
    func valueChangeText(isPositive: Bool) -> some View {
        font(.subheadline)
            .padding(.horizontal, 2)
            .foregroundColor(isPositive ? .primary : .white)
            .background(isPositive ? .green : .red)
            .clipShape(RoundedRectangle(cornerRadius: 4))
    }
}
