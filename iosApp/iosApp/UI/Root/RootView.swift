import Shared
import SwiftUI

struct RootView: View {
    private let component: RootComponent

    init(_ component: RootComponent) {
        self.component = component
    }

    var body: some View {
        StackView(
            stackValue: StateValue(component.stack),
            getTitle: { _ in "" },
            onBack: component.onBackClicked
        ) { child in
            switch onEnum(of: child) {
            case let .home(homeChild):
                HomeView(homeChild.component)
            case .tokenNavigation(_):
                EmptyView() // used only on android
            case let .tokenFeed(tokenFeedChild):
                TokenFeedView(tokenFeedChild.component)
            }
        }
    }
}
