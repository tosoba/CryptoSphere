import Shared
import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

    init() {
        configureGlobalStyles()
    }

    var body: some Scene {
        WindowGroup {
            RootView(component: appDelegate.rootComponent)
        }
    }

    private func configureGlobalStyles() {
        configureSegmentedControlStyle()
    }

    private func configureSegmentedControlStyle() {
        let attributes: [NSAttributedString.Key: Any] = [
            .font: UIFont.from(\.manrope_regular, withSize: 14),
        ]
        UISegmentedControl.appearance().setTitleTextAttributes(attributes, for: .normal)
        UISegmentedControl.appearance().setTitleTextAttributes(attributes, for: .selected)
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    private static let TAG = "AppDelegate"

    private var stateKeeper = StateKeeperDispatcherKt.StateKeeperDispatcher(savedState: nil)
    private lazy var backgroundJobsManager = IosBackgroundJobsManager()

    lazy var dependencyContainer = DependencyContainerBuilderKt.buildDependencyContainer(
        backgroundJobsManager: backgroundJobsManager
    )

    lazy var iosDependencyContainer = IosDependencyContainer(context: PlatformContext.shared)

    fileprivate lazy var rootComponent: RootComponent =
        dependencyContainer
            .rootComponentFactory
            .invoke(
                componentContext: DefaultComponentContext(
                    lifecycle: ApplicationLifecycle(),
                    stateKeeper: stateKeeper,
                    instanceKeeper: nil,
                    backHandler: nil
                ),
                colorExtractor: ColorExtractor(
                    imageLoader: ImageLoaderKt.imageLoader(
                        context: CoilPlatformContext.shared,
                        cacheDir: PlatformContext.shared.cachePath,
                        interceptors: Set()
                    ),
                    context: CoilPlatformContext.shared
                )
            )

    func application(
        _: UIApplication, didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        backgroundJobsManager.registerPeriodicTokensSync()
        enqueuePeriodicTokensSync()
        return true
    }

    func application(_: UIApplication, shouldSaveSecureApplicationState coder: NSCoder) -> Bool {
        StateKeeperUtilsKt.save(coder: coder, state: stateKeeper.save())
        return true
    }

    func application(_: UIApplication, shouldRestoreSecureApplicationState coder: NSCoder) -> Bool {
        stateKeeper = StateKeeperDispatcherKt.StateKeeperDispatcher(
            savedState: StateKeeperUtilsKt.restore(coder: coder)
        )
        return true
    }

    private func enqueuePeriodicTokensSync() {
        Task(priority: .background) {
            do {
                try await enqueuePeriodicTokensSyncUseCase(container: dependencyContainer)
            } catch {
                LoggerKt.kermit.e(
                    messageString: error.localizedDescription,
                    throwable: nil,
                    tag: AppDelegate.TAG
                )
            }
        }
    }
}
