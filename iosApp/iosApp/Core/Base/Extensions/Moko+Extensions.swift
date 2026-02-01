import Shared
import SwiftUI

extension String {
    init(_ resource: StringResource) {
        self.init(ResourcesKt.getString(stringResource: resource).localized())
    }

    init(_ resourceKeyPath: KeyPath<MR.strings, StringResource>) {
        self.init(
            ResourcesKt.getString(stringResource: MR.strings()[keyPath: resourceKeyPath])
                .localized()
        )
    }

    init(_ resourceKeyPath: KeyPath<MR.strings, StringResource>, parameter: Any) {
        self.init(
            ResourcesKt.getString(
                stringResource: MR.strings()[keyPath: resourceKeyPath],
                parameter: parameter
            )
            .localized()
        )
    }
}

extension Font {
    init(_ resourceKeyPath: KeyPath<MR.fonts, FontResource>, withSize size: Double) {
        self.init(UIFont.from(resourceKeyPath, withSize: size))
    }

    init(_ resourceKeyPath: KeyPath<MR.fonts, FontResource>, withSizeOf textStyle: UIFont.TextStyle) {
        self.init(
            resourceKeyPath,
            withSize: UIFont.preferredFont(forTextStyle: textStyle).pointSize
        )
    }
}

extension UIFont {
    static func from(_ resourceKeyPath: KeyPath<MR.fonts, FontResource>, withSize size: Double) -> UIFont {
        MR.fonts()[keyPath: resourceKeyPath].uiFont(withSize: size)
    }
}
