import Shared
import SwiftUI

struct NewsFeedView: View {
    private let component: NewsFeedComponent

    init(component: NewsFeedComponent) {
        self.component = component
    }

    var body: some View {
        Text("NewsFeed")
    }
}
