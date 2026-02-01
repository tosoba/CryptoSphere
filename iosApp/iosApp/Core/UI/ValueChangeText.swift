import SwiftUI

extension Text {
    func valueChangeText(isPositive: Bool) -> some View {
        font(Font(\.spacegrotesk_regular, withSizeOf: .subheadline))
            .padding(.horizontal, 2)
            .foregroundColor(isPositive ? .black : .white)
            .background(isPositive ? .green : .red)
            .clipShape(RoundedRectangle(cornerRadius: 4))
    }
}
