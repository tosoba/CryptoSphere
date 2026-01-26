import Shared

extension PlatformContext {
    static var shared: PlatformContext {
        companion.INSTANCE
    }
}

extension CoilPlatformContext {
    static var shared: CoilPlatformContext {
        companion.INSTANCE
    }
}
