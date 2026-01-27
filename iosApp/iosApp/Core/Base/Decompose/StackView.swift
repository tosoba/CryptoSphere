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
        StackViewUIKit(
            components: stack.map { $0.instance! },
            onBack: onBack,
            childContent: content
        )
        .ignoresSafeArea()
    }
}

private struct StackViewUIKit<T: AnyObject, Content: View>: UIViewControllerRepresentable {
    var components: [T]
    var onBack: (_ toIndex: Int32) -> Void
    var childContent: (T) -> Content

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIViewController(context: Context) -> UINavigationController {
        context.coordinator.syncChanges(self)

        let navigationController = UINavigationController(
            rootViewController: context.coordinator.viewControllers.first!
        )
        let appearance = UINavigationBarAppearance()
        appearance.configureWithDefaultBackground()
        appearance.shadowColor = .clear
        navigationController.navigationBar.standardAppearance = appearance
        navigationController.navigationBar.scrollEdgeAppearance = appearance
        navigationController.navigationBar.compactAppearance = appearance
        return navigationController
    }

    func updateUIViewController(_ navigationController: UINavigationController, context: Context) {
        context.coordinator.syncChanges(self)
        navigationController.setViewControllers(context.coordinator.viewControllers, animated: true)
    }

    private func createViewController(_ component: T, _ coordinator: Coordinator) -> NavigationItemHostingController {
        let controller = NavigationItemHostingController(rootView: childContent(component))
        controller.coordinator = coordinator
        controller.component = component
        controller.onBack = onBack
        return controller
    }

    class Coordinator: NSObject {
        var parent: StackViewUIKit<T, Content>
        var viewControllers = [NavigationItemHostingController]()
        var preservedComponents = [T]()

        init(_ parent: StackViewUIKit<T, Content>) {
            self.parent = parent
        }

        func syncChanges(_ parent: StackViewUIKit<T, Content>) {
            self.parent = parent
            let count = max(preservedComponents.count, parent.components.count)

            for i in 0 ..< count {
                if i >= parent.components.count {
                    viewControllers.removeLast()
                } else if i >= preservedComponents.count {
                    viewControllers.append(parent.createViewController(parent.components[i], self))
                } else if parent.components[i] !== preservedComponents[i] {
                    viewControllers[i] = parent.createViewController(parent.components[i], self)
                }
            }

            preservedComponents = parent.components
        }
    }

    class NavigationItemHostingController: UIHostingController<Content> {
        fileprivate(set) weak var coordinator: Coordinator?
        fileprivate(set) var component: T?
        fileprivate(set) var onBack: ((_ toIndex: Int32) -> Void)?

        override func viewDidAppear(_ animated: Bool) {
            super.viewDidAppear(animated)

            guard let components = coordinator?.preservedComponents else { return }
            guard let index = components.firstIndex(where: { $0 === component }) else { return }

            if index < components.count - 1 {
                onBack?(Int32(index))
            }
        }
    }
}
