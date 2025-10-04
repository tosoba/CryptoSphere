import Shared
import SwiftUI

struct TokenCarouselViewController: UIViewControllerRepresentable {
    let tokens: [TokenItem]
    let onItemClick: (TokenItem) -> Void

    func makeUIViewController(context _: Context) -> UIViewController {
        TokenCarouselControllerKt.TokenCarouselController(tokens: tokens, onItemClick: onItemClick)
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}
