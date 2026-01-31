import Shared
import SwiftUI

struct RootView: View {
    private let component: RootComponent

    @StateObject @KotlinOptionalStateFlow private var colorExtractorResult: ColorExtractor.Result?

    @Environment(\.colorScheme) private var colorScheme

    init(component: RootComponent) {
        self.component = component

        _colorExtractorResult = .init(component.colorExtractorResult)
    }

    var body: some View {
        StackView(
            stackValue: StateValue(component.stack),
            onBack: component.onBackClicked
        ) { child in
            switch onEnum(of: child) {
            case let .home(homeChild):
                HomeView(homeChild.component)
            case .tokenNavigation:
                EmptyView() // Used only on android.
            case let .tokenFeed(tokenFeedChild):
                TokenFeedView(tokenFeedChild.component)
            }
        }
        .environment(
            \.cryptoSphereTheme,
            CryptoSphereTheme(colorScheme: colorScheme, colorExtractorResult: colorExtractorResult)
        )
        .environment(\.colorExtractorResultProvider, component)
    }
}
