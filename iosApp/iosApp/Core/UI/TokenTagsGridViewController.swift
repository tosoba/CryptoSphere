import Shared
import SwiftUI

struct TokenTagsGridViewController: UIViewControllerRepresentable {
    let token: TokenItem
    let mainTokenTagNames: Set<String>
    let rowCount: Int32
    @Binding var measuredHeight: CGFloat
    
    @Environment(\.colorExtractorResultProvider) private var colorExtractorResultProvider

    func makeUIViewController(context _: Context) -> ContainerViewController {
        let containerVC = ContainerViewController()

        let composeVC = TokenTagsGridViewControllerKt.tokenTagsGridViewController(
            token: token,
            mainTokenTagNames: mainTokenTagNames,
            rowCount: rowCount,
            colorExtractorResultProvider: colorExtractorResultProvider,
            heightChanged: { height in
                measuredHeight = CGFloat(truncating: height) / UIScreen.main.scale
            },
            scrollStateChanged: { isScrolling in
                let scrolling = isScrolling.boolValue
                DispatchQueue.main.async {
                    containerVC.isScrolling = scrolling
                }
            }
        )

        containerVC.addChild(composeVC)
        containerVC.view.addSubview(composeVC.view)
        composeVC.view.frame = containerVC.view.bounds
        composeVC.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        composeVC.didMove(toParent: containerVC)

        return containerVC
    }

    func updateUIViewController(_: ContainerViewController, context _: Context) {}

    class ContainerViewController: UIViewController {
        var isScrolling: Bool = false {
            didSet {
                guard oldValue != isScrolling else { return }
                updateNavigationGestureEnabled()
            }
        }

        override func viewDidLoad() {
            super.viewDidLoad()
            view.backgroundColor = .clear
        }

        override func viewDidAppear(_ animated: Bool) {
            super.viewDidAppear(animated)
            updateNavigationGestureEnabled()
        }

        override func viewWillDisappear(_ animated: Bool) {
            super.viewWillDisappear(animated)
            navigationController?.interactivePopGestureRecognizer?.isEnabled = true
            if #available(iOS 26.0, *) {
                navigationController?.interactiveContentPopGestureRecognizer?.isEnabled = true
            }
        }

        private func updateNavigationGestureEnabled() {
            guard let navController = navigationController else { return }
            navController.interactivePopGestureRecognizer?.isEnabled = !isScrolling
            if #available(iOS 26.0, *) {
                navController.interactiveContentPopGestureRecognizer?.isEnabled = !isScrolling
            }
        }
    }
}
