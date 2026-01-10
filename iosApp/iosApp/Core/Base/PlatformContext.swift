import Shared

extension PlatformContext {
    static var shared: PlatformContext {
        companion.INSTANCE
    }
}
