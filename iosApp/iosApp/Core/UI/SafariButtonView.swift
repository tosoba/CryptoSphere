import SwiftUI

struct SafariButtonView: View {
    let action: () -> Void

    @Environment(\.cryptoSphereTheme) private var theme

    var body: some View {
        Button(action: action) {
            Image(systemName: "safari")
                .font(.largeTitle)
        }
        .buttonStyle(.borderedProminent)
        .tint(theme.color(\.primary))
        .controlSize(.large)
        .feedShadow()
        .clipShape(.circle)
    }
}
