import Shared
import SwiftUI

struct CryptoSphereTheme {
    let colorScheme: ColorScheme

    private var colorSet: Material3ColorScheme {
        colorScheme == .dark ? DarkColorSchemeKt.darkColorScheme : LightColorSchemeKt.lightColorScheme
    }

    func uiColor(_ keyPath: KeyPath<Material3ColorScheme, UInt64>) -> UIColor {
        colorSet[keyPath: keyPath].toUIColor()
    }

    func color(_ keyPath: KeyPath<Material3ColorScheme, UInt64>) -> Color {
        Color(uiColor(keyPath))
    }
}

struct CryptoSphereThemeKey: EnvironmentKey {
    static let defaultValue = CryptoSphereTheme(colorScheme: .light)
}

extension EnvironmentValues {
    var cryptoSphereTheme: CryptoSphereTheme {
        get { self[CryptoSphereThemeKey.self] }
        set { self[CryptoSphereThemeKey.self] = newValue }
    }
}
