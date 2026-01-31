import SwiftUI

func listItemShape(for index: Int, outOf count: Int) -> AnyShape {
    if count == 1 {
        AnyShape(RoundedRectangle(cornerRadius: 16))
    } else if index == 0 {
        topOnlyRoundedCornersShape()
    } else if index == count - 1 {
        bottomOnlyRoundedCornersShape()
    } else {
        AnyShape(Rectangle())
    }
}

func topOnlyRoundedCornersShape() -> AnyShape {
    AnyShape(
        UnevenRoundedRectangle(
            topLeadingRadius: 16,
            bottomLeadingRadius: 0,
            bottomTrailingRadius: 0,
            topTrailingRadius: 16
        )
    )
}

func bottomOnlyRoundedCornersShape() -> AnyShape {
    AnyShape(
        UnevenRoundedRectangle(
            topLeadingRadius: 0,
            bottomLeadingRadius: 16,
            bottomTrailingRadius: 16,
            topTrailingRadius: 0
        )
    )
}
