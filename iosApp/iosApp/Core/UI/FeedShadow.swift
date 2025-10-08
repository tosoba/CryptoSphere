import SwiftUI

struct FeedShadowModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .shadow(color: .black.opacity(0.5), radius: 8, x: 0, y: 2)
            .shadow(color: .black.opacity(0.5), radius: 2, x: 0, y: 1)
    }
}

extension View {
    func feedShadow() -> some View {
        modifier(FeedShadowModifier())
    }
}
