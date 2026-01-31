import Shared
import SwiftUI

struct TokenFeedParameterCardsColumnView: View {
    let parameters: [TokenFeedParameter]

    var body: some View {
        VStack(spacing: 2) {
            ForEach(Array(parameters.enumerated()), id: \.offset) { index, parameter in
                TokenFeedParameterCardView(
                    parameter: parameter,
                    shape: listItemShape(for: index, outOf: parameters.count)
                )
            }
        }
    }
}
