import Shared
import SwiftUI

struct TokenFeedView: View {
    private let component: TokenFeedComponent

    init(component: TokenFeedComponent) {
        self.component = component
    }

    var body: some View {
        Text(String(\.feed))
    }
}
