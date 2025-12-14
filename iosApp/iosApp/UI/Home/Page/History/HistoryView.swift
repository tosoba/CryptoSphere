import Shared
import SwiftUI

struct HistoryView: View {
    private let component: HistoryComponent

    init(component: HistoryComponent) {
        self.component = component
    }

    var body: some View {
        Text(String(\.history))
    }
}
