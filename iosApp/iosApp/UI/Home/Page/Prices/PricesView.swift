import Shared
import SwiftUI

struct PricesView: View {
    private let component: PricesComponent

    init(component: PricesComponent) {
        self.component = component
    }

    var body: some View {
        Text(String(\.prices))
    }
}
