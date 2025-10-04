import Shared
import SwiftUI

struct TokenCarouselViewController: UIViewControllerRepresentable {
    let tokens: [TokenItem]
    let onItemClick: (TokenItem) -> Void
    @Binding var measuredHeight: CGFloat

    func makeUIViewController(context _: Context) -> UIViewController {
        TokenCarouselViewControllerKt.TokenCarouselViewController(
            tokens: tokens,
            onItemClick: onItemClick,
            heightChanged: { height in
                measuredHeight = CGFloat(truncating: height) / UIScreen.main.scale
            }
        )
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}
