import Shared
import SwiftUI

struct TokenTagsGridViewController: UIViewControllerRepresentable {
    let token: TokenItem
    let mainTokenTagNames: Set<String>
    let rowCount: Int32
    @Binding var measuredHeight: CGFloat

    func makeUIViewController(context _: Context) -> UIViewController {
        TokenTagsGridViewControllerKt.TokenTagsGridViewController(
            token: token,
            mainTokenTagNames: mainTokenTagNames,
            rowCount: rowCount,
            heightChanged: { height in
                measuredHeight = CGFloat(truncating: height) / UIScreen.main.scale
            }
        )
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}
