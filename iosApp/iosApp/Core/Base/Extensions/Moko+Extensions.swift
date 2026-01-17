import Shared
import SwiftUI

extension String {
    init(_ resourceKey: KeyPath<MR.strings, StringResource>) {
        self.init(
            ResourcesKt.getString(
                stringResource: MR.strings()[keyPath: resourceKey]
            ).localized()
        )
    }

    init(_ resourceKey: KeyPath<MR.strings, StringResource>, parameter: Any) {
        self.init(
            ResourcesKt.getString(
                stringResource: MR.strings()[keyPath: resourceKey],
                parameter: parameter
            ).localized()
        )
    }
}
