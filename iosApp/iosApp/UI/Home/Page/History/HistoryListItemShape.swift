import SwiftUI

func historyListItemShape(index: Int, count: Int, firstForDate: Bool, lastForDate: Bool) -> AnyShape {
    if count == 2 {
        AnyShape(RoundedRectangle(cornerRadius: 16))
    } else if firstForDate {
        topOnlyRoundedCornersShape()
    } else if index == count - 1 || lastForDate {
        bottomOnlyRoundedCornersShape()
    } else {
        AnyShape(Rectangle())
    }
}
