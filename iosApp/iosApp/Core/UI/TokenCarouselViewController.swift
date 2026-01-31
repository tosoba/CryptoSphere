import Shared
import SwiftUI

struct TokenCarouselViewController: UIViewControllerRepresentable {
    let tokens: [TokenItem]
    let onItemClick: (TokenItem) -> Void
    @Binding var measuredHeight: CGFloat

    @Environment(\.colorExtractorResultProvider) private var colorExtractorResultProvider

    func makeUIViewController(context _: Context) -> UIViewController {
        TokenCarouselViewControllerKt.tokenCarouselViewController(
            tokens: tokens,
            colorExtractorResultProvider: colorExtractorResultProvider,
            onItemClick: onItemClick,
            heightChanged: { height in
                measuredHeight = CGFloat(truncating: height) / UIScreen.main.scale
            }
        )
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}
