import Shared
import SwiftUI

struct TokenFeedParameterCardsColumnView: View {
    let parameters: [TokenFeedParameter]

    var body: some View {
        VStack(spacing: 2) {
            ForEach(Array(parameters.enumerated()), id: \.offset) { index, parameter in
                TokenFeedParameterCardView(
                    parameter: parameter,
                    cornerRadius: cornerRadius(for: index)
                )
            }
        }
    }

    private func cornerRadius(for index: Int) -> (top: CGFloat, bottom: CGFloat) {
        if parameters.count == 1 {
            return (16, 16)
        } else if index == 0 {
            return (16, 0)
        } else if index == parameters.count - 1 {
            return (0, 16)
        } else {
            return (0, 0)
        }
    }
}
