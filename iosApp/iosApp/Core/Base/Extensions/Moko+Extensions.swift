import Shared
import SwiftUI

extension String {
    init(_ resource: StringResource) {
        self.init(ResourcesKt.getString(stringResource: resource).localized())
    }

    init(_ resourceKey: KeyPath<MR.strings, StringResource>) {
        self.init(
            ResourcesKt.getString(
                stringResource: MR.strings()[keyPath: resourceKey]
            )
            .localized()
        )
    }

    init(_ resourceKey: KeyPath<MR.strings, StringResource>, parameter: Any) {
        self.init(
            ResourcesKt.getString(
                stringResource: MR.strings()[keyPath: resourceKey],
                parameter: parameter
            )
            .localized()
        )
    }
}

extension Font {
    init(resource: KeyPath<MR.fonts, FontResource>, withSize: Double) {
        self.init(MR.fonts()[keyPath: resource].uiFont(withSize: withSize))
    }

    init(resource: KeyPath<MR.fonts, FontResource>, withSizeOf textStyle: UIFont.TextStyle) {
        self.init(
            MR.fonts()[keyPath: resource]
                .uiFont(withSize: UIFont.preferredFont(forTextStyle: textStyle).pointSize)
        )
    }
}
