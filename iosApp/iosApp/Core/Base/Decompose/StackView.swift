import Shared
import SwiftUI
import UIKit

struct StackView<T: AnyObject, Content: View>: View {
    @StateValue private var stackValue: ChildStack<AnyObject, T>
    private let onBack: (_ toIndex: Int32) -> Void
    @ViewBuilder private let content: (T) -> Content

    init(
        stackValue: StateValue<ChildStack<AnyObject, T>>,
        onBack: @escaping (_: Int32) -> Void,
        @ViewBuilder content: @escaping (T) -> Content
    ) {
        _stackValue = stackValue
        self.onBack = onBack
        self.content = content
    }

    private var stack: [Child<AnyObject, T>] { stackValue.items }

    var body: some View {
        NavigationStack(
            path: Binding(
                get: { stack.dropFirst() },
                set: { updatedPath in onBack(Int32(updatedPath.count)) }
            )
        ) {
            content(stack.first!.instance!)
                .navigationDestination(for: Child<AnyObject, T>.self) {
                    content($0.instance!)
                }
        }
    }
}
