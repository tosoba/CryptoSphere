import Shared
import SwiftUI

struct SearchView: View {
    private let component: SearchComponent

    init(component: SearchComponent) {
        self.component = component
    }

    var body: some View {
        Text("Search")
    }
}
