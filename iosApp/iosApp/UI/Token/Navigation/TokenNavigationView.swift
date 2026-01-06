import Shared
import SwiftUI

struct TokenNavigationView: View {
    private let component: TokenNavigationComponent

    init(component: TokenNavigationComponent) {
        self.component = component
    }

    var body: some View {
        Text(String(\.feed))
    }
}
