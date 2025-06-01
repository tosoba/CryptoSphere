import Shared
import SwiftUI

struct HomeView: View {
    private let component: HomeComponent

    init(_ component: HomeComponent) {
        self.component = component
    }

    var body: some View {
        Text("Home")
    }
}
