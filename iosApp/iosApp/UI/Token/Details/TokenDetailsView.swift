import Shared
import SwiftUI

struct TokenDetailsView: View {
    private let component: TokenDetailsComponent

    init(component: TokenDetailsComponent) {
        self.component = component
    }

    var body: some View {
        Text("Token Details")
    }
}
