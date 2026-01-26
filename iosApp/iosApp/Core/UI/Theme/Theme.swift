import Shared
import SwiftUI

struct CryptoSphereTheme {
    let colorScheme: ColorScheme
    let colorExtractorResult: ColorExtractor.Result?

    private var colorSet: Material3ColorScheme {
        guard let scheme = colorExtractorResult?.color.toColorScheme(isDark: colorScheme == .dark) else {
            return colorScheme == .dark ? DarkColorSchemeKt.darkColorScheme : LightColorSchemeKt.lightColorScheme
        }
        return scheme
    }

    func uiColor(_ keyPath: KeyPath<Material3ColorScheme, UInt64>) -> UIColor {
        colorSet[keyPath: keyPath].toUIColor()
    }

    func color(_ keyPath: KeyPath<Material3ColorScheme, UInt64>) -> Color {
        Color(uiColor(keyPath))
    }
}

struct CryptoSphereThemeKey: EnvironmentKey {
    static let defaultValue = CryptoSphereTheme(colorScheme: .light, colorExtractorResult: nil)
}

extension EnvironmentValues {
    var cryptoSphereTheme: CryptoSphereTheme {
        get { self[CryptoSphereThemeKey.self] }
        set { self[CryptoSphereThemeKey.self] = newValue }
    }
}
