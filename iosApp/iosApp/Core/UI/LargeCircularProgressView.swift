import SwiftUI

struct LargeCircularProgressView: View {
    var body: some View {
        ProgressView()
            .scaleEffect(1.5)
            .containerRelativeFrame([.vertical, .horizontal])
    }
}
