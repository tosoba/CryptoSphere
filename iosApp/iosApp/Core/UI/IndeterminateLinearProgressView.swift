import Shared
import SwiftUI

struct IndeterminateLinearProgressView: View {
    let isVisible: Bool

    @State private var width: CGFloat = 0
    @State private var offset: CGFloat = 0

    var body: some View {
        Rectangle()
            .foregroundColor(.gray.opacity(0.15))
            .readWidth()
            .overlay(
                Rectangle()
                    .foregroundColor(.accentColor)
                    .frame(width: width * 0.26, height: 6)
                    .clipShape(Capsule())
                    .offset(x: -width * 0.6, y: 0)
                    .offset(x: width * 1.2 * offset, y: 0)
                    .animation(.default.repeatForever().speed(0.5), value: offset)
                    .onAppear {
                        withAnimation {
                            self.offset = 1
                        }
                    }
            )
            .clipShape(Capsule())
            .opacity(isVisible ? 1 : 0)
            .animation(.default, value: isVisible)
            .frame(height: 6)
            .onPreferenceChange(WidthPreferenceKey.self) { width in
                self.width = width
            }
    }
}

private struct WidthPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat = 0

    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = max(value, nextValue())
    }
}

private struct ReadWidthModifier: ViewModifier {
    private var sizeView: some View {
        GeometryReader { geometry in
            Color.clear.preference(key: WidthPreferenceKey.self, value: geometry.size.width)
        }
    }

    func body(content: Content) -> some View {
        content.background(sizeView)
    }
}

private extension View {
    func readWidth() -> some View {
        modifier(ReadWidthModifier())
    }
}

struct IndeterminateLinearProgressViewOverlayModifier: ViewModifier {
    let loadState: LoadState?
    let alignment: Alignment

    func body(content: Content) -> some View {
        content.overlay(alignment: alignment) {
            let isVisible = if case .loading = onEnum(of: loadState) { true } else { false }
            IndeterminateLinearProgressView(isVisible: isVisible)
        }
    }
}

extension View {
    func indeterminateLinearProgressViewOverlay(
        loadState: LoadState?,
        alignment: Alignment = .bottom
    ) -> some View {
        modifier(IndeterminateLinearProgressViewOverlayModifier(loadState: loadState, alignment: alignment))
    }
}
