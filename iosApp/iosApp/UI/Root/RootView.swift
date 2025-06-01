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
            getTitle: { _ in "CryptoSphere" },
            onBack: component.onBackClicked
        ) { child in
            switch child { // TODO: skie for nicer switches
            case let child as RootComponentChild.Home: HomeView(child.component)
            case let child as RootComponentChild.Token: EmptyView() // TODO: TokenView
            default: EmptyView()
            }
        }
    }
}
