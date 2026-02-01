import SwiftUI

func historyListItemShape(for index: Int, outOf count: Int, firstOnDate: Bool, lastOnDate: Bool) -> AnyShape {
    if count == 2 {
        AnyShape(RoundedRectangle(cornerRadius: 16))
    } else if firstOnDate {
        topOnlyRoundedCornersShape()
    } else if index == count - 1 || lastOnDate {
        bottomOnlyRoundedCornersShape()
    } else {
        AnyShape(Rectangle())
    }
}
