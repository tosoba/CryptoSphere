import Shared
import SwiftUI

struct CryptoSphereTheme {
    let colorScheme: ColorScheme
    let colorExtractorResult: ColorExtractor.Result?

    private var materialColorScheme: Material3ColorScheme {
        let color = colorExtractorResult?.color ?? CryptoSphereThemeKt.fallbackSeedColor
        return color.toColorScheme(isDark: colorScheme == .dark)
    }

    func uiColor(_ keyPath: KeyPath<Material3ColorScheme, UInt64>) -> UIColor {
        materialColorScheme[keyPath: keyPath].toUIColor()
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

/// Needed for compose views used in SwiftUI.
/// This way, their theme can update based on StateFlow<ColorExtractor.Result> on compose side.
/// Colors retrieved via CryptoSphereTheme in swift do not update when passed via UIViewControllerRepresentable's makeUIViewController.
struct ColorExtractorResultProviderKey: EnvironmentKey {
    static let defaultValue: ColorExtractorResultProvider = EmptyColorExtractorResultProvider()
}

extension EnvironmentValues {
    var colorExtractorResultProvider: ColorExtractorResultProvider {
        get { self[ColorExtractorResultProviderKey.self] }
        set { self[ColorExtractorResultProviderKey.self] = newValue }
    }
}
